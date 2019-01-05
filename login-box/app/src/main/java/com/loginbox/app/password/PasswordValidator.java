package com.loginbox.app.password;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordValidator {
    private final int workFactor;

    public PasswordValidator(int workFactor) {
        this.workFactor = workFactor;
    }

    public String digest(String password) {
        String salt = newSalt();
        String digest = BCrypt.hashpw(password, salt);
        return digest;
    }

    public boolean verify(String candidate, String digest) {
        return BCrypt.checkpw(candidate, digest);
    }

    protected String newSalt() {
        return BCrypt.gensalt(workFactor);
    }
}
