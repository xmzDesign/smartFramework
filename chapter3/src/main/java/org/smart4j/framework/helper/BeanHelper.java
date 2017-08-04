package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectUtil;

public final class BeanHelper {
	/**
	 * 通过ClassHeper 获取bean以后，进行实例化
	 */
	private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>,Object>();
	static{
		Set<Class<?>> beanClassSet=ClassHelper.getBeanClassSet();
		for(Class<?> beanClass:beanClassSet){
			Object object = ReflectUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, object);
		}
	}
	
	/**
	 * 获取bean映射
	 */
	public static Map<Class<?>,Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	public static <T> T getBean(Class<T> cls){
		if(!BEAN_MAP.containsKey(cls)){
			throw new RuntimeException("can not get bean by class:"+cls);
		}
		return (T) BEAN_MAP.get(cls);
	}
	
	public static void setBean(Class<?> cls,Object obj){
		BEAN_MAP.put(cls, obj);
	}

}
