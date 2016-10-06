package com.goddess.ec.manage.service;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.model.Collection;

public class CollectionService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(CollectionService.class);

	public static final CollectionService service = new CollectionService();
	
	/**
	 * 添加收藏
	 * @param userId 用户id
	 * @param relatedId 收藏品id
	 * @return
	 */
	public boolean saveCollection(int userId, int relatedId, String collectionType) {
		return new Collection().put("user_id", userId).put("related_id", relatedId).put("collection_type", collectionType).save();
	}
	
	public boolean cancelCollection(int collectionId) {
		return Collection.dao.deleteById(collectionId);
	}
}
