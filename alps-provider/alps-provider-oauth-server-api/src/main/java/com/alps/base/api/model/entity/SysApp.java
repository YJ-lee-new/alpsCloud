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
 * TodoTODO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_app")
@TableAlias("app")
public class SysApp extends AbstractEntity {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2336134749804191820L;

    @TableId( value = "app_id", type = IdType.AUTO)
    private Long appId;

    /**
     * API访问key
     */
    private String appKey;
    /**
     * API访问密钥
     */
    private String secretKey;

    /**
     * app类型：server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    private String appType;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * app名称
     */
    private String appName;

    /**
     * app英文名称
     */
    private String appNameEn;
    /**
     * 移动应用操作系统：ios-苹果 android-安卓
     */
    private String appOs;


    /**
     * 用户ID:默认为0
     */
    private Long developerId;

    /**
     * app描述
     */
    private String appDesc;

    /**
     * 官方网址
     */
    private String website;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;
    
    private String scope;

}