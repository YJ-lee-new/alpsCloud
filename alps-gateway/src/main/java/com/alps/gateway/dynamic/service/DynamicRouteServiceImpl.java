package com.alps.gateway.dynamic.service;

import static com.alps.gateway.client.routes.RedisRouteDefinitionRepository.GATEWAY_ROUTES_PREFIX;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alps.common.core.domain.PageResult;
import com.alps.gateway.client.vo.GatewayRoutesVO;
import com.alps.gateway.dynamic.definition.GatewayFilterDefinition;
import com.alps.gateway.dynamic.definition.GatewayPredicateDefinition;
import com.alps.gateway.dynamic.definition.GatewayRouteDefinition;
import com.alps.gateway.dynamic.domain.GatewayRoutes;
import com.alps.gateway.dynamic.mapper.DynamicRoutesMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import reactor.core.publisher.Mono;

@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware, DynamicRouteService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Autowired
    private DynamicRoutesMapper gatewayRoutesMapper;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    
    
    /**
     * 初始化 转化对象
     */
    private static MapperFacade routeDefinitionMapper;
    private static MapperFacade routeVOMapper;
    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(GatewayRouteDefinition.class, GatewayRoutes.class)
                .exclude("filters")
                .exclude("predicates")
                .byDefault();
        routeDefinitionMapper = mapperFactory.getMapperFacade();

        //  routeVOMapper
        mapperFactory.classMap(GatewayRoutes.class, GatewayRoutesVO.class)
                .byDefault();
        routeVOMapper = mapperFactory.getMapperFacade();

    }


    /**
     *  给spring注册事件
     *      刷新路由
     */
    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 新增路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String add(GatewayRouteDefinition  gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setDelFlag(0);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        //添加进数据库
        gatewayRoutesMapper.insert(gatewayRoutes);
        //写入缓存
        gatewayRouteDefinition.setId(gatewayRoutes.getId());
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(),  JSONObject.toJSONString(gatewayRouteDefinition));
        //更新gateway中路由
        RouteDefinition routeDefinition = transform2RouteDefinition(gatewayRouteDefinition);
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * 修改路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @Override
    public String update(GatewayRouteDefinition gatewayRouteDefinition) {
        GatewayRoutes gatewayRoutes = transformToGatewayRoutes(gatewayRouteDefinition);
        gatewayRoutes.setCreateTime(new Date());
        gatewayRoutes.setUpdateTime(new Date());
        //update数据库信息
        gatewayRoutesMapper.updateById(gatewayRoutes);

        //删除原来redis中数据
    	redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete(gatewayRouteDefinition.getId()); 
        //重新添加redis中变更后的路由
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put(gatewayRouteDefinition.getId(),  JSONObject.toJSONString(gatewayRouteDefinition));
        //将路由信息更新到gateway的RouteDefinition中
        RouteDefinition routeDefinition = transform2RouteDefinition(gatewayRouteDefinition);
        try {
            delete(gatewayRouteDefinition.getId());
        } catch (Exception e) {
            return " 404: update fail! Not find the route: "+ routeDefinition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "updat the route fail!";
        }
    }


    /**
     * 删除路由
     * @param id
     * @return
     */
    @Override
    public Mono<ResponseEntity<Object>> delete(String id) {
    	//删除数据库中信息
        gatewayRoutesMapper.deleteById(id);
        //删除缓存
        redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete( id ); 
        //更新gateway中信息
        return this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(() -> {
            return Mono.just(ResponseEntity.ok().build());
        })).onErrorResume((t) -> {
            return t instanceof NotFoundException;
        }, (t) -> {
            return Mono.just(ResponseEntity.notFound().build());
        });
        
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @Override
    public PageResult<GatewayRoutesVO> findAll(Map<String, Object> params) {
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
        List<GatewayRoutes> alls = gatewayRoutesMapper.selectByMap(new HashMap<String,Object>());
        PageInfo<GatewayRoutesVO> pageInfo = new PageInfo<>(routeVOMapper.mapAsList(alls, GatewayRoutesVO.class));
        return PageResult.<GatewayRoutesVO>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    /**
     * @return
     */
    @Override
    public String synchronization() {
    	//查找出所有数据库中的路由
        HashMap <String,Object> map = new HashMap <String,Object>();
        map.put("del_Flag", 0);
        List<GatewayRoutes> alls = gatewayRoutesMapper.selectByMap(map);
        //更新缓存中信息.先删除所有缓存中的路由信息,再重新写入
        redisTemplate.delete(GATEWAY_ROUTES_PREFIX);
        for (GatewayRoutes route:   alls) {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(route.getDescription())
                    .id(route.getId())
                    .order(route.getOrder())
                    .uri(route.getUri())
                    .build();

            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(route.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(route.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put( route.getId() ,  JSONObject.toJSONString(gatewayRouteDefinition));
            
            
        }

        return "success";
    }

    /**
     * 更改路由状态
     *
     * @param params
     * @return
     */
    @Override
    public String updateFlag(Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        Integer flag = MapUtils.getInteger(params, "flag");

        GatewayRoutes gatewayRoutes = gatewayRoutesMapper.selectById(id);
        if (gatewayRoutes == null) {
            return "路由不存在";
        }

        if (flag == 1){
            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).delete( id ); 
            
        }else {
            GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder()
                    .description(gatewayRoutes.getDescription())
                    .id(gatewayRoutes.getId())
                    .order(gatewayRoutes.getOrder())
                    .uri(gatewayRoutes.getUri())
                    .build();

            List<GatewayFilterDefinition> gatewayFilterDefinitions = JSONArray.parseArray(gatewayRoutes.getFilters(), GatewayFilterDefinition.class);
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = JSONArray.parseArray(gatewayRoutes.getPredicates(), GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);

            redisTemplate.boundHashOps(GATEWAY_ROUTES_PREFIX).put( gatewayRoutes.getId() ,  JSONObject.toJSONString(gatewayRouteDefinition));
            
        }

        gatewayRoutes.setDelFlag(flag);
        gatewayRoutes.setUpdateTime(new Date());
        int i = gatewayRoutesMapper.updateById(gatewayRoutes);
        return i > 0 ? "更新成功": "更新失败";
    }

    /**
     * 转化路由对象  GatewayRoutes
     * @param gatewayRouteDefinition
     * @return
     */
    private GatewayRoutes transformToGatewayRoutes(GatewayRouteDefinition gatewayRouteDefinition){
        GatewayRoutes definition = new GatewayRoutes();
        routeDefinitionMapper.map(gatewayRouteDefinition,definition);
        //设置路由id
        if (!StringUtils.isNotBlank(definition.getId())){
            definition.setId(java.util.UUID.randomUUID().toString().toUpperCase().replace("-",""));
        }
       
        String filters = JSONArray.toJSONString(gatewayRouteDefinition.getFilters());
        String predicates = JSONArray.toJSONString(gatewayRouteDefinition.getPredicates());

        definition.setFilters(filters);
        definition.setPredicates(predicates);

        return definition;
    }
    
    /**
     * 转化路由对象  GatewayRoutes
     * @param gatewayRouteDefinition
     * @return
     */
    private RouteDefinition transform2RouteDefinition(GatewayRouteDefinition gwdefinition){
    	RouteDefinition definition = new RouteDefinition();
        definition.setId(gwdefinition.getId());
        definition.setOrder(gwdefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList=new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList=gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList<>();
        List<GatewayFilterDefinition> gatewayFilters = gwdefinition.getFilters();
        for(GatewayFilterDefinition filterDefinition : gatewayFilters){
            FilterDefinition filter = new FilterDefinition();
            filter.setName(filterDefinition.getName());
            filter.setArgs(filterDefinition.getArgs());
            filters.add(filter);
        }
        definition.setFilters(filters);

        URI uri = null;
        if(gwdefinition.getUri().startsWith("http")){
            uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        }else{
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gwdefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }
    
    
    /**
     * 查询全部数据库中route信息，并写入redis内存
     */
    @Cacheable(value = GATEWAY_ROUTES_PREFIX)
    public List<GatewayRoutes> getAllRoute() {
    	List<GatewayRoutes> gatewayRouteList = gatewayRoutesMapper.selectByMap(new HashMap<String,Object>());
    	return gatewayRouteList;
    }
    
}
