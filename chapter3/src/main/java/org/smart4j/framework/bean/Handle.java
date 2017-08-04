package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装Action信息
 * @author Minzhe Xu	2017年7月4日上午11:41:16
 *
 */
public class Handle {
	/**
	 * Controller类
	 */
	private Class<?> controllerClass;
	/**
	 * Action方法
	 */
	private Method actionMethod;
	
	public Class<?> getControllerClass() {
		return controllerClass;
	}
	public Method getActionMethod() {
		return actionMethod;
	}
	public Handle(Class<?> controllerClass,Method actionMethod){
		this.controllerClass=controllerClass;
		this.actionMethod=actionMethod;
	}

}
