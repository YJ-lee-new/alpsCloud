package com.alps.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alps.job.admin.core.model.AlpsJobGroup;

import java.util.List;

/**
 * @author  Yujie.lee on 16/9/30.
 */
@Mapper
public interface AlpsJobGroupDao {

    public List<AlpsJobGroup> findAll();

    public List<AlpsJobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(AlpsJobGroup alpsJobGroup);

    public int update(AlpsJobGroup alpsJobGroup);

    public int remove(@Param("id") int id);

    public AlpsJobGroup load(@Param("id") int id);
}
