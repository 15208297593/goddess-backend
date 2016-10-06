package com.goddess.ec.manage.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.Syslog;
import com.goddess.ec.manage.tools.ToolContext;
import com.goddess.ec.manage.tools.ToolString;
import com.jfinal.core.Controller;

/**
 * 公共Controller
 *
 * @author dongyihao 2014年9月27日 下午11:44:37
 */
public abstract class BaseController extends Controller {

	private static Logger log = Logger.getLogger(BaseController.class);

	/**
	 * 全局变量
	 */
	protected String ids; // 主键
	protected SplitPage splitPage; // 分页封装
	protected List<?> list; // 公共list
	protected Syslog reqSysLog; // 访问日志

	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl() {
		String toUrl = getPara("toUrl");
		render(toUrl);
	}

	/**
	 * 获取当前请求国际化参数
	 *
	 * @return
	 */
	protected String getI18nPram() {
		return getAttr("localePram");
	}

	/**
	 * 获取项目请求根路径
	 *
	 * @return
	 */
	protected String getCxt() {
		return getAttr("cxt");
	}

	/**
	 * 获取当前用户id
	 *
	 * @return
	 */
	protected String getUserIds() {
		return reqSysLog.getStr("userids");
	}

	/**
	 * 重写getPara，进行二次decode解码
	 */
	@Override
	public String getPara(String name) {
		String value = getRequest().getParameter(name);
		if (null != value && !value.isEmpty()) {
			try {
				value = URLDecoder.decode(value, ToolString.encoding);
			} catch (UnsupportedEncodingException e) {
				log.error("decode异常：" + value);
			}
		}
		return value;
	}

	/**
	 * 判断验证码是否正确
	 */
	protected boolean authCode() {
		String authCodePram = getPara("authCode");
		String authCodeCookie = ToolContext.getAuthCode(getRequest());
		if (null != authCodePram && null != authCodeCookie) {
			authCodePram = authCodePram.toLowerCase();// 统一小写
			authCodeCookie = authCodeCookie.toLowerCase();// 统一小写
			if (authCodePram.equals(authCodeCookie)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取查询参数 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 *
	 * @return
	 */
	public Map<String, String> getQueryParam() {
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (name.startsWith("_query") && !value.isEmpty()) {// 查询参数分拣
				String key = name.substring(7);
				if (null != value && !value.trim().equals("")) {
					queryParam.put(key, value.trim());
				}
			}
		}

		return queryParam;
	}

	/**
	 * 设置默认排序
	 *
	 * @param colunm
	 * @param mode
	 */
	public void defaultOrder(String colunm, String mode) {
		if (null == splitPage.getOrderColunm() || splitPage.getOrderColunm().isEmpty()) {
			splitPage.setOrderColunm(colunm);
			splitPage.setOrderMode(mode);
		}
	}

	/**
	 * 排序条件 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 *
	 * @return
	 */
	public String getOrderColunm() {
		String orderColunm = getPara("orderColunm");
		return orderColunm;
	}

	/**
	 * 排序方式 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 *
	 * @return
	 */
	public String getOrderMode() {
		String orderMode = getPara("orderMode");
		return orderMode;
	}

	protected int calcReturnPoints(int price) {
		return price / 10;
	}
	/************************************ get set 方法 ************************************************/

	public Syslog getReqSysLog() {
		return reqSysLog;
	}

	public void setReqSysLog(Syslog reqSysLog) {
		this.reqSysLog = reqSysLog;
	}

	public void setSplitPage(SplitPage splitPage) {
		this.splitPage = splitPage;
	}

}
