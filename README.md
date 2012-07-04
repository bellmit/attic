# Triggers Jenkins/Hudson builds automatically on commit or push

`git-build` provides a tool for automatically starting builds in Jenkins from
Git, usable from the command line or as a hook. This can reduce wasted effort
polling rarely-updated repositories by allowing the repository to notify the
build server on change, instead.

## Configuring your build

You'll need to set up your Jenkins builds to allow remote activation:

* "Trigger builds remotely" must be enabled.
* You must provide your build with an authentication token.

You'll need to keep track of your Jenkins base URL
("http://builds.example.com/") and the name of your build ("my-project") to
configure buildhook.

## Configuring your repository

You'll need to add Jenkins-related configuration to your repository's
`.git/config` file. The easiest way is to use `git config`:

    git config jenkins.url http://builds.example.com/
    git config jenkins.job my-project
    git config jenkins.token SECRET_TOKEN

## Build-on-push

To configure a repository to automatically submit build requests on push,
you'll need to run `git-build` from your repository's post-receive hook :

    #!/bin/bash -e
    # in .git/hooks/post-receive, which must be chmod +x
    
    /path/to/git-build --optional

(You can also configure buildhook to submit build requests on commit using
a post-commit hook instead of a post-receive hook.)

## The build command

Git-build also provides a 'build' command. To use it, you'll need to add the
directory containing `git-build` to your `$PATH`. Then you can run `git build`
to submit a build at any time.

This command accepts two options:

* `--optional` (`-o`): do not fail if the build configuration is incomplete.
* `--cause CAUSE` (`-c`): include a 'cause' message in the Jenkins build trigger.

## Use mercurial?

You might be interested in the
[Mercurial version](https://bitbucket.org/ojacobson/buildhook/) of this
command.
