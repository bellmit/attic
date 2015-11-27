package com.loginbox.app.password.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.app.config.Environment;
import com.loginbox.app.password.PasswordValidator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PasswordValidatorFactory {
    @Min(4)
    @Max(30)
    private int workFactor = Environment.config("BCRYPT_WORK_FACTOR", Integer::valueOf, 10);

    @JsonProperty
    public int getWorkFactor() {
        return workFactor;
    }

    public PasswordValidator build() {
        return new PasswordValidator(workFactor);
    }
}
