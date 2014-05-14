class bamboo::user {
  $home = '/var/lib/bamboo'

  group { 'bamboo':
    ensure => present,
    system => true,
  }

  user { 'bamboo':
    ensure   => present,
    system   => true,
    gid      => bamboo,
    home     => $home,
    shell    => '/bin/bash',
    password => '*',
  }

  file { $home:
    ensure => directory,
    owner  => 'bamboo',
    group  => 'bamboo',
    mode   => '0660',
    notify => Service['bamboo'],
  }
}