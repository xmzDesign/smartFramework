/**
 * 
 */
package org.smart4j.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Minzhe Xu	2017年7月18日下午2:57:16
 *	
 */
public class StreamUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(StreamUtil.class);

	public static String getString(ServletInputStream inputStream) {
		StringBuilder sb=new StringBuilder();
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
		} catch (Exception e) {
			LOGGER.error("get string failure", e);
			throw new RuntimeException(e);
		}
		
		return sb.toString();
	}

}
