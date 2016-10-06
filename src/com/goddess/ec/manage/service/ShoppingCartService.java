package com.goddess.ec.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ShoppingCartService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ShoppingCartService.class);

    public static final ShoppingCartService service = new ShoppingCartService();

	/**
	 * 获取购物车
	 * @param userId
	 * @return
	 */
	public List<Record> getShoppingCart(int userId) {
		return Db.find(ToolSqlXml.getSql("manage.shoppingCart.getShoppingCart"), userId);
	}

	/**
	 * 添加购物车
	 * @return
	 */
	public boolean addShoppingCart(Record cartCommodity) {
		return Db.save("shopping_cart", cartCommodity);
	}
	
	/**
	 * 修改购物车商品数量
	 * @param cartCommodityId
	 * @param amount
	 * @return
	 */
    public boolean updatePurchaseAmount(int cartCommodityId, int amount) {
    	Record cartCommodity = Db.findById("shopping_cart", cartCommodityId).set("purchase_amount", amount);
    	return Db.update("shopping_cart", cartCommodity);
    }
	
	/**
	 * 更新购物车商品
	 * @param cartCommodityId
	 * @param params 待更新的属性
	 * @return
	 */
    public boolean updateShoppingCart(int cartCommodityId, Record cartCommodity) {
    	return Db.update("shopping_cart", Db.findById("shopping_cart", cartCommodityId).setColumns(cartCommodity));
    }

    /**
     * 删除购物车中商品/清空购物车
     * @param userId
     * @param cartCommodityId
     * @return
     */
    public boolean deleteCommodities(int userId, String[] cartCommodityIds) {
    	if (userId == 0) {
        	Map<String, Object> params = new HashMap<String, Object>();
    		params.put("commoditiy_ids", StringUtils.join(cartCommodityIds, ','));
    		return Db.update(ToolSqlXml.getSql("manage.shoppingCart.deleteCommodities", params)) == cartCommodityIds.length;
    	} else {
    		// 清空购物车
    		return Db.update(ToolSqlXml.getSql("manage.shoppingCart.clearShoppingCart"), userId) != 0;
    	}
    }

}