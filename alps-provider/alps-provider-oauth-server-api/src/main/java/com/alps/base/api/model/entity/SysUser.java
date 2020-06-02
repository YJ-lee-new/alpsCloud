package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class SysUser extends AbstractEntity
{
	
	private static final long serialVersionUID = -735161640894047414L;
    /**
     * 系统用户ID
     */
    @TableId( value = "user_id", type = IdType.AUTO)
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
       * 密码凭证：站内的保存密码、站外的不保存或保存token）
     */
    private String password;
    
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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


    /** 部门ID */
    private Long deptId;
    
    /** 用户性别 */
    private String sex;
    
    /** 角色组 */
    @TableField(exist=false)
    private  Long[] roleIds;
    
    @TableField(exist=false)
    private SysDept dept;
    
    @TableField(exist=false)
    private Long roleId;
    
    public SysUser(Long userId,String userName, String account, String password, String accountType, String domain, String registerIp) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.accountType = accountType;
        this.domain = domain;
        this.registerIp = registerIp;
    }

    
}
