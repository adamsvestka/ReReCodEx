# ReCodEx API

This is a set of utilities that provide a client for interacting with ReCodEx, whose API I have partially reverse-engineered. It facilitates the process of making requests and handling JSON deserialization with the help of various Java helper classes. Jackson ObjectMapper library is used for JSON serialization and deserialization, while the Java HTTP Client library is employed for processing HTTP requests. API endpoints can be called asynchronously, returning a CompletableFuture holding the resulting data.

## Calling an Endpoint

To call an endpoint, simply use the appropriate method on the ReCodEx class, pass in the required parameters and handle the returned CompletableFuture.

Example for logging in:

```java
ReCodEx.login("username", "password")
        .thenAccept(payload -> {
            var accessToken = payload.accessToken;
            // Do something with the access token.
        })
        .exceptionally(e -> {
            // Handle exception if it occurs during login.
            e.printStackTrace();
            return null;
        });
```

### Error Handling

When an error occurs in the API request or when the ReCodEx API returns a 'success: false' response, a ReCodExApiException is thrown, which can be caught using the `exceptionally()` method on the CompletableFuture.

```java
ReCodEx.getUser("userId")
        .thenAccept(user -> {
            System.out.println("User name: " + user.name);
        })
        .exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
```

The ReCodExApiException provides information about the error, including the HTTP status code, error code and error message:

```java
if (e instanceof ReCodExApiException) {
    ReCodExApiException apiException = (ReCodExApiException) e;
    System.err.println("Error code: " + apiException.error.code);
    System.err.println("Message: " + apiException.error.message);
}
```

## Endpoints

Below are the available endpoints, their corresponding methods in the ReCodEx class, their required parameters, and the HTTP request format.

### Login

Used for retrieving an access token for a user.

Method: `ReCodEx.login(String username, String password)`

Parameters:

- `username`: A string containing the user's username.
- `password`: A string containing the user's password.

HTTP Request example:

```http
POST /cas/login?service=https://recodex.mff.cuni.cz/cas-auth-ext/ HTTP/1.1
Host: idp.cuni.cz
Content-Type: application/x-www-form-urlencoded

username={{username}}&password={{password}}&execution={{execution}}&_eventId=submit
```

### Get Instances

A list of instances the user has access to.

Method: `ReCodEx.getInstances(UUID userId)`

Parameters:

- `userId`: The userID as a UUID.

HTTP Request example:

```http
GET /api/v1/users/{{userId}}/instances HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```

### Get Groups

A list of groups the user is a member of. If `ancestors` is set to false, only study groups are returned. Otherwise, all ancestor groups are returned as well (e.g. a study group's parent course group, semester and instance).

Method: `ReCodEx.getGroups(boolean ancestors, UUID instanceId)`

Parameters:

- `ancestors`: A boolean value indicating whether to include ancestor groups.
- `instanceId`: The instanceID as a UUID.

HTTP Request example:

```http
GET /api/v1/groups?ancestors={{ancestors}}&instanceId={{instanceId}} HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```

### Get User

Get information about a user such as their name, email, avatar, etc.

Method: `ReCodEx.getUser(UUID userId)`

Parameters:

- `userId`: The userID as a UUID.

HTTP Request example:

```http
GET /api/v1/users/{{userId}} HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```

### Get Assignments

Get a list of assignments for a given group. The Get Groups endpoint already returns a list of assignments for each group, but this endpoint returns more information about each assignment.

Method: `ReCodEx.getAssignments(UUID groupId)`

Parameters:

- `groupId`: The groupID as a UUID.

HTTP Request example:

```http
GET /api/v1/groups/{{groupId}}/assignments HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```

### Get Stats

Get statistics for assignments in a given group. The Get Groups endpoint already returns some information about each assignment, but this endpoint is used to additionally get points for each assignment.

Method: `ReCodEx.getStats(UUID groupId)`

Parameters:

- `groupId`: The groupID as a UUID.

HTTP Request example:

```http
GET /api/v1/groups/{{groupId}}/students/stats HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```

### Get Can Submit

Get the number of submission attempts for assignments in a given group. The Get Groups endpoint already returns some information about each assignment.

Method: `ReCodEx.getCanSubmit(UUID groupId)`

Parameters:

- `groupId`: The groupID as a UUID.

HTTP Request example:

```http
GET /api/v1/exercise-assignments/{{groupId}}/can-submit HTTP/1.1
Host: recodex.mff.cuni.cz
Authorization: Bearer {{accessToken}}
```
