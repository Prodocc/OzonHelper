package com.example.OzonHelper.enums.max;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AttachmentType {
    IMAGE("image", "Изображение"),
    KEYBOARD("inline_keyboard", "Сообщение или пост с кнопкой");


    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    AttachmentType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
