package org.smart4j.framework.bean;
/**
 * 封装请求消息
 * @author Minzhe Xu	2017年7月4日上午11:37:22
 *
 */
public class Request {
	/**
	 * 请求方法
	 */
	private String requestMethod;
	/**
	 * 请求路劲
	 */
	private String requestPath;
	
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getRequestPath() {
		return requestPath;
	}
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	
	public Request(String requestPath,String requestMethod){
		this.requestPath = requestPath;
		this.requestMethod = requestMethod;
	}

}
