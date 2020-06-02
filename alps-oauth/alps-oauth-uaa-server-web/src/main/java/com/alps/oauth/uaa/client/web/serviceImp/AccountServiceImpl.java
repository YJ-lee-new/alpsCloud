package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.common.constant.BaseConstants;
import com.alps.common.utils.StringUtils;
import com.alps.oauth.uaa.client.web.mapper.SysAccountLogsMapper;
import com.alps.oauth.uaa.client.web.mapper.SysRoleUserMapper;
import com.alps.oauth.uaa.client.web.mapper.SysUserMapper;
import com.alps.oauth.uaa.client.web.service.IAccountService;
import com.alps.base.api.model.entity.SysAccountLogs;
import com.alps.base.api.model.entity.SysRoleUser;
import com.alps.base.api.model.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通用账号
 *
 * @author YJ.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements IAccountService {

//    @Autowired
//    private sysUserMapper sysUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    
    @Autowired
    private SysAccountLogsMapper sysAccountLogsMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    @Override
    public SysUser getAccountById(Long accountId) {
        return sysUserMapper.selectById(accountId);
    }
    /**
     * 根据主键获取账号信息
     *
     * @param account
     * @return
     */
    @Override
    public SysUser getAccountByAccount(String account) {
    	 QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
         queryWrapper.lambda().eq(SysUser::getAccount, account)
                              .eq(SysUser::getDomain, "@superAdmin.com");
    	return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public SysUser getAccount(String account, String accountType, String domain) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getAccount, account)
                .eq(SysUser::getAccountType, accountType)
                .eq(SysUser::getDomain, domain);
        SysUser sysUser = null ;
        try {
        	
        	sysUser  = sysUserMapper.selectOne(queryWrapper);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
        return sysUser;

    }

    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    @Override
    public SysUser register(Long userId, String userName,String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        SysUser sysUser = new SysUser(userId,userName, account, password, accountType, domain, registerIp);
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(sysUser.getCreateTime());
        sysUser.setStatus(status);
        sysUserMapper.insert(sysUser);
        return sysUser;
    }
    
    /**
     * 
     */
	@Override
	public SysUser registerWeb(SysUser sysUser, String accountType, String domain, String registerIp) {
		if (isExist(sysUser.getAccount(), accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", sysUser.getAccount(), domain));
        }
		sysUser.setCreateTime(new Date());
	    sysUser.setUpdateTime(sysUser.getCreateTime());
	    sysUser.setAccountType(accountType);
	    sysUserMapper.insert(sysUser);
		
	    // 新增用户与角色管理
        insertUserRole(sysUser);
	    
		return sysUser;
	}

	  /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysRoleUser> list = new ArrayList<SysRoleUser>();
            for (Long roleId : roles)
            {
            	SysRoleUser ur = new SysRoleUser();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
            	sysRoleUserMapper.batchUserRole(list);
            }
        }
    }

	

    /**
     * 检测账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public Boolean isExist(String account, String accountType, String domain) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getAccount, account)
                .eq(SysUser::getAccountType, accountType)
                .eq(SysUser::getDomain, domain);
        int count = sysUserMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    @Override
    public int removeAccount(Long accountId) {
        return sysUserMapper.deleteById(accountId);
    }


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    @Override
    public int updateStatus(Long accountId, Integer status) {
    	SysUser baseAccount = new SysUser();
        baseAccount.setUserId(accountId);
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        return sysUserMapper.updateById(baseAccount);
    }

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    @Override
    public int updateStatusByUserId(Long userId, String domain, Integer status) {
        if (status == null) {
            return 0;
        }
        SysUser baseAccount = new SysUser();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(SysUser::getDomain, domain)
                .eq(SysUser::getUserId, userId);
        return sysUserMapper.update(baseAccount, wrapper);
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    @Override
    public int updatePasswordByUserId(Long userId, String domain, String password) {
    	SysUser baseAccount = new SysUser();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setPassword(passwordEncoder.encode(password));
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .in(SysUser::getAccountType, BaseConstants.ACCOUNT_TYPE_USERNAME, BaseConstants.ACCOUNT_TYPE_EMAIL, BaseConstants.ACCOUNT_TYPE_MOBILE)
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getDomain, domain);
        return sysUserMapper.update(baseAccount, wrapper);
    }

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    @Override
    public int removeAccountByUserId(Long userId, String domain) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getDomain, domain);
        return sysUserMapper.delete(wrapper);
    }


    /**
     * 添加登录日志
     *
     * @param log
     */
    @Override
    public void addLoginLog(SysAccountLogs log) {
        QueryWrapper<SysAccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysAccountLogs::getAccountId, log.getAccountId())
                .eq(SysAccountLogs::getUserId, log.getUserId());
        int count = sysAccountLogsMapper.selectCount(queryWrapper);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        sysAccountLogsMapper.insert(log);
    }

}
