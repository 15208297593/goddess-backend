package com.goddess.ec.manage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.data.DeliveryRequest;
import com.goddess.ec.manage.data.DeliveryResponse;
import com.goddess.ec.manage.plugin.PropertiesPlugin;
import com.goddess.ec.manage.tools.ToolJson;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class OrderService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(OrderService.class);

    public static final OrderService service = new OrderService();

    /**
     * 订单列表
     * @param splitPage
     */
    public void getOrders(SplitPage splitPage) {
        splitPageBase(splitPage, "SELECT o.*,u.cell_num,u.nick_name", "manage.order.splitPage");
    }

    /**
     * 品牌订单列表
     * @param splitPage
     */
    public void getBrandOrders(SplitPage splitPage) {
    	splitPageBase(splitPage, "SELECT o.*,u.cell_num,u.nick_name", "manage.order.brandOrder");
    }

    /**
     * 取得退货订单列表
     * @param splitPage
     */
    public void getReturnOrders(SplitPage splitPage) {
    	splitPageBase(splitPage, "SELECT o.*,u.cell_num,u.nick_name", "manage.order.getReturnOrders");
    }
    
    /**
     * 取得订单商品
     * @param orderId
     * @return
     */
    public List<Record> getOrderCommodities(String orderId) {
    	return Db.find(ToolSqlXml.getSql("manage.order.getOrderCommodities"), orderId);
    }

    /**
     * 取得品牌订单商品
     * 
     * @return
     */
    public Record getBrandOrderComms(Integer commodityId, Integer userCustomizationId, int brandId) {

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("commodity_id", commodityId);
    	params.put("user_customization_id", userCustomizationId);
    	params.put("brand_id", brandId);
    	return Db.findFirst(ToolSqlXml.getSql("manage.order.isBrandComm", params));
    }
    
    /**
     * 取得退货订单
     * @return
     */
    public Record getReturnOrder(String returnOrderId) {

    	return Db.findFirst(ToolSqlXml.getSql("manage.order.getReturnOrder"), returnOrderId);
    }
    
    /**
     * 取得生产商
     * @param type
     * @return
     */
    public List<Record> getManufacturers() {
    	return Db.find(ToolSqlXml.getSql("manage.order.getManufacturers"));
    }

    /**
     * 取得用户定制属性
     * @param userCustomizationId 用户定制
     * @return
     */
    public List<Record> getCustomizationPros(int userCustomizationId) {
    	return Db.find(ToolSqlXml.getSql("manage.order.getCustomizationPros"), userCustomizationId);
    }
    
    /**
     * 取得用户定制属性
     * @param userCustomizationId 用户定制
     * @return
     */
    public List<Record> getCustomizationPics(int userCustomizationId) {
    	return Db.find(ToolSqlXml.getSql("manage.order.getCustomizationPics"), userCustomizationId);
    }
    
    /**
     * 更新订单生产商
     * 
     * @param manufacturerId
     * @param orderId
     */
    public boolean updateOrderManufacturer(int manufacturerId, String orderId) {
    	return Db.update(ToolSqlXml.getSql("manage.order.updateOrderManufacturer"), manufacturerId, orderId) != 0;
    }
    
    /**
     * 发起订单物流的订阅请求
     * @param company
     * @param number
     * @throws IOException 
     */
    public DeliveryResponse pollDelivery(String orderId, String company, String number, String from, String to) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

        	DeliveryRequest req = new DeliveryRequest();
    		req.setCompany(company);
    		req.setFrom(from);
    		req.setTo(to);
    		req.setNumber(number);
    		req.getParameters().put("callbackurl", PropertiesPlugin.getParamMapValue(DictKeys.delivery_callbackurl).toString()+orderId);
    		req.setKey(PropertiesPlugin.getParamMapValue(DictKeys.delivery_key).toString());
    		
            HttpPost httpPost = new HttpPost(PropertiesPlugin.getParamMapValue(DictKeys.delivery_poll_url).toString());
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("schema", "json"));
            nvps.add(new BasicNameValuePair("param", JsonKit.toJson(req)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                // ensure it is fully consumed
                EntityUtils.consume(entity);
                return ToolJson.fromJson2Bean(result, DeliveryResponse.class);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
