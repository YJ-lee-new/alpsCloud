package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.alps.common.enums.AbstractEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 系统用户-登录账号
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_user")
public class BaseSysUser extends AbstractEntity
{
	
	private static final long serialVersionUID = -735161640894047414L;
    /**
     * 系统用户ID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long userId;

    /**
     * 登陆名
     */
    private String userName;

    /**
     * 用户类型:super-超级管理员 normal-普通管理员
     */
    private String userType;

    /**
     * 企业ID
     */
    private String companyId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 描述
     */
    private String userDesc;

    /**
       * 密码凭证：站内的保存密码、站外的不保存或保存token）
     */
    private String password;


    /**
     * 状态:0-禁用 1-正常 2-锁定
     */
    private Integer status;
	
//--------uers//
    
    /**
     * 标识：手机号、邮箱、 系统用户名、或第三方应用的唯一标识
     */
    private String account;

    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    private String accountType;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 账号域
     */
    private String domain;

    
 //account----//   
    
    private String salt;
	

    /** 部门ID */
    private Long deptId;

    private String delFlag;
    
    private String loginId;

    /** 用户性别 */
    private String sex;
    
    
    public BaseSysUser(Long userId, String account, String password, String accountType, String domain, String registerIp) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.accountType = accountType;
        this.domain = domain;
        this.registerIp = registerIp;
    }
    
}
