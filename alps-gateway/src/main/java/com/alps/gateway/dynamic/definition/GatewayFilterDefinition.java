package com.alps.gateway.dynamic.definition;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * Todo 路由过滤器模型
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayFilterDefinition {
    private String name;
    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();
    //此处省略Get和Set方法
}
