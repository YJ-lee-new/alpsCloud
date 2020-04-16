package com.alps.zuul.shiro;

import com.alps.common.core.domain.ReturnResult;
import com.alps.provider.system.domain.SysUser;
import com.alps.zuul.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author:Yujie.lee
 * Date:2019年11月2日
 * TodoTODO
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService SysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String name = token.getUsername();
        String password = String.valueOf(token.getPassword());
        // 从数据库获取对应用户名密码的用户
        ReturnResult<SysUser> loginResult = SysUserService.login(name,password);
        if (loginResult.getStatus() == 400) {
            throw new AuthenticationException();
        } else {
            SysUser SysUser = (SysUser) loginResult.getData();
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    SysUser, //用户
                    SysUser.getPassword(), //密码
                    getName()  //realm name
            );
            return authenticationInfo;
        }
    }
}
