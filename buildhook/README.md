# Triggers Jenkins/Hudson builds automatically on commit or push

`buildhook.py` provides two tools for automatically starting builds in 
Jenkins from Mercurial: a command-line extension and a hook. This can reduce
wasted effort polling rarely-updated repositories by allowing the repository
to notify the build server on change, instead.

## Configuring your build

You'll need to set up your Jenkins builds to allow remote activation:

* "Trigger builds remotely" must be enabled.
* You must provide your build with an authentication token.

You'll need to keep track of your Jenkins base URL
("http://builds.example.com/") and the name of your build ("my-project") to
configure buildhook.

## Configuring your repository

You'll need to add Jenkins-related configuration to your repository's
`.hg/hgrc` file:

    [jenkins]
    url = http://builds.example.com/
    job = my-project
    token = SECRET_TOKEN

### Multiple repositories

If you share the same Jenkins server across several projects (not unusual),
you can put the Jenkins URL in a shared config file (such as `~/.hgrc` or
mercurial-server's `/etc/remote-hgrc.d/` directory)
instead of configuring it for each project.

## Build-on-push

To configure a repository to automatically submit build requests on push,
you'll need to add a changegroup hook to the repository's `.hg/hgrc` file:

    [hooks]
    changegroup.buildhook = python:/path/to/buildhook.py:buildhook

The 'buildhook' hook will silently exit without submitting a build request if
the Jenkins configuration is incomplete. You can observe the warning it emits
in this scenario using the '--verbose' flag to your Mercurial commands.

(You can also configure buildhook to submit build requests on commit using
a commit hook instead of a changegroup hook.)

## The build command

Buildhook also provides a 'build' command. To use it, you'll need to enable
buildhook as an extension by adding the following to your `~/.hgrc` file:

    [extensions]
    buildhook = /path/to/buildhook.py

Once you've enabled the extension, `hg build` will use the `[jenkins]`
section to submit build requests. See `hg help buildhook` and
`hg help build` for details.

## Use git?

You might be interested in the
[Git version](https://github.com/ojacobson/git-build/) of this extension.
