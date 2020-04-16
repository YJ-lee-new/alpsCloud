package com.alps.common.oauth2.exception;

public class AlpsAlertException extends AlpsException {
    private static final long serialVersionUID = 4908906410210213271L;

    public AlpsAlertException() {
    }

    public AlpsAlertException(String msg) {
        super(msg);
    }

    public AlpsAlertException(int code, String msg) {
        super(code, msg);
    }

    public AlpsAlertException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
