package com.alps.gateway.dynamic.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.alps.gateway.dynamic.domain.GatewayRoutes;
import com.alps.plaform.database.mapper.SuperMapper;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Mapper
public interface DynamicRoutesMapper extends  SuperMapper<GatewayRoutes> {
}