package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.alps.common.enums.AbstractEntity;
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
public class BaseMenu extends AbstractEntity {
    private static final long serialVersionUID = -4414780909980518788L;
    /**
     * 菜单Id
     */
    @TableId(type= IdType.ID_WORKER)
    private Long menuId;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 图标
     */
    private String icon;

    /**
     * 父级菜单
     */
    private Long parentId;

    /**
     * 请求协议:/,http://,https://
     */
    private String scheme;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    private String target;

    /**
     * 优先级 越小越靠前
     */
    private Integer priority;

    /**
     * 描述
     */
    private String menuDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;

    /**
     * 服务ID
     */
    private String serviceId;


    /** 组件路径 */
    private String component;

    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 类型（M目录 C菜单 F按钮） */
    private String menuType;

    /** 权限字符串 */
    private String perms;


    /** 子菜单 */
    private List<BaseMenu> children = new ArrayList<BaseMenu>();

}
