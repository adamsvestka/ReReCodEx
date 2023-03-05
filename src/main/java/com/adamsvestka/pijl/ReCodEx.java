package com.adamsvestka.pijl;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReCodEx {
    public static cz.cuni.mff.recodex.api.v1.login.cas_uk authenticate(String username, String password) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(Redirect.NEVER)
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .build();

        HttpRequest request;
        HttpResponse<String> response;
        try {
            URI casUri = new URI("https://idp.cuni.cz/cas/login?service=https://recodex.mff.cuni.cz/cas-auth-ext/");

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
                redirectUri = new URI(response.headers().firstValue("Location").orElseThrow());
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
                    .uri(new URI("https://recodex.mff.cuni.cz/api/v1/login/cas-uk"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(String.format("{\"token\":\"%s\"}",
                            URLEncoder.encode(params.get("token"), "UTF-8"))))
                    .build();
            response = client.send(request, BodyHandlers.ofString());

            return App.mapper.readValue(response.body(), cz.cuni.mff.recodex.api.v1.login.cas_uk.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException("INTERNAL: Invalid URI syntax: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("INTERNAL: I/O error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("INTERNAL: Interrupted: " + e.getMessage());
        }
    }
}
