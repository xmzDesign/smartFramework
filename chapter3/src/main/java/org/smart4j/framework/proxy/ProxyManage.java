package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyManage {
	/**
     * 创建代理
     * @param targetClass 目标类
     * @param proxyList 代理列表
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T>T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object targetObj, Method method, Object[] paramsObjects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObj,method,methodProxy,paramsObjects,proxyList).doProxyChain();
            }
        });
    }
}
