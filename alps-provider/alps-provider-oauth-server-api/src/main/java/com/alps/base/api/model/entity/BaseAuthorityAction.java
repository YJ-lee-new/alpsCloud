package com.alps.base.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * Todo 系统权限-功能操作关联表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("base_authority_action")
public class BaseAuthorityAction extends AbstractEntity {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 操作资源ID
     */
    private Long actionId;

    /**
     * 权限ID
     */
    private Long authorityId;
}
