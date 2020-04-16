package com.alps.common.exception;

import com.alps.common.enums.ErrorCodeEnum;

/**
 * 业务异常
 * 
 * @author : yujie.lee
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    protected int code;
    
    public BusinessException(String message)
    {
    	super(message);
    }

    public BusinessException(String message, Throwable e)
    {
        super(message, e);
    }

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
	}

	public BusinessException(ErrorCodeEnum codeEnum, Object... args) {
		super(String.format(codeEnum.msg(), args));
		this.code = codeEnum.code();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
