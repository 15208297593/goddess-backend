package com.goddess.ec.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.PropertyType;
import com.goddess.ec.manage.service.PropertyService;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolWeb;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 属性类别素材管理
 */
@Controller(controllerKey = "/admin/property")
public class PropertyController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PropertyController.class);


    public void index() {
    	render("/property/propertyTypeList.html");
    }

    public void getPropertyTypes() {

        SplitPage sp = new SplitPage();
        // 检索条件设定
        sp.setPageNumber(getParaToInt("page"));
        sp.setPageSize(getParaToInt("rows"));
        sp.setOrderColunm(getPara("sort"));
        sp.setOrderMode(getPara("order"));
        
        Map<String, String> params = new HashMap<String, String>();
        sp.setQueryParam(params);

        PropertyService.service.getPropertyTypes(sp);

        Map<String, Object> jsonRes = new HashMap<String, Object>();
		jsonRes.put("total",  sp.getPage().getTotalRow());
		jsonRes.put("rows",  sp.getPage().getList());
        renderJson(jsonRes);
    }
    
    /**
     * 初始化属性类别详情页
     */
    public void getPropertyType() {
    	int propertyTypeId = getParaToInt(0, 0);
    	// 已有属性类别
    	if (propertyTypeId != 0) {
    		Record pt = Db.findFirst(ToolSqlXml.getSql("manage.property.getPropertyTypesByPtid"), propertyTypeId);
    		setAttr("type", pt);
    		
    	} else {
    		Record pt = new Record();
    		setAttr("type", pt);
    	}
    	
    	setAttr("material_types", Db.find(ToolSqlXml.getSql("manage.property.getMaterialTypes")));
    	setAttr("property_rank", propertyTypeId == 0 ? "" : PropertyService.service.getPropertyRank(propertyTypeId));
    	render("/property/propertyDetail.html");
    }
    
    public void getMaterialsByPtId() {

        renderJson(Db.find(ToolSqlXml.getSql("manage.property.getMaterialsByPtId"), getParaToInt(1), getParaToInt(0)));
    }

    @Before(Tx.class)
    public void savePropertyType() {

    	Integer materialTypeId = getParaToInt("material_type_id", null);
    	PropertyType pt = new PropertyType();

    	createPropertyType(pt, materialTypeId);
		Map<String, String> params = ToolWeb.getParamMap(getRequest());
		
		// 添加原型属性类别
		if (pt.save()) {
			setAttr("property_type", pt);
			savePropertiesWithRank(params, pt.getInt("property_type_id"));
		} else {
			setAttr("errorMsg", "属性类别添加失败！");
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    @Before(Tx.class)
    public void updatePropertyType() {

    	int propertyTypeId = getParaToInt("property_type_id");
    	Integer materialTypeId = getParaToInt("material_type_id", null);
    	
		Map<String, String> params = ToolWeb.getParamMap(getRequest());
		
    	PropertyType pt = PropertyType.dao.findById(propertyTypeId);
    	createPropertyType(pt, materialTypeId);
		pt.update();
		
		updateProperties(params, propertyTypeId);
		// 属性排序未改变，不更新
		if (!getPara("cur_property_rank").equals(getPara("property_rank")))
			updatePropertyRank(propertyTypeId, getPara("property_rank"));

		renderJson(new String[]{"errorMsg"});
    
    }
    
    @Before(Tx.class)
    private void updateProperties(Map<String, String> params, int propertyTypeId) {
    	
    	Integer curMaterialTypeId = getParaToInt("cur_material_type_id", 0);
    	Integer materialTypeId = getParaToInt("material_type_id", null);
    	// 更换了原材料类别，删除现有原属性类别的所有属性
		if (curMaterialTypeId != materialTypeId.intValue()) {
			PropertyService.service.deletePropertiesByType(propertyTypeId);
			// 删除【基础关联类别】中使用原属性类别的数据
			PropertyService.service.deleteBaseRelatePropertiesByType(propertyTypeId);
    		// 添加新加属性类别所有属性
    		savePropertiesWithRank(params, propertyTypeId);
    	// 更新现有属性
		} else {
			List<Record> addProperties = new ArrayList<Record>();
			List<String> deleteProperties = new ArrayList<String>();
			for (String key : params.keySet()) {
				if (key.startsWith("property_id_")) {
					String propertyId = params.get(key);
					String pMaterialId = params.get("material_id_" + key.replace("property_id_", ""));
					// 新加的属性
					if (StringUtils.isBlank(propertyId) && pMaterialId != null) {
						addProperties.add(new Record().set("property_type_id", propertyTypeId).set(
															"material_id", Integer.valueOf(pMaterialId)));
					// 删除的属性
					} else if (!StringUtils.isBlank(propertyId) && pMaterialId == null) {
						deleteProperties.add(propertyId);
					}
				}
			}
			if (addProperties.size() != 0) {
				int[] result = PropertyService.service.saveProperties(addProperties);
				for (int r : result) {
					if (r == AppConstants.NG) {
						setAttr("errorMsg", "部分属性更新失败！");
						break;
					}
				}
			}
			if (deleteProperties.size() != 0) {
				if (PropertyService.service.deleteProperties(deleteProperties) != deleteProperties.size()) {
					setAttr("errorMsg", "部分属性更新失败！");
				}
				// 删除【基础关联类别】中使用这些属性的数据
				PropertyService.service.deleteBaseRelateProperties(deleteProperties);
			}
		}
    }

    /**
     * 更新属性排列顺序
     */
    @Before(Tx.class)
    private void updatePropertyRank(int propertyTypeId, String ranks) {
    	
    	// 删除该类别的属性排序
    	Db.update(ToolSqlXml.getSql("manage.property.resetPropertyRank"), propertyTypeId);
    	if (StringUtils.isEmpty(ranks)) {
        	setAttr("property_rank", "");
        	return;
    	}
    	String[] rankArray = ranks.split(",");
    	
    	List<Record> updateProperties = new ArrayList<Record>();
    	for (int i = rankArray.length, j = 1; i > 0; i--, j++) {
    		updateProperties.add(new Record().set("property_id", getParaToInt("property_id_" + Integer.valueOf(rankArray[i-1]))).set("rank", j));
    	}
    	int[] result = PropertyService.service.updatePropertyRank(updateProperties);
    	int r = 0;
    	while (r < result.length && result[r] == AppConstants.OK)
    		r++;
    	if (r < result.length) {
    		setAttr("errorMsg", "部分属性更新失败！");
    	}
    	setAttr("property_rank", PropertyService.service.getPropertyRank(propertyTypeId));
    }
    
    /**
     * 验证是否可更新属性类别，用户定制中是否有尚未付款的记录{property_type_id}
     */
    public void checkUpdateType() {
    	checkUpdate("checkTypeIsUsed", getPara(0), "该属性类别");
    }

    /**
     * 验证是否可删除属性，用户定制中是否有尚未付款的记录{property_type_id}
     */
    public void checkUpdateProperties() {
    	String deletedProperties = StringUtils.join(getPara(0).split("_"), ",");
    	checkUpdate("checkPropertyIsUsed", deletedProperties, "该类别下的某些属性【"+StringUtils.join(getPara(1).split("_"), ",")+"】");
    }
    
    private void checkUpdate(String sqlId, String ids, String msg) {
    	List<Record> res = Db.find(ToolSqlXml.getSql("manage.property."+sqlId), 
    			AppConstants.CUSTOMIZE_STATUS_INCART, ids);
    	if (res.size() == 1)
			setAttr("errorMsg", String.format("使用%s的定制品正在用户【%s】的购物车中", msg, res.get(0).getStr("cell_num")));
    	renderJson(new String[]{"errorMsg"});
    }
    
    private void createPropertyType(PropertyType pt, Integer materialTypeId) {

    	pt.set("type_name", getPara("type_name"));
    	pt.set("type_name_ext", getPara("type_name_ext"));
		pt.set("material_type_id", materialTypeId);
		pt.set("is_ref", "1");
    }
    
    private void savePropertiesWithRank(Map<String, String> params, int propertyTypeId) {
		String[] ranks = getPara("property_rank").split(",");
		Map<String, Integer> rankMap = new HashMap<String, Integer>();
		for (int i = ranks.length; i > 0; i--) {
			rankMap.put(ranks[ranks.length-i], i);
		}
		List<Record> properties = new ArrayList<Record>();
		for (String key : params.keySet()) {
			if (key.startsWith("material_id_")) {
				String materialId = params.get(key);
				properties.add(new Record().set(
						"property_type_id", propertyTypeId).set(
						"material_id", Integer.valueOf(materialId)).set(
						"rank", rankMap.get(materialId) == null ? 0 : rankMap.get(materialId)));
			}
		}
		
		int[] result = PropertyService.service.savePropertiesWithRank(properties);
		for (int r : result) {
			if (r == 0) {
				setAttr("errorMsg", "部分属性添加失败！");
				break;
			}
		}
    }
    
    @Before(Tx.class)
    public void deletePropertyType() {

    	int propertyTypeId = getParaToInt(0);
    	PropertyType.dao.deleteById(propertyTypeId);
    	PropertyService.service.deletePropertiesByType(propertyTypeId);
    	// 删除的类别被基础类别引用，以下两步将会删除数据
    	// 删除引用此类别的基础类别
    	Db.update(ToolSqlXml.getSql("manage.property.deleteBaseRefType"), propertyTypeId);
    	// 删除【基础关联类别】中使用原属性类别的数据
    	PropertyService.service.deleteBaseRelatePropertiesByType(propertyTypeId);
    	
    	// 删除的类别被关联类别引用，以下一步将会删除数据
    	// 删除引用此类别的关联类别
    	Db.update(ToolSqlXml.getSql("manage.property.deleteBaseRelateType"), propertyTypeId);
    	
		renderJson(new String[]{"errorMsg"});
    }

    /**
     * 更新属性排列顺序
     */
    @Before(Tx.class)
    public void updatePropertyRank() {
    	
    	int propertyTypeId = getParaToInt(0);
    	String ranks = getPara(1);
    	String[] rankArray = ranks.split(",");
    	
    	List<Record> updateProperties = new ArrayList<Record>();
    	for (int i = rankArray.length, j = 1; i > 0; i--, j++) {
    		updateProperties.add(new Record().set("property_id", Integer.valueOf(rankArray[i-1])).set("rank", j));
    	}
    	int[] result = PropertyService.service.updatePropertyRank(updateProperties);
    	int r = 0;
    	while (r < result.length && result[r] == AppConstants.OK)
    		r++;
    	if (r < result.length) {
    		setAttr("errorMsg", "部分属性更新失败！");
    	}
    	setAttr("property_rank", propertyTypeId == 0 ? "" : PropertyService.service.getPropertyRank(propertyTypeId));
		renderJson(new String[]{"errorMsg", "property_rank"});
    }

}
