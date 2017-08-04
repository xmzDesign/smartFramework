/**
 * 
 */
package org.smart4j.framework.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author Minzhe Xu	2017年7月23日上午10:51:26
 *	
 */
public @interface Transaction {

}
