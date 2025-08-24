package com.complete.api.gateway.enums;

import java.util.HashMap;
import java.util.Map;

public enum DefaultRoles {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    public final String label;
    private static final Map<String, DefaultRoles> map = new HashMap<>();

    static {
        for (DefaultRoles e : values()) {
            map.put(e.label, e);
        }
    }

    private DefaultRoles(String label) {
        this.label = label;

    }

    public static DefaultRoles valueOfName(String label) {
        return map.get(label);
    }
}
