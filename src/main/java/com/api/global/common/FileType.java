package com.api.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FileType {

    POST("post/"),
    RECRUITMENT("recruitment/");

    private final String path;
}
