define ldap::domain($suffix, $database_dn, $data_dir, $root_dn = '', $root_pw = '', $ldifs_dir = "/etc/ldap/ldifs/domains") {
  file { "${ldifs_dir}/${name}":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/domain.ldif.erb"),
  }

  file { $data_dir:
    ensure => directory,
    owner  => openldap,
    group  => openldap,
    mode   => 750,
  }

  ldap::add { "domains/${name}":
    base    => "cn=config",
    filter  => "(&(objectClass=olcDatabaseConfig)(olcSuffix=${suffix}))",
    require => [
      File["${ldifs_dir}/${name}"],
      File[$data_dir],
    ]
  }

  if $root_dn != '' {
    file { "${ldifs_dir}/${name}_rootdn":
      owner   => root,
      group   => root,
      mode    => 440,
      content => template("ldap/domain_rootdn.ldif.erb"),
    }

    ldap::modify { "${ldifs_dir}/${name}_rootdn":
      ldif    => "${ldifs_dir}/${name}_rootdn",
      require => [
        File["${ldifs_dir}/${name}_rootdn"],
        Ldap::Add["domains/${name}"],
      ],
    }
  }
}
