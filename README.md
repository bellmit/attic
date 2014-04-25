# Github Exporter

1. Get it: `git clone https://ojacobson@bitbucket.org/ojacobson/github-export.git`

2. Install requests: `pip install -r requirements.txt`

3. Run it: `./export -u your-github-username`

4. Enjoy.

Issues should be valid mbox files; see the code, or run an export. Gists and
repos are cloned as mirrors (`git clone --mirror`) and will include pull
request refs, but not pull request metadata.

If someone criticises your company in public, try not to respond with "but
not all men" or with legal threats. Have some class.

MIT license; share and enjoy freely.
