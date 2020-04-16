package com.alps.common.oauth2.exception;

import com.alps.common.enums.ErrorCodeEnum;

/**
 * 基础错误异常
 *
 * @author Yujie .lee
 */
public class AlpsException extends RuntimeException {

    private static final long serialVersionUID = 3655050728585279326L;

    private int code = ErrorCodeEnum.ERROR.code();

    public AlpsException() {

    }

    public AlpsException(String msg) {
        super(msg);
    }

    public AlpsException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public AlpsException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
