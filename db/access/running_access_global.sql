create table running_access_global
(
  `key` varchar(255) not null comment '键名',
  value text not null comment '值'
)
comment 'Access相关全局变量';

create unique index running_access_global_key_uindex
on running_access_global (`key`);

alter table running_access_global
add constraint running_access_global_pk
primary key (`key`);
