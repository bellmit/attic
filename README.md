# `git-issue` - automatically correlate issue branches and Redmine tickets

The `git-issue` command automates some of the process of creating and working
with issue branches using common FreshBooks conventions. It will work on
branches named

* `issue-1234`
* `issue-1234-some-description`

and will automatically create branches (tracking origin/master) as needed.

## Installation

To install this script, check out the source tree somewhere and add it to your
`$PATH`. Alternately, place the `git-issue` script (or a symlink to it) in a
directory that's already in your `$PATH`.

## Basic usage

To create a new issue branch or to switch back to an existing issue branch:

    $ git-issue 12321
    