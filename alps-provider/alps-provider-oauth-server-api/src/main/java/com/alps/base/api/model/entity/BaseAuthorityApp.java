package com.alps.base.api.model.entity;

import com.alps.common.enums.AbstractEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 系统权限-应用关联
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("base_authority_app")
public class BaseAuthorityApp extends AbstractEntity {
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 过期时间:null表示长期
     */
    private Date expireTime;

    private static final long serialVersionUID = 1L;
}
