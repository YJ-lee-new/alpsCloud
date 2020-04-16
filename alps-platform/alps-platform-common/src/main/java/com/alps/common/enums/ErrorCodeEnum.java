package com.alps.common.enums;

/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * Todo 定义alps统一错误信息
 */
public enum ErrorCodeEnum {
	OK(0, "success"),
    FAIL(1, "fail"),
    
	ALPS403(403, "访问被拒绝"),

	ALPS401(401, "Token is invalid!"),
	
	ALPS406(406, "请求未授权"),
	
	ALPS500(500, "未知异常"),
	
	ALPS404(1404, "No client with requested"),
	
	/**
     * oauth2返回码
     */
    INVALID_TOKEN(100, "invalid_token"),
    INVALID_SCOPE(101, "invalid_scope"),
    INVALID_REQUEST(102, "invalid_request"),
    INVALID_CLIENT(103, "invalid_client"),
    INVALID_GRANT(104, "invalid_grant"),
    REDIRECT_URI_MISMATCH(105, "redirect_uri_mismatch"),
    UNAUTHORIZED_CLIENT(106, "unauthorized_client"),
    EXPIRED_TOKEN(107, "expired_token"),
    UNSUPPORTED_GRANT_TYPE(108, "unsupported_grant_type"),
    UNSUPPORTED_RESPONSE_TYPE(109, "unsupported_response_type"),
    UNAUTHORIZED(110, "unauthorized"),
    SIGNATURE_DENIED(111, "signature_denied"),

    ACCESS_DENIED(403, "access_denied"),
    ACCESS_DENIED_BLACK_LIMITED(4031, "access_denied_black_limited"),
    ACCESS_DENIED_WHITE_LIMITED(4032, "access_denied_white_limited"),
    ACCESS_DENIED_AUTHORITY_EXPIRED(4033, "access_denied_authority_expired"),
    ACCESS_DENIED_UPDATING(4034, "access_denied_updating"),
    ACCESS_DENIED_DISABLED(4035, "access_denied_disabled"),
    ACCESS_DENIED_NOT_OPEN(4036, "access_denied_not_open"),
    /**
     * 账号错误
     */
    BAD_CREDENTIALS(300, "bad_credentials"),
    ACCOUNT_DISABLED(301, "account_disabled"),
    ACCOUNT_EXPIRED(302, "account_expired"),
    CREDENTIALS_EXPIRED(303, "credentials_expired"),
    ACCOUNT_LOCKED(304, "account_locked"),
    USERNAME_NOT_FOUND(305, "username_not_found"),

    /**
     * 请求错误
     */
    BAD_REQUEST(400, "bad_request"),
    NOT_FOUND(404, "not_found"),
    METHOD_NOT_ALLOWED(405, "method_not_allowed"),
    MEDIA_TYPE_NOT_ACCEPTABLE(406, "media_type_not_acceptable"),
    TOO_MANY_REQUESTS(4029, "too_many_requests"),
    /**
     * 系统错误
     */
    ERROR(500, "error"),
    GATEWAY_TIMEOUT(504, "gateway_timeout"),
    SERVICE_UNAVAILABLE(5050303, "service_unavailable"),
	
 /**
  * alert
  */
    ALERT(1001, "alert");
    
	private int code;
	private String msg;

	/**
	 * Msg string.
	 *
	 * @return the string
	 */
	public String msg() {
		return msg;
	}

	/**
	 * Code int.
	 *
	 * @return the int
	 */
	public int code() {
		return code;
	}

	ErrorCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * Gets enum.
	 *
	 * @param code the code
	 *
	 * @return the enum
	 */
	public static ErrorCodeEnum getEnum(int code) {
		for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
			if (ele.code() == code) {
				return ele;
			}
		}
		return null;
	}
	
	 public static ErrorCodeEnum getEnum(String message) {
	        for (ErrorCodeEnum type : ErrorCodeEnum.values()) {
	            if (type.msg().equals(message)) {
	                return type;
	            }
	        }
	        return ALPS500;
	    }
	
	
}
