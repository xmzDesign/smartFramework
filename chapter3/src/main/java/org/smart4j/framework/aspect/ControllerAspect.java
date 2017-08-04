package org.smart4j.framework.aspect;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.proxy.AspectProxy;
/**
 * 拦截所有的Controller方法
 * @author Minzhe Xu	2017年7月23日下午4:39:37
 *
 */
public class ControllerAspect extends  AspectProxy{
	private static final Logger LOGGER=LoggerFactory.getLogger(ControllerAspect.class);
	
	private long begin;

	@Override
	public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
		LOGGER.debug("------begin--------");
		LOGGER.debug(String.format("class: %s", cls.getName()));
		LOGGER.debug(String.format("method: %s", method.getName()));
		begin=System.currentTimeMillis();
	}

	@Override
	public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
		LOGGER.debug(String.format("time: %dms", System.currentTimeMillis()-begin));
		LOGGER.debug("---------end--------");
	}
	
	
	
	
	

}
