class postgresql::replication(
  $replication_user     = 'replication',
  $replication_password = 'replication'
) {
  $check_user_exists = shellquote(
    'psql',
    '--tuples-only',
    '--no-align',
    '--command',
    "SELECT r.rolname
    FROM pg_catalog.pg_roles r
    WHERE r.rolname = '${replication_user}'"
  )

  $create_user = shellquote(
    'psql',
    '--tuples-only',
    '--no-align',
    '--command',
    "CREATE ROLE ${replication_user}
        LOGIN
        REPLICATION
        PASSWORD '${replication_password}'"
  )

  exec { "createuser ${replication_user}":
    command => $create_user,
    user    => postgres,
    group   => postgres,
    onlyif  => "[ \"\$(${check_user_exists})\" != '${replication_user}' ]",
    require => Service['postgresql'],
  }

  file { '/var/lib/postgresql/.ssh':
    ensure  => directory,
    owner   => postgres,
    group   => postgres,
    mode    => 0444,
    require => Class['postgresql'],
  }

  file { '/var/lib/postgresql/.ssh/id_rsa':
    ensure  => file,
    owner   => postgres,
    group   => postgres,
    mode    => 0400,
    source  => 'puppet:///modules/postgresql/ssh/id_rsa',
    require => Class['postgresql'],
  }

  file { '/var/lib/postgresql/.ssh/authorized_keys':
    ensure  => file,
    owner   => postgres,
    group   => postgres,
    mode    => 0444,
    source  => 'puppet:///modules/postgresql/ssh/id_rsa.pub',
    require => Class['postgresql'],
  }
}
