package org.smart4j.framework.helper;


import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

/**
 * 类操作助手
 * @author Minzhe Xu	2017年7月3日上午9:44:15
 *
 */
public final class ClassHelper {
	private static final Set<Class<?>> CLASS_SET;
	
	static{
		String packageName=ConfigHelper.getByKey("smart.framework.app.base_package");
		CLASS_SET=ClassUtil.getClassSet(packageName);
	}
	/**
	 * 获取应用包名下所有类
	 * @return
	 */
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	/**
	 * 获取所有的Service类
	 * @return
	 */
	public static Set<Class<?>> getServiceClassSet(){
		Set<Class<?>> set=new HashSet<Class<?>>();
		for(Class<?> cls:CLASS_SET){
			if(cls.isAnnotationPresent(Service.class)){
				set.add(cls);
			}
		}
		return set;
	}
	/**
	 * 获取controller
	 * @return
	 */
	public static Set<Class<?>> getControllerClassSet(){
		Set<Class<?>> set=new HashSet<Class<?>>();
		for(Class<?> cls:CLASS_SET){
			if(cls.isAnnotationPresent(Controller.class)){
				set.add(cls);
			}
		}
		return set;
	}
	/**
	 * 获取应用包名下所有的Bean类
	 */
	public static Set<Class<?>> getBeanClassSet(){
		Set<Class<?>> beanClassSet=new HashSet<Class<?>>();
		beanClassSet.addAll(getServiceClassSet());
		beanClassSet.addAll(getControllerClassSet());
		return beanClassSet;
	}
	/**
	 * 获取应用包名下某父类的所有子类
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		Set<Class<?>> classSet=new HashSet<Class<?>>();
		for(Class<?> cls:CLASS_SET){
			if(superClass.isAssignableFrom(cls)&&!superClass.equals(cls)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotatiion){
		Set<Class<?>> classSet=new HashSet<Class<?>>();
		for(Class<?> cls:CLASS_SET){
			if(cls.isAnnotationPresent(annotatiion)){
				classSet.add(cls);
			}
		}
		return classSet;
	}

}
