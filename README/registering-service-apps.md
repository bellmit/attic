# Registering Service Applications

Once deployed, Login-Box needs to be configured for each service application that would like to authenticate against it.

Client to Admin Service:

    POST /admin/application/register HTTP/1.1
    -- authentication headers --
    Content-Type: application/x-login-box+json
    Accept: application/x-login-box+json; version=1
    
    {
        "app": "example-application",
        "name": "An Example Application",
        "url": "https://application.example.com/",
        "notify": {
            "logout": "https://application.example.com/notify/logged-out",
        }
    }

The `notify` property, and each child property, are optional. _If_ provided, then Login-Box will notify this application in response to various authentication events. The only supported event is `logout`, triggered when the user initiates a logout through Login-Box.

Admin Service to Client:

    HTTP/1.1 200 Ok
    Content-Type: application/x-login-box+json
    
    {
        "app": "example-application",
        "createdBy": {
            "userId": "b0bb3f08-e84b-4c83-9483-d89d08da42e6",
            "username": "example-admin"
        },
        "createdAt": "2015-03-06T22:04Z",
        "secret": "c2a579c8-72c1-4137-a92c-4c1fc3879c20",
        "urls": {
            "config": "https://login.example.com/",
            "auth": "https://login.example.com/begin-auth",
            "verify": "https://login.example.com/verify"
        },
        "notify": {
            "logout": "https://application.example.com/logged-out"
        }
    }

The user is responsible for delivering the `secret` and `urls` to the service application. However, service applications can optionally auto-negotiate some elements of their configuration: the `config` URL is always the root URL of the Login-Box instance, and can be interrogated by service applications using their respective secrets to discover URLs and other configuration elements:

    service -> login-box:
        GET / HTTP/1.1
        Authorize: Bearer YzJhNTc5YzgtNzJjMS00MTM3LWE5MmMtNGMxZmMzODc5YzIw
        Accept: application/x-login-box+json; version=1
    login-box -> service:
        HTTP/1.1 200 Ok
        Content-Type: application/x-login-box+json
        
        {
            "urls": {
                "config": "https://login.example.com/",
                "auth": "https://login.example.com/begin-auth",
                "verify": "https://login.example.com/verify"
            }
        }

Applications using configuration negotiation should do so once, at startup.
