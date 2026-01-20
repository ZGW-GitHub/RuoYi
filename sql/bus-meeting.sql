# drop table if exists bus_topic_info;
create table bus_topic_info
(
    id              bigint auto_increment comment 'id' primary key,
    title           varchar(255)  not null default '' comment '标题',
    topic_type      varchar(20)   not null comment '类型',
    topic_status    varchar(20)   not null comment '状态',
    report_people   varchar(50)   null comment '汇报人',
    report_unit     varchar(50)   null comment '汇报单位',
    file_info       varchar(5000) null comment '文件信息',
    attachment_info varchar(5000) null comment '附件信息',
    ext_info        LONGTEXT      null comment '扩展信息',

    order_no        int           not null default 0 comment '序号',
    deleted         bigint                 default 0 comment '删除标志（0代表存在）',
    create_by       varchar(64)            default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64)            default '' comment '更新者',
    update_time     datetime comment '更新时间',
    remark          varchar(500)           default null comment '备注'
) comment '议题表';

# drop table if exists bus_meeting_info;
create table bus_meeting_info
(
    id                 bigint auto_increment comment 'id' primary key,
    title              varchar(255)  not null default '' comment '标题',
    meeting_type       varchar(20)   not null comment '类型',
    meeting_status     varchar(20)   not null comment '状态',
    meeting_time       varchar(30)   null comment '会议时间',
    meeting_host       varchar(30)   null comment '主持人',
    participation_info varchar(5000) null comment '参会情况',
    file_info          varchar(5000) null comment '会议文件信息',
    attachment_info    varchar(5000) null comment '附件信息',
    ext_info           LONGTEXT      null comment '扩展信息',

    order_no           int           not null default 0 comment '序号',
    deleted            bigint                 default 0 comment '删除标志（0代表存在）',
    create_by          varchar(64)            default '' comment '创建者',
    create_time        datetime comment '创建时间',
    update_by          varchar(64)            default '' comment '更新者',
    update_time        datetime comment '更新时间',
    remark             varchar(500)           default null comment '备注'
) comment '会议表';

# drop table if exists bus_meeting_topic;
create table bus_meeting_topic
(
    id          bigint auto_increment comment 'id' primary key,
    meeting_id  bigint   not null comment '会议ID',
    topic_id    bigint   not null comment '议题ID',
    ext_info    LONGTEXT null comment '扩展信息',

    order_no    int      not null default 0 comment '序号',
    deleted     bigint            default 0 comment '删除标志（0代表存在）',
    create_by   varchar(64)       default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)       default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500)      default null comment '备注',
    index inx_meetingId (meeting_id, deleted),
    index inx_topicId (topic_id, deleted)
) comment '会议议题关联表';


-- 名称、序列号、IP、端口、状态(未连接、有线连接、无线连接)
# drop table if exists bus_device_info;
create table bus_device_info
(
    id            bigint auto_increment comment 'id' primary key,
    device_serial varchar(32) not null comment '设备序列号',
    device_name   varchar(32) not null comment '设备名称',
    device_ip     varchar(64)          default '' comment 'IP',
    device_port   int                  default null comment '端口',
    device_status varchar(20) not null comment '状态',
    ext_info      LONGTEXT    null comment '扩展信息',

    order_no      int         not null default 0 comment '序号',
    deleted       bigint               default 0 comment '删除标志（0代表存在）',
    create_by     varchar(64)          default '' comment '创建者',
    create_time   datetime comment '创建时间',
    update_by     varchar(64)          default '' comment '更新者',
    update_time   datetime comment '更新时间',
    remark        varchar(500)         default null comment '备注'
) comment '设备信息表';