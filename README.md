# Puppet modules for Glassfish 3

This module provides classes and extension types for managing Glassfish 3
servers. This module is very much a work in progress; see the
[Limitations](#limitations) section below for some of the more severe
"gotchas".

This module provides a class, `glassfish`, which installs Glassfish 3 in
`/opt/glassfish/glassfish3`. It also installs an `asadmin` binary in
`/usr/local/bin`. This module also provides a define, `glassfish::service`,
which uses `asadmin create-service` to configure Glassfish to start on boot.
Finally, this module provides the following new types for managing Glassfish:

* `glassfish_domain` for creating, starting, stopping, and destroying domains.
* `glassfish_protocol` for creating, configuring, and destroying protocol
  configurations.
* `glassfish_network_listener` for creating, configuring, and destroying
  network endpoints.
* `glassfish_http` for creating, configuring, and destroying HTTP
  configurations in protocol configurations.

See the types' respective sources for valid parameters.

## Limitations

* This module has a hard time managing domains if more than one DAS is
  running on the box Puppet runs on.
* You can only manage Glassfish objects in the "server" instance (the one that
  hosts the Glassfish DAS). No clusters, no standalone instances, and no
  freestanding configs.
* Glassfish configuration values are wired into this module on an as-needed
  basis. I haven't yet figured out how to generate a more complete mapping
  automatically. I've gone to some effort to make adding new fields and new
  configurable endpoints relatively easy (mostly through Ruby shenanigans), so
  if you need something that's not there, you may be able to implement it
  yourself, but it still sucks.

## See Also

You may also be interested in [Lars Tobias Skjong-Børsting's
implementation](https://github.com/larstobi/puppet-glassfish). (I'd be using
his code too, but the GPL made it impractical for me to do so.)

## Bugs & Complaints

Please report bugs via Github, at
http://github.com/ojacobson/puppet-glassfish/. Pull requests are optional, but
much appreciated.