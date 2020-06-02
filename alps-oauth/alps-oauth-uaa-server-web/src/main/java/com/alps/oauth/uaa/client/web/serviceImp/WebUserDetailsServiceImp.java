package com.alps.oauth.uaa.client.web.serviceImp;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alps.base.api.model.AlpsConstants;
import com.alps.base.api.model.UserAccount;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.common.oauth2.utils.WebUtils;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
/**
 * @author:Yujie.lee
 * Date:2020年2月14日
 */
@Service
@Slf4j
public class WebUserDetailsServiceImp implements UserDetailsService{

	@Autowired
	private ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	System.out.println("=========================> 开始鉴权 +" + username);
    	 Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
         String loginType = parameterMap.get("login_type");
         String domain = parameterMap.get("domain");
    	UserAccount account= userService.login(username,loginType,domain);
        if (account == null || account.getUserId() == null) {
        	log.info("系统用户 " + username + " 不存在!");
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        domain = account.getDomain();
        Long accountId = account.getUserId();
        Long userId = account.getUserId();
        String password = account.getPassword();
        String nickName = account.getNickName();
        String avatar = account.getAvatar();
        String accountType = account.getAccountType();
        boolean accountNonLocked = account.getStatus() != AlpsConstants.ACCOUNT_STATUS_LOCKED;
        boolean credentialsNonExpired = true;
        boolean enabled = account.getStatus() == AlpsConstants.ACCOUNT_STATUS_NORMAL ? true : false;
        boolean accountNonExpired = true;
        AlpsUserDetails userDetails = new AlpsUserDetails();
        userDetails.setDomain(domain);
        userDetails.setAccountId(accountId);
        userDetails.setUserId(userId);
        userDetails.setUsername(username);
        userDetails.setPassword(password);
        userDetails.setNickName(nickName);
        userDetails.setAuthorities(account.getAuthorities());
        userDetails.setAvatar(avatar);
        userDetails.setAccountId(accountId);
        userDetails.setAccountNonLocked(accountNonLocked);
        userDetails.setAccountNonExpired(accountNonExpired);
        userDetails.setAccountType(accountType);
        userDetails.setCredentialsNonExpired(credentialsNonExpired);
        userDetails.setEnabled(enabled);
        return userDetails;
    }

}
