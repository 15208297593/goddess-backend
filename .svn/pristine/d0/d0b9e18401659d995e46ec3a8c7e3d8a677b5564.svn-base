package com.goddess.ec.manage.beetl.func;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * 过滤xml文档函数
 * 
 * @author dongyihao 2014年9月27日 下午11:30:31
 */
public class EscapeXml implements Function {

	private static Logger log = Logger.getLogger(EscapeXml.class);

	@Override
	public Object call(Object[] arg, Context context) {
		if (arg.length != 1 || null == arg[0] || !(arg[0] instanceof String)) {
			return "";
		}
		String content = null;
		try {
			content = (String) arg[0];
		} catch (Exception e) {
			return "";
		}

		log.debug("xml转换处理");

		return StringEscapeUtils.escapeXml(content);
	}

}
