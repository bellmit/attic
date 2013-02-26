class glassfish(
  $version = '3.1.2.2',
) {
    group { 'glassfish':
        ensure => present,
        system => true,
    }

    user { 'glassfish':
        ensure => present,
        system => true,
        home   => '/opt/glassfish',
        gid    => 'glassfish',
        shell  => '/bin/bash',
    }

    file { '/opt/glassfish':
        ensure => directory,
        owner  => glassfish,
        group  => glassfish,
        mode   => 0644,
        require => [
            User['glassfish'],
            Group['glassfish'],
        ],
    }

    file { '/opt/glassfish/distributions':
        ensure  => directory,
        owner   => root,
        group   => root,
        mode    => 0444,
        source  => 'puppet:///modules/glassfish/distributions/',
        recurse => true,
    }

    exec { "unzip /opt/glassfish/distributions/glassfish-$version.zip":
        cwd     => '/opt/glassfish',
        path    => '/usr/bin:/bin',
        creates => '/opt/glassfish/glassfish3',
        require => File['/opt/glassfish/distributions'],
    }

    file { '/usr/local/bin/asadmin':
        ensure  => file,
        owner   => root,
        group   => root,
        mode    => 0555,
        source  => 'puppet:///modules/glassfish/asadmin',
        require => Exec["unzip /opt/glassfish/distributions/glassfish-$version.zip"],
    }

    file { '/opt/glassfish/glassfish3/glassfish/domains':
        ensure => directory,
        owner  => glassfish,
        group  => glassfish,
        mode   => 0640,
        require => [
            Exec["unzip /opt/glassfish/distributions/glassfish-$version.zip"],
            User['glassfish'],
            Group['glassfish'],
        ]
    }

    glassfish_domain { 'domain1':
        ensure  => absent,
        require => [
            File['/usr/local/bin/asadmin'],
            File['/opt/glassfish/glassfish3/glassfish/domains'],
        ],
    }
}
