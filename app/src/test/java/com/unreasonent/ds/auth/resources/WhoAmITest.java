package com.unreasonent.ds.auth.resources;

import com.google.common.collect.Maps;
import com.unreasonent.ds.auth.User;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WhoAmITest {
    private final WhoAmI resource = new WhoAmI();

    @Test
    public void returnsIdentity() {
        User user = new User("bob", Maps.newHashMap());

        assertThat(resource.whoAmI(user), equalTo(user));
    }
}
