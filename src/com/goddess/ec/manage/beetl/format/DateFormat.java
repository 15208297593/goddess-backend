package com.goddess.ec.manage.beetl.format;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.beetl.core.Format;

/**
 * 页面日期格式化
 * 
 * @author dongyihao 2014年9月27日 下午11:23:29
 */
public class DateFormat implements Format {

	public Object format(Object data, String pattern) {
		if (data == null)
			return null;
		if (Date.class.isAssignableFrom(data.getClass())) {
			SimpleDateFormat sdf = null;
			if (pattern == null) {
				sdf = new SimpleDateFormat();
			} else {
				sdf = new SimpleDateFormat(pattern);
			}
			return sdf.format((Date) data);

		} else if (data.getClass() == Long.class) {
			// Date date = new Date((Long) data);
			SimpleDateFormat sdf = null;
			if (pattern == null) {
				sdf = new SimpleDateFormat();
			} else {
				sdf = new SimpleDateFormat(pattern);
			}
			return sdf.format((Date) data);

		} else {
			throw new RuntimeException("Arg Error:Type should be Date:" + data.getClass());
		}

	}

}