package com.loginbox.build

class Versions {
    static String detect() {
        envVersion() ?: gitVersion() ?: defaultVersion()
    }

    static String envVersion() {
        System.getenv('SOURCE_VERSION')
    }

    static String gitVersion() {
        try {
            "git rev-parse HEAD".execute().text.trim()
        } catch (IOException ioe) {
            null
        }
    }

    static String defaultVersion() {
        "UNKNOWN"
    }
}