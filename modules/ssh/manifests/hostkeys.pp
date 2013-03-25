class ssh::hostkeys(
  $hostname = $::hostname
) {
  # I know, I know, publishing hostkeys in SCM is dumb. I don't have a better
  # suggestion for making the servers come up ready to communicate with one
  # another -- you have to start somewhere, and without a puppet master you
  # can't use storedconfigs to publish the host keys back and recombine them.

  # Obviously, you should not use these keys in production.

  file { '/etc/ssh':
    ensure  => directory,
    owner   => root,
    group   => root,
    source  => "puppet:///modules/ssh/hostkeys/${hostname}/",
    recurse => true,
    notify  => Service['ssh'],
  }

  file { '/etc/ssh/ssh_known_hosts':
    ensure => file,
    owner  => root,
    group  => root,
    mode   => 0444,
    source => 'puppet:///modules/ssh/hostkeys/ssh_known_hosts',
  }
}