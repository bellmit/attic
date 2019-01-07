define ldap::module($ldifs_dir = "/etc/ldap/ldifs/modules") {
  file { "${ldifs_dir}/${name}.ldif":
    owner   => root,
    group   => root,
    mode    => 440,
    content => template("ldap/module.ldif.erb"),
  }

  ldap::add { "modules/${name}.ldif":
    base    => "cn=config",
    filter  => "(&(objectClass=olcModuleList)(olcModuleLoad=${name}))",
    require => [
      File["${ldifs_dir}/${name}.ldif"],
    ],
  }
  
}