package com.alps.gateway.dynamic.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alps.common.core.domain.PageResult;
import com.alps.common.core.domain.Result;
import com.alps.common.enums.ErrorCodeEnum;
import com.alps.gateway.dynamic.definition.GatewayRouteDefinition;
import com.alps.gateway.dynamic.service.DynamicRouteService;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
/**
 * 动态路由服务
 * 重写Endpoint 端点所有方法,将ruote相关信息写入数据库
 */
@RestController
@RequestMapping("/route")
public class DynamicRoutesController {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    //增加路由
    @PostMapping("/add")
    public Result<?> add(@RequestBody GatewayRouteDefinition  gatewayRouteDefinition) {
        return Result.succeed(dynamicRouteService.add(gatewayRouteDefinition),ErrorCodeEnum.OK.msg());
    }

    //更新路由
    @PostMapping("/update")
    public Result <?> update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        return Result.succeed(dynamicRouteService.update(gatewayRouteDefinition),ErrorCodeEnum.OK.msg());
    }

    //删除路由
    @DeleteMapping("/{id}")
    public Result <?> delete(@PathVariable String id) {
        return Result.succeed(dynamicRouteService.delete(id).toString());
    }

    //获取全部数据  
    @GetMapping("/findAll")
    public PageResult<?> findAll(@RequestParam Map<String, Object> params){
        return dynamicRouteService.findAll(params);
    }

    //将mysql中同步到redis中
    @GetMapping("/synchronization")
    public Result<?> synchronization() {
        return Result.succeed(dynamicRouteService.synchronization(),ErrorCodeEnum.OK.msg());
    }


    //修改路由状态
    @GetMapping("/updateFlag")
    public Result <?> updateFlag(@RequestParam Map<String, Object> params) {
        return Result.succeed(dynamicRouteService.updateFlag(params));
    }





}
