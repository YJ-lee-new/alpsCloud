package com.alps.platform.log.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alps.common.constant.UaaConstant;
import com.alps.common.utils.auth.StringUtils2;


public class TokenUtil {

	public static String getToken (){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String header = request.getHeader(UaaConstant.Authorization) ;
		String token = StringUtils2.isBlank(StringUtils2.substringAfter(header, OAuth2AccessToken.BEARER_TYPE+" ")) ? request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) :  StringUtils2.substringAfter(header, OAuth2AccessToken.BEARER_TYPE +" ");
		
		token = StringUtils2.isBlank(request.getHeader(UaaConstant.TOKEN_HEADER)) ? token : request.getHeader(UaaConstant.TOKEN_HEADER) ;
		
		
		return token ;

	}
	
}
