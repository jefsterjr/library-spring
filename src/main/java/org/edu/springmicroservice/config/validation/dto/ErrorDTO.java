package org.edu.springmicroservice.config.validation.dto;

public class ErrorDTO {
    private String msg;

    public ErrorDTO(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
