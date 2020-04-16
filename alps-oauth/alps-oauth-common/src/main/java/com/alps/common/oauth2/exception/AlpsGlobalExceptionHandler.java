package com.alps.common.oauth2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.common.core.domain.ResultBody;
import com.alps.common.enums.ErrorCodeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class AlpsGlobalExceptionHandler {


    /**
     * 统一异常处理
     * AuthenticationException
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({AuthenticationException.class})
    public static ResultBody authenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * OAuth2Exception
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({OAuth2Exception.class, InvalidTokenException.class})
    public static ResultBody oauth2Exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 自定义异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({AlpsException.class})
    public static ResultBody alpsException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 其他异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public static ResultBody exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static ResultBody resolveException(Exception ex, String path) {
        ErrorCodeEnum code = ErrorCodeEnum.ERROR;
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = ex.getMessage();
        String superClassName = ex.getClass().getSuperclass().getName();
        String className = ex.getClass().getName();
        if (className.contains("UsernameNotFoundException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.USERNAME_NOT_FOUND;
        } else if (className.contains("BadCredentialsException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.BAD_CREDENTIALS;
        } else if (className.contains("AccountExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.ACCOUNT_EXPIRED;
        } else if (className.contains("LockedException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.ACCOUNT_LOCKED;
        } else if (className.contains("DisabledException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.ACCOUNT_DISABLED;
        } else if (className.contains("CredentialsExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.CREDENTIALS_EXPIRED;
        } else if (className.contains("InvalidClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.INVALID_CLIENT;
        } else if(className.contains("NoSuchClientException")) { 
        	httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.ALPS404;
        }else if (className.contains("UnauthorizedClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.UNAUTHORIZED_CLIENT;
        }else if (className.contains("InsufficientAuthenticationException") || className.contains("AuthenticationCredentialsNotFoundException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.UNAUTHORIZED;
        } else if (className.contains("InvalidGrantException")) {
            code = ErrorCodeEnum.ALERT;
            if ("Bad credentials".contains(message)) {
                code = ErrorCodeEnum.BAD_CREDENTIALS;
            } else if ("User is disabled".contains(message)) {
                code = ErrorCodeEnum.ACCOUNT_DISABLED;
            } else if ("User account is locked".contains(message)) {
                code = ErrorCodeEnum.ACCOUNT_LOCKED;
            }
        } else if (className.contains("InvalidScopeException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.INVALID_SCOPE;
        } else if (className.contains("InvalidTokenException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCodeEnum.INVALID_TOKEN;
        } else if (className.contains("InvalidRequestException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCodeEnum.INVALID_REQUEST;
        } else if (className.contains("RedirectMismatchException")) {
            code = ErrorCodeEnum.REDIRECT_URI_MISMATCH;
        } else if (className.contains("UnsupportedGrantTypeException")) {
            code = ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE;
        } else if (className.contains("UnsupportedResponseTypeException")) {
            code = ErrorCodeEnum.UNSUPPORTED_RESPONSE_TYPE;
        } else if (className.contains("UserDeniedAuthorizationException")) {
            code = ErrorCodeEnum.ACCESS_DENIED;
        } else if (className.contains("AccessDeniedException")) {
            code = ErrorCodeEnum.ACCESS_DENIED;
            httpStatus = HttpStatus.FORBIDDEN.value();
            if (ErrorCodeEnum.ACCESS_DENIED_BLACK_LIMITED.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_BLACK_LIMITED;
            } else if (ErrorCodeEnum.ACCESS_DENIED_WHITE_LIMITED.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_WHITE_LIMITED;
            } else if (ErrorCodeEnum.ACCESS_DENIED_AUTHORITY_EXPIRED.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_AUTHORITY_EXPIRED;
            }else if (ErrorCodeEnum.ACCESS_DENIED_UPDATING.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_UPDATING;
            }else if (ErrorCodeEnum.ACCESS_DENIED_DISABLED.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_DISABLED;
            } else if (ErrorCodeEnum.ACCESS_DENIED_NOT_OPEN.msg().contains(message)) {
                code = ErrorCodeEnum.ACCESS_DENIED_NOT_OPEN;
            }
        } else if (className.contains("HttpMessageNotReadableException")
                || className.contains("TypeMismatchException")
                || className.contains("MissingServletRequestParameterException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCodeEnum.BAD_REQUEST;
        } else if (className.contains("NoHandlerFoundException")) {
            httpStatus = HttpStatus.NOT_FOUND.value();
            code = ErrorCodeEnum.NOT_FOUND;
        } else if (className.contains("HttpRequestMethodNotSupportedException")) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
            code = ErrorCodeEnum.METHOD_NOT_ALLOWED;
        } else if (className.contains("HttpMediaTypeNotAcceptableException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCodeEnum.MEDIA_TYPE_NOT_ACCEPTABLE;
        } else if (className.contains("MethodArgumentNotValidException")) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            code = ErrorCodeEnum.ALERT;
            return ResultBody.failed().code(code.code()).msg(bindingResult.getFieldError().getDefaultMessage());
        } else if (className.contains("IllegalArgumentException")) {
            //参数错误
            code = ErrorCodeEnum.ALERT;
            httpStatus = HttpStatus.BAD_REQUEST.value();
        } else if (className.contains("AlpsAlertException")) {
            code = ErrorCodeEnum.ALERT;
        } else if (className.contains("AlpsSignatureException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCodeEnum.SIGNATURE_DENIED;
        }else if(message.equalsIgnoreCase(ErrorCodeEnum.TOO_MANY_REQUESTS.name())){
            code = ErrorCodeEnum.TOO_MANY_REQUESTS;
        }
        return buildBody(ex, code, path, httpStatus);
    }

    /**
     * 构建返回结果对象
     *
     * @param exception
     * @return
     */
    private static ResultBody buildBody(Exception exception, ErrorCodeEnum resultCode, String path, int httpStatus) {
        if (resultCode == null) {
            resultCode = ErrorCodeEnum.ERROR;
        }
        ResultBody resultBody = ResultBody.failed().code(resultCode.code()).msg(exception.getMessage()).path(path).httpStatus(httpStatus);
        log.error("==> error:{} exception: {}",resultBody, exception);
        return resultBody;
    }

}
