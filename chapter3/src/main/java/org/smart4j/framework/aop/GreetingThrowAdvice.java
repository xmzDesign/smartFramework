package org.smart4j.framework.aop;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

public class GreetingThrowAdvice implements ThrowsAdvice{
	public void afterThrowing(Method method,Object []args,Object target,Exception e){
		System.out.println("--------Throw Exception------------");
		System.out.println("Target Class:"+target.getClass().getName());
		System.out.println("Method Name:"+method.getName());
		System.out.println("Exception Message");
	}

}
