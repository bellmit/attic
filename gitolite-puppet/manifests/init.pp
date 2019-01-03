class gitolite {
    group { 'git':
        ensure => present,
        system => true,
    }

    user { 'git':
        ensure   => present,
        system   => true,
        gid      => git,
        home     => '/var/lib/gitolite3',
        shell    => '/bin/bash',
        password => '*',
        comment  => 'git repository hosting,,,',
    }

    git::clone { 'git://github.com/sitaramc/gitolite':
        creates => '/opt/gitolite',
    }

    exec { '/opt/gitolite/install':
        command => '/opt/gitolite/install -ln /usr/local/bin',
        path    => '/usr/bin:/bin:/usr/local/bin:/usr/sbin:/sbin:/usr/local/sbin',
        user    => root,
        group   => root,
        creates => '/usr/local/bin/gitolite',
        require => Git::Clone['git://github.com/sitaramc/gitolite'],
    }

    File {
        owner   => git,
        group   => git,
        require => User['git'],
    }

    file { '/var/lib/gitolite3':
        ensure  => directory,
        mode    => 0644,
    }

    file { '/var/lib/gitolite3/admin.pub':
        ensure => file,
        mode   => 0644,
        source => 'puppet:///modules/gitolite/admin.pub',
        notify => Exec['gitolite setup'],
    }

    file { '/var/lib/gitolite3/.gitolite.rc':
        ensure => file,
        mode   => 0644,
        source => 'puppet:///modules/gitolite/gitolite.rc',
        notify => Exec['gitolite setup'],
    }

    exec { 'gitolite setup':
        # Aww, man, no umask param? http://projects.puppetlabs.com/issues/4424
        # The naive solution (without bash -c and quoting hell) gives
        # err: […]/Exec[gitolite setup]: Failed to call refresh: Could not find command 'umask'
        command     => "bash -c 'umask 0027 && gitolite setup --pubkey admin.pub'",
        path        => '/usr/bin:/bin:/usr/local/bin:/usr/sbin:/sbin:/usr/local/sbin',
        environment => [
            'HOME=/var/lib/gitolite3',
            'USER=git',
        ],
        user        => git,
        group       => git,
        cwd         => '/var/lib/gitolite3',
        refreshonly => true,
        require     => Exec['/opt/gitolite/install'],
    }

    file { '/var/lib/gitolite3/repositories/gitolite-admin.git/description':
        ensure  => absent,
        require => Exec['gitolite setup'],
    }
}
