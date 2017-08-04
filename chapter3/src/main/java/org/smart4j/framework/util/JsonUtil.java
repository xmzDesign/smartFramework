package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(JsonUtil.class);
	
	private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

	public static <T> String toJson(T model) {
		String json;
		try {
			json=OBJECT_MAPPER.writeValueAsString(model);
		} catch (Exception e) {
			LOGGER.error("convert POJO to JSON failue",e);
			throw new RuntimeException(e);
		}
		return json;
	}
	
	/**
	 * 将json转为POJO
	 */
	public static <T> T fromJson(String json,Class<T> type){
		T pojo;
		try {
			pojo=OBJECT_MAPPER.readValue(json, type);
		} catch (Exception e) {
			LOGGER.error("convert JSON to POJO failue",e);
			throw new RuntimeException(e);
		}
		return pojo;
	}

}
