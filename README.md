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

## License

Copyright 2018 Owen Jacobson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


