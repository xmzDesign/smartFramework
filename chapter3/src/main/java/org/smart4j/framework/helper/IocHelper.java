package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectUtil;

public class IocHelper {
	static{
		Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
		if(beanMap!=null &&beanMap.size()>0){
			for(Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet()){
				//从beanMap中获取Bean类与Bean实例
				Class<?> beanClass=beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//获取Bean类定义的所有成员变量
				Field[] beanFields=beanClass.getDeclaredFields();
				if(ArrayUtils.isNotEmpty(beanFields)){
					//遍历所有的Bean Field
					for(Field beanField:beanFields){
						if(beanField.isAnnotationPresent(Inject.class)){
							//在Bean Map 中获取Bean Field 对应的实例
							Class<?> beanFieldClass=beanField.getType();//返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型
							Object beanFieldInstance=beanMap.get(beanFieldClass);
							if(beanFieldInstance!=null){
								//通过反射初始化BeanField的值
								ReflectUtil.setField(beanInstance, beanField, beanFieldInstance);
							}
						}
					}
				}
			}
		}
	}

}
