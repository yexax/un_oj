create database if not exists shiyi_oj;
use shiyi_oj;
create table if not exists user(
     id          bigint auto_increment primary key comment 'id',
     userAccount varchar(256) not null comment '账号',
     password    varchar(512) not null comment '密码',
     username    varchar(256) null comment '用户名',
     userRole    varchar(10) default 'user' not null comment '用户角色:user/admin/ban',
     createTime  datetime default CURRENT_TIMESTAMP not null,
     updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
     isDelete tinyint default 0 not null comment '是否删除'
);

create table if not exists question(
     id          bigint auto_increment primary key comment 'id',
     title       varchar(512) not null comment '题目标题',
     content     text not null comment '题目内容',
     tags        varchar(1024) comment '题目标签列表',
     submitNum   int default 0 not null comment '提交数',
     acceptNum   int default 0 not null comment '通过数',
     judgeCase   text null comment '判题用例JSON数组',
     judgeConfig text null comment '判题配置',
     userId      bigint not null comment '创建用户id',
     createTime  datetime default CURRENT_TIMESTAMP not null,
     updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
     isDelete tinyint default 0 not null comment '是否删除'
)