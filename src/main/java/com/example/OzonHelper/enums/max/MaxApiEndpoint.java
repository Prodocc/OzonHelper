package com.example.OzonHelper.enums.max;

public enum MaxApiEndpoint {
    BOT_INFO("/me"),
    SUBSCRIPTIONS("/subscriptions"),
    MESSAGES("/messages"),
    UPLOADS("/uploads");

    private final String path;

    MaxApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String host) {
        return host + this.path;
    }
}
