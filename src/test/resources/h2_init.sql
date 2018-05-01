drop table if exists t_category;

create table t_category (
  id bigint(21) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  code varchar(255) NOT NULL COMMENT '编码',
  name varchar(255) NOT NULL COMMENT '名称',
  type int(11) unsigned NOT NULL COMMENT '类型',
  cover varchar(255) NOT NULL COMMENT '封面图片',
  created_on bigint(21) unsigned NOT NULL COMMENT '创建时间',
  updated_on bigint(21) unsigned NOT NULL COMMENT '修改时间',
  created_by bigint(21) unsigned NOT NULL COMMENT '创建人',
  updated_by bigint(21) unsigned NOT NULL COMMENT '修改人',
  PRIMARY KEY (id)
);

truncate table t_category;

insert into t_category (code,name,type,cover,created_on,updated_on,created_by,updated_by) values 
('design','设计思路',1,'',1,1,1525104000000,1525104000000),
('source','源码研读',1,'',1,1,1525104000000,1525104000000),
('architecture','架构分享',1,'',1,1,1525104000000,1525104000000),
('trouble','问题排查',1,'',1,1,1525104000000,1525104000000),
('skill','代码技巧',1,'',1,1,1525104000000,1525104000000);
