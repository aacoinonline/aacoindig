package com.aacoin.dig;

public class ResourceException extends Exception {
    private int code = 1;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public void code(int code) {
        this.code = code;
    }

    public ResourceException(int errorCode, String message) {
        super(message);
        code(errorCode);
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause.getMessage(), cause);
//      add by zhouhe 1201 [correct exception should be add the setting just as follow]
//        if (cause instanceof ServiceException) {
//            ServiceException se = (ServiceException) cause;
//            code = se.getCode();
//        }        if (cause instanceof ServiceException) {
//            ServiceException se = (ServiceException) cause;
//            code = se.getCode();
//        }
    }
}
