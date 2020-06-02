package com.alps.base.api.model;

import com.alps.base.api.model.entity.SysUser;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
public class UserAccount extends SysUser implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<AlpsAuthority> authorities = Lists.newArrayList();
    /**
     * 第三方账号
     */
    private String thirdParty;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    public Collection<AlpsAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<AlpsAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Collection<Map> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Map> roles) {
        this.roles = roles;
    }
}
