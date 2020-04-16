package com.alps.oauth.server.service.impl;

import com.alps.base.api.model.entity.BaseMenu;
import com.alps.common.constant.BaseConstants;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.BaseAccountLogs;
import com.alps.base.api.model.entity.BaseRole;
import com.alps.base.api.model.entity.BaseSysUser;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsSecurityConstants;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.common.oauth2.utils.WebUtils;
import com.alps.oauth.server.mapper.BaseSysUserMapper;
import com.alps.oauth.server.model.PageParams;
import com.alps.oauth.server.service.*;
import com.alps.provider.system.domain.SysMenu;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl extends BaseServiceImpl<BaseSysUserMapper, BaseSysUser> implements UserService {

//    @Autowired
//    private BaseUserMapper baseUserMapper;

    @Autowired
    private BaseSysUserMapper baseUserMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AccountService accountService;

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_ADMIN;

    /**
     * 添加系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void addUser(BaseSysUser baseUser) {
        if(getUserByUsername(baseUser.getUserName())!=null){
            throw new AlpsAlertException("用户名:"+baseUser.getUserName()+"已存在!");
        }
        baseUser.setCreateTime(new Date());
        baseUser.setUpdateTime(baseUser.getCreateTime());
        //保存系统用户信息
        baseUserMapper.insert(baseUser);
        //默认注册用户名账户
        accountService.register(baseUser.getUserId(), baseUser.getUserName(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringUtils.matchEmail(baseUser.getEmail())) {
            //注册email账号登陆
            accountService.register(baseUser.getUserId(), baseUser.getEmail(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringUtils.matchMobile(baseUser.getMobile())) {
            //注册手机号账号登陆
            accountService.register(baseUser.getUserId(), baseUser.getMobile(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
    }

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void updateUser(BaseSysUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return;
        }
        if (baseUser.getStatus() != null) {
            accountService.updateStatusByUserId(baseUser.getUserId(), ACCOUNT_DOMAIN, baseUser.getStatus());
        }
        baseUserMapper.updateById(baseUser);
    }

    /**
     * 添加第三方登录用户
     *
     * @param baseUser
     * @param accountType
     */
    @Override
    public void addUserThirdParty(BaseSysUser baseUser, String accountType) {
        if (!accountService.isExist(baseUser.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            baseUser.setUserType(BaseConstants.USER_TYPE_ADMIN);
            baseUser.setCreateTime(new Date());
            baseUser.setUpdateTime(baseUser.getCreateTime());
            //保存系统用户信息
            baseUserMapper.insert(baseUser);
            // 注册账号信息
            accountService.register(baseUser.getUserId(), baseUser.getUserName(), baseUser.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
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
    public IPage<BaseSysUser> findListPage(PageParams pageParams) {
        BaseSysUser query = pageParams.mapToObject(BaseSysUser.class);
        QueryWrapper<BaseSysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseSysUser::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), BaseSysUser::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), BaseSysUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), BaseSysUser::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return baseUserMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseSysUser> findAllList() {
        List<BaseSysUser> list = baseUserMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseSysUser getUserById(Long userId) {
        return baseUserMapper.selectById(userId);
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    @Override
    public UserAccount getUserAccount(Long userId) {
        // 用户权限列表
        List<AlpsAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map> roles = Lists.newArrayList();
        List<BaseRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                Map roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                AlpsAuthority authority = new AlpsAuthority(role.getRoleId().toString(), AlpsSecurityConstants.AUTHORITY_PREFIX_ROLE + role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
        }

        //查询系统用户资料
        BaseSysUser baseUser = getUserById(userId);

        // 加入用户权限
        List<AlpsAuthority> userGrantedAuthority = authorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(baseUser.getUserName()));
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        UserAccount userAccount = new UserAccount();
        // 昵称
        userAccount.setNickName(baseUser.getNickName());
        // 头像
        userAccount.setAvatar(baseUser.getAvatar());
        // 权限信息
        userAccount.setAuthorities(authorities);
        userAccount.setRoles(roles);
        return userAccount;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseSysUser getUserByUsername(String username) {
        QueryWrapper<BaseSysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseSysUser::getUserName, username);
        BaseSysUser saved = baseUserMapper.selectOne(queryWrapper);
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
                System.out.println("");
            }
            // 用户权限信息
            UserAccount userAccount = getUserAccount(baseAccount.getUserId());
            // 复制账号信息
            BeanUtils.copyProperties(baseAccount, userAccount);
            return userAccount;
        }
        return null;
    }

    @Override
    public ResultBody getInfo() {
        // 角色集合
        List<BaseRole> roles = roleService.getUserRoles(AlpsHelper.getUserId());
        // 权限集合
        Set<String> permissions = menuService.selectMenuPermsByUserId(AlpsHelper.getUserId());
        Set<String> roleSets=new HashSet<>();
        for (BaseRole role : roles)
        {
            if (com.alps.common.utils.StringUtils.isNotEmpty(role.getRoleCode()))
            {
                roleSets.add(role.getRoleCode());
            }
        }
        ResultBody resultBody=new ResultBody();
        resultBody.put("roles",roleSets);
        resultBody.put("permissions",permissions);
        return resultBody;
    }
}