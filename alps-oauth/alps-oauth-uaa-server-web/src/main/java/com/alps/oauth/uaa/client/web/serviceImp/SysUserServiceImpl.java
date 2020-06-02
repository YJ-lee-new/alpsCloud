package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.constant.BaseConstants;
import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.SysAccountLogs;
import com.alps.base.api.model.entity.SysDept;
import com.alps.base.api.model.entity.SysRole;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.core.domain.ResultBody;
import com.alps.common.core.text.Convert;
import com.alps.common.exception.BusinessException;
import com.alps.common.oauth2.config.CommonConstants;
import com.alps.common.oauth2.exception.AlpsAlertException;
import com.alps.common.oauth2.security.AlpsAuthority;
import com.alps.common.oauth2.security.AlpsHelper;
import com.alps.common.oauth2.security.AlpsSecurityConstants;
import com.alps.common.oauth2.utils.StringUtils;
import com.alps.common.oauth2.utils.WebUtils;
import com.alps.oauth.uaa.client.web.mapper.SysDeptMapper;
import com.alps.oauth.uaa.client.web.mapper.SysUserMapper;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.alps.oauth.uaa.client.web.service.IAccountService;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import com.alps.oauth.uaa.client.web.service.ISysRoleService;
import com.alps.oauth.uaa.client.web.service.ISysUserService;
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
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

//    @Autowired
//    private sysUserMapper sysUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private IAccountService accountService;

    private final String ACCOUNT_DOMAIN_USER = BaseConstants.ACCOUNT_DOMAIN_USER;
    private final String ACCOUNT_DOMAIN_SPUER = BaseConstants.ACCOUNT_DOMAIN_SUPER;

    /**
     * 添加系统用户
     *
     * @param sysUser
     * @return
     */
    @Override
    public int addUser(SysUser sysUser) {
        if(getUserByUsername(sysUser.getUserName())> 0){
            throw new AlpsAlertException("用户名:"+sysUser.getUserName()+"已存在!");
        }
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(sysUser.getCreateTime());
      
        if (StringUtils.matchEmail(sysUser.getAccount())) {
            //注册email账号登陆
            accountService.register(sysUser.getUserId(), sysUser.getEmail(),  sysUser.getAccount(),sysUser.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, sysUser.getStatus(), ACCOUNT_DOMAIN_USER, null);
        }else if (StringUtils.matchMobile(sysUser.getAccount())) {
            //注册手机号账号登陆
            accountService.register(sysUser.getUserId(), sysUser.getMobile(), sysUser.getAccount(), sysUser.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, sysUser.getStatus(), ACCOUNT_DOMAIN_USER, null);
        } else {
        	//默认注册用户名账户
            accountService.registerWeb(sysUser,BaseConstants.ACCOUNT_TYPE_USERNAME , sysUser.getDomain(), null);
        }
        
        
        
        return 1;
    }

    /**
     * 更新系统用户
     *
     * @param sysUser
     * @return
     */
    @Override
    public int updateUser(SysUser sysUser) {
        if (sysUser == null || sysUser.getUserId() == null) {
            return 0;
        }
        sysUser.setUpdateTime(new Date());
        return sysUserMapper.updateById(sysUser);
    }

    /**
     *   批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String uids) throws BusinessException
    {
        Long[] userIds = Convert.toLongArray(uids);
        for (Long userId : userIds)
        {
        	SysUser user = sysUserMapper.selectById(userId);
        	 if ("@superAdmin.com" == user.getDomain()){
                throw new BusinessException("不允许删除超级管理员用户");
            }
        }
        return sysUserMapper.deleteUserByIds(userIds);
    }

     
    /**
     * 添加第三方登录用户
     *
     * @param sysUser
     * @param accountType
     */
    @Override
    public void addUserThirdParty(SysUser sysUser, String accountType) {
        if (!accountService.isExist(sysUser.getUserName(), accountType, ACCOUNT_DOMAIN_USER)) {
            sysUser.setUserType(BaseConstants.USER_TYPE_ADMIN);
            sysUser.setCreateTime(new Date());
            sysUser.setUpdateTime(sysUser.getCreateTime());
            //保存系统用户信息
            sysUserMapper.insert(sysUser);
            // 注册账号信息
            accountService.register(sysUser.getUserId(), sysUser.getUserName(),sysUser.getAccount(), sysUser.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN_USER, null);
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public int  updatePassword(Long userId, String password) {
        return accountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN_USER, password);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SysUser> findListPage(PageParams pageParams) {
        SysUser query = pageParams.mapToObject(SysUser.class);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), SysUser::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), SysUser::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), SysUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), SysUser::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return sysUserMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SysUser> findAllList() {
        List<SysUser> list = sysUserMapper.selectList(new QueryWrapper<>());
        return list;
    }
    
    /**
     * 查询权限列表
     *Web 接口查询
     * @return
     */
    @Override
	public List<SysUser> selectAuthorityUserList(SysUser sysUser){
    	List<SysUser> list = sysUserMapper.selectAuthorityUserList(sysUser);
    	return list;
    }
    
    /**
     * 查询权限列表
     *Web 接口查询
     * @return
     */
    @Override
    public List<SysUser> selectUnAuthorityUserList(SysUser sysUser){
    	List<SysUser> list = sysUserMapper.selectUnAuthorityUserList(sysUser);
    	return list;
    }
    
    /**
     * 查询列表
     *
     * @return
     */
	@Override
	public List<SysUser> findUserList(SysUser sysUser) {
		List<SysUser> list = sysUserMapper.selectUserList(sysUser);
		return list;
	}

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public SysUser selectUserById(Long userId) {
    	SysUser  sysUser = sysUserMapper.selectById(userId);
         if(sysUser != null){
        	 long deptId = sysUser.getDeptId();
        	 SysDept dept = sysDeptMapper.selectById(deptId);
        	 sysUser.setDept(dept);
         }
         return sysUser;
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
        List<SysRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (SysRole role : rolesList) {
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
        SysUser sysUser = selectUserById(userId);

        // 加入用户权限
//        List<AlpsAuthority> userGrantedAuthority = authorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(sysUser.getUserName()));
//        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
//            authorities.addAll(userGrantedAuthority);
//        }
        UserAccount userAccount = new UserAccount();
        // 昵称
        userAccount.setNickName(sysUser.getUserName());
        // 头像
        userAccount.setAvatar(sysUser.getAvatar());
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
    public int getUserByUsername(String account) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getAccount, account);
        SysUser saved = sysUserMapper.selectOne(queryWrapper);
        int n = 0;
        if(saved != null) {
        	n = 1;
        }
        return n;
    }
    
    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public int getUserByEmail(String email) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getEmail, email);
        SysUser saved = sysUserMapper.selectOne(queryWrapper);
        int n = 0;
        if(saved != null) {
        	n = 1;
        }
        return n;
    }

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public int getUserByMobile(String mobile) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SysUser::getMobile, mobile);
        SysUser saved = sysUserMapper.selectOne(queryWrapper);
        int n = 0;
        if(saved != null) {
        	n = 1;
        }
        return n;
    }

    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserAccount login(String account,String loginType,String domain) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        SysUser sysAccout = null;
        if (StringUtils.isNotBlank(loginType)) { // 第三方登录标识
            sysAccout = accountService.getAccount(account, loginType, ACCOUNT_DOMAIN_USER);
        } else {
            // 非第三方登录

            //用户名登录
            sysAccout = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN_USER);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                sysAccout = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN_USER);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
                sysAccout = accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN_USER);
            }
        }
        //检查是否是超级管理员登录
        if(sysAccout == null) {
        	sysAccout =  accountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN_SPUER);
        }

        // 获取用户详细信息
        if (sysAccout != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                	SysAccountLogs log = new SysAccountLogs();
                    log.setDomain(ACCOUNT_DOMAIN_USER);
                    log.setUserId(sysAccout.getUserId());
                    log.setAccount(sysAccout.getAccount());
                    log.setAccountId(String.valueOf(sysAccout.getUserId()));
                    log.setAccountType(sysAccout.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    accountService.addLoginLog(log);
                }
            } catch (Exception e) {
                System.out.println("");
            }
            // 用户权限信息
            UserAccount userAccount = getUserAccount(sysAccout.getUserId()); 
            // 复制账号信息
            BeanUtils.copyProperties(sysAccout, userAccount);
            return userAccount;
        }
        return null;
    }

    @Override
    public ResultBody getInfo() {
        // 角色集合
        List<SysRole> roles = roleService.getUserRoles(AlpsHelper.getUserId());
        // 权限集合
        Set<String> permissions = menuService.selectMenuPermsByUserId(AlpsHelper.getUserId());
        Set<String> roleSets=new HashSet<>();
        for (SysRole role : roles)
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