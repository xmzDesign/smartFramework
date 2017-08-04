package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManage;
import org.smart4j.framework.proxy.TransactionProxy;

public class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);
    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

            for(Map.Entry<Class<?>, List<Proxy>> targetEnt:targetMap.entrySet()){
                Class<?> targetClass = targetEnt.getKey();
                List<Proxy> proxyList = targetEnt.getValue();
                Object proxy = ProxyManage.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop fail",e);
        }

    }

    /**
     * 获取 aspect 设置的 注解类,类型的所有class 集合
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        // Aspect 是对一类注解类 进行拦截,这里是 不为空并且不是 aspect注解,就可以获取该注解
        if(annotation != null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取需要被代理的 所有 class 对象
     * @return key= 切面 ,value = 该切面所增强的所有 class 对象
     * @throws Exception
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>,Set<Class<?>>>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }



    /**
     *  关联普通界面代理类
     * @param proxyMap
     */
    private static void addAspectProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception {
        // 获取 所有的切面代理类 的 子类
        Set<Class<?>> aspectProxySet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> cls : aspectProxySet) {
            if(cls.isAnnotationPresent(Aspect.class)){// 判断该class 是否有 aspect注解(是否是一个切面标注)
                Aspect aspect = cls.getAnnotation(Aspect.class); // 获取注解类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect); // 由于 aspect 的值是接收一个 注解类,所以这里是获取到 使用该注解类的所有class
                proxyMap.put(cls,targetClassSet);
            }
        }
    }

    /**
     * 关联事务代理
     * @param proxyMap
     * @throws Exception
     */
    private static void addTransactionProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception {
        // 获取 所有的service类，这里是简单的处理，默认就把services注解标识的类。都拿过来换成事务代理对象，然后在代理执行的时候，会根据
        //是否有Transaction注解。而去对事务进行操作。
        Set<Class<?>> servicesProxySet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class,servicesProxySet); //将 切面 与 目标类进行关联
    }


    /**
     * 把 class 和 增强它的切面实例进行关联
     * @param proxyMap 切面对应需要增强的class集合.
     * @return
     * @throws Exception
     */
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>,List<Proxy>>();
        for(Map.Entry<Class<?>,Set<Class<?>>> proxyEnt : proxyMap.entrySet()){
            Class<?> proxyClass = proxyEnt.getKey(); // 获取切面class
            Set<Class<?>> targetClassSet = proxyEnt.getValue(); //切面所需要增强的 class 对象(也就是我们需要把该切面在哪些实例上增强的对象class)
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy)proxyClass.newInstance(); // 创建该切面的实例
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else{
                   List<Proxy>  proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }

}
