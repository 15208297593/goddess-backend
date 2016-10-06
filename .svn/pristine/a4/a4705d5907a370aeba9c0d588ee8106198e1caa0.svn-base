package com.goddess.ec.manage.message;

import org.apache.commons.lang3.StringUtils;

public enum AppError {

    ERROR_MSG_1(101, "手机号或者密码错误"),
    ERROR_MSG_2(102, "验证码已过期，请重新获取"),
    ERROR_MSG_3(103, "验证码不正确"),
    ERROR_MSG_4(104, "手机号已被注册"),
    ERROR_MSG_5(105, "您的帐号已被删除"),
    ERROR_MSG_6(106, "您的帐号已登录超时，请重新登录"),
    ERROR_MSG_7(107, "您的帐号验证失败"),
    ERROR_MSG_8(108, "手机号未注册"),
    ERROR_MSG_9(109, "您今天获取验证码的次数过多"),
    ERROR_MSG_10(110, "该账户不能在此微信号中使用"),
    ERROR_MSG_11(111, "请从公众号进入注册"),
    ERROR_MSG_10001(10001, "收藏失败"),
    ERROR_MSG_10002(10002, "取消收藏失败"),
    ERROR_MSG_10003(10003, "加入购物车失败"),
    ERROR_MSG_10004(10004, "购物车商品删除失败"),

    ERROR_MSG_20001(20001, "微信通信失败"),
    ERROR_MSG_20002(20002, "微信签名校验失败"),
    ERROR_MSG_20003(20003, "微信相关业务失败");

    // 成员变量
    private int code;
    private String message;

    // 构造方法
    private AppError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (AppError err : AppError.values()) {
            if (err.getCode() == code) {
                return err.getMessage();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * codeを取得する<br>
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * codeを設定する<br>
     *
     * @param code code
     */
    public void setCode(int code) {
        this.code = code;
    }


    /**
     * messageを取得する<br>
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * messageを設定する<br>
     *
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
