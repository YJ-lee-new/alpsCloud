package com.alps.base.api.model;

import com.alps.base.api.model.entity.SysMenu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo菜单权限
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityMenu extends SysMenu implements Serializable {

    private static final long serialVersionUID = 3474271304324863160L;
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;


    private List<AuthorityAction> actionList;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public List<AuthorityAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<AuthorityAction> actionList) {
        this.actionList = actionList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof AuthorityMenu)) {
            return false;
        }
        AuthorityMenu a = (AuthorityMenu) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
