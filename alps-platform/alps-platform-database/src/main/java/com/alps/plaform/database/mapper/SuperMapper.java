package com.alps.plaform.database.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuperMapper<T> extends BaseMapper<T> {
    IPage<T> pageList(Page<T> page, @Param("ew") Wrapper<?> wrapper);

    List<EntityMap> getEntityMap(@Param("ew") Wrapper<?> wrapper);
}
