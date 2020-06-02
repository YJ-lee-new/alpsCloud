package com.alps.oauth.uaa.client.web.service;

import com.alps.base.api.model.entity.SysApi;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 接口资源管理
 *
 * @author YJ.lee
 */
public interface ISysApiService extends IBaseService<SysApi> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SysApi> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SysApi> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    SysApi getApi(Long apiId);


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    int isExist(String apiCode);
    
    /**
     * 检查接口编码是否存在
     *
     * @param apiCode,apiId
     * @return
     */
     int isExistCodeBeside(String apiCode,String apiId); 
    
    /**
     * 检查接口编码是否存在
     *
     * @param path
     * @return
     */
    int isPathExist(String path);
    
    /**
     * 保存检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    int isPathHasBesideThis(String path,String apiId);

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    void addApi(SysApi api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    void updateApi(SysApi api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    SysApi getApi(String apiCode);

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
    int getCount(QueryWrapper<SysApi> queryWrapper);
    
    /**
     * 查询sys_api信息
     * 
     * @param apiId sys_apiID
     * @return sys_api信息
     */
	public SysApi selectSysApiById(String apiId);
	
	/**
     * 查询sys_api列表
     * 
     * @param SysApi sys_api信息
     * @return sys_api集合
     */
	public List<SysApi> selectSysApiList(SysApi sysApi);
	
	/**
     * 新增sys_api
     * 
     * @param SysApi sys_api信息
     * @return 结果
     */
	public int insertSysApi(SysApi sysApi);
	
	/**
     * 修改sys_api
     * 
     * @param SysApi sys_api信息
     * @return 结果
     */
	public int updateSysApi(SysApi sysApi);
		
	/**
     * 删除sys_api信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSysApiByIds(String ids);

}
