package com.pagonxt.onetradefinance.integrations.model;


public class ErrorSantander {

    private String code;
    private String message;
    private String level;
    private String description;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ErrorSantander{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                '}';
    }
}
