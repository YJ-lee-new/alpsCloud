package com.alps.pf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfAppMapper;
import com.alps.provider.pf.domain.PfApp;
import com.alps.pf.service.IPfAppService;

/**
 * pf_app 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfAppServiceImpl implements IPfAppService 
{
	@Autowired
	private PfAppMapper pfAppMapper;

	/**
     * 查询pf_app信息
     * 
     * @param appId pf_appID
     * @return pf_app信息
     */
    @Override
	public PfApp selectPfAppById(String appId)
	{
	    return pfAppMapper.selectPfAppById(appId);
	}
	
	/**
     * 查询pf_app列表
     * 
     * @param pfApp pf_app信息
     * @return pf_app集合
     */
	@Override
	public List<PfApp> selectPfAppList(PfApp pfApp)
	{
	    return pfAppMapper.selectPfAppList(pfApp);
	}
	
    /**
     * 新增pf_app
     * 
     * @param pfApp pf_app信息
     * @return 结果
     */
	@Override
	public int insertPfApp(PfApp pfApp)
	{
	    return pfAppMapper.insertPfApp(pfApp);
	}
	
	/**
     * 修改pf_app
     * 
     * @param pfApp pf_app信息
     * @return 结果
     */
	@Override
	public int updatePfApp(PfApp pfApp)
	{
	    return pfAppMapper.updatePfApp(pfApp);
	}

	/**
     * 删除pf_app对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfAppByIds(String ids)
	{
		return pfAppMapper.deletePfAppByIds(Convert.toStrArray(ids));
	}
	
}
