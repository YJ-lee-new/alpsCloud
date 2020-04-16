package com.alps.pf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfApiMapper;
import com.alps.provider.pf.domain.PfApi;
import com.alps.pf.service.IPfApiService;

/**
 * pf_api 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfApiServiceImpl implements IPfApiService 
{
	@Autowired
	private PfApiMapper pfApiMapper;

	/**
     * 查询pf_api信息
     * 
     * @param apiId pf_apiID
     * @return pf_api信息
     */
    @Override
	public PfApi selectPfApiById(String apiId)
	{
	    return pfApiMapper.selectPfApiById(apiId);
	}
	
	/**
     * 查询pf_api列表
     * 
     * @param pfApi pf_api信息
     * @return pf_api集合
     */
	@Override
	public List<PfApi> selectPfApiList(PfApi pfApi)
	{
	    return pfApiMapper.selectPfApiList(pfApi);
	}
	
    /**
     * 新增pf_api
     * 
     * @param pfApi pf_api信息
     * @return 结果
     */
	@Override
	public int insertPfApi(PfApi pfApi)
	{
	    return pfApiMapper.insertPfApi(pfApi);
	}
	
	/**
     * 修改pf_api
     * 
     * @param pfApi pf_api信息
     * @return 结果
     */
	@Override
	public int updatePfApi(PfApi pfApi)
	{
	    return pfApiMapper.updatePfApi(pfApi);
	}

	/**
     * 删除pf_api对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfApiByIds(String ids)
	{
		return pfApiMapper.deletePfApiByIds(Convert.toStrArray(ids));
	}
	
}
