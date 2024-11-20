
-- CREATE DATABASE xtquant_v2 CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
-- CREATE USER 'xtquant'@'%' IDENTIFIED BY '%your_password%'; -- 请替换为自己的密码
-- GRANT ALL PRIVILEGES ON xtquant_v2.* TO 'xtquant'@'%' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

use xtquant_v2;

-- 字典表
drop table if exists t_dict;
create table t_dict (
    `id`    bigint       primary key auto_increment comment 'ID',
    `type`  tinyint      not null    comment '字典项类型, 1:定单类型, 2:价格类型, 3:定单状态',
    `k`     smallint     not null    comment 'Key',
    `v`     varchar(255) not null    comment 'Value',
    unique key `i_type_code_for_dict` (`type`, `k`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典';

-- 定单类型
insert into t_dict (`type`, `k`, `v`) values (1, 23, '买');
insert into t_dict (`type`, `k`, `v`) values (1, 24, '卖');

-- 价格类型
insert into t_dict (`type`, `k`, `v`) values (2, 5,  '最新价');
insert into t_dict (`type`, `k`, `v`) values (2, 11, '限价');
insert into t_dict (`type`, `k`, `v`) values (2, 42, '最优五档即时成交剩余撤销');
insert into t_dict (`type`, `k`, `v`) values (2, 43, '最优五档即时成交剩转限价');
insert into t_dict (`type`, `k`, `v`) values (2, 44, '对手方最优价格委托');
insert into t_dict (`type`, `k`, `v`) values (2, 45, '本方最优价格委托');
insert into t_dict (`type`, `k`, `v`) values (2, 46, '即时成交剩余撤销委托');
insert into t_dict (`type`, `k`, `v`) values (2, 47, '最优五档即时成交剩余撤销');
insert into t_dict (`type`, `k`, `v`) values (2, 48, '全额成交或撤销');

-- 定单状态
insert into t_dict (`type`, `k`, `v`) values (3, 48,  '未报');
insert into t_dict (`type`, `k`, `v`) values (3, 49,  '待报');
insert into t_dict (`type`, `k`, `v`) values (3, 50,  '已报');
insert into t_dict (`type`, `k`, `v`) values (3, 51,  '已报待撤');
insert into t_dict (`type`, `k`, `v`) values (3, 52,  '部成待撤');
insert into t_dict (`type`, `k`, `v`) values (3, 53,  '部撤');
insert into t_dict (`type`, `k`, `v`) values (3, 54,  '已撤');
insert into t_dict (`type`, `k`, `v`) values (3, 55,  '部成');
insert into t_dict (`type`, `k`, `v`) values (3, 56,  '已成');
insert into t_dict (`type`, `k`, `v`) values (3, 57,  '废单');
insert into t_dict (`type`, `k`, `v`) values (3, 255, '未知');
insert into t_dict (`type`, `k`, `v`) values (3, -1,  '保存在本地');

-- 资金表
drop table if exists t_asset;
create table t_asset (
    `id`                bigint         primary key auto_increment comment 'ID',
    `account_id`        varchar(32)    not null    comment '账号id',
    `date`              int            not null    comment '日期, format: YYYYMMDD',
    `total_asset`       decimal(16, 2) not null    comment '总资产',
    `market_value`      decimal(16, 2) not null    comment '证券市值',    
    `cash`              decimal(16, 2) not null    comment '可用资金',
    `withdrawable_cash` decimal(16, 2) not null    comment '可取资金',
    `profit`            decimal(16, 2) not null    comment '盈亏金额',
    unique key `i_account_date_for_asset` (`account_id`, `date`),
    index `i_date_for_asset` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资金';

-- 持仓表
drop table if exists t_position;
create table t_position (
    `id`               bigint         primary key auto_increment comment 'ID',
    `account_id`       varchar(32)    not null    comment '账号id',
    `date`             int            not null    comment '日期, format: YYYYMMDD',
    `stock_code`       char(9)        not null    comment '股票代码',
    `volume`           bigint         not null    comment '持仓数量',
    `can_use_volume`   bigint         not null    comment '可用数量',
    `frozen_volume`    bigint         not null    comment '冻结数量',
    `on_road_volume`   bigint         not null    comment '在途数量',
    `open_price`       decimal(10, 3) not null    comment '平均建仓成本价',
    `market_value`     decimal(16, 2) not null    comment '市值',
    `yesterday_volume` bigint         not null    comment '昨日持仓数量',
    `profit`           decimal(16, 2) not null    comment '盈亏金额',
    unique key `i_account_date_stock_for_position` (`account_id`, `date`, `stock_code`),
    index `i_date_stock_for_position` (`date`, `stock_code`),
    index `i_stock_for_position` (`stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='持仓';

-- 定单表
drop table if exists t_order;
create table t_order (
    `id`               bigint         primary key auto_increment comment 'ID',
    `account_id`       varchar(32)    not null    comment '账号id',
    `date`             int            not null    comment '日期, format: YYYYMMDD',
    `stock_code`       char(9)        not null    comment '股票代码',
    `order_remark`     varchar(23)    not null    comment '备注(实为下游调用者设定的定单id, 具有唯一性)',    
    `order_id`         bigint             null    comment '定单编号',
    `order_sys_id`     varchar(32)        null    comment '委托编号(柜台合同编号)',
    `order_time`       char(6)            null    comment '报单时间, format: HHMMSS',
    `order_type`       tinyint        not null    comment '定单类型, 见字典项"定单类型"',
    `order_volume`     bigint         not null    comment '定单数量',
    `price_type`       tinyint        not null    comment '定单价格类型, 见字典项"价格类型"',
    `price`            decimal(10, 3) not null    comment '定单价格',
    `traded_volume`    bigint         null        comment '成交数量',
    `traded_price`     decimal(10, 3) null        comment '成交均价',
    `order_status`     smallint       not null    comment '定单状态, 见字典项"定单状态"',
    `canceled_volume`  bigint         not null    comment '已撤单数量',
    `strategy_name`    varchar(32)    null        comment '策略名称',
    `created_at`       bigint         not null    comment '创建时间(毫秒)',
    `error_msg`        varchar(255)   null        comment '错误信息(废单原因)',
    unique key `i_remark_for_order` (`order_remark`),    
    index `i_account_date_stock_for_order` (`account_id`, `date`, `stock_code`),
    index `i_account_stock_for_order` (`account_id`, `stock_code`),
    index `i_stock_for_order` (`stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定单';
