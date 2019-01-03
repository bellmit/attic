class jira::service(
  $ensure = running,
  $enable = true
) {
  require 'java::v7'
  require 'jira::user'
  require 'jira::install'

  $jira_home    = $jira::user::home
  $jira_install = $jira::install::current

  file { '/etc/init/jira.conf':
    ensure  => file,
    owner   => root,
    group   => root,
    mode    => '0444',
    content => template('jira/init/jira.conf.erb'),
  }

  service { 'jira':
    ensure   => $ensure,
    enable   => $enable,
    provider => upstart,
    subscribe => [
      Class['jira::install'],
      File['/etc/init/jira.conf'],
    ]
  }
}