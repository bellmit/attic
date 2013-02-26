define glassfish::service(
    $ensure,
    $user = 'glassfish'
) {
    if ($ensure == present) {
        exec { "asadmin create-service $title":
            command => "asadmin --terse create-service --serviceuser $user $title",
            user    => root,
            path    => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            creates => "/etc/init.d/GlassFish_$title",
            require => [
                File['/usr/local/bin/asadmin'],
                Class['java'],
            ],
        }

        file { "/etc/init.d/GlassFish_$title":
            ensure  => file,
            owner   => root,
            group   => root,
            mode    => 0555,
            require => Exec["asadmin create-service $title"],
        }
    }

    if ($ensure == absent) {
        exec { "rm -f /etc/init.d/GlassFish_$title":
            path   => '/usr/local/bin:/usr/sbin:/sbin:/usr/bin:/bin',
            onlyif => "[ -e /etc/init.d/GlassFish_$title ]",
        }
    }
}