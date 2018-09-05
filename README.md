# Deadbird

Delete most of your Twitter data.

## Installation

* Have Python 3.6 or later.
* Make a virtualenv.
* Clone this code somewhere.
* `pip install -e .`

## Configuration

These scripts will request a Twitter login token the first time you run them. The token is stored in your platform's user data directory. (On macOS, this works out to `~/Library/Application\ Support/ca.grimoire.deadbird.Client/`) Deleting this directory will forcibly log out this program.

## Running

    ./shred-dms
    ./shred-faves
    ./shred-followers
    ./shred-follows
    ./shred-tweets

## Support Policy

This software is provided as-is. If you open a ticket, I'll do what I can to help, but it might take a while.
