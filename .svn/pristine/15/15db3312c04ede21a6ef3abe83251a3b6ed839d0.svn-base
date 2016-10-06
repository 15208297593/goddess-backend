package com.goddess.ec.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.MaterialType;
import com.goddess.ec.manage.model.PropertyType;
import com.goddess.ec.manage.service.CusConfigureService;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolWeb;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 原型配置管理
 */
@Controller(controllerKey = "/admin/cusConfigure")
public class CusConfigureController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(CusConfigureController.class);

    public void getPropertyTypes() {

    	int prototypeId = getParaToInt(0);
//    	List<Record> propertyTypes = PrototypeService.service.getPropertyTypes(prototypeId);
    	setAttr("type_rank", CusConfigureService.service.getTypeRank(prototypeId));
    	setAttr("prototype_id", prototypeId);
//    	setAttr("propertyTypes", propertyTypes);
//    	render("/prototype/propertyTypes.html");
    	render("/prototype/newPropertyTypes.html");
    
    }
    
    public void getNewPropertyTypes() {

    	int prototypeId = getParaToInt(0);
        Map<String, Object> jsonRes = new HashMap<String, Object>();
    	List<PropertyType> types = PropertyType.dao.find(ToolSqlXml.getSql("manage.prototype.getPropertyTypesByPid"), prototypeId);
		jsonRes.put("total", types.size());
		jsonRes.put("rows",  types);
        renderJson(jsonRes);
    }
    
//    /**
//     * 保存该基础款可选属性类别
//     */
//    public void savePropertyTypes() {
//    	
//    	/// Jedis implements Closable. Hence, the jedis instance will be auto-closed after the last statement.
//    	String selectedTypes = getPara("selected_types");
//    	if (StringUtils.isNotEmpty(selectedTypes)) {
//    		String[] typeArr = selectedTypes.split(",");
//	    	Transaction t = null;
//	    	try (Jedis jedis = RedisPlugin.getRedisThread()) {
//	    		
//	    		t = jedis.multi();
//	
//	    		for (String type : typeArr) {
//	    			String typeKey = String.format(template_propertyType, getPara("prototype_id"), type);
//	    			String rank = getPara("rank_"+type);
//	    			// 选中类别的属性
//    				t.hset(typeKey, "material_type_id", type);
//    				t.hset(typeKey, "rank", rank.toString());
//    				t.hset(typeKey, "is_required", getPara("is_required_"+type));
//    				t.hset(typeKey, "init_mid", getPara("init_mid_"+type));
//    				t.hset(typeKey, "noOption_alert", getPara("noOption_alert_"+type));
//    				
//    				// 选中类别列表
//    				String typeListKey = String.format(template_propertyTypes, getPara("prototype_id"));
//    				// 删除原有的类别列表
//    				t.del(typeListKey);
//    				t.zadd(typeListKey, rank == null ? 999 : Double.valueOf(rank), type);
//	    		}
//	    		// 持久化
//	    		t.save();
//	    		t.exec();
//	    	} catch (Exception e) {
//	    		if (t != null)
//	    			t.discard();
//			}
//    	}
//    }

    /**
     * 初始化属性类别详情页
     */
    public void getPropertyType() {
    	int propertyTypeId = getParaToInt(0, 0);
    	int prototypeId = getParaToInt(1);
    	int parentTypeId = getParaToInt(2,0);
    	int materialTypeId = getParaToInt(3,0);
    	// 已有属性类别
    	if (propertyTypeId != 0) {
    		Record pt = Db.findFirst(ToolSqlXml.getSql("manage.prototype.getPropertyTypesByPtid"), propertyTypeId);
    		setAttr("property_type", pt);
    		
    	} else {
    		Record pt = new Record();
    		pt.set("prototype_id", prototypeId);
    		setAttr("property_type", pt);
    		setAttr("properties", new ArrayList<Record>());
	    	setAttr("conflict_type", new ArrayList<Record>());
    	}
    	int ptType = 0;
    	if (parentTypeId == 0 && materialTypeId == 0) {
    		ptType = AppConstants.TYPE_PT_PRAENT_WITHOUT_M;
    	} else if (parentTypeId == 0 && materialTypeId != 0) {
    		ptType = AppConstants.TYPE_PT_PRAENT_WITH_M;
    	} else if (parentTypeId != 0 && materialTypeId != 0) {
    		ptType = AppConstants.TYPE_PT_CHILD_WITH_M;
    	}
    	setAttr("pt_type", ptType);
    	// 已添加类别的非子类别，剔除自身
    	setAttr("parent_types", Db.find(ToolSqlXml.getSql("manage.prototype.getParentTypesByPid"), prototypeId, parentTypeId == 0 ? propertyTypeId : 0));
    	// 只有非子类别，才可选择原材料类别
    	setAttr("material_types", Db.find("select * from material_type where delete_flag = 0"));
    	render("/prototype/properties.html");
    }
    
    /**
     * 根据所选原材料类别，取得冲突类别及所有原材料属性
     */
    public void getMaterialsAndType() {

    	int materialTypeId = getParaToInt(0);
    	int propertyTypeId = getParaToInt(1, 0);
    	int prototypeId = getParaToInt(2);
		List<Record> otherTypes = Db.find(ToolSqlXml.getSql("manage.prototype.getPropertyTypesWithoutSelf"), materialTypeId, prototypeId);
		// 冲突类型
		if (propertyTypeId != 0) {
			Record pt = Db.findFirst(ToolSqlXml.getSql("manage.prototype.getPropertyTypesByPtid"), propertyTypeId);
    		setAttr("property_type", pt);
			String conflictType = pt.getStr("conflict_type");
			if (StringUtils.isNotEmpty(conflictType)) {
				Map<String, Object> ct = (Map<String, Object>)JSONUtils.parse(conflictType);
				for (Record r : otherTypes) {
					Object mid = ct.get(r.getInt("material_type_id").toString());
					// 有冲突
					if (mid != null) {
						r.set("is_conflict", "1");
						r.set("replace_material_id", mid);
					}
				}
			}
		}
    	List<Record> properties = CusConfigureService.service.getProperties(materialTypeId, propertyTypeId);
    	setAttr("properties", properties);
    	setAttr("property_rank", propertyTypeId == 0 ? "" : CusConfigureService.service.getPropertyRank(propertyTypeId));
    	setAttr("conflict_type", otherTypes);
    	setAttr("property_type_id", propertyTypeId);
    	setAttr("exhibition_type", MaterialType.dao.findById(materialTypeId).getStr("exhibition_type"));
    	render("/prototype/materialsAndType.html");
    }
    
    /**
     * 添加原型属性
     */
    @Before(Tx.class)
    public void saveProperties() {

    	Integer materialTypeId = getParaToInt("material_type_id", null);
    	Integer parentTypeId = getParaToInt("parent_type_id", null);
    	PropertyType pt = new PropertyType();
    	createPropertyType(pt, parentTypeId, materialTypeId);
		// 未选择原材料类别时，作为父属性类别
		if (materialTypeId == null) {
			if (parentTypeId != null)
				setAttr("errorMsg", "选择了父类别的子类别必须选择原材料类别！");
			else
				pt.save();
			renderJson(new String[]{"errorMsg"});
			return;
		}
		Map<String, String> params = ToolWeb.getParamMap(getRequest());
		updateConflictType(pt, params);
		// 添加原型属性类别
		if (pt.save()) {
			setAttr("property_type", pt);
			savePropertiesWithRank(params, pt.getInt("property_type_id"));
		} else {
			setAttr("errorMsg", "属性类别添加失败！");
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    private void createPropertyType(PropertyType pt, Integer parentTypeId, Integer materialTypeId) {

    	pt.set("type_name", getPara("type_name"));
    	pt.set("type_name_ext", getPara("type_name_ext"));
    	pt.set("prototype_id", getParaToInt("prototype_id"));
    	pt.set("parent_type_id", parentTypeId);
		pt.set("material_type_id", materialTypeId);
		pt.set("rank", getParaToInt("type_rank", 0));
		pt.set("is_required", getPara("is_required", "0"));
		pt.set("default_material_id", getParaToInt("default_material_id"));
		// 针对不同商品的原材料用料比例
		pt.set("material_amount", getParaToInt("material_amount", 100));
    }
    
    private void savePropertiesWithRank(Map<String, String> params, int propertyTypeId) {
		String[] ranks = getPara("property_rank").split(",");
		Map<String, Integer> rankMap = new HashMap<String, Integer>();
		for (int i = ranks.length; i > 0; i--) {
			rankMap.put(ranks[i-1], i);
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
		
		int[] result = CusConfigureService.service.savePropertiesWithRank(properties);
		for (int r : result) {
			if (r == 0) {
				setAttr("errorMsg", "部分属性添加失败！");
				break;
			}
		}
    }
    
    private void updateConflictType(PropertyType pt, Map<String, String> params) {

		JSONObject conflictType = new JSONObject();
		for (String key : params.keySet()) {
			if (key.startsWith("conflict_type_")) {
				String mtId = params.get(key);
				conflictType.put(mtId, params.get("replace_material_id_"+mtId));
			}
		}
		// 更新冲突类型
		pt.set("conflict_type", conflictType.size() == 0 ? null : conflictType.toJSONString()).set(
				"conflict_alert", getPara("conflict_alert"));
    }
    
    /**
     * 更新原型属性
     */
    @Before(Tx.class)
    public void updateProperties() {

    	int propertyTypeId = getParaToInt("property_type_id");
    	int prototypeId = getParaToInt("prototype_id");
    	Integer curMaterialTypeId = getParaToInt("cur_material_type_id", 0);
    	Integer materialTypeId = getParaToInt("material_type_id", null);
    	Integer parentTypeId = getParaToInt("parent_type_id", null);
    	// 更新前属性类别的类型
    	int ptType = getParaToInt("pt_type", 0);
    	
    	PropertyType pt = PropertyType.dao.findById(propertyTypeId);
    	createPropertyType(pt, parentTypeId, materialTypeId);

    	// 更新前为不关联原材料的父类别，更新为关联原材料，则删除该父类别的所有子类别
    	if (ptType == AppConstants.TYPE_PT_PRAENT_WITHOUT_M && materialTypeId != null) {
    		CusConfigureService.service.deleteChildrenType(propertyTypeId);
    	}
    	
    	// 未选择原材料类别
    	if (materialTypeId == null) {
    		if (parentTypeId != null) {
				setAttr("errorMsg", "选择了父类别的子类别必须选择原材料类别！");
    		} else {
    			pt.update();
    			CusConfigureService.service.deletePropertiesByType(propertyTypeId);
    		}
    		renderJson(new String[]{"errorMsg"});
    		return;
    	} else {
    		
			Map<String, String> params = ToolWeb.getParamMap(getRequest());
			updateConflictType(pt, params);
			pt.update();
			
    		// 更换了原材料类别，删除现有原属性类别的所有属性
    		if (curMaterialTypeId != materialTypeId.intValue()) {
    			CusConfigureService.service.deletePropertiesByType(propertyTypeId);
	    		// 添加新加属性类别所有属性
	    		savePropertiesWithRank(params, propertyTypeId);
	    	// 更新现有属性
    		} else {
				List<Record> addProperties = new ArrayList<Record>();
				List<String> deleteProperties = new ArrayList<String>();
				List<Record> deleteCustomizations = new ArrayList<Record>();
				for (String key : params.keySet()) {
					if (key.startsWith("property_id_")) {
						String propertyId = params.get(key);
						String materialId = key.replace("property_id_", "");
						String pMaterialId = params.get("material_id_" + key.replace("property_id_", ""));
						// 新加的属性
						if (StringUtils.isBlank(propertyId) && pMaterialId != null) {
							addProperties.add(new Record().set("property_type_id", propertyTypeId).set(
																"material_id", Integer.valueOf(pMaterialId)));
						// 删除的属性
						} else if (!StringUtils.isBlank(propertyId) && pMaterialId == null) {
							deleteProperties.add(propertyId);
							deleteCustomizations.add(new Record().set("prototype_id", prototypeId).set(
																	"material_type_id", getParaToInt("material_type_id")).set(
																	"material_id", materialId));
						}
					}
				}
				if (addProperties.size() != 0) {
					int[] result = CusConfigureService.service.saveProperties(addProperties);
					for (int r : result) {
						if (r == AppConstants.NG) {
							setAttr("errorMsg", "部分属性更新失败！");
							break;
						}
					}
				}
				if (deleteProperties.size() != 0) {
					int count = CusConfigureService.service.deleteProperties(deleteProperties);
					if (count != deleteProperties.size()) {
						setAttr("errorMsg", "部分属性更新失败！");
					}
				}
				if (deleteCustomizations.size() != 0) 
					CusConfigureService.service.deleteCustomizationByProperties(deleteCustomizations);
    		}
    	}
		renderJson(new String[]{"errorMsg"});
    }

    @Before(Tx.class)
    public void deletePropertyType() {

    	int propertyTypeId = getParaToInt(0);
    	int prototypeId = getParaToInt(1);
    	int materialTypeId = getParaToInt(2);
    	PropertyType.dao.deleteById(propertyTypeId);
    	CusConfigureService.service.deletePropertiesByType(propertyTypeId);
    	CusConfigureService.service.deleteChildrenType(propertyTypeId);
    	CusConfigureService.service.deleteCustomizationByType(prototypeId, materialTypeId);
		renderJson(new String[]{"errorMsg"});
    }

    @Before(Tx.class)
    public void updateTypeRank() {
    	
    	int prototypeId = getParaToInt(0);
    	String ranks = getPara(1);
    	String[] rankArray = ranks.split(",");

		List<Record> updateTypes = new ArrayList<Record>();
		for (int i = rankArray.length, j = 1; i > 0; i--,j++) {
			updateTypes.add(new Record().set("property_type_id", Integer.valueOf(rankArray[i-1])).set("rank", j));
		}
		int[] result = CusConfigureService.service.updateTypeRank(updateTypes);
		int r = 0;
		while (r < result.length && result[r] == AppConstants.OK)
			r++;
		if (r < result.length) {
			setAttr("errorMsg", "部分属性类别更新失败！");
		}
		ranks = CusConfigureService.service.getTypeRank(prototypeId);
		setAttr("ranks", ranks);
		renderJson(new String[]{"errorMsg", "ranks"});
    }

    @Before(Tx.class)
    public void updatePropertyRank() {
    	
    	int propertyTypeId = getParaToInt(0);
    	String ranks = getPara(1);
    	String[] rankArray = ranks.split(",");
    	
    	List<Record> updateProperties = new ArrayList<Record>();
    	for (int i = rankArray.length, j = 1; i > 0; i--, j++) {
    		updateProperties.add(new Record().set("property_id", Integer.valueOf(rankArray[i-1])).set("rank", j));
    	}
    	int[] result = CusConfigureService.service.updatePropertyRank(updateProperties);
    	int r = 0;
    	while (r < result.length && result[r] == AppConstants.OK)
    		r++;
    	if (r < result.length) {
    		setAttr("errorMsg", "部分属性更新失败！");
    	}
    	ranks = CusConfigureService.service.getPropertyRank(propertyTypeId);
    	setAttr("ranks", ranks);
    	renderJson(new String[]{"errorMsg", "ranks"});
    }
}
