package org.smart4j.framework.proxy;
/**
 * 
 * @author Minzhe Xu	2017年7月22日上午11:55:55
 *
 */
public interface Proxy {
	Object doProxy(ProxyChain proxyChain) throws Throwable;//链式代理，将多个代理通过一条链子串起来，一个个去执行

	

}
