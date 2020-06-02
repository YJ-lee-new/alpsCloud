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
 * Todo 系统资源-功能操作
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_aciton")
public class SysAction extends AbstractEntity {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 资源ID
     */
    @TableId( value = "action_id", type = IdType.AUTO)
	private Long actionId;
	/** 事件/action名称 */
	private String actionCode;
	/** 事件的名字，支持国际化 */
	private String actionNameEn;
	/**  */
	private String actionNameCn;
	/**  */
	private String actionNameTw;
	/**  */
	private String actionDesc;
	/**  */
	private String apiId;
	/** 是否有效 */
	private Integer status;
}
