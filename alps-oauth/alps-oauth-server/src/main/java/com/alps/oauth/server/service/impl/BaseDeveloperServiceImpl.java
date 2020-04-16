package com.alps.oauth.server.service.impl;

import com.alps.common.constant.BaseConstants;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.BaseAccountLogs;
import com.alps.base.api.model.entity.BaseDeveloper;
import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.common.oauth2.utils.WebUtils;
import com.alps.oauth.server.mapper.BaseDeveloperMapper;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.AccountService;
import com.alps.oauth.server.service.DeveloperService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: YJ.lee
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseDeveloperServiceImpl extends BaseServiceImpl<BaseDeveloperMapper, BaseDeveloper> implements DeveloperService {

    @Autowired
    private BaseDeveloperMapper baseDeveloperMapper;
    @Autowired
    private AccountService accountService;

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_PORTAL;

    /**
     * 添加系统用户
     *
     * @param baseDeveloper
     * @return
     */
    @Override
    public void addUser(BaseDeveloper baseDeveloper) {
        if(getUserByUsername(baseDeveloper.getUserName())!=null){
            throw new AlpsAlertException("用户名:"+baseDeveloper.getUserName()+"已存在!");
        }
        baseDeveloper.setCreateTime(new Date());
        baseDeveloper.setUpdateTime(baseDeveloper.getCreateTime());
        //保存系统用户信息
        baseDeveloperMapper.insert(baseDeveloper);
        //默认注册用户名账户
        accountService.register(baseDeveloper.getUserId(), baseDeveloper.getUserName(), baseDeveloper.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, baseDeveloper.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringUtils.matchEmail(baseDeveloper.getEmail())) {
            //注册email账号登陆
            accountService.register(baseDeveloper.getUserId(), baseDeveloper.getEmail(), baseDeveloper.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, baseDeveloper.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringUtils.matchMobile(baseDeveloper.getMobile())) {
            //注册手机号账号登陆
            accountService.register(baseDeveloper.getUserId(), baseDeveloper.getMobile(), baseDeveloper.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, baseDeveloper.getStatus(), ACCOUNT_DOMAIN, null);
        }
    }

    /**
     * 更新系统用户
     *
     * @param baseDeveloper
     * @return
     */
    @Override
    public void updateUser(BaseDeveloper baseDeveloper) {
        if (baseDeveloper == null || baseDeveloper.getUserId() == null) {
            return;
        }
        if (baseDeveloper.getStatus() != null) {
            accountService.updateStatusByUserId(baseDeveloper.getUserId(), ACCOUNT_DOMAIN, baseDeveloper.getStatus());
        }
        baseDeveloperMapper.updateById(baseDeveloper);
    }

    /**
     * 添加第三方登录用户
     *
     * @param baseDeveloper
     * @param accountType
     */
    @Override
    public void addUserThirdParty(BaseDeveloper baseDeveloper, String accountType) {
        if (!accountService.isExist(baseDeveloper.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            baseDeveloper.setUserType(BaseConstants.USER_TYPE_ADMIN);
            baseDeveloper.setCreateTime(new Date());
            baseDeveloper.setUpdateTime(baseDeveloper.getCreateTime());
            //保存系统用户信息
            baseDeveloperMapper.insert(baseDeveloper);
            // 注册账号信息
            accountService.register(baseDeveloper.getUserId(), baseDeveloper.getUserName(), baseDeveloper.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public void updatePassword(Long userId, String password) {
        accountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseDeveloper> findListPage(PageParams pageParams) {
        BaseDeveloper query = pageParams.mapToObject(BaseDeveloper.class);
        QueryWrapper<BaseDeveloper> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseDeveloper::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), BaseDeveloper::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), BaseDeveloper::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), BaseDeveloper::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return baseDeveloperMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseDeveloper> findAllList() {
        List<BaseDeveloper> list = baseDeveloperMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseDeveloper getUserById(Long userId) {
        return baseDeveloperMapper.selectById(userId);
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseDeveloper getUserByUsername(String username) {
        QueryWrapper<BaseDeveloper> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseDeveloper::getUserName, username);
        BaseDeveloper saved = baseDeveloperMapper.selectOne(queryWrapper);
        return saved;
    }


    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserAccount login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        BaseSysUser baseAccount = null;
        if (StringUtils.isNotBlank(loginType)) {
            baseAccount = accountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            baseAccount = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                baseAccount = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
                baseAccount = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }
        }
        // 获取用户详细信息
        if (baseAccount != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    BaseAccountLogs log = new BaseAccountLogs();
                    log.setDomain(ACCOUNT_DOMAIN);
                    log.setUserId(baseAccount.getUserId());
                    log.setAccount(baseAccount.getAccount());
                    log.setAccountId(String.valueOf(baseAccount.getUserId()));
                    log.setAccountType(baseAccount.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    accountService.addLoginLog(log);
                }
            } catch (Exception e) {
                System.out.println("添加登录日志失败:{}");
            }
            // 用户权限信息
            // 复制账号信息
            UserAccount userAccount = new UserAccount();
            BeanUtils.copyProperties(userAccount, baseAccount);
            return userAccount;
        }
        return null;
    }
}
