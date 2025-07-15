# phodo-quiz

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                   | Description                                                                        |
| ------------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Rate Limiting](https://start.ktor.io/p/ktor-server-rate-limiting)     | Manage request rate limiting as you see fit                                        |
| [Koin](https://start.ktor.io/p/koin)                                   | Provides dependency injection                                                      |
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [MongoDB](https://start.ktor.io/p/mongodb)                             | Adds MongoDB database to your application                                          |
| [Call Logging](https://start.ktor.io/p/call-logging)                   | Logs client requests                                                               |
| [Swagger](https://start.ktor.io/p/swagger)                             | Serves Swagger UI for your project                                                 |
| [Default Headers](https://start.ktor.io/p/default-headers)             | Adds a default set of headers to HTTP responses                                    |
| [Request Validation](https://start.ktor.io/p/request-validation)       | Adds validation for incoming requests                                              |
| [Authentication](https://start.ktor.io/p/auth)                         | Provides extension point for handling the Authorization header                     |
| [Authentication OAuth](https://start.ktor.io/p/auth-oauth)             | Handles OAuth Bearer authentication scheme                                         |
| [Authentication Basic](https://start.ktor.io/p/auth-basic)             | Handles 'Basic' username / password authentication scheme                          |
| [Authentication JWT](https://start.ktor.io/p/auth-jwt)                 | Handles JSON Web Token (JWT) bearer authentication scheme                          |
| Google Sign-in                                                         | Authenticates users with Google Sign-in and generates JWT tokens                   |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Google Sign-in Integration

This project includes a Google Sign-in API that allows users to authenticate using their Google accounts. The API verifies the ID token from Google, creates or updates the user in the database, and returns a JWT token for subsequent authenticated requests.

### API Endpoint

```
POST /v1/auth/google-signin
```

#### Request Body

```json
{
  "idToken": "Google ID token from client"
}
```

#### Response

```json
{
  "status": "Success",
  "data": {
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "User Name",
      "profilePicUrl": "https://example.com/profile.jpg",
      "createdAt": "2023-01-01T00:00:00Z",
      "lastLoginAt": "2023-01-01T00:00:00Z"
    },
    "token": "JWT token for authentication"
  },
  "message": "Sign in successful"
}
```

### Setup Instructions

1. Create a Google Cloud project and configure OAuth credentials
2. Set the following environment variables:
   - `GOOGLE_CLIENT_ID`: Your Google OAuth client ID
   - `JWT_SECRET`: Secret key for signing JWT tokens (default: "default_secret_change_in_production")
   - `JWT_ISSUER`: Issuer for JWT tokens (default: "phodo-quiz-backend")
   - `JWT_AUDIENCE`: Audience for JWT tokens (default: "phodo-quiz-app")
   - `JWT_REALM`: Realm for JWT tokens (default: "phodo-quiz")

### Testing

The Google Sign-in feature includes comprehensive test coverage:
- Unit tests for the UserRepo implementation
- Unit tests for the AuthService
- Integration tests for the API endpoint
