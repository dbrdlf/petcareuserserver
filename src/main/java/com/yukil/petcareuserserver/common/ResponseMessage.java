package com.yukil.petcareuserserver.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseMessage<T> {
    private T data;
    private LocalDateTime timestamp;

    public ResponseMessage(T data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
