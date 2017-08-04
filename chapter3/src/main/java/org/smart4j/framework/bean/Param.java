package org.smart4j.framework.bean;

import java.util.Map;

import org.smart4j.framework.util.CastUtil;

public class Param {
	private Map<String,Object> paramMap;
	
	public Param(Map<String,Object> paramMap){
		this.paramMap=paramMap;
	}
	/**
	 * 根据参数名获取long型参数
	 */
	public long getLong(String name){
		return CastUtil.castLong(paramMap.get(name));
	}
	/**
	 * 获取所得字段信息
	 * 
	 */
	public Map<String,Object> getMap(){
		return paramMap;
	}

}
