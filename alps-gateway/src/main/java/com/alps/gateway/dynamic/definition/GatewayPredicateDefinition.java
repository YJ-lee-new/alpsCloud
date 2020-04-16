package com.alps.gateway.dynamic.definition;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * Todo 路由断言模型
 */
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayPredicateDefinition {
    private String name;
    private Map<String, String> args = new LinkedHashMap<>();
}
