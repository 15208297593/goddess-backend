SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS goddess.accessory;
DROP TABLE IF EXISTS goddess.admin_users;
DROP TABLE IF EXISTS goddess.base_relate_type;
DROP TABLE IF EXISTS goddess.brand;
DROP TABLE IF EXISTS goddess.catalog;
DROP TABLE IF EXISTS goddess.classification;
DROP TABLE IF EXISTS goddess.collections;
DROP TABLE IF EXISTS goddess.comments;
DROP TABLE IF EXISTS goddess.commodities;
DROP TABLE IF EXISTS goddess.commodity_accessory;
DROP TABLE IF EXISTS goddess.commodity_catalog;
DROP TABLE IF EXISTS goddess.commodity_classification;
DROP TABLE IF EXISTS goddess.commodity_pic;
DROP TABLE IF EXISTS goddess.customization;
DROP TABLE IF EXISTS goddess.customization_pic;
DROP TABLE IF EXISTS goddess.customization_properties;
DROP TABLE IF EXISTS goddess.designer;
DROP TABLE IF EXISTS goddess.design_project;
DROP TABLE IF EXISTS goddess.design_vote;
DROP TABLE IF EXISTS goddess.logo_print;
DROP TABLE IF EXISTS goddess.manufacturer;
DROP TABLE IF EXISTS goddess.materials;
DROP TABLE IF EXISTS goddess.material_type;
DROP TABLE IF EXISTS goddess.orders;
DROP TABLE IF EXISTS goddess.order_accessory;
DROP TABLE IF EXISTS goddess.order_commodities;
DROP TABLE IF EXISTS goddess.properties;
DROP TABLE IF EXISTS goddess.property_type;
DROP TABLE IF EXISTS goddess.prototype;
DROP TABLE IF EXISTS goddess.prototype_classification;
DROP TABLE IF EXISTS goddess.prototype_pic;
DROP TABLE IF EXISTS goddess.rank_list;
DROP TABLE IF EXISTS goddess.RELATIONS;
DROP TABLE IF EXISTS goddess.return_order;
DROP TABLE IF EXISTS goddess.scene;
DROP TABLE IF EXISTS goddess.scene_commodities;
DROP TABLE IF EXISTS goddess.shopping_cart;
DROP TABLE IF EXISTS goddess.system_params;
DROP TABLE IF EXISTS goddess.users;
DROP TABLE IF EXISTS goddess.user_address;
DROP TABLE IF EXISTS goddess.user_customization;
DROP TABLE IF EXISTS goddess.user_design;
DROP TABLE IF EXISTS goddess.wx_user_info;




/* Create Tables */

CREATE TABLE goddess.accessory
(
	accessory_id int NOT NULL AUTO_INCREMENT,
	-- 0.logo样式；1.内部开格
	-- 生成订单时，logo印制作为订单配饰的一个类别存储
	accessory_type char NOT NULL COMMENT '0.logo样式；1.内部开格
生成订单时，logo印制作为订单配饰的一个类别存储',
	accessory_name varchar(16),
	accessory_price int,
	accessory_pic varchar(64),
	PRIMARY KEY (accessory_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.admin_users
(
	user_id int NOT NULL AUTO_INCREMENT,
	user_name varchar(16) NOT NULL,
	password varchar(32) NOT NULL,
	role char,
	cell_num varchar(16),
	email varchar(32),
	PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.base_relate_type
(
	base_type int,
	base_property_id int,
	relate_type int,
	relate_ref_type int,
	-- 影响商品价格的原材料对商品基础价格的加成
	price_addition int COMMENT '影响商品价格的原材料对商品基础价格的加成'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.brand
(
	brand_id int NOT NULL AUTO_INCREMENT,
	brand_name varchar(32),
	brand_initial char,
	brand_logo varchar(64),
	brand_banner varchar(64),
	brand_des varchar(512),
	new_amount smallint,
	collected_amount int,
	delete_flag char,
	PRIMARY KEY (brand_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.catalog
(
	catalog_id int NOT NULL AUTO_INCREMENT,
	catalog_name varchar(16),
	catalog_level tinyint unsigned,
	parent_id int,
	rank tinyint,
	PRIMARY KEY (catalog_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.classification
(
	classification_id int NOT NULL AUTO_INCREMENT,
	classification_name varchar(16),
	classification_level tinyint unsigned,
	-- 1：包袋；2：配饰
	category char COMMENT '1：包袋；2：配饰',
	parent_id int,
	rank tinyint,
	PRIMARY KEY (classification_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.collections
(
	user_id int NOT NULL,
	-- 1：商品；2：品牌
	collection_type char NOT NULL COMMENT '1：商品；2：品牌',
	-- 商品id或其他
	related_id int NOT NULL COMMENT '商品id或其他',
	collect_date timestamp DEFAULT NULL,
	UNIQUE (user_id, collection_type, related_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.comments
(
	comment_id int NOT NULL AUTO_INCREMENT,
	user_id int,
	user_design_id int,
	commodity_id int,
	content varchar(200),
	comment_date timestamp,
	PRIMARY KEY (comment_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.commodities
(
	commodity_id int NOT NULL AUTO_INCREMENT,
	user_customization_id int,
	prototype_id int,
	-- 【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串
	property_key varchar(128) COMMENT '【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串',
	base_properties varchar(256),
	detail_properties varchar(256),
	commodity_pic varchar(64),
	-- 1：用户；2：商家
	commodity_source char COMMENT '1：用户；2：商家',
	commodity_name varchar(64),
	commodity_des varchar(512),
	price int,
	-- 作为商品被他人购买时，给原创者返还积分
	return_points int COMMENT '作为商品被他人购买时，给原创者返还积分',
	stock int,
	shelve_date timestamp DEFAULT NULL,
	withdraw_date timestamp DEFAULT NULL,
	is_shelved char NOT NULL,
	is_new char,
	brand_id int,
	PRIMARY KEY (commodity_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.commodity_accessory
(
	accessory_id int NOT NULL,
	logo_print_id int,
	prototype_id int,
	commodity_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.commodity_catalog
(
	commodity_id int,
	catalog_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.commodity_classification
(
	commodity_id int,
	-- 1：包袋；2：配饰
	category char COMMENT '1：包袋；2：配饰',
	classification_id int,
	brand_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.commodity_pic
(
	commodity_pic_id int NOT NULL AUTO_INCREMENT,
	commodity_id int NOT NULL,
	pic_type char,
	commodity_pic varchar(64),
	rank tinyint DEFAULT 0,
	PRIMARY KEY (commodity_pic_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.customization
(
	prototype_id int NOT NULL,
	-- 【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串
	property_key varchar(128) NOT NULL COMMENT '【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串',
	-- 该图片用于定制时的展示
	custome_pic varchar(64) COMMENT '该图片用于定制时的展示',
	PRIMARY KEY (prototype_id, property_key)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


-- 用于高端定制
CREATE TABLE goddess.customization_pic
(
	customization_pic_id int NOT NULL AUTO_INCREMENT,
	user_customization_id int,
	pic_url varchar(64),
	rank tinyint DEFAULT 0,
	pic_type char,
	PRIMARY KEY (customization_pic_id)
) ENGINE = InnoDB COMMENT = '用于高端定制' DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.customization_properties
(
	user_customization_id int,
	prototype_id int,
	material_type_id int,
	material_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.designer
(
	designer_id int,
	cell_num varchar(16),
	password varchar(32),
	nick_name varchar(16),
	-- 0.女；1.男
	gender char COMMENT '0.女；1.男',
	-- 头像url
	head varchar(64) COMMENT '头像url',
	logo varchar(64),
	delete_flag char
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.design_project
(
	project_id int NOT NULL AUTO_INCREMENT,
	title varchar(64),
	description varchar(512),
	deadline timestamp,
	PRIMARY KEY (project_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.design_vote
(
	user_design_id int,
	user_id int,
	project_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.logo_print
(
	logo_print_id int NOT NULL AUTO_INCREMENT,
	logo_name varchar(16),
	logo_pic varchar(64),
	logo_price int,
	-- 1：挂牌；2：内牌
	logo_type char COMMENT '1：挂牌；2：内牌',
	logo_type_name varchar(16),
	-- 0：单选；1：多选
	is_multi char COMMENT '0：单选；1：多选',
	PRIMARY KEY (logo_print_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.manufacturer
(
	manufacturer_id int NOT NULL AUTO_INCREMENT,
	manufacturer_name varchar(32),
	-- 订单通过邮件发送给生产商
	manufacturer_email varchar(32) COMMENT '订单通过邮件发送给生产商',
	-- 1.包包
	manufacture_type char NOT NULL COMMENT '1.包包',
	PRIMARY KEY (manufacturer_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.materials
(
	material_id int NOT NULL AUTO_INCREMENT,
	material_type_id int NOT NULL,
	material_name varchar(32),
	-- 实物原材料图片URL，虚拟原材料对应值（颜色）
	material_exhibition varchar(64) COMMENT '实物原材料图片URL，虚拟原材料对应值（颜色）',
	-- 影响商品价格的原材料对商品基础价格的加成
	price_addition int DEFAULT 0 COMMENT '影响商品价格的原材料对商品基础价格的加成',
	delete_flag char NOT NULL,
	PRIMARY KEY (material_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.material_type
(
	material_type_id int NOT NULL AUTO_INCREMENT,
	type_name varchar(16) NOT NULL,
	-- 英文名称
	type_name_ext varchar(16) COMMENT '英文名称',
	-- 1：不显示名称；2：显示名称...
	customize_exhibition char COMMENT '1：不显示名称；2：显示名称...',
	-- 1.图片；2.特定字符串
	exhibition_type char COMMENT '1.图片；2.特定字符串',
	delete_flag char NOT NULL,
	PRIMARY KEY (material_type_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.orders
(
	order_id char(32) NOT NULL,
	payer_id int NOT NULL,
	-- 显示给客户。
	-- 1：未发货；2：已发货；3：已签收
	order_status char NOT NULL COMMENT '显示给客户。
1：未发货；2：已发货；3：已签收',
	-- 区别于订单状态，管理系统使用
	-- 1:未下单给生产商；2:未发货给客户；3:已发货给客户；4:客户已签收
	internal_order_status char NOT NULL COMMENT '区别于订单状态，管理系统使用
1:未下单给生产商；2:未发货给客户；3:已发货给客户；4:客户已签收',
	order_amount int,
	fee_type varchar(16),
	freight int,
	-- 1：微信支付；2：支付宝支付
	pay_mode char COMMENT '1：微信支付；2：支付宝支付',
	payment int,
	pay_fee_type varchar(16),
	used_points int,
	order_date timestamp DEFAULT NULL,
	message varchar(256),
	consignee_name varchar(8),
	consignee_telephone varchar(16),
	province_first_stage_name varchar(16),
	address_city_second_stage_name varchar(16),
	address_counties_third_stage_name varchar(32),
	address_detail_info varchar(128),
	delivery_com_no varchar(32),
	delivery_no varchar(32),
	delivery_info text,
	delivery_sign_date timestamp,
	wx_pay_status char,
	wx_err_code varchar(32),
	wx_err_msg varchar(256),
	transaction_id varchar(32),
	pay_params text,
	wx_attach text,
	wx_code_url varchar(64),
	settle_trade_no varchar(32),
	settle_date timestamp,
	delivery_order_id varchar(32),
	deposit_pay_mode char,
	deposit_trade_no varchar(32),
	deposit_fee int,
	deposit_fee_type varchar(16),
	deposit_transaction_id varchar(32),
	deposit_date timestamp,
	-- 1：PC端，2：微信公众号，3：APP
	order_source char COMMENT '1：PC端，2：微信公众号，3：APP',
	cancel_reason varchar(32),
	cancel_date timestamp,
	apply_refund_fee int,
	refund_no varchar(32),
	refund_transaction_id varchar(32),
	refund_date timestamp,
	PRIMARY KEY (order_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.order_accessory
(
	order_commodity_id int,
	accessory_name varchar(16),
	-- 0.logo样式；1.内部开格
	-- 生成订单时，logo印制作为订单配饰的一个类别存储
	accessory_type char COMMENT '0.logo样式；1.内部开格
生成订单时，logo印制作为订单配饰的一个类别存储',
	accessory_price int,
	accessory_pic varchar(64)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.order_commodities
(
	order_commodity_id int NOT NULL AUTO_INCREMENT,
	order_id char(32) NOT NULL,
	user_customization_id int,
	commodity_id int,
	commodity_name varchar(64),
	base_properties varchar(256),
	detail_properties varchar(256),
	price int,
	-- 作为商品被他人购买时，给原创者返还积分
	return_points int COMMENT '作为商品被他人购买时，给原创者返还积分',
	purchase_amount tinyint,
	-- 用于制造的图片，若无用户印花，则为定制品图片URL，若有用户印花，则为合成图片
	commodity_pic varchar(64) COMMENT '用于制造的图片，若无用户印花，则为定制品图片URL，若有用户印花，则为合成图片',
	user_printing varchar(64),
	manufacturer_id int,
	-- 用户输入logo
	logo_content varchar(16) COMMENT '用户输入logo',
	PRIMARY KEY (order_commodity_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.properties
(
	property_id int NOT NULL AUTO_INCREMENT,
	property_type_id int NOT NULL,
	material_id int NOT NULL,
	-- 该类别需要的原材料的数量
	material_amount int COMMENT '该类别需要的原材料的数量',
	rank tinyint DEFAULT 0,
	PRIMARY KEY (property_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.property_type
(
	property_type_id int NOT NULL AUTO_INCREMENT,
	prototype_id int,
	is_base char,
	base_ref_type int,
	is_ref char,
	material_type_id int,
	type_name varchar(16),
	type_name_ext varchar(16),
	is_required char,
	-- 冲突的原材料类别及替代材料，格式：{原材料类别id:"原材料类别id@原材料id"}
	conflict_type varchar(128) COMMENT '冲突的原材料类别及替代材料，格式：{原材料类别id:"原材料类别id@原材料id"}',
	conflict_alert varchar(64),
	rank tinyint DEFAULT 0,
	PRIMARY KEY (property_type_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


-- 定制的基本款型
CREATE TABLE goddess.prototype
(
	prototype_id int NOT NULL AUTO_INCREMENT,
	brand_id int,
	-- 1：包包；以后扩展其他类别用
	prototype_type char NOT NULL COMMENT '1：包包；以后扩展其他类别用',
	prototype_name varchar(32),
	prototype_pic varchar(64),
	base_properties varchar(256),
	prototype_des varchar(512),
	-- 图片url
	des_pic varchar(64) COMMENT '图片url',
	-- 商品价格的基本定价
	base_price int COMMENT '商品价格的基本定价',
	print_price int DEFAULT 0,
	print_frame varchar(128),
	delete_flag char NOT NULL,
	PRIMARY KEY (prototype_id)
) ENGINE = InnoDB COMMENT = '定制的基本款型' DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.prototype_classification
(
	prototype_id int,
	-- 1：包袋；2：配饰
	category char COMMENT '1：包袋；2：配饰',
	classification_id int,
	brand_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.prototype_pic
(
	prototype_pic_id int NOT NULL AUTO_INCREMENT,
	prototype_id int,
	pic_url varchar(64),
	rank tinyint DEFAULT 0,
	pic_type char,
	PRIMARY KEY (prototype_pic_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.rank_list
(
	user_design_id int,
	-- 0：点赞。。。
	list_type char COMMENT '0：点赞。。。',
	votes int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


-- 用户间的关注，拉黑的关系
CREATE TABLE goddess.RELATIONS
(
	-- USERS.USER_ID
	FOLLOWER int NOT NULL COMMENT 'USERS.USER_ID',
	-- USERS.USER_ID
	FOLLOWED int NOT NULL COMMENT 'USERS.USER_ID',
	-- 0：未拉黑/1：被拉黑
	DEFRIEND char(1) COMMENT '0：未拉黑/1：被拉黑',
	CONSTRAINT relation UNIQUE (FOLLOWER, FOLLOWED)
) ENGINE = InnoDB COMMENT = '用户间的关注，拉黑的关系' DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.return_order
(
	return_order_id varchar(32) NOT NULL,
	return_user_id int,
	order_id char(32),
	-- 订单商品id
	return_commodity_id int COMMENT '订单商品id',
	apply_refund_fee int,
	return_reason varchar(32),
	-- 1.待审核；2.待验货；3.待退款；4.完成
	return_status char NOT NULL COMMENT '1.待审核；2.待验货；3.待退款；4.完成',
	return_apply_date timestamp,
	refund_fee int,
	refund_no varchar(32),
	refund_transaction_id varchar(32),
	-- 原卡退回
	refund_type char COMMENT '原卡退回',
	refund_date timestamp,
	PRIMARY KEY (return_order_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.scene
(
	scene_id int NOT NULL AUTO_INCREMENT,
	scene_name varchar(16),
	-- 扩展英文名称
	scene_name_ext varchar(16) COMMENT '扩展英文名称',
	destination char,
	scene_pic varchar(64),
	rank tinyint DEFAULT 0,
	scene_url varchar(64),
	-- 前端页面路由需要的url
	route_url varchar(64) COMMENT '前端页面路由需要的url',
	-- 1：PC；2：微信公众号；3：APP
	terminal char COMMENT '1：PC；2：微信公众号；3：APP',
	page char,
	location char,
	-- 商品id或品牌id。。。
	related_id int COMMENT '商品id或品牌id。。。',
	PRIMARY KEY (scene_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.scene_commodities
(
	scene_id int,
	commodity_id int
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.shopping_cart
(
	cart_commodity_id int NOT NULL AUTO_INCREMENT,
	user_id int,
	commodity_id int,
	user_customization_id int,
	purchase_amount tinyint,
	purchase_date timestamp DEFAULT NULL,
	PRIMARY KEY (cart_commodity_id),
	UNIQUE (user_id, commodity_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.system_params
(
	category varchar(32),
	code varchar(32),
	text varchar(128)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.users
(
	user_id int NOT NULL AUTO_INCREMENT,
	openid varbinary(32),
	cell_num varchar(16),
	password varchar(32),
	nick_name varchar(16),
	-- 0.女；1.男
	gender char COMMENT '0.女；1.男',
	-- 头像url
	head varchar(64) COMMENT '头像url',
	email varchar(64),
	points int DEFAULT 0,
	collection_amount smallint DEFAULT 0,
	customization_amount smallint DEFAULT 0,
	order_amount smallint DEFAULT 0,
	design_amount smallint,
	follow_amount smallint,
	fans_amount smallint,
	register_date timestamp DEFAULT NULL,
	-- 用户地址id
	last_used_address int COMMENT '用户地址id',
	delete_flag char,
	PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.user_address
(
	user_address_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	consignee_name varchar(8),
	consignee_telephone varchar(16),
	address_postal_code varchar(16),
	province_first_stage_name varchar(16),
	address_city_second_stage_name varchar(16),
	address_counties_third_stage_name varchar(32),
	address_detail_info varchar(128),
	national_code varchar(16),
	PRIMARY KEY (user_address_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.user_customization
(
	user_customization_id int NOT NULL AUTO_INCREMENT,
	-- 1.私人定制；2.高端定制
	customize_type char NOT NULL COMMENT '1.私人定制；2.高端定制',
	user_id int NOT NULL,
	customize_name varchar(32),
	prototype_id int,
	-- 【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串
	property_key varchar(128) COMMENT '【_原材料类别id:原材料id_原材料类别id:原材料id_】组合成的检索关键字串',
	base_properties varchar(256),
	-- 该图片用于定制时的展示
	custome_pic varchar(64) COMMENT '该图片用于定制时的展示',
	price int,
	-- 作为商品被他人购买时，给原创者返还积分
	return_points int COMMENT '作为商品被他人购买时，给原创者返还积分',
	user_printing varchar(64),
	-- 用于高端定制
	customize_des text COMMENT '用于高端定制',
	-- 0：未完成；1：已完成；2：在购物车中；3：已删除
	customize_status char COMMENT '0：未完成；1：已完成；2：在购物车中；3：已删除',
	customize_date timestamp DEFAULT NULL,
	customize_tags varchar(256),
	PRIMARY KEY (user_customization_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.user_design
(
	user_design_id int NOT NULL AUTO_INCREMENT,
	project_id int,
	user_id int,
	user_customization_id int,
	design_name varchar(16),
	design_date timestamp,
	design_pic varchar(32),
	-- 0：不可；1：可
	shelvable char COMMENT '0：不可；1：可',
	-- 1：已提交；2：已上榜；3：未通过审核
	design_status char COMMENT '1：已提交；2：已上榜；3：未通过审核',
	reject_reason varchar(64),
	ups smallint,
	comments smallint,
	PRIMARY KEY (user_design_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;


CREATE TABLE goddess.wx_user_info
(
	user_id int NOT NULL,
	nickname varchar(45) NOT NULL,
	sex char,
	language varchar(10),
	city varchar(20),
	province varchar(20),
	country varchar(10),
	headimgurl varchar(150),
	PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8;



