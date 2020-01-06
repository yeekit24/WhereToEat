INSERT IGNORE INTO `user_info` (
	`user_id`,
	`username`,
	`api_key`,
	`active_status`,
	`modified_by`,
	`modified_time`
)VALUES(
  1,'SYSTEM_ADMIN',NULL,1,1,NOW()
);

INSERT IGNORE INTO `login_device_type`
(
	`login_device_type_id`,
	`login_device_type_code`,
	`login_device_type_desc`,
	`active_status`,
	`modified_by`,
	`modified_time`
)
VALUES
(
  1,
	"ANDROID",
	"Android",
	1,1,NOW()
);

INSERT IGNORE INTO `login_device_type`
(
	`login_device_type_id`,
	`login_device_type_code`,
	`login_device_type_desc`,
	`active_status`,
	`modified_by`,
	`modified_time`
)
VALUES
(
	2,
	"WEB",
	"Web",
	1,1,NOW()
);

INSERT IGNORE INTO `login_device_type`
(
	`login_device_type_id`,
	`login_device_type_code`,
	`login_device_type_desc`,
	`active_status`,
	`modified_by`,
	`modified_time`
)
VALUES
(
	3,
	"APPLE",
	"Apple iProduct",
	1,1,NOW()
);
