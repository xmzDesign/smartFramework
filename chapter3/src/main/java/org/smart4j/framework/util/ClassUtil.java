package org.smart4j.framework.util;


import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 * @author Minzhe Xu	2017年7月2日下午6:18:03
 *
 */
public class ClassUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(ClassUtil.class);
	/**
	 * 获取类加载器
	 */
	public static ClassLoader getClassLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	/**
	 * 加载类
	 * 需要提供类名与是否初始化的标志，这里提到初始化指是否执行类的静态代码块
	 * 
	 * 为了提高性能 可以将isInitialized 设置为false
	 */
	public static Class<?> loadClass(String className,boolean isInitialized){
		Class<?> cls;
		try {
			cls=Class.forName(className, isInitialized,  getClassLoader());
		} catch (Exception e) {
			LOGGER.error("load class failue",e);
			throw new RuntimeException(e);
		}
		return cls;
	}
	/**
	 * 获取指定包名下的所有类
	 */
	public static Set<Class<?>> getClassSet(String packageName){
		Set<Class<?>> classSet=new HashSet<Class<?>>();
		try {
			Enumeration<URL> urls=getClassLoader().getResources(packageName.replace(".", "/"));
			while(urls.hasMoreElements()){
				URL url=urls.nextElement();
				if(url!=null){
					String protocol=url.getProtocol();
					 if ("file".equals(protocol)) {  
	                        // 本地自己可见的代码  
						 String packagePath=url.getPath().replaceAll("%20", "");
	                        addClass(classSet, packagePath,packageName);  
	                    } else if ("jar".equals(protocol)) {  
	                       JarURLConnection jarURLConnection=(JarURLConnection) url.openConnection(); 
	                       if(jarURLConnection!=null){
	                    	   JarFile jarFile=jarURLConnection.getJarFile();
	                    	   if(jarFile!=null){
	                    		   Enumeration<JarEntry> jarEntities=jarFile.entries();
	                    		   while(jarEntities.hasMoreElements()){
	                    			   JarEntry jarEntry=jarEntities.nextElement();
	                    			   String jarEntryName = jarEntry.getName();
	                    			   if(jarEntryName.endsWith(".class")){
	                    				   String className = jarEntryName.
	                    						   substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
	                    				   doAddClass(classSet,className);

	                    			   }
	                    		   }
	                    	   }
	                       }
	                    }  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classSet;
	}
	public static void doAddClass(Set<Class<?>> classSet, String className) {
		 Class<?> cls = loadClass(className, false);
	        classSet.add(cls);
		
	}
	public static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
		File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                // 只需要 文件并且是.class的文件,或则是目录 都返回true
                return file.isFile() && file.getName().endsWith(".class") || file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if(file.isFile()){ // 是指定的文件 就获取到全限定类名 然后装载它
                String className = fileName.substring(0, fileName.lastIndexOf(".")); // 把.class后最截取掉
                if(StringUtils.isNotBlank(packageName)){
                    className = packageName + "." + className; // 根据包名 + 文件名 得到这个类的全限定名称,
                }
                doAddClass(classSet,className);
            }else { // 是文件 就递归自己. 获取 文件夹的绝对路径,和 当前文件夹对应的 限定包名.方便 文件里面直接使用

                String subPackagePath= fileName;
                if(StringUtils.isNotBlank(subPackagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath; // 第一次:由基础包名 得到绝对路径,再加上当前文件夹名称 = 当前文件夹的绝对路径
                }
                subPackagePath = file.getAbsolutePath(); // 该方法获得文件的绝对路径.和上面的代码效果是一致的
                String subPackageName = fileName;
                if(StringUtils.isNotBlank(subPackageName)){
                    subPackageName = packageName + "." + subPackageName; // 第一次: 基础包名 加文件夹名称 组合成 当前包名 +
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
		
	}

}
