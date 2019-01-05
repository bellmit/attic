package com.loginbox.app.mime;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TypesTest {
    @Test
    public void loginBoxTypes() {
        assertThat(Types.APPLICATION_LOGIN_BOX,
                equalTo(Types.APPLICATION_LOGIN_BOX_TYPE.toString()));
    }
}