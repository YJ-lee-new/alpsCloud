package com.alps.gateway.dynamic.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;

import com.alps.common.core.domain.PageResult;
import com.alps.gateway.client.vo.GatewayRoutesVO;
import com.alps.gateway.dynamic.definition.GatewayRouteDefinition;
import com.alps.gateway.dynamic.domain.GatewayRoutes;

import reactor.core.publisher.Mono;

/**
 * 操作 路由 Service
 */
/**
 * @author:Yujie.lee
 * Date:2020年1月11日
 * TodoTODO
 */
/**
 * @author:Yujie.lee
 * Date:2020年1月11日
 * TodoTODO
 */
/**
 * @author:Yujie.lee
 * Date:2020年1月11日
 * TodoTODO
 */
public interface DynamicRouteService {

    /**
     * 新增路由
     * @param gatewayRouteDefinition
     * @return
     */
	String add(GatewayRouteDefinition gatewayRouteDefinition);

    /**
     * 修改路由
      * @param gatewayRouteDefinition
     * @return
     */
	String update(GatewayRouteDefinition gatewayRouteDefinition);



    /**
     * 删除路由
     * @param id
     * @return
     */
	Mono<ResponseEntity<Object>> delete(String id);


    /**
     * 查询全部数据
     * @return
     */
    PageResult<GatewayRoutesVO> findAll(Map<String, Object> params);

    /**
     *  同步redis数据 从mysql中同步过去
     *
     * @return
     */
    String synchronization();


    /**
     * 更改路由状态
     * @param params
     * @return
     */
    String updateFlag(Map<String, Object> params);
    

}
