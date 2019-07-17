CREATE DATABASE db_zilean;

USE db_zilean;

# 延迟任务表
CREATE TABLE db_zilean.tbl_zilean_queue
(
    id          BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键id',
    delayed_id  VARCHAR(40)              NOT NULL COMMENT '延迟id',
    name        VARCHAR(40)              NOT NULL COMMENT '延迟任务名称',
    delay       INT(11)                  NOT NULL COMMENT '延迟时间',
    call_back   VARCHAR(200)             NOT NULL COMMENT '回调地址，http://,https://',
    header      VARCHAR(200)  DEFAULT '' NOT NULL COMMENT '回调请求头，json格式',
    body        VARCHAR(2000) DEFAULT '' NOT NULL COMMENT '回调请求体，任意格式',
    status      INT(4)        DEFAULT 1  NOT NULL COMMENT '延迟状态：1delayed、2ready、3failed、4finish、5delete、6cancel',
    create_time BIGINT(20)               NOT NULL COMMENT '创建时间:yyyyMMddHHmmss',
    update_time BIGINT(20)               NOT NULL COMMENT '更新时间:yyyyMMddHHmmss',
    ver         INT(11)       DEFAULT 1  NOT NULL COMMENT '版本号',
    token_id    BIGINT(20)               NOT NULL COMMENT 'token_id',
    ttr         INT(11)                  NOT NULL COMMENT '回调超时时间',
    retry_time  INT(4)        DEFAULT 0  NOT NULL COMMENT '重试次数',
    type        INT(4)        DEFAULT 1  NOT NULL COMMENT '任务类型：1simple',
    response    varchar(200)  DEFAULT '' COMMENT 'response'
) ENGINE = InnoDB
  CHARSET = utf8 COMMENT '延迟任务表';
CREATE INDEX idx_create_time ON db_zilean.tbl_zilean_queue (create_time);
CREATE INDEX idx_delayed_id ON db_zilean.tbl_zilean_queue (delayed_id);
CREATE INDEX idx_status ON db_zilean.tbl_zilean_queue (status);


#  token表
CREATE TABLE db_zilean.tbl_zilean_token
(
    id          BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键id',
    username    VARCHAR(40)      NOT NULL COMMENT 'username',
    password    VARCHAR(40)      NOT NULL COMMENT 'password',
    salt        VARCHAR(40)      NOT NULL COMMENT 'salt',
    nickname    VARCHAR(40)      NOT NULL COMMENT '昵称',
    token       VARCHAR(50)      NOT NULL COMMENT 'token',
    create_time BIGINT(20)       NOT NULL COMMENT '创建时间:yyyyMMddHHmmss',
    update_time BIGINT(20)       NOT NULL COMMENT '更新时间:yyyyMMddHHmmss',
    ver         INT(4) DEFAULT 1 NOT NULL COMMENT '版本号',
    status      INT(4) DEFAULT 1 NOT NULL COMMENT '状态：1正常'

) ENGINE = InnoDB
  CHARSET = utf8 COMMENT 'token表';
CREATE INDEX idx_token ON db_zilean.tbl_zilean_token (token);


#  统计表
CREATE TABLE db_zilean.tbl_zilean_statistics
(
    id            BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键id',
    create_time   BIGINT(20)        NOT NULL COMMENT '创建时间:yyyyMMddHHmmss',
    update_time   BIGINT(20)        NOT NULL COMMENT '更新时间:yyyyMMddHHmmss',

    visit_num     INT(11) DEFAULT 0 NOT NULL COMMENT '当日访问量',
    visit_total   INT(11) DEFAULT 0 NOT NULL COMMENT '访问总量',

    delayed_num   INT(11) DEFAULT 0 NOT NULL COMMENT '当日延迟任务数',
    delayed_total INT(11) DEFAULT 0 NOT NULL COMMENT '延迟任务总数',

    ready_num     INT(11) DEFAULT 0 NOT NULL COMMENT '当日读取任务数',
    ready_total   INT(11) DEFAULT 0 NOT NULL COMMENT '读取任务总数',

    success_num   INT(11) DEFAULT 0 NOT NULL COMMENT '当日成功任务数',
    success_total INT(11) DEFAULT 0 NOT NULL COMMENT '成功任务总数',

    failed_num    INT(11) DEFAULT 0 NOT NULL COMMENT '当日失败任务数',
    failed_total  INT(11) DEFAULT 0 NOT NULL COMMENT '失败任务总数',

    ver           INT(4)  DEFAULT 1 NOT NULL COMMENT '版本号',
    status        INT(4)  DEFAULT 1 NOT NULL COMMENT '状态：1正常'

) ENGINE = InnoDB
  CHARSET = utf8 COMMENT '统计表';
CREATE INDEX idx_create_time ON db_zilean.tbl_zilean_statistics (create_time);

INSERT INTO db_zilean.tbl_zilean_statistics (id, create_time, visit_num, visit_total, delayed_num, delayed_total, ready_num, ready_total, success_num, success_total, failed_num, failed_total, ver, status) VALUES (3, 20190714000000, 99, 99, 0, 4, 0, 0, 0, 0, 0, 0, 1, 1);
