class bamboo::maven {
  require 'bamboo::user'

  file { "${bamboo::user::home}/.m2":
    ensure  => directory,
    owner   => bamboo,
    group   => bamboo,
    mode    => 0640,
  }

  file { "${bamboo::user::home}/.m2/settings.xml":
    ensure  => file,
    owner   => bamboo,
    group   => bamboo,
    mode    => 0440,
    source  => 'puppet:///modules/jenkins/m2/settings.xml',
  }
}
