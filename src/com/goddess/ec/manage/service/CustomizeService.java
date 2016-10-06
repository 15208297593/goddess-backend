package com.goddess.ec.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.plugin.PropertiesPlugin;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CustomizeService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(CustomizeService.class);

    public static final CustomizeService service = new CustomizeService();

    /**
     * 取得原型及其拥有的属性类别
     * @param splitPage
     */
    public void getPrototypesWithTypes(SplitPage splitPage) {
        splitPageBase(splitPage, "SELECT ap.prototype_id,min(ap.prototype_name) as name,min(ap.prototype_pic) as pic,"
        		+ "CAST(group_concat(distinct if(c.prototype_id is null, ap.type_name, null)) AS CHAR) as unimported_types,"
        		+ "CAST(group_concat(distinct ap.type_name ORDER BY ap.rank desc) AS CHAR) as all_types", "manage.customize.splitPage");
    }
    
    public List<Record> getPropertiesByPrototypeId(int prototypeId) {
    	return Db.find(ToolSqlXml.getSql("manage.customize.getPropertiesByPrototypeId"), prototypeId);
    }
    

    public Record isPrototypeCustomized(int prototypeId) {
    	return Db.findFirst(ToolSqlXml.getSql("manage.customize.isPrototypeCustomized"), prototypeId);
    }

    public List<Record> getPropertiesCombination(int prototypeId) {
    	return Db.find(ToolSqlXml.getSql("manage.customize.getPropertiesCombination"), prototypeId);
    }
    
    public List<Record> getUnCustomizedCombination(int prototypeId) {
    	return Db.find(ToolSqlXml.getSql("manage.customize.getUnCustomizedCombination"), prototypeId, prototypeId);
    }
    
    public void getCustomizedPic(SplitPage splitPage) {
        splitPageBase(splitPage, "SELECT c.property_key,p.prototype_name,p.prototype_id,c.custome_pic", "manage.customize.getCustomizedPic");
    }
    
    public void searchCustomizationsByPropertyKey(SplitPage splitPage) {
    	// 使用distinct删除重复数据，因为可能存在用户定制上架的商品和商家上架的商品，选用了相同的属性（用户定制的商品上传了印花，否则将不会有重复属性的商品上架）
        splitPageBase(splitPage, "SELECT DISTINCT c.property_key,c.custome_pic,if(co.commodity_id is null, 0, 1) as is_shelved", "manage.customize.searchCustomizationsByPropertyKey");
    }

    /**
     * 取得所有原材料组合
     * @return
     */
    public Map<Integer, Map<Integer, Record>> getMaterialCombination() {
    	List<Record> records = Db.find(ToolSqlXml.getSql("manage.customize.getMaterialCombination"), AppConstants.MATERIAL_EXHIBITION_TYPE_COLOR,
    			PropertiesPlugin.getParamMapValue(DictKeys.image_url));
    	Map<Integer, Map<Integer, Record>> typeAndMaterial = new HashMap<Integer, Map<Integer, Record>>();
    	for (Record r : records) {
    		Map<Integer, Record> materials = typeAndMaterial.get(r.getInt("material_type_id"));
    		if (materials == null) {
    			materials = new HashMap<Integer, Record>();
    			typeAndMaterial.put(r.getInt("material_type_id"), materials);
    		}
    		materials.put(r.getInt("material_id"), r);
    	}
    	return typeAndMaterial;
    }

    /**
     * 批量保存
     * @param records
     */
    public void saveCustomizations(List<Record> records) {
		Db.batch(ToolSqlXml.getSql("manage.customize.saveCustomizations"), "prototype_id,property_key,custome_pic", records, records.size());
    }
    
    /**
     * 批量更新
     * @param records
     */
    public int[] updateCustomizations(List<Record> records) {
		return Db.batch(ToolSqlXml.getSql("manage.customize.updateCustomizations"), "custome_pic,prototype_id,property_key", records, records.size());
    }

    /**
     * 批量保存定制属性
     * @param records
     */
    public void saveCustomizationProperties(List<Record> records) {
    	Db.batch(ToolSqlXml.getSql("manage.customize.saveCustomizationProperties"), "user_customization_id,prototype_id,material_type_id,material_id", records, records.size());
    }
    
    /**
     * 批量保存定制属性
     * @param records
     */
    public void saveOrderAccessories(List<Record> records) {
    	Db.batch(ToolSqlXml.getSql("manage.customize.saveOrderAccessories"), "order_commodity_id, accessory_name, accessory_type, accessory_price, accessory_pic", records, records.size());
    }
    
    /**
     * 删除定制属性
     * @param userCustomizationId
     */
    public void deleteCustomizationProperties(int userCustomizationId) {
    	Db.update(ToolSqlXml.getSql("manage.customize.deleteCustomizationProperties"), userCustomizationId);
    }
    
    /**
     * 删除定制属性
     * @param userCustomizationId
     */
    public void deleteOrderAccs(int orderCommodityId) {
    	Db.update(ToolSqlXml.getSql("manage.customize.deleteOrderAccs"), orderCommodityId);
    }
}
