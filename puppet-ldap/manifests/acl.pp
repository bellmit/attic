define ldap::acl($database_dn, $entries, $ldifs_dir = "/etc/ldap/ldifs/acls") {
  file { "${ldifs_dir}/${name}":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/acl.ldif.erb"),
  }

  ldap::modify { "${ldifs_dir}/${name}":
    ldif    => "${ldifs_dir}/${name}",
    require => [
      File["${ldifs_dir}/${name}"],
    ],
  }
}