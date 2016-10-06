package com.goddess.ec.manage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.Material;
import com.goddess.ec.manage.model.MaterialType;
import com.goddess.ec.manage.service.MaterialService;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 原材料管理
 */
@Controller(controllerKey = "/admin/material")
public class MaterialController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(MaterialController.class);

    public void index() {
    	render("/material/materialList.html");
    }
    
    /**
     * 原材料类别列表
     */
    public void getMaterialTypes() {

        Map<String, Object> jsonRes = new HashMap<String, Object>();
        List<MaterialType> mList = MaterialService.service.getMaterialTypes();
        jsonRes.put("total", mList.size());
        jsonRes.put("rows",  mList);
        
        renderJson(jsonRes);
    }
    
    /**
     * 新增加原材料类别
     */
    public void saveMaterialType() {
    	
    	MaterialType mt = new MaterialType();
    	// 保存新类别
    	if (!mt.set("type_name", getPara("type_name")).set(
    				"type_name_ext", getPara("type_name_ext")).set(
    				"exhibition_type", getPara("exhibition_type")).set(
    				"customize_exhibition", getPara("customize_exhibition")).set(
    				"delete_flag", AppConstants.DELETE_FLAG_VALID).save()) {
    		setAttr("errorMsg", "原材料类别添加失败！");
    	}
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新原材料类别
     */
    public void updateMaterialType() {
    	
    	MaterialType mt = MaterialType.dao.findById(getPara("material_type_id"));
    	// 保存新类别
    	if (!mt.set("type_name", getPara("type_name")).set(
					"type_name_ext", getPara("type_name_ext")).set(
					"exhibition_type", getPara("exhibition_type")).set(
					"customize_exhibition", getPara("customize_exhibition")).update()) {
    		setAttr("errorMsg", "原材料类别更新失败！");
    	}
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 删除原材料类别
     */
    public void deleteMaterialType() {
    	updateTDeleteFlag(AppConstants.DELETE_FLAG_INVALID);
    	MaterialService.service.deleteCustomizationByType(getPara(0));
    	renderJson(new String[]{"errorMsg"});
    }
    /**
     * 恢复原材料类别
     */
    public void recoverMaterialType() {
    	updateTDeleteFlag(AppConstants.DELETE_FLAG_VALID);
    	renderJson(new String[]{"errorMsg"});
    }
    
    private void updateTDeleteFlag(String deleteFlag) {
    	if (!MaterialType.dao.findById(getPara(0)).set("delete_flag", deleteFlag).update())
    		setAttr("errorMsg", "删除失败");
    }
    
    /**
     * 取得某个类别原材料
     * 
     */
    public void getMaterials() {
        renderJson(MaterialService.service.getMaterialsByType(getPara(0)));
    }
    
    /**
     * 添加新原材料
     */
    @Before(Tx.class)
    public void saveMaterial() {
    	UploadFile f = getFile();
    	String materialTypeId = getPara("material_type_id");
    	String exhibitionType = getPara("exhibition_type");

		Material m = new Material();
		m.set("material_type_id", materialTypeId).set(
			"material_name", getPara("material_name")).set(
			"price_addition", getParaToInt("price_addition")).set(
			"delete_flag", AppConstants.DELETE_FLAG_VALID);
		if (AppConstants.MATERIAL_EXHIBITION_TYPE_COLOR.equals(exhibitionType)) {
			m.set("material_exhibition", getPara("material_exhibition"));
		}
		try {
			if (!m.save()) {
				setAttr("errorMsg", "添加失败！");
			} else if (!AppConstants.MATERIAL_EXHIBITION_TYPE_COLOR.equals(exhibitionType) && f != null) {
				String saveURL = ToolUtils.saveUploadFile(f, AppConstants.RELATIVE_PATH_MATERIAL + materialTypeId, m.getInt("material_id").toString(), ToolUtils.getExtension(f.getOriginalFileName()));
				Material.dao.findById(m.getInt("material_id")).set("material_exhibition", saveURL).update();
			}
		} catch (IOException e) {
			e.printStackTrace();
			setAttr("errorMsg", "上传文件存储失败");
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新原材料
     */
    public void updateMaterial() {
    	UploadFile upFile = getFile();
    	String materialTypeId = getPara("material_type_id");
    	
    	String materialExhibition = getPara("material_exhibition");
		try {
			Material m = Material.dao.findById(getPara("material_id"));
			if (!AppConstants.MATERIAL_EXHIBITION_TYPE_COLOR.equals(getPara("exhibition_type")) && upFile != null) {
				materialExhibition = ToolUtils.saveUploadFile(upFile, AppConstants.RELATIVE_PATH_MATERIAL + materialTypeId, getPara("material_id"), ToolUtils.getExtension(upFile.getOriginalFileName()));
				m.set("material_exhibition", materialExhibition);
			} else if (AppConstants.MATERIAL_EXHIBITION_TYPE_COLOR.equals(getPara("exhibition_type"))) {
				m.set("material_exhibition", materialExhibition);
			}
			if (!m.set("material_name", getPara("material_name")).set(
					"price_addition", getParaToInt("price_addition")).update()) {
				setAttr("errorMsg", "更新失败！");
			}
		} catch (IOException e) {
			setAttr("errorMsg", "上传文件存储失败！");
			e.printStackTrace();
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 可更新验证，用户是否正在使用该原材料类别
     */
    public void checkUpdateMaterialType() {
    	checkUpdate("checkTypeIsUsed", getParaToInt(0), "该原材料类别");
    }
    
    /**
     * 可更新验证，用户是否正在使用该原材料
     */
    public void checkUpdateMaterial() {
    	checkUpdate("checkMaterialIsUsed", getParaToInt(0), "该原材料");
    }
    
    private void checkUpdate(String sqlId, int id, String msg) {

    	List<Record> res = Db.find(ToolSqlXml.getSql("manage.material."+sqlId), AppConstants.CUSTOMIZE_STATUS_FINISHED, id, AppConstants.IS_SHELVED_ON, id);
    	if (res.size() == 2) {
    		String errorMsg = "%s正在被用户【%s】用于未完成的定制中，并且正在被上架商品【%s】使用中";
    		String cellNum = res.get(0).getStr("cell_num");
    		Integer commodityId = res.get(0).getInt("commodity_id");
    		if (cellNum == null)
    			cellNum = res.get(1).getStr("cell_num");
    		else if (commodityId == null)
    			commodityId = res.get(1).getInt("commodity_id");
    		setAttr("errorMsg", String.format(errorMsg, msg, cellNum, commodityId));
    	} else if (res.size() == 1) {
    		if (StringUtils.isNotEmpty(res.get(0).getStr("cell_num")))
    			setAttr("errorMsg", String.format("%s正在被用户【%s】用于未完成的定制中", msg, res.get(0).getStr("cell_num")));
    		else
    			setAttr("errorMsg", String.format("%s正在被上架商品【%s】使用中", msg, res.get(0).getInt("commodity_id")));
    	}
    	renderJson(new String[]{"errorMsg"});
    
    }
    
    /**
     * 删除原材料
     */
    public void deleteMaterial() {
    	updateMDeleteFlag(AppConstants.DELETE_FLAG_INVALID);
    	MaterialService.service.deleteCustomizationByProperty(getPara(1), getPara(0));
    	renderJson(new String[]{"errorMsg"});

    }
    /**
     * 恢复原材料
     */
    public void recoverMaterial() {
    	updateMDeleteFlag(AppConstants.DELETE_FLAG_VALID);
    	renderJson(new String[]{"errorMsg"});
    }
    
    private void updateMDeleteFlag(String deleteFlag) {
    	if (!Material.dao.findById(getPara(0)).set("delete_flag", deleteFlag).update())
    		setAttr("errorMsg", "删除失败");
    }
}
