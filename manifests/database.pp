class bamboo::database(
  $password
) {
  require 'postgresql::server'

  postgresql::role { 'bamboo':
    login      => true,
    password   => $password,
    # Bamboo jobs can involve creating and dropping DBs. We'll tolerate the
    # risk of Bamboo blowing up its OWN DB.
    createdb   => true,
    # Bamboo jobs can involve CREATE EXTENSION, which involves SUPER. Might as
    # well just grant CREATEROLE too, since SUPER can grant it to itself.
    createrole => true,
    superuser  => true,
  }

  postgresql::database { 'bamboo':
    owner   => 'bamboo',
    require => Postgresql::Role['bamboo'],
  }
}
