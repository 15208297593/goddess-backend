package com.goddess.ec.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.CategoryType;
import com.goddess.ec.manage.model.Commodity;
import com.goddess.ec.manage.tools.ToolSqlFormatter;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CommodityService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(CommodityService.class);

    public static final CommodityService service = new CommodityService();

    /**
     * 商品详情
     * @param commodityId
     * @return
     */
    public Commodity getCommodityById(int commodityId) {
        return Commodity.dao.findById(commodityId);
    }

    /**
     * 商品列表
     * @param splitPage
     */
    public void getCommodities(SplitPage splitPage) {
    	String select = "SELECT c.*,b.brand_name";
    	
    	Map<String, String> params = splitPage.getQueryParam();
    	String catalogIds = params.get("catalog_ids");
    	StringBuilder tables = new StringBuilder();
    	StringBuilder whereConds = new StringBuilder();
    	Map<String, String> catalogMap = new HashMap<String, String>();
    	if (StringUtils.isNotEmpty(catalogIds)) {
//    		String[] catalogs = catalogIds.split("-");
    		int tNum = 1;
    		for (String subCatalog : catalogIds.split("_")) {
    			String[] id = subCatalog.split("@");
    			String ids = catalogMap.get(id[0]);
    			if (ids == null) {
    				ids = id[1];
    				if (catalogMap.size() > 0) tables.append(",");
    				tables.append(" commodity_catalog cc"+tNum);
    				whereConds.append(" AND cc"+tNum+".catalog_id in #($catalog_ids_"+tNum+"$)#");
    				catalogMap.put(id[0], ids);
    				params.put(id[0], "catalog_ids_"+tNum);
    				tNum++;
    			} else {
    				ids += "_" + id[1];
    				catalogMap.put(id[0], ids);
    			}
    		}
    		tables.append(" where 1=1 ");
			int cNum = 1;
			for (String key : catalogMap.keySet()) {
				params.put(params.remove(key), catalogMap.get(key));
				if (cNum < catalogMap.size())
					tables.append(" AND cc"+cNum+".commodity_id = cc"+(cNum+1)+".commodity_id ");
				cNum++;
			}
    		params.put("catalog_condition", tables.append(whereConds).toString());
    	}
    	params.remove("catalog_ids");
        splitPageBase(splitPage, select, "manage.commodity.splitPage");
    }

    public List<Record> getCatalogs() {
    	return getTree(Db.find(ToolSqlXml.getSql("manage.commodity.getCatalogs")));
    }
    
    /**
     * 取得所有商品类别
     * 
     * @return
     */
    public Map<String, Record> getClassifications() {

    	List<Record> nodes = Db.find(ToolSqlXml.getSql("manage.commodity.getAllClassification"));
    	return getClassificationTree(nodes);
    }
    

	protected List<Record> getTree(List<Record> nodes) {
    	Map<Integer, Record> nodeMap = new HashMap<Integer, Record>();
    	List<Record> rankTree = new ArrayList<Record>();
    	
    	for (Record r : nodes) {
    		Integer parentId = r.getInt("parent_id");
    		
    		nodeMap.put(r.getInt("id"), r);
    		if (parentId != 0) {
    			
    			Record node = nodeMap.get(parentId);
    			List<Record> children = node.get("children");
    			if (children == null) {
    				children = new ArrayList<Record>();
    				node.set("children", children);
    			}
    			children.add(r);
			// 0级结点，父结点为0
    		} else {
    			rankTree.add(r);
    		}
    	}
    	return rankTree;
    }
    
    public Map<String, Record> getClassificationTree(List<Record> nodes) {

    	Map<Integer, Record> classificationTree = new HashMap<Integer, Record>();
    	Map<String, Record> categories = new HashMap<String, Record>();
    	for (Record r : nodes) {
    		Integer parentId = r.getInt("parent_id");

    		classificationTree.put(r.getInt("id"), r);
    		if (parentId != 0) {

    			Record node = classificationTree.get(parentId);
    			List<Record> children = node.get("children");
    			if (children == null) {
    				children = new ArrayList<Record>();
    				node.set("children", children);
    			}
    			children.add(r);
    		// 1级结点，父结点为0
    		} else {
    			Record category =categories.get(r.getStr("category"));
    			if (category == null) {
        			List<Record> children = new ArrayList<Record>();
    				category = new Record().set("children", children).set("name", CategoryType.getEnumByCode(r.getStr("category")).getText());
    				categories.put(r.getStr("category"), category);
    			}
    			List<Record> children = category.get("children");
    			children.add(r);
    		}
    	}
    	return categories;
    }
    
    /**
     * 商品图片
     * @param commodityId
     * @return
     */
    public List<Record> getCommodityPics(int commodityId) {
    	return Db.find(ToolSqlXml.getSql("manage.commodity.getCommodityPics"), commodityId);
    }
    
    /**
     * 计算所有材料的加成价格
     * @param propertyKey
     * @return
     */
    public int getAdditionPrice(String propertyKey) {

    	String[] proKey = propertyKey.substring(1, propertyKey.length()-1).split("_");
    	String keys = ToolSqlFormatter.formatInPart(proKey.length);
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("keys", keys);
    	Record priceAddition = Db.findFirst(ToolSqlXml.getSql("manage.customize.getPriceAddition", queryParam), proKey);
    	return priceAddition == null ? 0 : priceAddition.getBigDecimal("price_addition").intValue();
    }
    
    /**
     * 取得商品定制属性
     * 
     * @param userCustomizationId
     * @param commodityId
     * @return
     */
    public List<Record> getCommodityCustomizationProps(Integer userCustomizationId, Integer commodityId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("user_customization_id", userCustomizationId);
        return Db.find(ToolSqlXml.getSql("manage.commodity.getCommodityCustomizationProps", params), userCustomizationId == null ? commodityId : userCustomizationId);
    }
}
