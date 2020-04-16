package com.alps.zuul.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author:Yujie.lee
 * Date:2019年11月2日
 * TodoTODO
 */
public class JsonMapperTool {


	// 定义jackson对象

	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**

	 * 将对象转换成json字符串。

	 * <p>Title: pojoToJson</p>

	 * <p>Description: </p>

	 * @param data

	 * @return

	 */
	public static String objectToJson(Object data) {
		try {
			MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**

	 * 将json结果集转化为对象

	 *

	 * @param jsonData json数据

	 * @param clazz 对象中的object类型

	 * @return

	 */
	public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
		try {
			T t = MAPPER.readValue(jsonData, beanType);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




}
