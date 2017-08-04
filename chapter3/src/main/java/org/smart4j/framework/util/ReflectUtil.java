package org.smart4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * @author Minzhe Xu	2017年7月3日下午3:25:04
 *
 */
public class ReflectUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(ReflectUtil.class);
	/**
	 * 创建实例
	 * @param cls
	 * @return
	 */
	public static Object newInstance(Class<?> cls){
		Object instance;
		try {
			instance=cls.newInstance();
		} catch (Exception e) {
			LOGGER.error("new instance error",e);
			throw new RuntimeException();
		}
		return instance;
	}
	/**
	 * 调用方法
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object invokeMethod(Object obj,Method method,Object...args){
		Object result;
		try {
			method.setAccessible(true);
			result = method.invoke(obj, args);
			
		} catch (Exception e) {
			LOGGER.error("invoke method error",e);
			throw new RuntimeException();
		}
		return result;
	}
	
	public static void setField(Object obj,Field field,Object value){
		try {
			field.setAccessible(true);
			field.set(obj, value);//将指定对象变量上此 Field 对象表示的字段设置为指定的新值。
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
