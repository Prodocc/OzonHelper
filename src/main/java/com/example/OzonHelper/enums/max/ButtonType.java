package com.example.OzonHelper.enums.max;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ButtonType {
    CALLBACK("callback"),
    LINK("link"),
    REQUEST_CONTACT("request_contact"),
    REQUEST_GEO_LOCATION("request_geo_location"),
    OPEN_APP("open_app"),
    MESSAGE("message"),
    CLIPBOARD("clipboard");

    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;

    ButtonType(String apiValue) {
        this.apiValue = apiValue;
    }
}
