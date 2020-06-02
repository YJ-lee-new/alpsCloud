package com.alps.oauth.uaa.client.web.service;

import com.alps.base.api.model.UserAccount;
import com.alps.base.api.model.entity.SysUser;
import com.alps.common.core.domain.ResultBody;
import com.alps.oauth.uaa.client.web.model.PageParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: YJ.lee
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
	int addUser(SysUser sysUser);

    /**
     * 更新系统用户
     *
     * @param sysUser
     * @return
     */
	int updateUser(SysUser sysUser);

    /**
     * 添加第三方登录用户
     *
     * @param sysUser
     * @param accountType
     * @param
     */
    void addUserThirdParty(SysUser sysUser, String accountType);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    int updatePassword(Long userId, String password);
    
    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteUserByIds(String uids) throws Exception;
     
    /**
               *  接口分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SysUser> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SysUser> findAllList();

    /**
     * 查询列表
     *Web 接口查询
     * @return
     */
    List<SysUser> findUserList(SysUser sysUser);
    /**
     * 查询权限列表
     *Web 接口查询
     * @return
     */
    List<SysUser> selectAuthorityUserList(SysUser sysUser);
    
    /**
     * 查询权限列表
     *Web 接口查询
     * @return
     */
    List<SysUser> selectUnAuthorityUserList(SysUser sysUser);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser selectUserById(Long userId);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    UserAccount getUserAccount(Long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    int getUserByUsername(String account);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    int getUserByEmail(String email);
    
    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    int getUserByMobile(String mobile);

    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     * @return
     */
    UserAccount login(String account,String loginType,String domain);

    ResultBody getInfo();

}
