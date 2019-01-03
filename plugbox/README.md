# Plugbox

Standardizable deployment tooling.

## Theory:

Every deployable service has:

* A source tree containing an unready application (for example, a Python app)

* A prepared tree derived from the source tree by automated transforms (for
  example, `setup.py` produces a `foo.tar.gz` which must be uploaded, or a
  PHP project is delivered as a directory of files to include in a web root)

* Optionally, a set of "start commands" that initialize the service, then
  exit.

* Optionally, a set of "stop commands" that shut down the service, then exit.

* Optionally, a set of "post-upload" commands that prepare a version for
  production.

* Optionally, a set of "refresh commands" that deploy a new version without
  loss of service.

* Optionally, a set of configuration files that are shared between releases.
  Plugbox assumes that configuration data does not change from deploy to
  deploy, and stores it separately from the deployed files for each release.

* Optionally, a set of data files maintained by the service that are shared
  between releases. Plugbox assumes that state data must be retained from
  deploy to deploy, and stores it separately from the deployed files for each
  release.

(Yes, there are a lot of optional steps. You can leave stuff out for any
specific deployment, but you can't add new oens.)

Services are deployed to one or more hosts, over SSH. Plugbox supports "jump
host" environments, where SSH connections must pass through a management or
gateway box before reaching the target servers. Plugbox supports
command-locked SSH keys, which can restrict deploy users from running
unexpected programs.

Deployment can either be _sequential_, where each host is completely deployed
in turn, or _stepped_, where each step is run on all hosts before the next
step is startee.

Plugbox provides two components:

* A server-side tool, `plugbox server`, which integrates with `sshd` to
  provide deployment services.

* A client-side tool, `plugbox`, which deploys code over SSH.

Plugbox requires the following dependencies:

* On the server:

    * Python 2.7
    * A reasonably modern `rsync`
    * OpenSSH `sshd`

* On jump hosts:

    * OpenSSH `sshd` and `ssh`

* On clients:

    * Python 2.7
        * PyYAML
    * OpenSSH `ssh`
    * Netcat (`nc`), if using a jumphost, because of reasons.
    * A reasonably modern `rsync`

Windows users: get a Unix box.

## Server-side Layout

    # Dramatis Personæ
    SERVICE=some-service                  # a name to conjure by
    HOMEDIR=/home/${SERVICE}              # a home to live in
    DEPLOY=${HOMEDIR}/deploy              # a directory of versions
    VERSION=${DEPLOY}/2014-04-01T12:34:56 # a directory
    LIVE_VERSION=${DEPLOY_ROOT}/live      # the live version (a symlink)
    PREV_VERSION=${DEPLOY_ROOT}/previous  # the next live version, for deploys (a symlink)
    NEXT_VERSION=${DEPLOY_ROOT}/next      # the last live version, for rollbacks (a symlink)
    LIVE_CONVENIENCE=${HOMEDIR}/live      # the live version (a symlink, for convenience)
    SERVICE_CONFIG=${HOMEDIR}/config      # a directory of service config data, for your CM tools (puppet)
    SERVICE_DATA=${HOMEDIR}/data          # a directory of service-owned data
    
    # Supporting players
    PLUGBOX_CONFIG=${HOMEDIR}/.plugbox    # a directory of plugbox-specific config files
    PLUGBOX_HOOKS=${PLUGBOX_CONFIG}/server-hooks
                                          # a directory of plugbox hook programs

Don't run two services out of the same source tree. Service accounts are
cheap; use 'em.

## Client-side Layout

    SOURCES=some-dir                    # a directory
    PLUGFILE=${SOURCES}/plugbox.yaml    # a config file

## The Config File

Actual structureTBD during implementation. YAML; relatively flat preferred.

* Default host list (TBD: non-default host lists live where?)
* Jumphost settings
* Rollout mode (sequential or stepped)
* File replacement prompt settings

Sketch YAML:

    ---
    gateway: "deployer@gateway.example.com"
    service: "example"
    hosts:
        -   hostname: "web01"
        -   hostname: "web02"
            service: "funweb" # the username is different here because muppets
        -   hostname: "web01"
            gateway: null # this one is reachable directly, no jump host
    deploy:
        clone-live-version: true
        confirm-files: false

Anything that might concievably be a command-line flag on the client _must_
be a config file entry. Options are bad; they encourage variance from The One
True Deploy. Config files can be version-controlled.

## The Deployment Experience:

1. (Done once) Set up the project:

        plugbox init
        # edit the generated config.yaml configuration file
        # check it into source control and forget about it

2. Deploy the current project:

        plugbox deploy

3. Rollback from the current tree:

        plugbox rollback

4. That's it.

## Deploy

Implemented as `plugbox deploy`

(Commands below are a sketch and will actually be handled within plugbox)

1. Pick a version number.

2. Shell into the host and ensure the deploy tree and tools are ready:

        ssh service@server plugbox server init

3. Prepare the source.

    * Plugbox internally invokes each `prepare-command` using `$SHELL -l -c
      <command>`, in order.

    * Any non-zero exit status aborts the deploy.

4. Create a new "next" version on the server:

        ssh service@server plugbox server next-version ${VERSION}

    * Special case: If the `clone-live-version` option is enabled, then
    
            ssh service@server plugbox server next-version --clone ${VERSION}
    
        which will _copy_ the `live` tree when creating the new version. This
        can speed up the rsync step for large projects.

5. Upload the prepared trees from step 3.

        rsync ${TREE}/ service@server:deploy/${VERSION}/${TREE}/

    * Something about prompting for modified and deleted files here. We
      use that at work. Suppress if `plugbox` has no pty, and abort unless
      `confirm-files` is set to false.

6. Roll out the upgrade.

        ssh service@server plugbox server upgrade-to ${VERSION}
        # internally,
        [ -e ${HOME}/.plugbox/server-hooks/refresh ] || plugbox server run-hook stop
        cp -a ${HOME}/deploy/live ${HOME}/deploy/previous
        mv ${HOME}/deploy/next ${HOME}/deploy/live
        [ -e ${HOME}/.plugbox/server-hooks/refresh ] && plugbox server run-hook refresh
        [ -e ${HOME}/.plugbox/server-hooks/refresh ] || plugbox server run-hook start

## Rollback

Implemented as `plugbox rollback`

We assume the current deploy is screwed, and avoid refresh even if it's
available.

1. Run the service's stop commands.

2. Use the `live` version's `previous` symlink to replace the `live` symlink.

3. Run the service's start commands.

Should we un-stitch the symlink chain?

## Cleanup

Implemented as `plugbox cleanup`. Entirely optional, but good for your disk
space.

Runs `plugbox server cleanup`, which in turn removes stale deploys (i.e.,
everything but the `next`, `live`, and `previous` deploy.)
