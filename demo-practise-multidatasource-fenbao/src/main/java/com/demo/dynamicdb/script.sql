-- 在DB1中创建student表
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------

-- Table structure for student

-- ----------------------------

DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(64)  NULL DEFAULT NULL,
`addr` varchar(255)  NULL DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

-- ----------------------------

-- Records of student

-- ----------------------------

INSERT INTO `student` VALUES (1, 'zhangsan', '北京');
INSERT INTO `student` VALUES (2, 'lisi', '上海');

SET FOREIGN_KEY_CHECKS = 1;

-- 在DB2中创建teacher表
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------

-- Table structure for teacher

-- ----------------------------

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(32)  NULL DEFAULT NULL,
`addr` varchar(255)  NULL DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------

-- Records of teacher

-- ----------------------------

INSERT INTO `teacher` VALUES (1, 'wangwu', '上海');

SET FOREIGN_KEY_CHECKS = 1;