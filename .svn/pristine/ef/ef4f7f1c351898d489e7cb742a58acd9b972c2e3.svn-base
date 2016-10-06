package com.goddess.ec.manage.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.data.WxRefund;
import com.goddess.ec.manage.data.WxRefundResponse;
import com.goddess.ec.manage.message.AppError;
import com.goddess.ec.manage.model.Order;
import com.goddess.ec.manage.model.OrderStatus;
import com.goddess.ec.manage.model.ReturnStatus;
import com.goddess.ec.manage.tools.ToolDateTime;
import com.goddess.ec.manage.tools.ToolMD5;
import com.goddess.ec.manage.tools.ToolReflect;
import com.goddess.ec.manage.tools.ToolUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class WxService  extends BaseService {

    private static Logger log = Logger.getLogger(WxService.class);

    public static final WxService service = new WxService();

    public static Properties wxProperties = new Properties();
    static {
    	try {
			wxProperties.load(WxService.class.getClassLoader().getResourceAsStream("weixin.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	private static String appId = wxProperties.getProperty("appid");
	private static String mchId = wxProperties.getProperty("mch_id");
	private static String apiKey = wxProperties.getProperty("api_key");
	
	public static String CODE_SUCCESS = "SUCCESS";
	public static String CODE_FAIL = "FAIL";
	
    /**
     * 微信接口请求（xml）
     * @throws Exception 
     */
	private String sendRequestInXml(String url, String content) throws Exception {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(EntityBuilder.create().setText(content).setContentType(ContentType.TEXT_XML.withCharset("UTF-8")).build());
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                // ensure it is fully consumed
                EntityUtils.consume(entity);
                return result;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
	
	/**
	 * 申请微信退款
	 * 
	 * 注意：
	 * 1.交易时间超过半年的订单无法提交退款；
	 * 2.微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额。
	 * 
	 * @param orderId
	 * @param refundFee
	 * 
	 * @return 退款错误信息
	 * @throws Exception
	 */
	public String cancelRefund(String orderId, int refundFee) throws Exception {

		// 关联退货订单
		Order o = Order.dao.findById(orderId);
		
//		int refundFee = o.getInt("apply_refund_fee");
		
		WxRefund refund = new WxRefund();
		refund.setAppid(appId);
		refund.setMch_id(mchId);
		// 操作员帐号, 默认为商户号
		refund.setOp_user_id(mchId);
		// 商户退款单号
		String refundNo = o.get("refund_no", String.valueOf(ToolDateTime.getDateByTime()));
		o.set("refund_no", refundNo);
		refund.setOut_refund_no(refundNo);
		refund.setRefund_fee(refundFee*100);
		
		String internalOrderStatus = o.getStr("internal_order_status");
		// 订金退款
		if (AppConstants.INTERNAL_ORDER_STATUS_DEPOSIT_PAID.equals(internalOrderStatus)) {
			refund.setOut_trade_no(o.getStr("deposit_trade_no"));
			refund.setTotal_fee(o.getInt("deposit_fee")*100);
			refund.setTransaction_id(o.getStr("deposit_transaction_id"));
		// 退货退款
		} else if (AppConstants.INTERNAL_ORDER_STATUS_FINISHED.equals(internalOrderStatus)) {
			
		// 支付后，取消订单退款
		} else {
			refund.setOut_trade_no(o.get("settle_trade_no", orderId).toString());
			refund.setTotal_fee(o.getInt("payment")*100);
			refund.setTransaction_id(o.getStr("transaction_id"));
		}
		refund.setNonce_str(createNonceStr());
		refund.setSign(generateSign(refund));
		
		String resp = sendRequestInXml(wxProperties.getProperty("refund_url"), ToolUtils.toXml(WxRefund.getXstream(), refund));
		WxRefundResponse wrr = ToolUtils.fromXml(WxRefundResponse.getXstream(), WxRefundResponse.class, resp);
		
		log.info(ToolUtils.toXml(WxRefundResponse.getXstream(), wrr));
		String errMsg = wrr.getReturn_msg();
		//TODO 退货退款的情况，不更新原订单状态，更新退货订单支付状态
		// 解析微信返回结果
		if (CODE_FAIL.equals(wrr.getReturn_code())) {
			o.set("wx_err_code", AppError.ERROR_MSG_20001.getCode());
			o.set("wx_err_msg", wrr.getReturn_msg());
		// 签名校验
		} else if (!wrr.getSign().equals(generateSign(wrr))) {
			o.set("wx_err_code", AppError.ERROR_MSG_20002.getCode());
			o.set("wx_err_msg", wrr.getReturn_msg());
		} else if (CODE_FAIL.equals(wrr.getResult_code())) {
			o.set("wx_err_code", AppError.ERROR_MSG_20003.getCode());
			o.set("wx_err_msg", wrr.getErr_code_des());
			errMsg = wrr.getErr_code_des();
		} else {
			o.set("internal_order_status", AppConstants.INTERNAL_ORDER_STATUS_CANCEL_REFUND);
			o.set("order_status", OrderStatus.CANCEL.getCode());
			o.set("refund_no", wrr.getOut_refund_no());
			o.set("refund_fee", wrr.getRefund_fee());
			o.set("refund_transaction_id", wrr.getRefund_id());
		}
		// 更新订单信息
		o.update();
		return errMsg;
	}
	
	/**
	 * 退货订金退款
	 * 
	 * @param returnOrderId
	 * @return
	 * @throws Exception
	 */
	public String returnDepositRefund(String returnOrderId) throws Exception {
		return null;
	}
	
	/**
	 * 退货退款
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String returnRefund(String returnOrderId, int refundFee) throws Exception {

		// 关联退货订单
		Record o = OrderService.service.getReturnOrder(returnOrderId);
		
//		int refundFee = o.getInt("apply_refund_fee");
		
		WxRefund refund = new WxRefund();
		refund.setAppid(appId);
		refund.setMch_id(mchId);
		// 操作员帐号, 默认为商户号
		refund.setOp_user_id(mchId);
		// 商户退款单号
		String refundNo = o.get("refund_no", String.valueOf(ToolDateTime.getDateByTime()));
		refund.setOut_refund_no(refundNo);
		refund.setRefund_fee(refundFee*100);
		refund.setOut_trade_no(o.getStr("settle_trade_no"));
		refund.setTotal_fee(o.getInt("payment")*100);
		refund.setTransaction_id(o.getStr("transaction_id"));
		
		refund.setNonce_str(createNonceStr());
		refund.setSign(generateSign(refund));
		
		String resp = sendRequestInXml(wxProperties.getProperty("refund_url"), ToolUtils.toXml(WxRefund.getXstream(), refund));
		WxRefundResponse wrr = ToolUtils.fromXml(WxRefundResponse.getXstream(), WxRefundResponse.class, resp);
		
		String errMsg = wrr.getReturn_msg();
		
		Record returnOrder = new Record().set("return_order_id", returnOrderId);
		// 解析微信返回结果
		if (CODE_FAIL.equals(wrr.getReturn_code())) {
			returnOrder.set("err_code", AppError.ERROR_MSG_20001.getCode());
			returnOrder.set("err_msg", wrr.getReturn_msg());
		// 签名校验
		} else if (!wrr.getSign().equals(generateSign(wrr))) {
			returnOrder.set("err_code", AppError.ERROR_MSG_20002.getCode());
			returnOrder.set("err_msg", wrr.getReturn_msg());
		} else if (CODE_FAIL.equals(wrr.getResult_code())) {
			returnOrder.set("err_code", AppError.ERROR_MSG_20003.getCode());
			returnOrder.set("err_msg", wrr.getErr_code_des());
			errMsg = wrr.getErr_code_des();
		} else {
			returnOrder.set("refund_no", wrr.getOut_refund_no()).set(
							"refund_fee", wrr.getRefund_fee()).set(
							"refund_transaction_id", wrr.getRefund_id()).set(
							"return_status", ReturnStatus.REFUNDED).set(
							"refund_date", ToolDateTime.getNow());
		}
		// 更新订单信息
		Db.update("return_order", returnOrder);
		return errMsg;
	}
	
	/**
	 * 查询退款
	 */
	public void refundQuery() {
		
	}
	
	public String createNonceStr() {
        return ToolUtils.getUuidByJdk(true);
    }

    public String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
	
	public String generateSign(Object signObject) {
		ToolReflect tr = ToolReflect.on(signObject);
		Map<String, ToolReflect> fields = tr.allfields();
		TreeMap<String, Object> StringA = new TreeMap<String, Object>();
		for (String key : fields.keySet()) {
			if ("sign".equals(key)) continue;
			Object value = fields.get(key).get();
			if (value != null && StringUtils.isNotBlank(value.toString()))
				StringA.put(key, value);
		}
		String sign = StringUtils.join(StringA.entrySet(), "&") + "&key=" + apiKey;
		return ToolMD5.MD5(sign).toUpperCase();
	}
}
