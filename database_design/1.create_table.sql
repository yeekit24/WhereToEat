CREATE TABLE IF NOT EXISTS user_info (
	`user_id` BIGINT NOT NULL AUTO_INCREMENT,
	`username` nvarchar(100) NOT NULL,
	`api_key` nvarchar(250) NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`user_id`),
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS country (
	`country_id` INT NOT NULL AUTO_INCREMENT,
	`country_code` nvarchar(30) NOT NULL,
	`country_desc` text NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`country_id`),
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS reference (
	`reference_id` BIGINT NOT NULL AUTO_INCREMENT,
  `URL` nvarchar(250) NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`reference_id`),
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS login_device_type(
	`login_device_type_id` SMALLINT NOT NULL,
	`login_device_type_code` nvarchar(30) NOT NULL,
	`login_device_type_desc` text NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NOT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`login_device_type_id`),
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS user_device(
	`user_device_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL,
	`login_device_type_id` SMALLINT NULL,
	`device_guid` nvarchar(400) NULL,
	`android_firebase_token` nvarchar(400) DEFAULT NULL,
	`api_key` nvarchar(255) DEFAULT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NOT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`user_device_id`),
	FOREIGN KEY(`user_id`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`login_device_type_id`) REFERENCES login_device_type(`login_device_type_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS choice_profile (
	`choice_profile_id` BIGINT NOT NULL AUTO_INCREMENT,
	`choice_profile_name` nvarchar(100) NOT NULL,
	`choice_profile_desc` text NOT NULL,
  `longtitude` DECIMAL(16,8) NULL,
  `latitude` DECIMAL(16,8) NULL,
  `user_id` BIGINT NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NOT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`choice_profile_id`),
	FOREIGN KEY(`user_id`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS choice_reference (
	`choice_reference_id` BIGINT NOT NULL AUTO_INCREMENT,
  `reference_id` BIGINT NULL,
	`user_id` BIGINT NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`choice_reference_id`),
  FOREIGN KEY(`user_id`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`reference_id`) REFERENCES reference(`reference_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS choices (
	`choice_id` BIGINT NOT NULL AUTO_INCREMENT,
	`choice_name` nvarchar(100) NOT NULL,
	`choice_desc` text NULL,
  `user_id` BIGINT NOT NULL,
  `choice_reference_id` BIGINT NULL,
  `choice_profile_id` BIGINT NULL,
	`sequence_no` int NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NULL,
	`modified_time` DATETIME NOT NULL,
  PRIMARY KEY(`choice_id`),
	FOREIGN KEY(`user_id`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`choice_reference_id`) REFERENCES choice_reference(`choice_reference_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`choice_profile_id`) REFERENCES choice_profile(`choice_profile_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS spin_action (
	`spin_action_id` BIGINT NOT NULL AUTO_INCREMENT,
	`selected_choice_id` BIGINT NOT NULL,
	`active_status` SMALLINT NOT NULL,
	`modified_by` BIGINT NOT NULL,
	`modified_time` DATETIME NOT NULL,
	PRIMARY KEY(`spin_action_id`),
	FOREIGN KEY(`selected_choice_id`) REFERENCES choices(`choice_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(`modified_by`) REFERENCES user_info(`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB  DEFAULT CHARSET=latin1;
