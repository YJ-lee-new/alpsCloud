package com.alps.base.api.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 

* <p>Title: SysDept</p>  

* <p>Description: </p>  

* @author YJ.lee  

* @date 2020年4月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_dept")
public class SysDept extends AbstractEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.ID_WORKER)
    /** 部门ID */
    private Long deptId;

    /** 父部门ID */
    private Long parentId;
    
    /** 公司ID */
    private Long companyId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    private String deptName;

    /** 显示顺序 */
    private String orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;
    
    /** 邮箱 */
    private String email;

    /** 部门状态:1正常,0停用 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父部门名称 */
    @TableField(exist=false)
    private String parentName;
}
