/**
 * 
 */
package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.junit.rules.MethodRule;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author Minzhe Xu	2017年7月19日下午4:00:19
 *	
 */
public class CglibProxy implements MethodInterceptor{

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		before();
		proxy.invokeSuper(obj, args);
		after();
		return null;
	}
	public CglibProxy(){}
	
	public static class CglibProxyHolder {
		private static CglibProxy instance=new CglibProxy();
	}
	public static CglibProxy getInstace(){
		return CglibProxyHolder.instance;
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getProxy(Class<T> cls){
		return (T) Enhancer.create(cls, this);
	}
	private void after() {
		System.out.println("代理之后");
	}

	private void before() {
		System.out.println("代理之前");
		
	}
	
	public static void main(String[] args) {
		HelloImpl helloProxy=getInstace().getProxy(HelloImpl.class);
		helloProxy.say("jordan");
	}

}
