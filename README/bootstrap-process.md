# Interactive Deployment

This case describes how Login-Box's protocol handles the initial deployment, bootstrapping from no knowledge to having a single user ID in its built-in directory with administrative control over Login-Box itself.

1. The user presses the Heroku deploy button.

2. Heroku configures and initializes Login-Box.

    * This process also creates a default directory within Login-Box.

3. Heroku directs the user to Login-Box's admin page.

4. Login-Box identifies that it has not yet been configured (TODO: how?) and redirects the user to Login-Box's installation page.

5. Login-Box's installation page identifies that it has not yet been configured (TODO: how?) and permits the user to begin the installation process.

6. The user provides credentials for an initial user ID on the default directory to the installation page.

7. The installation page creates the initial user ID, grants it administrative control over Login-Box itself, issues a session credential for the initial user ID to the user, and redirects the user to Login-Box's admin page.

9. Login-Box's admin page verifies the token. The user is now authenticated with Login-Box, and can administer Login-Box.
