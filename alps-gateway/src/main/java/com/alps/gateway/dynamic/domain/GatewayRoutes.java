package com.alps.gateway.dynamic.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("gateway_route")
public class GatewayRoutes extends AbstractEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5569650232021611612L;

	private String id;

    private String uri;

    private String predicates;

    private String filters;
    
    //处理栏位与mybatis同名的关健字
    @TableField("`order`")
    private Integer order;

    private String version;
    
    private String description;

    private Integer delFlag;
}