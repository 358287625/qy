DROP TABLE if EXISTS qy_app_tb;		
CREATE TABLE `qy_app_tb` (
  `aid` varchar(64) NOT NULL COMMENT '设备唯一标记',
  `mac` varchar(16) DEFAULT '' COMMENT '设备mac地址',
  `lng` varchar(8) DEFAULT '' COMMENT '设备经度',
  `lat` varchar(8) DEFAULT '' COMMENT '设备纬度',
  `st` tinyint(4) DEFAULT '0' COMMENT '在线状态，0在线，1表示离线',
  `code` varchar(8) DEFAULT '' COMMENT '行政区编码',
  `province` varchar(8) DEFAULT '' COMMENT '省',
  `city` varchar(8) DEFAULT '' COMMENT '市',
  `district` varchar(8) DEFAULT '' COMMENT '区',
  `addr` varchar(32) DEFAULT '' COMMENT '打印机详细地址',
  `ip` varchar(32) DEFAULT '127.0.0.1' COMMENT '打印机上网地址',
  `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `ver` varchar(32) DEFAULT '' COMMENT '打印设备pc版本',
  `os` varchar(32) DEFAULT '' COMMENT '打印设备pc操作系统',
  `tag` varchar(32) DEFAULT '' COMMENT '长连接推送tag',
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业app注册表';


DROP TABLE if EXISTS qy_printer_tb;	
CREATE TABLE `qy_printer_tb` (
  `did` VARCHAR(64) NOT NULL COMMENT '打印机数据库id',
  `pid` VARCHAR(32) DEFAULT "" COMMENT '打印机唯一标记',
  `aid` VARCHAR(64) DEFAULT "" COMMENT 'app 数据库id',
  `tag` VARCHAR(32) DEFAULT ""  COMMENT 'app的tag',
  `appSta` VARCHAR(32) DEFAULT ""  COMMENT 'app在线状态，0在线，1表示离线',
  `ctime` datetime DEFAULT  NOW() COMMENT '创建时间',
  `utime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业打印机注册表';


DROP TABLE if EXISTS qy_user_tb;	
CREATE TABLE `qy_user_tb` (
  `uid` VARCHAR(64) NOT NULL COMMENT '用户唯一标记',
  `lng` VARCHAR(8) DEFAULT ""  COMMENT '用户经度',
  `lat` VARCHAR(8) DEFAULT ""  COMMENT '用户纬度',
  `count`  INT DEFAULT 0 COMMENT '用户打印次数',
  `code` VARCHAR(8)  DEFAULT "" COMMENT '用户注册时行政区编码',
  `province` VARCHAR(8)  DEFAULT "" COMMENT '用户注册时省',
  `city` VARCHAR(8) DEFAULT "" COMMENT '用户注册时市',
  `district` VARCHAR(8) DEFAULT "" COMMENT '用户注册时区',
  `addr` VARCHAR(32) DEFAULT "" COMMENT '用户注册时详细地址',
  `ip` VARCHAR(32) DEFAULT '127.0.0.1' COMMENT '用户注册时上网地址',
  `ctime` datetime DEFAULT  NOW() COMMENT '创建时间',
  `utime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户打印注册表';


DROP TABLE if EXISTS qy_user_printer_tb;	
CREATE TABLE `qy_user_printer_tb` (
  `uid` VARCHAR(64) NOT NULL COMMENT '用户唯一标记',
  `did` VARCHAR(64)NOT NULL COMMENT '设备唯一标记',
  `ctime` datetime DEFAULT  NOW() COMMENT '创建时间',
  `utime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`uid`,`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户与打印机关联表';


DROP TABLE if EXISTS qy_user_doc_tb;	
CREATE TABLE `qy_user_doc_tb` (
  `docid` VARCHAR(64) NOT NULL COMMENT '文档唯一标记',
  `uid` VARCHAR(64) NOT NULL  COMMENT '用户唯一标记',
  `did` VARCHAR(64) DEFAULT ""  COMMENT '打印设备唯一标记',
  `srcpath` VARCHAR(64) DEFAULT "" COMMENT '原文件路径',
  `pdfpath` VARCHAR(64)  DEFAULT "" COMMENT 'pdf文件路径',
  `imgpath` VARCHAR(64)  DEFAULT "" COMMENT '图片文件路径',
  `num` INT  DEFAULT 0 COMMENT '打印份数',
  `ab` TINYINT  DEFAULT 0 COMMENT '正反打印.0单面打印，1双面打印',
  `st` TINYINT  DEFAULT 0 COMMENT '文档状态：0已上传，1已通知下载，2下载中，3已打印cg，4打印失败，失败时error有错误原因',
  `fee` INT  DEFAULT 0 COMMENT '支付金额',
  `feetype` TINYINT  DEFAULT 0 COMMENT '支付类型',
  `code` VARCHAR(8)  DEFAULT "" COMMENT '用户打印时政区编码',
  `province` VARCHAR(8)  DEFAULT "" COMMENT '用户打印时省',
  `city` VARCHAR(8) DEFAULT "" COMMENT '用户打印时市',
  `district` VARCHAR(8) DEFAULT "" COMMENT '用户打印时区',
  `addr` VARCHAR(32) DEFAULT "" COMMENT '用户打印时详细地址',
  `ip` VARCHAR(32) DEFAULT '127.0.0.1' COMMENT '用户打印时上网地址',
  `ua` VARCHAR(32) DEFAULT '' COMMENT '用户打印时浏览器ua',
  `error` VARCHAR(8) DEFAULT '' COMMENT '用户打印文档错误原因标号',
  `ctime` datetime DEFAULT  NOW() COMMENT '创建时间',
  `utime` datetime DEFAULT NOW() COMMENT '更新时间',
  PRIMARY KEY (`docid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户打印文档表';



ALTER TABLE qy_printer_tb ADD COLUMN pid VARCHAR(32) DEFAULT '' COMMENT'打印机id';
ALTER TABLE qy_printer_tb ADD COLUMN ver VARCHAR(32) DEFAULT '' COMMENT'打印设备pc版本';
ALTER TABLE qy_printer_tb ADD COLUMN os VARCHAR(32) DEFAULT '' COMMENT'打印设备pc操作系统';
ALTER TABLE qy_printer_tb ADD COLUMN tag VARCHAR(32) DEFAULT '' COMMENT'长连接推送tag'; 
ALTER TABLE qy_user_tb ADD COLUMN ua VARCHAR(64) DEFAULT '' COMMENT'用户第一次进来的ua'; 
ALTER TABLE qy_user_doc_tb ADD COLUMN pageFrom INT DEFAULT 0 COMMENT'从第几页开始打印'; 
ALTER TABLE qy_user_doc_tb ADD COLUMN pageTo INT DEFAULT 0 COMMENT'打印到第几页结束'; 

ALTER TABLE qy_user_doc_tb ADD COLUMN lng VARCHAR(16) DEFAULT '' COMMENT'上传文档ip的经度'; 
ALTER TABLE qy_user_doc_tb ADD COLUMN lat VARCHAR(16) DEFAULT '' COMMENT'上传文档ip的纬度'; 
ALTER TABLE qy_user_doc_tb DROP COLUMN imgpath;