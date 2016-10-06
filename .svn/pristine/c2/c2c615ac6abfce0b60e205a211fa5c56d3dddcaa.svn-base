package com.goddess.ec.manage.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.User;
import com.goddess.ec.manage.service.UserService;

@Controller(controllerKey = "/admin/user")
public class UserController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(UserController.class);

    /**
     * 用户列表
     */
    public void index() {
    	render("/user/userList.html");
    }
    
    public void searchUsers() {
        SplitPage sp = new SplitPage();
        // 检索页基本设定
        sp.setPageNumber(getParaToInt("page"));
        sp.setPageSize(getParaToInt("rows"));
        sp.setOrderColunm(getPara("sort"));
        sp.setOrderMode(getPara("order"));

        // 检索条件
        Map<String, String> queryParam = new HashMap<String, String>();
        // 订单内部状态
        queryParam.put("cell_num", getPara("cell_num"));
        String dateFrom = getPara("register_date_from");
        String dateTo = getPara("register_date_to");
        if (StringUtils.isNotBlank(dateFrom))
        	queryParam.put("register_date_from", dateFrom + " 00:00:00");
        if (StringUtils.isNotBlank(dateTo))
        	queryParam.put("register_date_to", dateTo+" 23:59:59");
        
        // 条件设定
        sp.setQueryParam(queryParam);

        // 检索处理
        UserService.service.getUsers(sp);

        Map<String, Object> jsonRes = new HashMap<String, Object>();
        jsonRes.put("total",  sp.getPage().getTotalRow());
        jsonRes.put("rows",  sp.getPage().getList());
        
        renderJson(jsonRes);
    }
    
//    public void getUserCommodities() {
//    	
//    	int userId = getParaToInt(0);
//    	setAttr("user_commodities", UserService.service.getUserCommodities(userId));
//    	setAttr("imgRoot", PropertiesPlugin.getParamMapValue(DictKeys.path_image));
//    	render("/user/userCommodities.html");
//    }
//    
    public void awardPoints() {
    	UserService.service.awardPoints(getParaToInt(0), getParaToInt(1));
    	renderJson();
    }
    
    public void markUser() {
    	if (!User.dao.findById(getParaToInt(0)).set("delete_flag", getPara(1)).update())
    		setAttr("errorMsg", "发送失败！");
    	renderJson(new String[]{"errorMsg"});
    }
}
