package org.smart4j.framework.proxy;

public class HelloProxy implements Hello{
	private Hello hello;
	
	public HelloProxy (){
		hello=new HelloProxy();
	}

	public void say(String name) {
		hello.say(name);
	}
	
	private void after() {
		System.out.println("代理之后");
	}

	private void before() {
		System.out.println("代理之前");
		
	}

}
