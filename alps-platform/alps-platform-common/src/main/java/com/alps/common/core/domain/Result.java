package com.alps.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.alps.common.utils.CodeUtils;

/**
 * @author:Yujie.lee
 * Date:2019年11月20日
 * TodoTODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T datas;
    private Integer resp_code;
    private String resp_msg;


	public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeUtils.SUCCESS.getCode(),msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, CodeUtils.SUCCESS.getCode(),msg);
    }

    public static <T> Result<T> succeedWith(T datas, Integer code,String msg) {
        return new Result<T>(datas, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, CodeUtils.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T model,String msg) {
        return failedWith(model, CodeUtils.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T datas, Integer code, String msg) {
        return new Result<T>( datas, code, msg);
    }
}
