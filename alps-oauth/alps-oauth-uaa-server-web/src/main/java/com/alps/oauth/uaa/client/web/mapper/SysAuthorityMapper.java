package com.alps.oauth.uaa.client.web.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.entity.SysAuthority;
import com.alps.plaform.database.mapper.SuperMapper;	

/**
 * sys_aciton_ref 数据层
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Repository
public interface SysAuthorityMapper extends SuperMapper<SysAuthority> {
	/**
     * 查询sys_aciton_ref信息
     * 
     * @param id sys_aciton_refID
     * @return sys_aciton_ref信息
     */
	public SysAuthority selectSysAuthorityById(Long id);
	
	/**
     * 查询sys_aciton_ref列表
     * 
     * @param SysAuthority sys_aciton_ref信息
     * @return sys_aciton_ref集合
     */
	public List<SysAuthority> selectSysAuthorityList(SysAuthority sysAuthority);
	
	/**
	 * 查询SysAuthorityLis
	 * 
	 * @param SysAuthority sys_aciton_ref信息
	 * @return SysAuthorityLis集合
	 */
	public List<SysAuthority> selectSysAutoListByRoleId(Long roleId);
	
	/**
     * 新增sys_aciton_ref
     * 
     * @param SysAuthority sys_aciton_ref信息
     * @return 结果
     */
	public int insertSysAuthority(SysAuthority sysAuthority);
	
	/**
     * 修改sys_aciton_ref
     * 
     * @param SysAuthority sys_aciton_ref信息
     * @return 结果
     */
	public int updateSysAuthority(SysAuthority sysAuthority);
	
	/**
     * 删除sys_aciton_ref
     * 
     * @param id sys_aciton_refID
     * @return 结果
     */
	public int deleteSysAuthorityById(Long id);
	
	/**
	 * 删除sys_aciton_ref
	 * 
	 * @param id sys_aciton_refID
	 * @return 结果
	 */
	public int deleteByRoleId(Long roleId);
	
	/**
     * 批量删除sys_aciton_ref
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSysAuthorityByIds(Long[] ids);
	
	/**
	 * 批量删除sys_aciton_ref
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSysAuthorityByAc(SysAuthority sysAuthority);
	
	/**
	 * 批量新增sys_aciton_ref
	 * 
	 * @param List
	 * @return 结果
	 */
	public int batchRoleGateway(List<SysAuthority> roleGatewayList);
	

    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenu(Map map);
    
    /**
     * 
     * 获取API权限List  
     * @param userName
     * @return
     */
    List<SysAuthority> findAuthorityApiList(String userName);
	
}