# select * from sys_config where config_key = 'bus.meeting.device.ip';
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, update_by)
VALUES ('会议-设备 IP 网段', 'bus.meeting.device.ip', '192.168.1.0', 'N', 'admin', NOW(), '');
