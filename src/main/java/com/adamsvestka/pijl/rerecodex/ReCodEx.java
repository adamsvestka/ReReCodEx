package com.adamsvestka.pijl.rerecodex;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import cz.cuni.mff.recodex.api.v1.ReCodExApiMapper;

/**
 * A class that contains methods for communicating with the ReCodEx API.
 * Methods return {@link java.util.concurrent.CompletableFuture
 * CompletableFutures} that can be used to asynchronously retrieve the results.
 */
public class ReCodEx {
    private static String accessToken;

    /**
     * A utility interface for running code in the background.
     */
    @FunctionalInterface
    private static interface ThrowingCallable<T> {
        T call() throws IOException, InterruptedException;
    }

    /**
     * Runs the given callable in the background and returns a CompletableFuture
     * that can be used to retrieve the result.
     * 
     * @param <T>      The type of the result.
     * @param callable The callable to run.
     * @return A CompletableFuture that can be used to retrieve the result.
     */
    private static <T> CompletableFuture<T> runInBackground(ThrowingCallable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return callable.call();
            }

            @Override
            protected void done() {
                try {
                    future.complete(get());
                } catch (ExecutionException e) {
                    future.completeExceptionally(e.getCause());
                } catch (InterruptedException e) {
                    future.completeExceptionally(e);
                }
            }
        };
        worker.execute();
        return future;
    }

    /**
     * Authenticates the user with the given credentials and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param username The username.
     * @param password The password.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote kinda hacky
     */
    public static CompletableFuture<cz.cuni.mff.recodex.api.v1.login.cas_uk.Response> authenticate(String username,
            String password) {
        return runInBackground(() -> {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(Redirect.NEVER)
                    .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                    .build();

            HttpRequest request;
            HttpResponse<String> response;

            URI casUri = URI.create("https://idp.cuni.cz/cas/login?service=https://recodex.mff.cuni.cz/cas-auth-ext/");

            // ===== GET https://recodex.mff.cuni.cz/login =====
            request = HttpRequest.newBuilder()
                    .uri(casUri)
                    .build();
            response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new RuntimeException("INTERNAL: Unexpected response code: " + response.statusCode());

            var input = Pattern.compile("name=\"execution\"\\s+value=\"([^\"]+)\"").matcher(response.body());
            if (!input.find())
                throw new RuntimeException("INTERNAL: Could not find execution");
            String execution = input.group(1);

            // ===== POST https://idp.cuni.cz/cas/login =====
            request = HttpRequest.newBuilder()
                    .uri(casUri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(String.format("username=%s&password=%s&execution=%s&_eventId=submit",
                            URLEncoder.encode(username, "UTF-8"),
                            URLEncoder.encode(password, "UTF-8"),
                            URLEncoder.encode(execution, "UTF-8"))))
                    .build();
            response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 401)
                throw new RuntimeException("Invalid credentials");
            if (response.statusCode() != 302)
                throw new RuntimeException("INTERNAL: Unexpected response code: " + response.statusCode());

            URI redirectUri = null;
            while (response.statusCode() >= 300 && response.statusCode() < 400) {
                redirectUri = URI.create(response.headers().firstValue("Location").orElseThrow());
                if (redirectUri.getPath() == "/login-extern/cas-uk")
                    break;
                request = HttpRequest.newBuilder()
                        .uri(redirectUri)
                        .build();
                response = client.send(request, BodyHandlers.ofString());
                if (response.statusCode() != 302 && response.statusCode() != 200)
                    throw new RuntimeException("INTERNAL: Unexpected response code: " + response.statusCode());
            }

            var params = Arrays.stream(redirectUri.getQuery().split("&"))
                    .map(e -> e.split("="))
                    .collect(Collectors.toMap(e -> e[0], e -> e[1]));

            // ===== POST https://recodex.mff.cuni.cz/api/v1/login/cas-uk =====
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://recodex.mff.cuni.cz/api/v1/login/cas-uk"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(String.format("{\"token\":\"%s\"}",
                            URLEncoder.encode(params.get("token"), "UTF-8"))))
                    .build();
            response = client.send(request, BodyHandlers.ofString());

            var json = ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.login.cas_uk.Response.class);

            accessToken = json.payload.accessToken;

            return json;
        });
    }

    /**
     * Retrieves the list of instances the user has access to and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param userId The user's ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET https://recodex.mff.cuni.cz/api/v1/user/{userId}/instances
     */
    public static CompletableFuture<cz.cuni.mff.recodex.api.v1.users.$id.instances.Response> getInstances(UUID userId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://recodex.mff.cuni.cz/api/v1/users/%s/instances",
                            URLEncoder.encode(userId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.users.$id.instances.Response.class);
        });
    }

    /**
     * Retrieves the list of groups the user is a member of and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param ancestors  Whether to include ancestor groups.
     * @param instanceId The instance ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET
     *          https://recodex.mff.cuni.cz/api/v1/groups?ancestors={ancestors}&instanceId={instanceId}
     */
    public static CompletableFuture<cz.cuni.mff.recodex.api.v1.groups.Response> getGroups(boolean ancestors,
            UUID instanceId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            String.format("https://recodex.mff.cuni.cz/api/v1/groups?ancestors=%s&instanceId=%s",
                                    URLEncoder.encode(ancestors ? "1" : "0", "UTF-8"),
                                    URLEncoder.encode(instanceId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.groups.Response.class);
        });
    }

    /**
     * Get details about the user with the given ID and returns a CompletableFuture
     * that can be used to retrieve the result.
     * 
     * @param userId The user's ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET https://recodex.mff.cuni.cz/api/v1/users/{userId}
     */
    public static CompletableFuture<cz.cuni.mff.recodex.api.v1.users.$id.Response.Payload> getUser(UUID userId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://recodex.mff.cuni.cz/api/v1/users/%s",
                            URLEncoder.encode(userId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.users.$id.Response.class).payload;
        });
    }

    /**
     * Retrieves the list of assignments for the given group and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param groupId The group's ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET https://recodex.mff.cuni.cz/api/v1/groups/{groupId}/assignments
     */
    public static CompletableFuture<List<cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.Payload>> getAssignments(
            UUID groupId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://recodex.mff.cuni.cz/api/v1/groups/%s/assignments",
                            URLEncoder.encode(groupId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.groups.$id.assignments.Response.class).payload;
        });
    }

    /**
     * Retrieves statistics about assignments in a given group and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param groupId The group's ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET
     *          https://recodex.mff.cuni.cz/api/v1/groups/{groupId}/students/stats
     */
    public static CompletableFuture<List<cz.cuni.mff.recodex.api.v1.groups.$id.students.stats.Response.Payload>> getStats(
            UUID groupId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://recodex.mff.cuni.cz/api/v1/groups/%s/students/stats",
                            URLEncoder.encode(groupId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.groups.$id.students.stats.Response.class).payload;
        });
    }

    /**
     * Retrieves the list of submissions for the given assignment and returns a
     * CompletableFuture that can be used to retrieve the result.
     * 
     * @param assignmentId The assignment's ID.
     * @return A CompletableFuture that can be used to retrieve the result.
     * 
     * @apiNote GET
     *          https://recodex.mff.cuni.cz/api/v1/exercise-assignments/{groupId}/can-submit
     */
    public static CompletableFuture<cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit.Response.Payload> getCanSubmit(
            UUID groupId) {
        return runInBackground(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            String.format("https://recodex.mff.cuni.cz/api/v1/exercise-assignments/%s/can-submit",
                                    URLEncoder.encode(groupId.toString(), "UTF-8"))))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

            return ReCodExApiMapper.getInstance().readValue(response.body(),
                    cz.cuni.mff.recodex.api.v1.exercise_assignments.$id.can_submit.Response.class).payload;
        });
    }
}
