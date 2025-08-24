package com.complete.api.gateway.enums;

import java.util.HashMap;
import java.util.Map;

public enum Permissions {
    CAN_VIEW_USERS("CAN_VIEW_USERS"),
    CAN_DELETE_USERS("CAN_DELETE_USERS"),
    CAN_ADD_ROLE("CAN_ADD_ROLE"),
    CAN_UPDATE_ROLE("CAN_UPDATE_ROLE"),
    CAN_DELETE_ROLE("CAN_DELETE_ROLE");

    public final String label;
    private static final Map<String, Permissions> map = new HashMap<>();

    static {
        for (Permissions e : values()) {
            map.put(e.label, e);
        }
    }

    private Permissions(String label) {
        this.label = label;

    }

    public static Permissions valueOfName(String label) {
        return map.get(label);
    }
}
