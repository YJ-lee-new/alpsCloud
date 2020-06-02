package com.alps.base.api.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_company")
@TableAlias("company")
public class SysCompany extends AbstractEntity {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2336134749804191820L;

    @TableId( value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * name
     */
    private String name;
    /**
     * addr
     */
    private String addr;

  
    private String attr;

}