package com.alps.oauth.server.service;

import com.alps.base.api.model.entity.BaseApi;
import com.alps.oauth.server.extra.IBaseService;
import com.alps.oauth.server.model.PageParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 接口资源管理
 *
 * @author YJ.lee
 */
public interface ApiService extends IBaseService<BaseApi> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseApi> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseApi> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    BaseApi getApi(Long apiId);


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    Boolean isExist(String apiCode);

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    void addApi(BaseApi api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    void updateApi(BaseApi api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    BaseApi getApi(String apiCode);

    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    void removeApi(Long apiId);


    /**
     * 获取数量
     *
     * @param queryWrapper
     * @return
     */
    int getCount(QueryWrapper<BaseApi> queryWrapper);

}
