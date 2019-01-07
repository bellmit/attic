class ldap::server {
  package { slapd:
    ensure => latest,
    notify => Service["slapd"],
  }

  package { ldap-utils:
    ensure => latest,
  }

  file { "/etc/default/slapd":
    owner   => root,
    group   => root,
    mode    => 444,
    content => template("ldap/default/slapd.erb"),
    require => Package["slapd"],
    notify  => Service["slapd"],
  }

  service { slapd:
    ensure  => running,
    enable  => true,
    require => [
      Package["slapd"],
      File["/etc/default/slapd"],
    ],
  }

  file { "/etc/ldap/schema":
    require => [
      Package["slapd"],
    ],
  }

  file { "/etc/ldap/ldifs":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,    
  }

  file { "/etc/ldap/ldifs/domains":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,    
  }

  file { "/etc/ldap/ldifs/acls":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,    
  }

  file { "/etc/ldap/ldifs/indices":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,    
  }

  file { "/etc/ldap/ldifs/modules":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,
  }

  file { "/etc/ldap/ldifs/unique":
    ensure => directory,
    owner  => root,
    group  => root,
    mode   => 550,
  }

  Ldap::Domain {
    require => [
      File["/etc/ldap/ldifs/domains"],
    ],
  }

  Ldap::Acl {
    require => [
      File["/etc/ldap/ldifs/acls"],
    ],
  }

  Ldap::Index {
    require => [
      File["/etc/ldap/ldifs/indices"],
    ],
  }

  Ldap::Module {
    require => [
      File["/etc/ldap/ldifs/modules"],
    ],
  }

  Ldap::Unique {
    require => [
      File["/etc/ldap/ldifs/unique"],
    ]
  }

  ldap::module { "back_hdb":
  }

  ldap::add { "cosine.ldif":
    ldif   => "/etc/ldap/schema/cosine.ldif",
    base   => "cn=schema,cn=config",
    filter => "(&(objectClass=olcSchemaConfig)(cn={*}cosine))",
  }

  ldap::add { "nis.ldif":
    ldif   => "/etc/ldap/schema/nis.ldif",
    base   => "cn=schema,cn=config",
    filter => "(&(objectClass=olcSchemaConfig)(cn={*}nis))",
    require => [
      Ldap::Add["cosine.ldif"],
    ],
  }

  ldap::add { "inetorgperson.ldif":
    ldif    => "/etc/ldap/schema/inetorgperson.ldif",
    base    => "cn=schema,cn=config",
    filter  => "(&(objectClass=olcSchemaConfig)(cn={*}inetorgperson))",
    require => [
      Ldap::Add["cosine.ldif"],
      Ldap::Add["nis.ldif"],
    ],
  }
}
