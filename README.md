# Cadastre

A document registry for the [Game of Agora](https://agoranomic.org/).

## Running Cadastre Locally

The following sections outline the process for getting Cadastre up and running
locally. Many of the steps only need to be done once, or only need to be done
when specific parts of the source code change.

<details>
    <summary>I want to run Cadastre locally, what do I do?</summary>

### Pre-flight Checklist

The following checklist summarizes the following sections. The answer to each
one must be "Yes" before proceeding to the next.

* [ ] Do you have a copy of the Cadastre source code?
* [ ] Do you have NodeJS 8.9.1 or later installed? (`node --version`)
* [ ] Do you have Python 3.6 or later installed? (`python3 --version`)
* [ ] Do you have a Postgres 9.6 or later database instance available?
    * [ ] Do you know the username, password, hostname, port, and database name
      needed to connect to it?
* [ ] Have you created a virtualenv?
    * [ ] Is it active?
* [ ] Have you installed the Node dependencies?
* [ ] Have you built the user interface?
* [ ] Have you installed the Python dependencies?
* [ ] Have you set the environment variables appropriately?
* [ ] Have you run the database migrations?
* [ ] Have you started the server?

### External Dependencies

To run Cadastre locally, you need the following prior dependencies:

* A copy of the Cadastre source code
* NodeJS 8.9.1 or later (to build the user interface)
* Python 3.6 or later (to run the service)
* A PostgreSQL database running PostgreSQL 9.6 or later

Cadastre also has a number of internal dependencies, installed below.

### Initial Setup

When setting up a fresh Cadastre instance, a few steps are necessary. To avoid
polluting the systemwide Python installation, Cadastre should be installed in a
virtual environment:

    python3.6 -m venv .venv
    source .venv/bin/activate

You can also manage virtual Python environments with Pipenv or
virtualenvwrapper.

The virtualenv can be destroyed and recreated quite easily, if necessary:

    deactivate
    rm -rf .venv
    python3.6 -m venv .venv
    source .venv/bin/activate

### UI Setup

Whenever the dependencies listed in `package.json` change, including during
initial setup, you will need to install the dependencies into `node_modules`:

    npm install

You'll also need to build the user interface at least once, even if you're not
using it:

    npm run build

NPM will run Webpack, which will build the UI from sources in the `ui`
directory and place the built bundles and resources in `static` for the service
to use.

The user interface must be rebuilt, and the service restarted, before changes
to the UI (whether from version control or from an editor) will be visible in
the running app.

The user interface build process inspects the `NODE_ENV` environment variable
to determine whether to run minification and whether to use React's production
build. Setting this environment variable to `development` will produce an
unminified UI with the development React build, while setting it to
`production` will minify and use a production React build. The default is
`production`.

### Python Setup

Whenever the dependencies listed in `requirements.txt` change, including during
initial setup, you will need to install the dependencies in the virtualenv.
With the virtualenv active (as above):

    pip install -r requirements.txt

### Configuring Cadastre

The Cadastre web service and related tools read their configuration from the
following environment variables:

* `PORT`: The TCP port number to listen on.

    Cadastre exposes a plain-HTTP service on the specified port, available to
    any client able to route to the host it's running on.

    If this environment variable is not set, the service will assume `5000` by
    default.

* `DATABASE_URL`: The URL of a PostgreSQL database.

    The service relies on a single PostgreSQL database to store its data. The
    `DATABASE_URL` environment variable holds the URL of a PostgreSQL database
    used to hold the registry's data.

    On Heroku, this is automatically provided by the `heroku-postgresql`
    Add-On. On other platforms, and locally, this environment variable must be
    exported by hand. It has the format
    `postgresql://<username>:<password>@<host>[:<port>]/<database>`, or any
    other format supported by libpq.

    If this environment variable is not set, the service will assume
    `postgresql://localhost/cadastre-devel` by default.

Please see your operating system's documentation for approaches for managing
environment variables. Ad-hoc environment changes can be set using your shell's
`export` command.

### Database Setup

All changes to the database schema are handled through Alembic, a Python
database migration tool. With the Python dependencies installed and the
environment variables set up, you can run all outstanding migrations by running:

    bin/release

This must be done any time the database schema, stored under `database` as a
set of migrations, changes.

If you're making changes to the database, follow suit. To create a new
migration file, run:

    alembic revision -m 'a short description here'

### Running Cadastre

With all of the setup completed, you can start the Cadastre service as follows:

    bin/web

The server will listen on `$PORT` - by default, it will be available at
http://localhost:5000/. From here, you can interact with it in a web browser,
or point an API client at it, and start using the service. The service emits
log messages to standard output - they should scroll past as you use the
service.

To shut down the service, type Ctrl-C, or send the `gunicorn` process created
by `bin/web` SIGTERM.

</details>

## Running Cadastre on Heroku

The following section describes deploying Cadastre on the Heroku platform. The
production Cadastre instance runs on this platform, but additional environments
can be created to test changes or to run an alternate registry.

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

<details>
    <summary>I want to run Cadastre in production, what do I do?</summary>

### Heroku Configuration Files

Cadastre was designed to be deployed to the Heroku cloud platform. Heroku takes
care of the majority of the setup process, through a constellation of
configuration files:

* `.heroku-build.yml` configures a custom build extension, setting some
  Heroku-specific configuration for the build process.

* `package.json` configures Heroku's NodeJS support to build the UI
  automatically during deployment.

* `requirements.txt` and `runtime.txt` configure Heroku's Python support to run
  Cadastre.

* `Procfile` instructs Heroku to run database migrations on deployment, and to
  start the Cadastre service on web dynos.

To deploy a Cadastre instance on Heroku, click the button at the top of this
chapter. If you prefer to do things by hand, follow this section.

### Pre-Flight Checklist

The following checklist summarizes the following sections. The answer to each
one must be "Yes" before proceeding to the next.

* [ ] Do you have Git installed? (`git --version`)
* [ ] Do you have a clone of the Cadastre Git repository?
    * [ ] Is your current directory inside of that clone?
* [ ] Do you have a Heroku account?
    * [ ] Do you know your username and password?
* [ ] Do you have the Heroku CLI installed? (`heroku --version`)
    * [ ] Are you logged in? (`heroku auth:whoami`, `heroku login`)
* [ ] Have you created a Heroku application?
* [ ] Have you set the application's buildpacks?
* [ ] Have you created a database for your application?
* [ ] Did you push the code?

### External Dependencies

You will need the following external dependencies in order to successfully
deploy Cadastre on Heroku:

* Git, to push code to Heroku.
* A git clone of the Cadastre source code.
* A Heroku account.
* The Heroku CLI, logged in under your Heroku account.

### Creating The Application

Heroku's unit of deployment is the Application. Within an application, all
processes use the same deployed code (though not always the same startup
command), and share access to resources such as databases.

To create an application for the Cadastre service, run the following from
within your clone of the Cadastre code:

    heroku apps:create

If for some reason you no longer want the application, you can safely destroy
it:

    heroku apps:destroy

This will prompt interactively to confirm that you want to destroy the
application, then shut down the service, destroy the database, and throw away
the configuration and source code.

### Configuring The Build Process

Heroku will automatically build source code before deploying it. However, the
build process will not automatically detect Cadastre's requirements, and must
be manually configured. To set the required list of buildpacks:

    heroku buildpacks:add https://github.com/ojacobson/heroku-buildpack-build-config
    heroku buildpacks:add heroku/nodejs
    heroku buildpacks:add heroku/python

This only needs to be done once, when creating the application. If the
buildpack list changes, remove all configured buildpacks and re-add them:

    heroku buildpacks:clear
    heroku buildpacks:add https://github.com/ojacobson/heroku-buildpack-build-config
    heroku buildpacks:add heroku/nodejs
    heroku buildpacks:add heroku/python

### Creating A Database

As with local deployment, a Heroku deployment of Cadastre requires a database.
Heroku's Add-Ons system handles this for us, and automatically provides the
necessary configuration for Cadastre.

To create a database:

    heroku addons:create heroku-postgresql
    heroku pg:wait

This only needs to be done once, under most circumstances. If you need to
recreate the database for any reason, it can be destroyed and re-created -
however, this will also destroy all data contained in it.

    heroku addons:destroy DATABASE
    heroku addons:create heroku-postgresql
    heroku pg:wait

### Push The Code

At this point, Heroku can take over and set everything up. Push the code to
Heroku to deploy it and start the service:

    git push heroku master

If you want to deploy a different branch, you'll need to rename it during the
push:

    git push heroku my-branch:master

Either way, Heroku will build, release, and start the service. This takes some
time - on the first build, it goes through the same dependency and build steps
outlined above for local deployments.

### Open The Application

Once the deployment is finished, your service is ready to use. Run:

    heroku open

This will open the UI in a web browser.

</details>
