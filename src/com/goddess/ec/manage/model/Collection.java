package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "collections", pkName="collection_id")
public class Collection extends BaseModel<Collection> {

	private static final long serialVersionUID = 6761767368352810428L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(Collection.class);

	public static final Collection dao = new Collection();

}
