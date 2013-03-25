class postgresql(
  $version   = '9.2',
  $encoding  = 'UTF8',
  $collation = 'en_US.UTF-8',
  $client_network = $::network_eth1,
  $client_netmask = $::netmask_eth1
) {
  file { '/etc/apt/sources.list.d/postgresql.list':
    ensure => file,
    owner  => root,
    group  => root,
    mode   => 0444,
    source => 'puppet:///modules/postgresql/postgresql.list',
    notify => Exec['apt-get update'],
  }

  file { '/etc/apt/trusted.gpg.d/postgresql.gpg':
    ensure => file,
    owner  => root,
    group  => root,
    mode   => 0444,
    source => 'puppet:///modules/postgresql/postgresql.gpg',
    notify => Exec['apt-get update'],
  }

  package { "postgresql-${version}":
    ensure => present,
  }

  package { "postgresql-doc-${version}":
    ensure => present,
  }

  $check_encoding_command = shellquote(
    'sudo',
    '-u',
    'postgres',
    'psql',
    '--tuples-only',
    '--no-align',
    '--command',
    "select pg_catalog.pg_encoding_to_char(d.encoding)
    from pg_catalog.pg_database d
    where d.datname = 'template0'"
  )
  $check_collation_command = shellquote(
    'sudo',
    '-u',
    'postgres',
    'psql',
    '--tuples-only',
    '--no-align',
    '--command',
    "select d.datcollate
    from pg_catalog.pg_database d
    where d.datname = 'template0'"
  )

  exec { "pg_dropcluster --stop ${version} main":
    user    => root,
    group   => root,
    onlyif  => [
      "[ -e /var/lib/postgresql/${version}/main ]",
      "[ \$(${check_encoding_command}) != '${encoding}' -o \$(${check_collation_command}) != '${collation}' ]",
    ],
    require => Package["postgresql-${version}"],
  }

  exec { "pg_createcluster ${version} main":
    command => "pg_createcluster --user postgres --encoding '${encoding}' --locale '${collation}' ${version} main",
    creates => "/var/lib/postgresql/${version}/main",
    user    => root,
    group   => root,
    require => Exec["pg_dropcluster --stop ${version} main"],
    notify  => Service["postgresql"],
  }

  file { "/etc/postgresql/${version}/main/pg_hba.conf":
    ensure  => file,
    owner   => postgres,
    group   => postgres,
    mode    => 0444,
    content => template('postgresql/pg_hba.conf'),
    require => Exec["pg_createcluster ${version} main"],
    notify  => Service['postgresql'],
  }

  file { "/etc/postgresql/${version}/main/postgresql.conf":
    ensure  => file,
    owner   => postgres,
    group   => postgres,
    mode    => 0444,
    source  => 'puppet:///modules/postgresql/postgresql.conf',
    require => Exec["pg_createcluster ${version} main"],
    notify  => Service['postgresql'],
  }

  service { 'postgresql':
    ensure     => running,
    enable     => true,
    hasrestart => true,
  }
}
