package com.goddess.ec.manage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.model.AdminUser;
import com.goddess.ec.manage.model.AdminUserRole;
import com.goddess.ec.manage.tools.ToolMD5;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 后台用户管理
 */
@Controller(controllerKey = "/admin/adminUser")
public class AdminUserController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(AdminUserController.class);

    public void index() {
    	
    	
    	setAttr("brands", JsonKit.toJson(Db.find("select brand_id,brand_name from brand"))).setAttr("roles", AdminUserRole.toJson()).render("/user/adminUserList.html");
    }
    
    /**
     * 后台用户列表
     */
    public void getAdminUsers() {

        Map<String, Object> jsonRes = new HashMap<String, Object>();
        List<AdminUser> mList = AdminUser.dao.find("select * from admin_users");
        jsonRes.put("total", mList.size());
        jsonRes.put("rows",  mList);
        
        renderJson(jsonRes);
    }
    
    /**
     * 新增加后台用户
     */
    public void saveAdminUser() {
    	
    	AdminUser m = new AdminUser();
    	if (!m.set("user_name", getPara("user_name")).set(
    				"role", getPara("role")).set(
    				"brand_id", getParaToInt("brand_id", null)).set(
					"password", ToolMD5.MD5(getPara("password"))).save()) {
    		setAttr("errorMsg", "后台用户添加失败！");
    	} else {
    		setAttr("adminUser",m);
    	}
    	renderJson(new String[]{"errorMsg", "adminUser"});
    }
    
    /**
     * 更新后台用户
     */
    public void resetPass() {
    	
    	AdminUser m = AdminUser.dao.findById(getPara("user_id"));
    	if (!m.set("user_name", getPara("user_name")).set(
					"password", ToolMD5.MD5(getPara("password"))).update()) {
    		setAttr("errorMsg", "后台用户更新失败！");
    	}
    	setAttr("adminUser",m);
    	renderJson(new String[]{"errorMsg", "adminUser"});
    }
    /**
     * 更新后台用户
     */
    public void updateAdminUser() {
    	
    	AdminUser m = AdminUser.dao.findById(getPara("user_id"));
    	if (!m.set("user_name", getPara("user_name")).set(
					"role", getPara("role")).set(
					"brand_id", getParaToInt("brand_id", null)).update()) {
    		setAttr("errorMsg", "后台用户更新失败！");
    	}
    	setAttr("adminUser",m);
    	renderJson(new String[]{"errorMsg", "adminUser"});
    }
    
    /**
     * 删除后台用户
     */
    public void deleteAdminUser() {
    	if (!AdminUser.dao.deleteById(getPara(0)))
    		setAttr("errorMsg", "删除失败");
    	renderJson(new String[]{"errorMsg"});
    }
}
