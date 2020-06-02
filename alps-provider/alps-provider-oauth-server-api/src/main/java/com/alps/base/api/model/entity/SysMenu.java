package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 系统资源-菜单信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_menu")
public class SysMenu extends AbstractEntity {
    private static final long serialVersionUID = -4414780909980518788L;
    /**
     * 菜单Id
     */
    @TableId(type= IdType.ID_WORKER)
    private Long menuId;
    
    /** 显示顺序 */
    private Integer orderNum;


    /**
     * 菜单名称
     */
    private String menuName;
    
    
    /**
     * 菜单代码
     */
    private String menuCode;
    
    /**
     * 事件对应的API
     */
    private Long apiId;
    
    /**
     * 菜单名称
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 父级菜单
     */
    private Long parentId;
    
    /** 父菜单名称 */
    @TableField(exist=false)
    private String parentName;

    /**
     * 请求协议:/,http://,https://
     */
    private String scheme;

    /** 菜单URL */
    private String url;

    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    private String target;


    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 服务ID
     */
    private String serviceId;


    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 类型（M目录 C菜单 F按钮） */
    private String menuType;
    
    @TableField(exist=false)
    private String apiName;


    /** 子菜单 */
    @TableField(exist=false)
    private List<SysMenu> children = new ArrayList<SysMenu>();

}
