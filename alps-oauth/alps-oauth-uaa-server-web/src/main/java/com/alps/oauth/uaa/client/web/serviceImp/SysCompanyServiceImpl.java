package com.alps.oauth.uaa.client.web.serviceImp;

import com.alps.base.api.model.entity.SysCompany;
import com.alps.oauth.uaa.client.web.mapper.SysCompanyMapper;
import com.alps.oauth.uaa.client.web.service.ISysCompanyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author YJ.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysCompanyServiceImpl extends BaseServiceImpl<SysCompanyMapper, SysCompany> implements ISysCompanyService {

    @Autowired
    private SysCompanyMapper sysCompanyMapper;
    
	@Override
	public List<SysCompany> selectSysAppList(SysCompany sysCompany) {
		QueryWrapper<SysCompany> queryWrapper = new QueryWrapper();
		return sysCompanyMapper.selectList(queryWrapper) ;
	}

}
