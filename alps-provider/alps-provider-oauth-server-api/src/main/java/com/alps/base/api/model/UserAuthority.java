package com.alps.base.api.model;

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
public class UserAuthority implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<AlpsAuthority> authorities = Lists.newArrayList();


    public Collection<AlpsAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<AlpsAuthority> authorities) {
        this.authorities = authorities;
    }

    public Collection<Map> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Map> roles) {
        this.roles = roles;
    }
}
