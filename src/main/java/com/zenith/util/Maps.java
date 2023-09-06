package com.zenith.util;

import java.util.HashMap;
import java.util.Map;

public final class Maps {
    private Maps() { }

    public static <K, V> Map<K, V> of(K k1, V v1) {
        final Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        final Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}
