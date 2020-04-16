package com.alps.oauth.server.service.impl;

import com.alps.common.constant.BaseConstants;
import com.alps.oauth.server.mapper.BaseAccountLogsMapper;
import com.alps.oauth.server.mapper.BaseAccountMapper;
import com.alps.oauth.server.mapper.BaseSysUserMapper;
import com.alps.oauth.server.service.AccountService;
import com.alps.base.api.model.entity.BaseAccountLogs;
import com.alps.base.api.model.entity.BaseSysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 通用账号
 *
 * @author YJ.lee
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAccountServiceImpl extends BaseServiceImpl<BaseSysUserMapper, BaseSysUser> implements AccountService {

//    @Autowired
//    private BaseAccountMapper baseAccountMapper;
    @Autowired
    private BaseSysUserMapper baseAccountMapper;
    
    @Autowired
    private BaseAccountLogsMapper baseAccountLogsMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    @Override
    public BaseSysUser getAccountById(Long accountId) {
        return baseAccountMapper.selectById(accountId);
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
    public BaseSysUser getAccount(String account, String accountType, String domain) {
        QueryWrapper<BaseSysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseSysUser::getAccount, account)
                .eq(BaseSysUser::getAccountType, accountType)
                .eq(BaseSysUser::getDomain, domain);
        BaseSysUser baseSysUser = null ;
        try {
        	
        	 baseSysUser  = baseAccountMapper.selectOne(queryWrapper);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("baocuola + =====>> " + e);
		}
        return baseSysUser;

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
    public BaseSysUser register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        String encodePassword = passwordEncoder().encode(password);
        BaseSysUser baseAccount = new BaseSysUser(userId, account, encodePassword, accountType, domain, registerIp);
        baseAccount.setCreateTime(new Date());
        baseAccount.setUpdateTime(baseAccount.getCreateTime());
        baseAccount.setStatus(status);
        baseAccountMapper.insert(baseAccount);
        return baseAccount;
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
        QueryWrapper<BaseSysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseSysUser::getAccount, account)
                .eq(BaseSysUser::getAccountType, accountType)
                .eq(BaseSysUser::getDomain, domain);
        int count = baseAccountMapper.selectCount(queryWrapper);
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
        return baseAccountMapper.deleteById(accountId);
    }


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    @Override
    public int updateStatus(Long accountId, Integer status) {
    	BaseSysUser baseAccount = new BaseSysUser();
        baseAccount.setUserId(accountId);
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        return baseAccountMapper.updateById(baseAccount);
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
        BaseSysUser baseAccount = new BaseSysUser();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        QueryWrapper<BaseSysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseSysUser::getDomain, domain)
                .eq(BaseSysUser::getUserId, userId);
        return baseAccountMapper.update(baseAccount, wrapper);
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
    	BaseSysUser baseAccount = new BaseSysUser();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setPassword(passwordEncoder().encode(password));
        QueryWrapper<BaseSysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .in(BaseSysUser::getAccountType, BaseConstants.ACCOUNT_TYPE_USERNAME, BaseConstants.ACCOUNT_TYPE_EMAIL, BaseConstants.ACCOUNT_TYPE_MOBILE)
                .eq(BaseSysUser::getUserId, userId)
                .eq(BaseSysUser::getDomain, domain);
        return baseAccountMapper.update(baseAccount, wrapper);
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
        QueryWrapper<BaseSysUser> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseSysUser::getUserId, userId)
                .eq(BaseSysUser::getDomain, domain);
        return baseAccountMapper.delete(wrapper);
    }


    /**
     * 添加登录日志
     *
     * @param log
     */
    @Override
    public void addLoginLog(BaseAccountLogs log) {
        QueryWrapper<BaseAccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccountLogs::getAccountId, log.getAccountId())
                .eq(BaseAccountLogs::getUserId, log.getUserId());
        int count = baseAccountLogsMapper.selectCount(queryWrapper);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        baseAccountLogsMapper.insert(log);
    }
}
