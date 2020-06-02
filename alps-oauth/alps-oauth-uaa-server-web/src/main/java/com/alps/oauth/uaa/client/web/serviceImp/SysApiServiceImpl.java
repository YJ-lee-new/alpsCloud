package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.common.constant.BaseConstants;
import com.alps.common.core.text.Convert;
import com.alps.base.api.model.entity.SysApi;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.oauth.uaa.client.web.mapper.SysApiMapper;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.ISysApiService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author YJ.lee
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysApiServiceImpl extends BaseServiceImpl<SysApiMapper, SysApi> implements ISysApiService {
    @Autowired
    private SysApiMapper sysApiMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SysApi> findListPage(PageParams pageParams) {
        SysApi query = pageParams.mapToObject(SysApi.class);
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getPath()), SysApi::getPath, query.getPath())
                .likeRight(ObjectUtils.isNotEmpty(query.getApiName()), SysApi::getApiName, query.getApiName())
                .likeRight(ObjectUtils.isNotEmpty(query.getApiCode()), SysApi::getApiCode, query.getApiCode())
                .eq(ObjectUtils.isNotEmpty(query.getServiceId()), SysApi::getServiceId, query.getServiceId())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), SysApi::getStatus, query.getStatus())
                .eq(ObjectUtils.isNotEmpty(query.getIsAuth()), SysApi::getIsAuth, query.getIsAuth());
        queryWrapper.orderByDesc("create_time");
        return sysApiMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysApi> findAllList(String serviceId) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(serviceId), SysApi::getServiceId, serviceId);
        List<SysApi> list = sysApiMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    @Override
    public SysApi getApi(Long apiId) {
        return sysApiMapper.selectById(apiId);
    }


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    @Override
    public int isExist(String apiCode) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysApi::getApiCode, apiCode);
        return getCount(queryWrapper);
    }
    
    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    @Override
    public int isExistCodeBeside(String apiCode,String apiId) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysApi::getApiCode, apiCode);
        queryWrapper.lambda().ne(SysApi::getApiId, apiId);
        return getCount(queryWrapper);
    }
    /**
     * 检查path是否存在
     *
     * @param path
     * @return
     */
    @Override
    public int isPathExist(String path) {
    	QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
    	queryWrapper.lambda().eq(SysApi::getPath, path);
    	return getCount(queryWrapper);
    }
    /**
     * 检查path是否存在
     *
     * @param path
     * @return
     */
    @Override
    public int isPathHasBesideThis(String path,String apiId) {
    	QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
    	queryWrapper.lambda().eq(SysApi::getPath, path);
    	queryWrapper.lambda().ne(SysApi::getApiId, apiId);
    	return getCount(queryWrapper);
    }

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    @Override
    public void addApi(SysApi api) {
        if (isExist(api.getApiCode())>0) {
            throw new AlpsAlertException(String.format("%s编码已存在!", api.getApiCode()));
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getStatus() == null) {
            api.setStatus(BaseConstants.ENABLED);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        if (api.getIsPersist() == null) {
            api.setIsPersist(0);
        }
        if (api.getIsAuth() == null) {
            api.setIsAuth(0);
        }
        api.setCreateTime(new Date());
        api.setUpdateTime(api.getCreateTime());
        sysApiMapper.insert(api);
    }

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    @Override
    public void updateApi(SysApi api) {
        SysApi saved = getApi(api.getApiId());
        if (saved == null) {
            throw new AlpsAlertException("信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode())>0) {
                throw new AlpsAlertException(String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        api.setUpdateTime(new Date());
        sysApiMapper.updateById(api);
    }

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    @Override
    public SysApi getApi(String apiCode) {
        QueryWrapper<SysApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SysApi::getApiCode, apiCode);
        return sysApiMapper.selectOne(queryWrapper);
    }


    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    @Override
    public void removeApi(Long apiId) {
        SysApi api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new AlpsAlertException(String.format("保留数据,不允许删除"));
        }
        sysApiMapper.deleteById(apiId);
    }


    /**
     * 获取数量
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public int getCount(QueryWrapper<SysApi> queryWrapper) {
        return sysApiMapper.selectCount(queryWrapper);
    }


	/**
     * 查询sys_api信息
     * 
     * @param apiId sys_apiID
     * @return sys_api信息
     */
    @Override
	public SysApi selectSysApiById(String apiId)
	{
	    return sysApiMapper.selectSysApiById(apiId);
	}
	
	/**
     * 查询sys_api列表
     * 
     * @param SysApi sys_api信息
     * @return sys_api集合
     */
	@Override
	public List<SysApi> selectSysApiList(SysApi sysApi)
	{
	    return sysApiMapper.selectSysApiList(sysApi);
	}
	
    /**
     * 新增sys_api
     * 
     * @param SysApi sys_api信息
     * @return 结果
     */
	@Override
	public int insertSysApi(SysApi sysApi)
	{
	    return sysApiMapper.insert(sysApi);
	}
	
	/**
     * 修改sys_api
     * 
     * @param SysApi sys_api信息
     * @return 结果
     */
	@Override
	public int updateSysApi(SysApi sysApi)
	{
	    return sysApiMapper.updateById(sysApi);
	}

	/**
     * 删除sys_api对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSysApiByIds(String ids)
	{
		return sysApiMapper.deleteSysApiByIds(Convert.toStrArray(ids));
	}

}
