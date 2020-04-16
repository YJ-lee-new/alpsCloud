package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Data
@NoArgsConstructor
@TableName("base_account_logs")
public class BaseAccountLogs implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1881457833226812160L;

	@TableId(type= IdType.ID_WORKER)
    private Long id;

    private Date loginTime;

    /**
     * 登录Ip
     */
    private String loginIp;

    /**
     * 登录设备
     */
    private String loginAgent;

    /**
     * 登录次数
     */
    private Integer loginNums;

    private Long userId;

    private String account;

    private String accountType;

    private String accountId;

    private String domain;
}
