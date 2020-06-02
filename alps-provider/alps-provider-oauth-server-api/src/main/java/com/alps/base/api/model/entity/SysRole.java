package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_role")
public class SysRole extends AbstractEntity {
    private static final long serialVersionUID = 5197785628543375591L;
    /**
     * 角色ID
     */
    @TableId(type= IdType.AUTO )
    private Long roleId;

    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色编码
     */
    private Integer roleSort;

    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色名称
     */
    private String companyId;
    
    /**
     * 权限类型
     */
    private String authorityTpye;
    
    /**
     * 角色名称
     */
    private Integer dataScope;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer delFlag;
    
    /** 菜单组 */
    @TableField(exist=false)
    private Long menuId;
    
	/** action组 */
    @TableField(exist=false)
    private Long[] actions;
    
    /** action组 */
    @TableField(exist=false)
    private Long[] menuIds;
    
    /** 是否选中 */
    @TableField(exist=false)
    private boolean selectflag = false;
}
