create database shiyi_oj;
use shiyi_oj;
create table user(
     id          bigint auto_increment primary key comment 'id',
     userAccount varchar(256) not null comment '账号',
     password    varchar(512) not null comment '密码',
     username    varchar(256) null comment '用户名',
     userRole    varchar(10) default 'user' not null comment '用户角色:user/admin/ban',
     createTime  datetime default CURRENT_TIMESTAMP not null,
     updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
     isDelete tinyint default 0 not null comment '是否删除'
)