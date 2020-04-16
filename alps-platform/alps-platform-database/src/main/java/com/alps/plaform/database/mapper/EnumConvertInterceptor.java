package com.alps.plaform.database.mapper;

public interface EnumConvertInterceptor {
    boolean convert(EntityMap map, String key, Object v);
}
