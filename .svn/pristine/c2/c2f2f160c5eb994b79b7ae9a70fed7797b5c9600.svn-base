package com.goddess.ec.manage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.support.json.JSONUtils;
import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.ReturnStatus;
import com.goddess.ec.manage.service.CommodityService;
import com.goddess.ec.manage.service.OrderService;
import com.goddess.ec.manage.service.WxService;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 订单
 */
@Controller(controllerKey = "/admin/return")
public class ReturnOrderController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ReturnOrderController.class);

    /**
     * 订单列表
     */
    public void index() {
    	
    	render("/order/returnOrderList.html");
    }
    
    public void searchOrders() {
        SplitPage sp = new SplitPage();
        // 检索页基本设定
        sp.setPageNumber(getParaToInt("page"));
        sp.setPageSize(getParaToInt("rows"));
//        sp.setOrderColunm(getPara("sort"));
//        sp.setOrderMode(getPara("order"));

        // 检索条件
        Map<String, String> queryParam = new HashMap<String, String>();
        // 订单内部状态
        queryParam.put("return_status", getPara("return_status"));
        queryParam.put("cell_num", getPara("cell_num"));
        queryParam.put("return_order_id", getPara("return_order_id"));
        String orderDateFrom = getPara("return_apply_date_from");
        String orderDateTo = getPara("return_apply_date_to");
        if (StringUtils.isNotBlank(orderDateFrom))
        	queryParam.put("return_apply_date_from", orderDateFrom + " 00:00:00");
        if (StringUtils.isNotBlank(orderDateTo))
        	queryParam.put("return_apply_date_to", orderDateTo+" 23:59:59");
        
        // 条件设定
        sp.setQueryParam(queryParam);

        // 检索处理
        OrderService.service.getReturnOrders(sp);

        Map<String, Object> jsonRes = new HashMap<String, Object>();
        jsonRes.put("total",  sp.getPage().getTotalRow());
        jsonRes.put("rows",  sp.getPage().getList());
        
        renderJson(jsonRes);
    }
    
    @SuppressWarnings("unchecked")
    public void getReturnCommodity() {
        
    	Record r = Db.findById("order_commodity_id", getPara(0));
    	Map<String, Object> baseProperties = (Map<String, Object>)JSONUtils.parse(String.format("{%s}", 
    			r.getStr("base_properties") == null ? "" : r.getStr("base_properties")));
    	r.set("base_properties", baseProperties);
    	// 定制属性
    	List<Record> customizePros = CommodityService.service.getCommodityCustomizationProps(r.getInt("user_customization_id"), r.getInt("commodity_id"));
		// 非定制上架商品的材料属性描述
		if ((customizePros == null || customizePros.size() == 0) && StringUtils.isNotEmpty(r.getStr("detail_properties"))) {
	    	Map<String, Object> detailProperties = (Map<String, Object>)JSONUtils.parse(String.format("{%s}", r.getStr("detail_properties")));
	    	for (String tp : detailProperties.keySet())
	    		customizePros.add(new Record().set("type_name", tp).set("material_name", detailProperties.get(tp)));
		}
		r.set("customize_properties", customizePros);

    	setAttr("order_commodities", new Record[]{r});
    	render("/order/orderCommodities.html");
    }
    
    /**
     * 同意退货
     */
    public void approve() {
    	updateReturnStatus(ReturnStatus.APPROVED);
    }

    /**
     * 退货商品入库
     */
    public void storage() {
    	updateReturnStatus(ReturnStatus.STORAGE);
    }

    /**
     * 停止退货流程
     */
    public void terminate() {
    	updateReturnStatus(ReturnStatus.UNUSUAL_FINISHED);
    }
    
    private void updateReturnStatus(ReturnStatus rs) {
    	
    	Db.update("return_order", new Record().set("return_order_id", getPara(0)).set("return_status", rs.getCode()));
    }
    
    /**
     * 退货退款
     * 
     * @throws Exception 
     */
    public void refund() throws Exception {
    	renderJson(WxService.service.returnRefund(getPara(0), getParaToInt(1)));
    }
}
