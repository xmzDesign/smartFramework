package org.smart4j.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {
	private Object target;
	public DynamicProxy(Object target){
		this.target=target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before();
		Object result = method.invoke(target, args);
		after();
		return result;
	}
	private void after() {
		System.out.println("代理之后");
	}

	private void before() {
		System.out.println("代理之前");
		
	}
	@SuppressWarnings("unchecked")
	public <T>T getProxy(){
		return  (T) Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), 
				this);
	}

	
	public static void main(String[] args) {
		
		DynamicProxy dynamicProxy=new DynamicProxy(new HelloImpl());
		Hello proxy = dynamicProxy.getProxy();
		proxy.say("jack");
		
	}

}
