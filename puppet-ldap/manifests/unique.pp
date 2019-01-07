define ldap::unique($database_dn, $overlay_dn, $unique_uris, $filter, $ldifs_dir = "/etc/ldap/ldifs/unique") {
  file { "${ldifs_dir}/${name}.ldif":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/unique.ldif.erb"),
  }

  file { "${ldifs_dir}/${name}_overlay.ldif":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/unique_overlay.ldif.erb"),
  }

  ldap::add { "unique/${name}_overlay.ldif":
    base    => "cn=config",
    filter  => "(&(objectClass=olcUniqueConfig)(${filter}))",
    require => [
      File["${ldifs_dir}/${name}_overlay.ldif"],
    ]
  }

  ldap::modify { "unique/${name}.ldif":
    require => [
      File["${ldifs_dir}/${name}.ldif"],
    ]
  }
}