package org.smart4j.framework.helper;

import java.util.Properties;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;
/**
 * 属性文件助手类
 * @author Minzhe Xu	2017年7月2日下午6:15:48
 *
 */
public final class ConfigHelper {
	private static final Properties CONFIG_PROPS=PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
	
	public static String getByKey(String keyName){
		return PropsUtil.getString(CONFIG_PROPS, keyName);
	}

}
