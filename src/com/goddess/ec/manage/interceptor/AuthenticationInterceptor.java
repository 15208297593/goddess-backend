package com.goddess.ec.manage.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.controller.BaseController;
import com.goddess.ec.manage.model.AdminUser;
import com.goddess.ec.manage.tools.ToolContext;
import com.goddess.ec.manage.tools.ToolWeb;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 权限认证拦截器
 */
public class AuthenticationInterceptor implements Interceptor {

	private static Logger log = Logger.getLogger(AuthenticationInterceptor.class);

	// TODO
	@Override
	public void intercept(Invocation ai) {
		BaseController contro = (BaseController) ai.getController();
		
		AdminUser adminUser = ToolContext.getCurrentUser(contro.getRequest(), true); 
		if(null != adminUser){
			// TODO 临时方案，可考虑介入shiro
			// 品牌商权限验证
			if ("2".equals(adminUser.getStr("role")) && (StringUtils.isEmpty(contro.getPara("brand_id")) || !contro.getPara("brand_id").equals(adminUser.getInt("brand_id").toString()))) {
				log.info("品牌商无权限!");
				contro.redirect("/admin/login");
				return;
			}
			//
	        try {
	            ai.invoke();
	        } catch (Exception e) {
//	            log.info("业务逻辑代码遇到异常时保存日志!");
//	            reqSysLog.set("status", "0");// 失败
//	            reqSysLog.set("description", e.getMessage());
//	            reqSysLog.set("cause", "3");// 业务代码异常
	//
//	            // TODO 失败的时候,返回错误状态值
	//
	        } finally {

	        }
		}else{
			String uri = ToolWeb.getRequestURIWithParam(contro.getRequest());
			// 登录页action
			if (uri.indexOf("/admin/login") >= 0) {
	            ai.invoke();
	            return;
			}
			contro.redirect("/admin/login");
		}
	}

}
