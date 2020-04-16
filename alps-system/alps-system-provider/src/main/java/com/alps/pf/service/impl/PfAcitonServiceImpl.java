package com.alps.pf.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.domain.Ztrees;
import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfAcitonMapper;
import com.alps.pf.mapper.PfActionRefMapper;
import com.alps.pf.mapper.PfAppMapper;
import com.alps.provider.pf.domain.PfAciton;
import com.alps.provider.pf.domain.PfActionRef;
import com.alps.provider.pf.domain.PfApp;
import com.alps.provider.system.domain.SysRole;
import com.alps.pf.service.IPfAcitonService;

/**
 * action 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfAcitonServiceImpl implements IPfAcitonService 
{
	@Autowired
	private PfAcitonMapper pfAcitonMapper;
	
	@Autowired
	private PfAppMapper  pfAppMapper;
	
	@Autowired
	private PfActionRefMapper  pfActionRefMapper;

	/**
     * 查询action信息
     * 
     * @param actionId actionID
     * @return action信息
     */
    @Override
	public PfAciton selectPfAcitonById(String actionId)
	{
	    return pfAcitonMapper.selectPfAcitonById(actionId);
	}
	
	/**
     * 查询action列表
     * 
     * @param pfAciton action信息
     * @return action集合
     */
	@Override
	public List<PfAciton> selectPfAcitonList(PfAciton pfAciton)
	{
	    return pfAcitonMapper.selectPfAcitonList(pfAciton);
	}
	
    /**
     * 新增action
     * 
     * @param pfAciton action信息
     * @return 结果
     */
	@Override
	public int insertPfAciton(PfAciton pfAciton)
	{
	    return pfAcitonMapper.insertPfAciton(pfAciton);
	}
	
	/**
     * 修改action
     * 
     * @param pfAciton action信息
     * @return 结果
     */
	@Override
	public int updatePfAciton(PfAciton pfAciton)
	{
	    return pfAcitonMapper.updatePfAciton(pfAciton);
	}

	/**
     * 删除action对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfAcitonByIds(String ids)
	{
		return pfAcitonMapper.deletePfAcitonByIds(Convert.toStrArray(ids));
	}

	/* (non-Javadoc)
	 * @see com.alps.pf.service.IPfAcitonService#getApiList(java.lang.String)
	 */
	@Override
	public List<Ztrees> getApiList(Long memuId,SysRole role) {
		List<Ztrees> ztrees = new ArrayList<Ztrees>();
		PfApp pfApp = new PfApp();
		//查找出所有app
		List<PfApp> applist = pfAppMapper.selectPfAppList(pfApp);
		PfActionRef pfActionRef  = new PfActionRef();
		pfActionRef.setMenuId(memuId);
		pfActionRef.setRoleId(role.getRoleId());
		//查找出该Role下该memu的所有actionRefList.
		List<PfActionRef> actionRefList = pfActionRefMapper.selectPfActionRefList(pfActionRef);
		for(PfApp pfa : applist) {
			/**
			 * 找出所有APP下面的API List
			 */
			String appId = pfa.getAppId();
			List<PfAciton> apilist = pfAcitonMapper.selectPfAcitonListByApp(appId);
			Ztrees ztree = new Ztrees();
			ztree.setId(pfa.getAppId());
            ztree.setpId("0");
            ztree.setName(pfa.getAppName());
            ztree.setTitle(pfa.getAppDesc());
            //check是否有chcked
            for(PfActionRef pfaf :actionRefList) {
            	if(pfaf.getActionId().equals(pfa.getAppId())) {
            	   ztree.setChecked(true);
            	}
            }
            ztrees.add(ztree);
			for( PfAciton pfac: apilist) {
				Ztrees ztree2 = new Ztrees();
				ztree2.setId(pfac.getActionId());
				ztree2.setpId(appId);
				ztree2.setName(pfac.getActionDesc());
				ztree2.setTitle(pfac.getActionCode());
				
				 for(PfActionRef pfaf :actionRefList) {
		            	if(pfaf.getActionId().equals(pfac.getActionId())) {
		            		ztree2.setChecked(true);
		            	}
		            }
	            ztrees.add(ztree2);
			}
		}
		
		return ztrees;
	}
	
	
}
