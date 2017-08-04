package org.smart4j.framework;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handle;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectUtil;
import org.smart4j.framework.util.StreamUtil;

public class DispatcherServlet extends HttpServlet{

	

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		//初始化Helper类
		HelperLoader.init();
		//获取ServletContext对象(用于注册Servlet)
		ServletContext servletContext=servletConfig.getServletContext();
		//注册处理JSP的Servlet
		ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getByKey("smart.framework.app.jsp_path"));
		//注册处理静态资源的默认servlet
		ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
		jspServlet.addMapping(ConfigHelper.getByKey(ConfigConstant.APP_ASSET_PATH));
		
	}
	
	/**
	 * 通过request 可以找到封装好的Handle 里面有request 对应的Class和Method
	 * 解析request携带的参数
	 * 通过反射调用相应的方法
	 * 返回相应的数据
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取请求方法和路径
		String requestMethod=request.getMethod().toLowerCase();
		String requestPath=request.getPathInfo();
		//获取Action处理器
		Handle handle=ControllerHelper.getHandle(requestMethod, requestPath);
		
		if(handle!=null){
			//获取controller类以及器bean实例
			Class<?> controllerClass=handle.getControllerClass();
			Object controllerBean=BeanHelper.getBean(controllerClass);
			//创建请求参数对象
			Map<String,Object> paramMap=new HashMap<String,Object>();
			Enumeration<String> paramNames=request.getParameterNames();
			while(paramNames.hasMoreElements()){
				String paramName=paramNames.nextElement();
				String paramValue=request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			
			//提取url中的参数
			String body=CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
			if(StringUtils.isNotEmpty(body)){
				String[] params=StringUtils.split(body, "&");
				if(ArrayUtils.isNotEmpty(params)){
					for(String param:params){
						String [] array=StringUtils.split(param, "=");
						if(ArrayUtils.isEmpty(array)&&array.length==2){
							String paramName=array[0];
							String paramValue=array[1];
							paramMap.put(paramName, paramValue);
						}
					}
				}
			}
			
			Param param=new Param(paramMap);
			//调用Action方法
			Method actionMethod=handle.getActionMethod();
			Object result=ReflectUtil.invokeMethod(controllerBean, actionMethod, param);
			//处理Action方法的返回值
			if(result instanceof View){
				//返回JSP页面
				View view=(View)result;
				String path=view.getPath();
				if(StringUtils.isNotEmpty(path)){
					if(path.startsWith("/")){
						response.sendRedirect(request.getContextPath()+path);
					}else{
						Map<String,Object> model=view.getModel();
						for(Map.Entry<String, Object> entry:model.entrySet()){
							request.setAttribute(entry.getKey(), entry.getValue());
						}
						request.getRequestDispatcher(ConfigHelper.getByKey(ConfigConstant.APP_JSP_PATH)+path).forward(request, response);
					}
				}
			}else if(result instanceof Data){
				//返回JSON数据
				Data data=(Data) result;
				Object model=data.getModel();
				if(model!=null){
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					PrintWriter pw=response.getWriter();
					String json=JsonUtil.toJson(model);
					pw.write(json);
					pw.flush();
					pw.close();
				}
			}
		}
	}
	

}
