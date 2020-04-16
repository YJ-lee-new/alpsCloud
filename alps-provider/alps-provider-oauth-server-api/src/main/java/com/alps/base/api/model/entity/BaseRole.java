package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.alps.common.enums.AbstractEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_role")
public class BaseRole extends AbstractEntity {
    private static final long serialVersionUID = 5197785628543375591L;
    /**
     * 角色ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long roleId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

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
    private Integer isPersist;
}
