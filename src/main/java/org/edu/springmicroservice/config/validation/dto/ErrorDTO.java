package org.edu.springmicroservice.config.validation.dto;

public class ErrorDTO {

    private String field;
    private String msg;

    public ErrorDTO(String field, String msg) {
        this.field = field;
        this.msg = msg;
    }

    public ErrorDTO() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
