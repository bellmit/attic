package com.loginbox.collections;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MapLiteralTest {
    @Test
    public void mapLiteral() {
        HashMap<String, String> expected = new HashMap<>();
        expected.put("3", "three");
        expected.put("5", "five");

        HashMap<String, String> actual = MapLiteral.hashMap(
                MapLiteral.entry("3", "three"),
                MapLiteral.entry("5", "five")
        );

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void emptyMapLiteral() {
        HashMap<String, String> actual = MapLiteral.hashMap();

        assertThat(actual.entrySet(), is(empty()));
    }
}
