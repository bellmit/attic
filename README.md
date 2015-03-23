# ZNC IRC Bouncer

... Now in Docker form!

This image exposes the following external resources:

* Port 7000, for IRC and HTTP (webadmin) access. Map with `-p 7000:7000` to
  expose to the outside world.

* A volume at `/znc/.znc`, containing ZNC's state and configuration. Map with
  `-v <host path>:/znc/.znc` if you want to configure ZNC externally, or if you
  want ZNC configuration to survive container upgrades.

The initial config comes up with the user `bouncer` with password `bouncer`. I
_strongly_ recommend you create a new user and delete the `bouncer` user.
