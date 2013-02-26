# RabbitMQ providers for Puppet

Wotcha. This module provides some basic management tools for configuring
RabbitMQ, via `rabbitmqctl` and package management. It's been tested on
CentOS, and should work as-is on Debian and Ubuntu too.

For a completeish example of how to use this class, see `example.pp` in this
directory.

## The `rabbitmq` Class

You can install and manage the RabbitMQ server itself using the `rabbitmq`
class. The simplest mode is simply

    include 'rabbitmq'

which will install RabbitMQ 2.8, Erlang R14, and start RabbitMQ.

This class is parameterized, but does not require Hiera on 2.7. (If you want
Hiera features, use Puppet 3 or configure them yourself.) It accepts the
following parameters:

* `ensure`, which may be set to the following values:
    * `absent`, which will stop the service, remove RabbitMQ, and remove
      Erlang.
    * `present`, which will install RabbitMQ and Erlang.
    * `running` (the default), which will also start the service.
    * `stopped`, which will stop the service but keep it installed.
* `enable`, which controls whether the service should start on boot. This
  defaults to `true`.
* `version`, which controls which version of RabbitMQ to install. This can be
  any package-management version, or `present`, or `latest`.
* `erlang_version`, which controls which version of Erlang ot install. It has
  the same values that `version` uses.

## New types

You can manage basic RabbitMQ objects via three new Puppet types.

### `rabbitmq_vhost`

This type invokes `rabbitmqctl add_vhost` and related commands to manage AMQP
virtual hosts. It accepts one parameter:

* `ensure`, which may be either `absent` or `present`, and controls whether
  the vhost will be created or destroyed when Puppet runs.

The vhost name will be taken from the resource name.

### `rabbitmq_user`

This type invokes `rabbitmqctl add_user` and related commands to manage
RabbitMQ users. It accepts two parameters:

* `ensure`, which may be either `absent` or `present`, and controls whether
  the user will be created or destroyed when Puppet runs.
* `password`, which forces the user's password to the configured value.
  Setting this property will cause Puppet to reset the user's password every
  time it runs, which generates a refresh event from this resource and some
  log noise. If this parameter is not set and Puppet needs to create the user,
  the user's initial password will default to their username.

The username will be taken from the resource name.

### `rabbitmq_permission`

This type invokes `rabbitmqctl set_permissions` and related commands to manage
the relationship between users and vhosts. It accepts six parameters:

* `ensure`, which may be either `absent` or `present`, and controls whether
  the user should have permissions on the vhost at all. Setting this to absent
  will cause the user's permissions to be removed entirely.
* `user`, which names the RabbitMQ user to control permissions on.
* `vhost`, which names the RabbitMQ vhost to control permissions on.
* `config_match`, which sets the user's "config" regex in RabbitMQ for that
  vhost. If unset, their existing config regex will be left unchanged; new
  grants will be created with a config regex of `''`.
* `write_match`, which sets the user's "write" regex in RabbitMQ for that
  vhost. If unset, their existing write regex will be left unchanged; new
  grants will be created with a write regex of `''`.
* `read_match`, which sets the user's "read" regex in RabbitMQ for that vhost.
  If unset, their existing read regex will be left unchanged; new grants will
  be created with a read regex of `''`.

The resource's name is unused by this type, and is purely informational. I
recommend setting it to "username (vhost)" to help identify grants quickly in
Puppet output.

## Bugs & Feedback

Submit bugs via https://github.com/ojacobson/puppet-rabbitmq or by email at
[owen.jacobson@grimoire.ca](mailto:owen.jacobson@grimoire.ca?subject=puppet-rabbitmq).
