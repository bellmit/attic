define glassfish::domain(
    $ensure,
    $user           = 'glassfish',
    $group          = 'glassfish',
    $admin_user     = 'admin',
    $admin_password = undef
) {
    file { "/opt/glassfish/glassfish3/glassfish/domains/.$title.passwords":
        ensure  => $ensure ? {
            'present' => 'file',
            'running' => 'file',
            'stopped' => 'file',
            default   => $ensure,
        },
        owner   => $user,
        group   => $group,
        mode    => 0400,
        content => template('glassfish/passwords.erb'),
    }

    if ($ensure == 'present') or ($ensure == 'running') {
        exec { "asadmin create-domain $title":
            command => "asadmin --user $admin_user --passwordfile /opt/glassfish/glassfish3/glassfish/domains/.$title.passwords create-domain --savelogin $title",
            path    => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            user    => $user,
            group   => $group,
            creates => "/opt/glassfish/glassfish3/glassfish/domains/$title",
            require => [
                File['/usr/local/bin/asadmin'],
                File["/opt/glassfish/glassfish3/glassfish/domains/.$title.passwords"],
                Class['java'],
            ],
        }
    }

    if ($ensure == 'absent') or ($ensure == 'stopped') {
        exec { "asadmin stop-domain $title":
            path    => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            user    => $user,
            group   => $group,
            onlyif  => "asadmin list-domains | grep -q -F '$title running'",
            require => [
                File['/usr/local/bin/asadmin'],
                Class['java'],
            ]
        }
    }

    if ($ensure == 'running') {
        exec { "asadmin start-domain $title":
            path    => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            user    => $user,
            group   => $group,
            onlyif  => "asadmin list-domains | grep -q -F '$title not running'",
            require => [
                File['/usr/local/bin/asadmin'],
                Exec["asadmin create-domain $title"],
                Class['java'],
            ]
        }
    }

    if ($ensure == 'absent') {
        exec { "asadmin delete-domain $title":
            path    => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            user    => $user,
            group   => $group,
            onlyif  => "[ -d /opt/glassfish/glassfish3/glassfish/domains/$title ]",
            require => [
                File['/usr/local/bin/asadmin'],
                Exec["asadmin stop-domain $title"],
                Class['java'],
            ]
        }
    }
}