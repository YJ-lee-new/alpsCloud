package com.alps.platform.log.model;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.alps.common.constant.UaaConstant;
import com.alps.common.utils.auth.TokenUtil;
import com.alps.common.utils.spring.SpringUtils;


/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 获取用户信息
 */
public class SysUserUtil {

	/**
	 * 获取登陆的 LoginAppUser
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static LoginAppUser getLoginAppUser() {
		
		// 当OAuth2AuthenticationProcessingFilter设置当前登录时，直接返回
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 当内部服务，不带token时，内部服务
		String accessToken = TokenUtil.getToken();
		RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
		Map<String, Object> params = (Map<String, Object>) redisTemplate.opsForValue()
				.get(UaaConstant.TOKEN + ":" + accessToken);
		if (params != null) {
			return (LoginAppUser) params.get(UaaConstant.AUTH);
		}

		return null;
	}
}
