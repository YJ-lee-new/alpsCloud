package com.alps.job.admin.controller;

import com.alps.job.admin.controller.annotation.PermissionLimit;
import com.alps.job.admin.core.model.AlpsJobGroup;
import com.alps.job.admin.core.model.AlpsJobUser;
import com.alps.job.admin.core.util.I18nUtil;
import com.alps.job.admin.dao.AlpsJobGroupDao;
import com.alps.job.admin.dao.AlpsJobUserDao;
import com.alps.job.admin.service.LoginService;
import com.alps.job.common.biz.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Yujie.lee 2020/4/10
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private AlpsJobUserDao alpsJobUserDao;
    @Resource
    private AlpsJobGroupDao alpsJobGroupDao;

    @RequestMapping
    @PermissionLimit(adminuser = true)
    public String index(Model model) {

        // 执行器列表
        List<AlpsJobGroup> groupList = alpsJobGroupDao.findAll();
        model.addAttribute("groupList", groupList);

        return "user/user.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String username, int role) {

        // page list
        List<AlpsJobUser> list = alpsJobUserDao.pageList(start, length, username, role);
        int list_count = alpsJobUserDao.pageListCount(start, length, username, role);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        maps.put("data", list);  					// 分页列表
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> add(AlpsJobUser alpsJobUser) {

        // valid username
        if (!StringUtils.hasText(alpsJobUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input")+I18nUtil.getString("user_username") );
        }
        alpsJobUser.setUsername(alpsJobUser.getUsername().trim());
        if (!(alpsJobUser.getUsername().length()>=4 && alpsJobUser.getUsername().length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }
        // valid password
        if (!StringUtils.hasText(alpsJobUser.getPassword())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input")+I18nUtil.getString("user_password") );
        }
        alpsJobUser.setPassword(alpsJobUser.getPassword().trim());
        if (!(alpsJobUser.getPassword().length()>=4 && alpsJobUser.getPassword().length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }
        // md5 password
        alpsJobUser.setPassword(DigestUtils.md5DigestAsHex(alpsJobUser.getPassword().getBytes()));

        // check repeat
        AlpsJobUser existUser = alpsJobUserDao.loadByUserName(alpsJobUser.getUsername());
        if (existUser != null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("user_username_repeat") );
        }

        // write
        alpsJobUserDao.save(alpsJobUser);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> update(HttpServletRequest request, AlpsJobUser alpsJobUser) {

        // avoid opt login seft
        AlpsJobUser loginUser = (AlpsJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (loginUser.getUsername().equals(alpsJobUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
        }

        // valid password
        if (StringUtils.hasText(alpsJobUser.getPassword())) {
            alpsJobUser.setPassword(alpsJobUser.getPassword().trim());
            if (!(alpsJobUser.getPassword().length()>=4 && alpsJobUser.getPassword().length()<=20)) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
            }
            // md5 password
            alpsJobUser.setPassword(DigestUtils.md5DigestAsHex(alpsJobUser.getPassword().getBytes()));
        } else {
            alpsJobUser.setPassword(null);
        }

        // write
        alpsJobUserDao.update(alpsJobUser);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/remove")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> remove(HttpServletRequest request, int id) {

        // avoid opt login seft
        AlpsJobUser loginUser = (AlpsJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (loginUser.getId() == id) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
        }

        alpsJobUserDao.delete(id);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ReturnT<String> updatePwd(HttpServletRequest request, String password){

        // valid password
        if (password==null || password.trim().length()==0){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        password = password.trim();
        if (!(password.length()>=4 && password.length()<=20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit")+"[4-20]" );
        }

        // md5 password
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // update pwd
        AlpsJobUser loginUser = (AlpsJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);

        // do write
        AlpsJobUser existUser = alpsJobUserDao.loadByUserName(loginUser.getUsername());
        existUser.setPassword(md5Password);
        alpsJobUserDao.update(existUser);

        return ReturnT.SUCCESS;
    }

}
