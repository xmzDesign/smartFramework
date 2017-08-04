/**
 * 
 */
package org.smart4j.framework.aop;

/**
 * @author Minzhe Xu	2017年7月19日下午4:44:37
 *	
 */
public class GreetingImpl implements Greeting {

	/* (non-Javadoc)
	 * @see org.smart4j.framework.aop.Greeting#sayHello(java.lang.String)
	 */
	public void sayHello(String name) {
		System.out.println("hello"+name);
		throw new RuntimeException("Error");
	}
	

}
