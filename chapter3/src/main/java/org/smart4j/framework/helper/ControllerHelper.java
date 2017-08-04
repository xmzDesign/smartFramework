package org.smart4j.framework.helper;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handle;
import org.smart4j.framework.bean.Request;

public final class ControllerHelper {
	/**
	 * 用于存放请求和处理器的映射关系(简称Action Map)
	 */
	private static final Map<Request,Handle> ACTION_MAP=new HashMap<Request,Handle>();
	/**
	 * 获取所有的controller类中的所有的带Action注解的方法，
	 * 获取每个方法的请求路径和方法
	 * 把类和方法分装成一个handle
	 * 在把request当做key，handle当做value
	 */
	static{
		//获取所有的Controller类
		Set<Class<?>> controllerClassSet=ClassHelper.getControllerClassSet();
		if(controllerClassSet!=null){
			for(Class<?> controllerClass:controllerClassSet){
				//获取Controller中的方法
				Method[] methods = controllerClass.getDeclaredMethods();
				if(ArrayUtils.isNotEmpty(methods)){
					//遍历这些方法
					for(Method method:methods){
						//判断当前方法是否带有Action注解
						if(method.isAnnotationPresent(Action.class)){
							//从Action注解中获取URL映射规则
							Action action=method.getAnnotation(Action.class);
							String mapping = action.value();
							//验证url映射规则
							if(mapping.matches("\\w+:/\\w*")){
								String[] array=mapping.split(":");
								if(ArrayUtils.isNotEmpty(array)&&array.length==2){
									String requestMethod=array[0];
									String requestPath=array[1];
									Request request=new Request(requestPath, requestMethod);
									Handle handle=new Handle(controllerClass, method);
									ACTION_MAP.put(request, handle);
								}
							}
							
						}
					}
				}
				
			}
		}
	}
	
	public static Handle getHandle(String requestMethod,String requestPath){
		Request request=new Request(requestPath, requestMethod);
		return ACTION_MAP.get(request);
	}

}
