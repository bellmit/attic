# Plugbox

Standardizable deployment tooling.

## Theory:

Every deployable service has:

* A source tree containing an unready application (for example, a Python app)

* A prepared tree derived from the source tree by automated transforms (for
  example, `setup.py` produces `dist`)

* A set of "start commands" that initialize the service, then exit.

* A set of "stop commands" that shut down the service, then exit.

* Optionally, a set of "post-upload" commands that prepare a version for
  production.

* Optionally, a set of "refresh commands" that deploy a new version without
  loss of service.

Plugbox provides two components:

* A set of server-side tools for working with deployments

* A set of client-side Fabric recipies for deploying services.

Plugbox assumes that it can shell into the target host, run rsync, and run
commands. It must support "jump" hosts.

## Server Layout

    # Dramatis Personæ
    SERVICE=some-service                  # a name to conjure by
    HOMEDIR=/home/${SERVICE}              # a home to live in
    DEPLOY=${HOMEDIR}/deploy              # a directory of versions
    VERSION=${DEPLOY}/2014-04-01T12:34:56 # a directory
    LIVE_VERSION=${DEPLOY_ROOT}/live      # the live version (a symlink)
    PREV_VERSION=${DEPLOY_ROOT}/previous  # the next live version (a symlink)
    NEXT_VERSION=${DEPLOY_ROOT}/next      # the last live version (a symlink)
    LIVE_CONVENIENCE=${HOMEDIR}/live      # the live version (a symlink, for convenience)
    CONFIG=${HOMEDIR}/config              # a directory of service config data, for your CM tools (puppet)

    # Versions

## The Deployment Experience:

1. Deploy from the current tree:

        fab deploy

2. Rollback from the current tree:

        fab rollback

3. That's it.

## Configuring an app

    from fabric.api import *
    from plugbox import fabric as plug
    
    service = plug.Service()
    
    # zero or more prepare steps, in order
    @service.prepare
    def run_make():
        local('make')
    
    # one or more files/directories to upload to the version directory (no glob)
    service.upload('inc')
    # ---- OR ----
    # exactly one path to upload, which will become the version directory
    service.upload_version('dist')
    
    # uploads use rsync's --del option, so will purge unknown files.
    
    # zero or more shutdown steps, run in order. These are run in the service
    # user's home directory.
    @service.stop
    def stop_server():
        run('supervisorctl my-app stop')
    
    # zero or more startup steps, run in order. These are run in the service
    # user's home directory.
    @service.start
    def start_server():
        run('supervisorctl my-app start')
    
    # zero or more refresh steps, run in order. These are run in the service
    # user's home directory.
    @service.refresh
    def restart_server():
        run('apachectl graceful')
    
    # zero or more post-upload steps, run in order. These are run in the
    # version directory for the version being prepared.
    @service.post_upload
    def unpack_tars():
        run('virtualenv env')
    
    # required to make the tasks visible to Fabric
    deploy, rollback = service.tasks

Style guide: keep it short. Use plugbox to invoke your build system, rather
than using it as a build system in its own right.

## Process

(Commands below are a sketch and will actually be handled within fabric)

1. Prepare the source.

        fab prepare

2. Create a new version on the server:

3. Use the `live` symlink to set the `previous` symlink of the new version.

        VERSION=$(ssh service@server plugbox next-version)

4. Upload the prepared tree from step 1.

        fab upload:version=${VERSION}

5. Use the new version to set the `next` symlink of the `live` version.

6. If the server has no refresh commands, run the service's stop commands.

7. Adjust the `live` symlink to the new version.

    ssh service@server plugbox upgrade-to ${VERSION}

8. If the server has no refresh commands, run the service's start commands.

9. If the server has refresh commands, run the service's refresh commands.

## Rollback

Implemented as `plugbox rollback-to <version>`

We assume the current deploy is screwed, and avoid refresh even if it's
available.

1. Run the service's stop commands.

2. Use the `live` version's `previous` symlink to replace the `live` symlink.

3. Run the service's start commands.

Should we un-stitch the symlink chain?

## Cleanup

Implemented as `plugbox remove -v <version>`.

1. Ensure the target version is not the `live` version.

2. Remove the version from the symlink chain:

    1. Replace the target version's `next` version's `previous` symlink with the
       target version's `previous` symlink.

    2. Replace the target version's `previous` version's `next` symlink with the 
       target version's `next` symlink.

3. Remove the target version from the filesystem.
