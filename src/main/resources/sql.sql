DROP TABLE IF EXISTS `qy_customer`;
CREATE TABLE `qy_customer` (
  `id` varchar(40) NOT NULL,
  `shortCode` varchar(32) NOT NULL,
  `num` varchar(32) NOT NULL,
  `name` varchar(16) NOT NULL,
  `companyName` varchar(16) DEFAULT '',
  `phone` varchar(16) NOT NULL,
  `address` varchar(32) NOT NULL,
  `createUserName` varchar(8)NOT NULL,
  `createUserNum` bigint(32) NOT NULL,
  `createUserShortCode` bigint(32) NOT NULL,
  `createUserId` bigint(40) NOT NULL,
  `utime` datetime DEFAULT null,
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `qy_syslog`;
CREATE TABLE `qy_syslog` (
  `id` varchar(40) NOT NULL,
  `operUserName` varchar(8)  NOT NULL,
  `operUserId` varchar(40)  NOT NULL,
  `operUserNum` varchar(32)  NOT NULL,
  `operShortCode` varchar(32) NOT NULL,
  `operFuncName` varchar(8)  NOT NULL,
  `operIp` varchar(16)  NOT NULL,
  `operOrderNum` varchar(40) DEFAULT '',
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `qy_permission`;
CREATE TABLE `qy_permission` (
  `id` varchar(40) NOT NULL,
  `num` varchar(32) NOT NULL,
  `name` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `qy_sysconfig`;
CREATE TABLE `qy_sysconfig` (
  `id` varchar(40) NOT NULL,
  `num` varchar(32) NOT NULL,
  `name` varchar(18) NOT NULL,
  `createUserName` varchar(8) NOT NULL,
  `createUserNum` varchar(32) NOT NULL,
  `createUserShortCode` varchar(32) NOT NULL,
  `createUserId` varchar(40)NOT NULL,
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `qy_userinfor`;
CREATE TABLE `qy_userinfor` (
  `id` varchar(40) NOT NULL,
  `name` varchar(8) NOT NULL,
  `sex` tinyint NOT NULL,
  `phone` varchar(16) NOT NULL,
  `identityCard` varchar(32) NOT NULL,
  `loginName` varchar(8) NOT NULL,
  `pwd` varchar(32) NOT NULL,
  `num` varchar(32) NOT NULL,
  `shortCode` varchar(32) NOT NULL,
  `companyName` varchar(32) DEFAULT '',
  `companyId` varchar(40) DEFAULT '',
  `utime` datetime DEFAULT null,
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `qy_user_permission`;
CREATE TABLE `qy_user_permission` (
  `id` varchar(40) NOT NULL,
  `userId` varchar(40)NOT NULL,
  `permissionId` varchar(40) NOT NULL,
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE qy_userinfor ADD COLUMN status TINYINT DEFAULT 0 COMMENT '0代表有效，1代表已经被删除';
ALTER TABLE qy_customer ADD COLUMN status TINYINT DEFAULT 0 COMMENT '0代表有效，1代表已经被删除';
INSERT INTO `qy_permission` VALUES ('3b1ba5f5-5568-438a-9eb3-707488224f68', '5f096c3952a7ef54041c5ac33fdf56b0', '打印单据');
INSERT INTO `qy_permission` VALUES ('64e44862-4dd6-4bdd-9987-90d00e8cd4d3', '26b68a76e59b57d0d6370eaf6a5974de', '收款');
INSERT INTO `qy_permission` VALUES ('8014fae7-2291-45fb-8a29-80d99c0f74ff', 'f7ae5b27126b3a1e4302b73b87ea6400', '修改接件单');
INSERT INTO `qy_permission` VALUES ('934e7f1d-1bb8-4583-b574-7b24229f1a37', 'c85b5056068c1f39a089f718fec077ba', '开接件单');
INSERT INTO `qy_permission` VALUES ('989566f4-7a19-4716-bbc7-180d5134258d', '35e2e5dbb41a0846e12c80bd138f28af', 'admin');


INSERT INTO `qy_user_permission` VALUES ('4bdbbb0a-4430-4fb9-bb7c-34fc03073f0f', '1322d022-a232-45f1-b1bf-9507ab2a95b6', '35e2e5dbb41a0846e12c80bd138f28af', '2016-12-14 11:33:29');
-- 手机后8位为密码

ALTER TABLE qy_customer MODIFY COLUMN `createUserNum` VARCHAR(32) NOT NULL;
ALTER TABLE qy_customer MODIFY  COLUMN `createUserShortCode` VARCHAR(32) NOT NULL;
ALTER TABLE qy_customer MODIFY  COLUMN `createUserId` VARCHAR(40) NOT NULL;

  ALTER TABLE qy_syslog MODIFY  COLUMN `operUserName` varchar(16) DEFAULT '';
  ALTER TABLE qy_syslog MODIFY  COLUMN  `operUserId` varchar(40)  DEFAULT '';
  ALTER TABLE qy_syslog MODIFY  COLUMN  `operUserNum` varchar(32) DEFAULT '';
  ALTER TABLE qy_syslog MODIFY  COLUMN  `operShortCode` varchar(32)  DEFAULT '';
  ALTER TABLE qy_syslog MODIFY  COLUMN  `operFuncName` varchar(16)  DEFAULT '';
  ALTER TABLE qy_syslog MODIFY  COLUMN  `operIp` varchar(16)  DEFAULT '';

DROP TABLE IF EXISTS `qy_order_num`;
CREATE TABLE `qy_order_num` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `num` INT NOT NULL COMMENT '订单编号',
  `nyr` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `qy_order`;
CREATE TABLE `qy_order` (
  `id` varchar(40) NOT NULL,
  `orderNum` varchar(32) NOT NULL COMMENT"订单编号",
  `addTime` varchar(16) NOT NULL COMMENT"创建订单的日期",
  `total` int(11) NOT NULL COMMENT"订单总金额",
  `orderStatus` varchar(8) DEFAULT '0' COMMENT"订单流程状态",
  `payStatus` varchar(8) DEFAULT '0' COMMENT"订单付款状态",
  `userName` varchar(8) NOT NULL COMMENT"开单人",
  `userNum` varchar(32) NOT NULL COMMENT"开单人编号",
  `userShortCode` varchar(32) NOT NULL COMMENT"开单人简码",
  `userId` varchar(40) NOT NULL COMMENT"开单人id",
  `companyName` varchar(16) NOT NULL COMMENT"开单企业名称",
  `utime` datetime DEFAULT null COMMENT"订单修改时间",
  `ctime` datetime DEFAULT null COMMENT"订单创建时间",
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `qy_order_item`;
CREATE TABLE `qy_order_item` (
  `id` varchar(40) NOT NULL COMMENT '订单明细表的id',
  `code` varchar(32) NOT NULL COMMENT '订单明细表对应的title订单编号',
  `name` varchar(16) NOT NULL COMMENT '订单明细表的商品名称',
  `price` int(11) NOT NULL COMMENT '订单明细表的单价',
  `num` int(11) NOT NULL COMMENT '订单明细表的数量',
  `discount` varchar(8) NOT NULL COMMENT '订单明细表的折扣',
  `total` int(11) NOT NULL COMMENT '订单明细表的小计金额',
  `customerName` varchar(8) NOT NULL COMMENT '订单明细表的客户名',
  `customerNum` varchar(32) DEFAULT '' COMMENT '订单明细表的客户编码',
  `customerShortCode` varchar(32) DEFAULT '' COMMENT '订单明细表的客户简码',
  `customerId` varchar(40) DEFAULT '' COMMENT '订单明细表的客户id',
  `utime` datetime DEFAULT null,
  `ctime` datetime DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 ALTER TABLE qy_order_item ADD  COLUMN  `memo` varchar(32) DEFAULT '' COMMENT'订单备注';

 ALTER TABLE qy_order_item DROP COLUMN customerName;
 ALTER TABLE qy_order_item DROP COLUMN customerNum;
 ALTER TABLE qy_order_item DROP COLUMN customerShortCode;
 ALTER TABLE qy_order_item DROP COLUMN customerId;
 
ALTER TABLE qy_order ADD COLUMN `customerName` varchar(8) NOT NULL COMMENT '订单明细表的客户名';
ALTER TABLE qy_order ADD COLUMN  `customerNum` varchar(32) DEFAULT '' COMMENT '订单明细表的客户编码';
ALTER TABLE qy_order ADD COLUMN `customerShortCode` varchar(32) DEFAULT '' COMMENT '订单明细表的客户简码';
ALTER TABLE qy_order ADD COLUMN  `customerId` varchar(40) DEFAULT '' COMMENT '订单明细表的客户id';
ALTER TABLE qy_order ADD COLUMN `customerCompanyName` varchar(16) NOT NULL COMMENT '订单客公司';
ALTER TABLE qy_order ADD COLUMN  `customerPhone` varchar(16) DEFAULT '' COMMENT '订单客户联系电话';

ALTER TABLE qy_order ADD COLUMN  `payMemo` varchar(32) DEFAULT '' COMMENT '结清备注';
ALTER TABLE qy_order ADD COLUMN  `cancelMemo` varchar(32) DEFAULT '' COMMENT '作废备注';

ALTER TABLE qy_order_item ADD COLUMN file_size VARCHAR(16) DEFAULT "" COMMENT "文件大小";

ALTER TABLE qy_order_item ADD COLUMN tp VARCHAR(8) DEFAULT "" COMMENT "类型";
ALTER TABLE qy_order_item ADD COLUMN unit VARCHAR(8) DEFAULT "" COMMENT "单位";
ALTER TABLE qy_order_item ADD COLUMN spec VARCHAR(8) DEFAULT "" COMMENT "规格尺寸";
ALTER TABLE qy_order_item ADD COLUMN KCP VARCHAR(8) DEFAULT "" COMMENT "kcp";
ALTER TABLE qy_order DROP COLUMN addTime;


alter table qy_order_item drop primary key;
ALTER TABLE qy_order_item ADD COLUMN bdid BIGINT  NOT NULL AUTO_INCREMENT  primary key  ;

alter table qy_order drop primary key;
ALTER TABLE qy_order ADD COLUMN bdid BIGINT  NOT NULL AUTO_INCREMENT  primary key  ;

alter TABLE qy_order change bdid bdid   bigint(20) NOT NULL AUTO_INCREMENT AFTER  id;
alter TABLE qy_order_item change bdid bdid   bigint(20) NOT NULL AUTO_INCREMENT AFTER  id;

ALTER TABLE qy_order MODIFY COLUMN   `payMemo` varchar(64) DEFAULT '' COMMENT '结清备注';
ALTER TABLE qy_order MODIFY COLUMN    `cancelMemo` varchar(32) DEFAULT '' COMMENT '作废备注';

INSERT qy_order_num(num,nyr) values("1",DATE(NOW()));
ALTER TABLE qy_userinfor ADD COLUMN mwpwd VARCHAR(16) DEFAULT "" COMMENT "用户登录密码的明文";
 -- UPDATE qy_userinfor SET mwpwd="12345678" WHERE id="1322d022-a232-45f1-b1bf-9507ab2a95b6";
ALTER TABLE qy_order_item MODIFY  `memo` varchar(64) DEFAULT '' COMMENT '订单备注';
ALTER TABLE qy_customer  MODIFY  `address` varchar(32) DEFAULT "" COMMENT'客户地址';
ALTER TABLE qy_order_item MODIFY COLUMN `KCP` varchar(16) DEFAULT '' COMMENT 'kcp';
ALTER TABLE qy_order_item DROP COLUMN memo;
ALTER TABLE qy_order ADD COLUMN `memo` varchar(128) DEFAULT '' COMMENT '订单备注';

INSERT INTO `qy_sysconfig` VALUES ('0d4b65e9-3be0-4566-8a15-bfe5e8c8f7e1', '671a2384d772eaf328bcc58f9e6c7050', '科卓图文设计制作中心', '超级管理员', 'qy-', 'ljl', '512b4671-3f2f-4060-8d6c-5d721f449699', '2017-01-13 14:12:52');
INSERT INTO `qy_sysconfig` VALUES ('f29c7ae9-36f7-4a5d-9015-1df82c47e964', '671a2384d772eaf328bcc58f9e6c7050', '昆明聚源达彩印包装有限公司', '超级管理员', 'qy-', 'ljl', 'ad3fa31b-1575-4cc2-934e-84c8236fcee4', '2017-1-12 14:17:55');

ALTER TABLE qy_order DROP COLUMN  `memo` ;
ALTER TABLE qy_order_item ADD COLUMN  `memo` varchar(128) DEFAULT '' COMMENT '订单备注';
INSERT INTO `qy_userinfor` VALUES ('1322d022-a232-45f1-b1bf-9507ab2a95b6', '超级管理员', 0, '13708420639', '530324xxxxxxxxxxxx', 'ljl', '0d5aad94f675d34855c89cb437b05878', 'qy-', 'ljl', 'qy', '53ae0091-b15e-41be-a766-156c14c1eb88', '2016-12-14 10:40:52', '2016-12-14 10:40:52', 0, '12345678');
ALTER TABLE qy_order MODIFY COLUMN   `cancelMemo` varchar(64) DEFAULT '' COMMENT '作废备注';


ALTER TABLE qy_order_item  MODIFY  `name` varchar(32) NOT NULL COMMENT '订单明细表的商品名称';
