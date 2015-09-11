class jira::user {
  $home  = '/var/lib/jira'

  group { jira:
    ensure => present,
    system => true,
  }

  user { jira:
    ensure   => present,
    system   => true,
    gid      => jira,
    home     => $home,
    shell    => '/bin/bash',
    password => '*',
    comment  => 'JIRA Issue Tracking server,,,',
  }

  file { $home:
    ensure => directory,
    owner  => jira,
    group  => jira,
    mode   => '0640',
  }
}
