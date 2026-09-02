package com.example.OzonHelper.enums.max;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum UploadType {
    IMAGE("image","Изображение"),
    VIDEO("video","Видео"),
    AUDIO("audio","Аудио"),
    FILE("file","Файл");


    @Getter(onMethod_ = @JsonValue)
    private final String apiValue;
    @Getter
    private final String description;

    UploadType(String apiValue, String description) {
        this.apiValue = apiValue;
        this.description = description;
    }
}
