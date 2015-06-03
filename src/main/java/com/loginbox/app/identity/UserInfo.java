package com.loginbox.app.identity;

import com.loginbox.app.password.PasswordValidator;

public class UserInfo {
    private final String username;
    private final String contactEmail;
    private final String password;

    public UserInfo(String username, String contactEmail, String password) {
        this.username = username;
        this.contactEmail = contactEmail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo withDigestedPassword(PasswordValidator passwordValidator) {
        String plaintextPassword = getPassword();
        String passwordDigest = passwordValidator.digest(plaintextPassword);
        UserInfo result = new UserInfo(
                getUsername(),
                getContactEmail(),
                passwordDigest
        );
        return result;
    }
}
