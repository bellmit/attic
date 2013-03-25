Exec {
  path  => '/usr/sbin:/usr/bin:/sbin:/bin',
}

exec { 'apt-get update':
  user        => root,
  group       => root,
  refreshonly => true,
}

Package {
  require => Exec['apt-get update'],
}
include 'ssh'
include 'ssh::hostkeys'
include 'postgresql'
include 'postgresql::replication'
