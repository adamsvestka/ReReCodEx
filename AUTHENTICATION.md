# ReCodEx Authentication Flow

ReCodEx uses a CAS (Central Authentication Service) based authentication flow to obtain an access token for the API. In this document, I will explain each step of the authentication process in detail.

## Overview

1. Obtain the `execution` token by sending a GET request to the CAS login page.
2. Authenticate user credentials by submitting a POST request with the retrieved `execution` token, username, and password.
3. Store the `TGC` (Ticket Granting Cookie) and `ticket` value from the response headers.
4. Submit the `ticket` by sending a GET request to the `cas-auth-ext` endpoint.
5. Retrieve the `PHPSESSID` cookie from the response headers.
6. Retrieve the ReCodEx token from the URL parameter by sending another GET request to the `cas-auth-ext` endpoint with the `PHPSESSID` cookie.
7. Request the actual API access token by sending a POST request to the `login/cas-uk` endpoint with the ReCodEx token in the request body.
8. Store the `accessToken`, `userId`, and `instanceId` returned in the response.

## Step-by-step Instructions

### 1. Obtain the `execution` token

Send a GET request to the CAS login page URL:

```http
GET /cas/login?service=https://recodex.mff.cuni.cz/cas-auth-ext/ HTTP/1.1
Host: idp.cuni.cz
```

Parse the response HTML and extract the `execution` token from the hidden input field. Ensure that the input field is present and has a valid value.

### 2. Authenticate user credentials

Send a POST request to the same URL used in the previous step, including the `execution` token, username, and password in the request body:

```http
POST /cas/login?service=https://recodex.mff.cuni.cz/cas-auth-ext/ HTTP/1.1
Host: idp.cuni.cz
Content-Type: application/x-www-form-urlencoded

username={{username}}&password={{password}}&execution={{execution}}&_eventId=submit
```

Expect a `302` status code in the response, indicating a successful authentication.

### 3. Store the `TGC` and `ticket` value

Extract the `TGC` (Ticket Granting Cookie) from the response cookies, and the `ticket` value from the `Location` header. Store these values for subsequent requests.

### 4. Submit the `ticket`

Send a GET request to the `cas-auth-ext` endpoint with the `ticket` value in the URL:

```http
GET /cas-auth-ext/?ticket={{ticket}} HTTP/1.1
Host: recodex.mff.cuni.cz
```

Expect a `302` status code in the response, indicating a successful submission of the `ticket`.

### 5. Retrieve the `PHPSESSID` cookie

Extract the `PHPSESSID` cookie from the response headers and store it for the next request.

### 6. Retrieve the ReCodEx token

Send another GET request to the `cas-auth-ext` endpoint with the `PHPSESSID` cookie in the request headers:

```http
GET /cas-auth-ext/ HTTP/1.1
Host: recodex.mff.cuni.cz
Cookie: PHPSESSID={{PHPSESSID}}
```

Expect a `302` status code and a `Location` header containing the `token` parameter. Extract and store the ReCodEx token for the next request.

### 7. Request the actual API access token

Send a POST request to the `login/cas-uk` endpoint with the ReCodEx token in the request body:

```http
POST /api/v1/login/cas-uk HTTP/1.1
Host: recodex.mff.cuni.cz
Content-Type: application/json

{
    "token": "{{recodex-token}}"
}
```

Expect a JSON response containing the `accessToken`, `userId`, and `instanceId`, which you should store for future API calls.

## Storing Authentication Results

After a successful authentication, the obtained `accessToken`, `userId`, and `instanceId` are stored for future API requests. In addition to this, the user's information such as their full name, email, avatar URL, and associated instances are also stored.

To handle the storage of sensitive data securely, an encrypted key-value store is used. The implementation of this store can be found in the `LocalStorage` class. The storage uses the Jasypt library for encryption and decryption of the key-value pairs, providing an added layer of security. 

If a user chooses to have their credentials remembered, the username and password will also be stored in the encrypted local storage for convenience. The stored credentials will then be used to automatically authenticate the user upon the next application launch.

## See Also

- `User` class: Represents a user in the ReCodEx system and provides methods to load user data from external API payloads, check if the user is logged in, and perform logout. This class extends the `Observable` class to enable observing changes in the user's state.
- `LocalStorage` class: Provides a convenient and encrypted local storage functionality for securely storing and retrieving sensitive data, such as user credentials and API access tokens.
- `ReCodEx` class: Provides methods for interacting with the ReCodEx API, including authentication and user data retrieval.
- `Observable` class (if applicable): Provides a generic implementation of the Observer pattern for notifying subscribers when an object's state changes. The `User` class extends the `Observable` class to enable reacting to user-related updates.

With these classes and the provided authentication workflow, the application can securely authenticate users and retrieve their information for use throughout the application.