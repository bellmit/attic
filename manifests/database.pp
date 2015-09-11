class jira::database(
  $password
) {
  postgresql::role { 'jira':
    login      => true,
    password   => $password,
    createdb   => false,
    createrole => false,
    superuser  => false,
  }

  postgresql::database { 'jira':
    owner   => 'jira',
    require => Postgresql::Role['jira'],
  }
}