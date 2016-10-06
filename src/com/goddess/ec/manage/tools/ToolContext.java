package com.goddess.ec.manage.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.model.AdminUser;
import com.goddess.ec.manage.model.User;
import com.goddess.ec.manage.plugin.PropertiesPlugin;

/**
 * WEB上下文工具类
 *
 * @author 董华健 2012-9-7 下午1:51:04
 */
public class ToolContext {

	private static Logger log = Logger.getLogger(ToolContext.class);


	/**
	 * 获取当前登录用户
	 *
	 * @param request
	 * @param userAgentVali
	 *            是否验证 User-Agent
	 * @return
	 */
	public static AdminUser getCurrentUser(HttpServletRequest request, boolean userAgentVali) {
		String loginCookie = ToolWeb.getCookieValueByName(request, "authmark");
		if (null != loginCookie && !loginCookie.equals("")) {
			// 1. Base64解码cookie令牌
			try {
				loginCookie = ToolString.decode(loginCookie);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 2. 解密cookie令牌
			byte[] securityByte = Base64.decodeBase64(loginCookie);

//			String securityKey = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_securityKey_key);
//			byte[] keyByte = Base64.decodeBase64(securityKey);
//
//			byte[] dataByte = null;
			byte[] dataByte = securityByte;
//			try {
//				dataByte = ToolSecurityIDEA.decrypt(securityByte, keyByte);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			String data = new String(dataByte);
			String[] datas = data.split(".#."); // arr[0]：时间戳，arr[1]：USERID，arr[2]：USER_IP，
												// arr[3]：USER_AGENT

			// 3. 获取数据
			long loginDateTimes = Long.parseLong(datas[0]);// 时间戳
			String userIds = datas[1];// 用户id
			String ips = datas[2];// ip地址
			String userAgent = datas[3];// USER_AGENT

			String newIp = ToolWeb.getIpAddr(request);
			String newUserAgent = request.getHeader("User-Agent");

			Date start = ToolDateTime.getDate();
			start.setTime(loginDateTimes);
			int day = ToolDateTime.getDateDaySpace(start, ToolDateTime.getDate());

			// 4. 验证数据有效性
			if (ips.equals(newIp) && (userAgentVali ? userAgent.equals(newUserAgent) : true) && day <= 365) {
				Object userObj = AdminUser.dao.cacheGet(userIds);
				if (null != userObj) {
					AdminUser user = (AdminUser) userObj;
					return user;
				}
			}
		}

		return null;
	}

	/**
	 * 设置当前登录用户
	 *
	 * @param request
	 * @param response
	 * @param user
	 * @param autoLogin
	 */
	public static void setCurrentUser(HttpServletRequest request, HttpServletResponse response, AdminUser user,
	        boolean autoLogin) {
		// 1.设置cookie有效时间
		int maxAgeTemp = -1;
		if (autoLogin) {
			maxAgeTemp = (int) PropertiesPlugin.getParamMapValue(DictKeys.config_maxAge_key);
		}

		// 2.设置用户名到cookie
		String ecnNickName = user.getStr("user_name");
		try {
			ecnNickName = ToolString.encode(user.getStr("user_name"));
		} catch (Exception e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		ToolWeb.addCookie(response, "", "/", true, "userName", ecnNickName, maxAgeTemp);

		// 3.生成登陆认证cookie
		String userIds = user.getInt("user_id").toString();
		String ips = ToolWeb.getIpAddr(request);
		String userAgent = request.getHeader("User-Agent");
		long date = ToolDateTime.getDateByTime();

		StringBuilder token = new StringBuilder();// 时间戳#USERID#USER_IP#USER_AGENT
		token.append(date).append(".#.").append(userIds).append(".#.").append(ips).append(".#.").append(userAgent);
		String authToken = token.toString();
//		byte[] authTokenByte = null;
//		try {
//			authTokenByte = authToken.getBytes(ToolString.encoding);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		String securityKey = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_securityKey_key);
//		byte[] keyByte = Base64.decodeBase64(securityKey);
//
//		// 4. 认证cookie加密
//		byte[] securityByte = null;
		byte[] securityByte = authToken.getBytes();
//		try {
//			securityByte = ToolSecurityIDEA.encrypt(authTokenByte, keyByte);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String securityCookie = Base64.encodeBase64String(securityByte);

		// 5. 认证cookie Base64编码
		try {
			securityCookie = ToolString.encode(securityCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 6. 添加到Cookie
		ToolWeb.addCookie(response, "", "/", true, "authmark", securityCookie, maxAgeTemp);
	}

	/**
	 * 设置验证码
	 *
	 * @param response
	 * @param authCode
	 */
	public static void setAuthCode(HttpServletResponse response, String authCode) {
		byte[] authTokenByte = null;
		try {
			authTokenByte = authCode.getBytes(ToolString.encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String securityKey = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_securityKey_key);
		byte[] keyByte = Base64.decodeBase64(securityKey);

		// 加密
		byte[] securityByte = null;
		try {
			securityByte = ToolSecurityIDEA.encrypt(authTokenByte, keyByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String securityCookie = Base64.encodeBase64String(securityByte);

		// Base64编码
		try {
			securityCookie = ToolString.encode(securityCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 登陆认证cookie
		int maxAgeTemp = (int) PropertiesPlugin.getParamMapValue(DictKeys.config_maxAge_key);
		ToolWeb.addCookie(response, "", "/", true, "authCode", securityCookie, maxAgeTemp);
	}

	/**
	 * 获取验证码
	 *
	 * @param request
	 * @return
	 */
	public static String getAuthCode(HttpServletRequest request) {
		String authCode = ToolWeb.getCookieValueByName(request, "authCode");
		if (null != authCode && !authCode.equals("")) {
			// Base64解码
			try {
				authCode = ToolString.decode(authCode);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 解密
			byte[] securityByte = Base64.decodeBase64(authCode);

			String securityKey = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_securityKey_key);
			byte[] keyByte = Base64.decodeBase64(securityKey);

			byte[] dataByte = null;
			try {
				dataByte = ToolSecurityIDEA.decrypt(securityByte, keyByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
			authCode = new String(dataByte);
		}
		return authCode;
	}

	/**
	 * 获取请求参数
	 *
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (null != value && !value.isEmpty()) {
			try {
				value = URLDecoder.decode(value, ToolString.encoding).trim();
			} catch (UnsupportedEncodingException e) {
				log.error("decode异常：" + value);
			}
		}
		return value;
	}

	/**
	 * 请求流转字符串
	 *
	 * @param request
	 * @return
	 */
	public static String requestStream(HttpServletRequest request) {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			request.setCharacterEncoding(ToolString.encoding);
			inputStream = (ServletInputStream) request.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, ToolString.encoding);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			log.error("request.getInputStream() to String 异常", e);
			return null;
		} finally { // 释放资源
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					log.error("bufferedReader.close()异常", e);
				}
				bufferedReader = null;
			}

			if (null != inputStreamReader) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					log.error("inputStreamReader.close()异常", e);
				}
				inputStreamReader = null;
			}

			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("inputStream.close()异常", e);
				}
				inputStream = null;
			}
		}
	}
}
