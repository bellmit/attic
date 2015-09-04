package com.loginbox.app.setup.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.views.FormConvention;
import io.dropwizard.validation.MinSize;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import java.util.Objects;

public class SetupForm extends FormConvention {
    private final String username;
    private final String contactEmail;
    private final String password;
    private final String confirmPassword;

    public SetupForm(CsrfToken csrfToken) {
        super(csrfToken, "setup-form.ftl");
        this.username = null;
        this.contactEmail = null;
        this.password = null;
        this.confirmPassword = null;
    }

    @JsonCreator
    public SetupForm(
            @JsonProperty("username") @FormParam("username") String username,
            @JsonProperty("contactEmail") @FormParam("contactEmail") String contactEmail,
            @JsonProperty("password") @FormParam("password") String password,
            @JsonProperty("confirmPassword") @FormParam("confirmPassword") String confirmPassword,
            @JsonProperty("csrfToken") @FormParam("csrfToken") CsrfToken csrfToken) {
        super(csrfToken, "setup-form.ftl");
        this.username = username;
        this.contactEmail = contactEmail;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @NotNull
    @Length(min=1)
    public String getUsername() {
        return username;
    }

    @NotNull
    @Email(regexp = ".+@[^@]+")
    public String getContactEmail() {
        return contactEmail;
    }

    @JsonIgnore
    @NotNull
    @Length(min=8)
    public String getPassword() {
        return password;
    }

    @ValidationMethod(message="confirmation password did not match password")
    @JsonIgnore
    public boolean isPasswordsMatch() {
        return Objects.equals(getPassword(), confirmPassword);
    }

    @JsonIgnore
    public UserInfo userInfo() {
        return new UserInfo(username, contactEmail, password);
    }
}
