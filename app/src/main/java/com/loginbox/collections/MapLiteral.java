package com.loginbox.collections;

import java.util.HashMap;

/**
 * Support tools for creating maps without syntactic noise _or_ the awful initializer hack.
 */
public final class MapLiteral {
    private MapLiteral() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new HashMap with the given entries. For example:
     * <pre>
     *     HashMap&lt;Integer, String&gt; m = hashMap(
     *         entry(1, "foo"),
     *         entry(3, "bar")
     *     );
     * </pre>
     *
     * @param entries
     *         a sequence of map entries, usually from {@link #entry}.
     * @param <K>
     *         the key type of the resulting map.
     * @param <V>
     *         the value type of the resulting map.
     * @return a map with all of the passed entries populated into it.
     */
    @SafeVarargs
    public static <K, V> HashMap<K, V> hashMap(Entry<K, V>... entries) {
        HashMap<K, V> map = new HashMap<>();
        for (Entry<K, V> entry : entries)
            entry.storeTo(map);
        return map;
    }

    public static <K, V> Entry<K, V> entry(K key, V value) {
        return new Entry<>(key, value);
    }

    public static class Entry<K, V> {
        private final K key;
        private final V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private void storeTo(HashMap<K, V> map) {
            map.put(key, value);
        }
    }
}
