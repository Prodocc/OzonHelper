package com.example.OzonHelper.enums;

public enum MaxApiEndpoint {
    BOT_INFO("/me"),
    SUBSCRIPTIONS("/subscriptions"),
    MESSAGES("/messages");

    private final String path;

    MaxApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String host) {
        return host + this.path;
    }
}
