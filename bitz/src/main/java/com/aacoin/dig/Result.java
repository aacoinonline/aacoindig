package com.aacoin.dig;

public enum Result {
    //公共部分
    SUCCESS(0, "SUCCESS"),
    FAIL(1, "FAIL");

    public final int code;
    public final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
