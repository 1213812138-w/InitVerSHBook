/*
 Navicat Premium Dump SQL

 Source Server         : LocalDatabase
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : localhost:3306
 Source Schema         : campus-product

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 19/12/2025 15:30:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '反馈主键id',
  `user_id` int NULL DEFAULT NULL COMMENT '发起反馈的用户id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '反馈标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '反馈内容',
  `picture_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图片列表',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '管理员回复内容',
  `admin_reply_status` tinyint NULL DEFAULT NULL COMMENT '管理员是否已经处理',
  `created_time` datetime NULL DEFAULT NULL COMMENT '反馈创建时间',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '反馈回复时间',
  `type` enum('bug','suggestion','complaint','other') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'other' COMMENT '反馈类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (1, 10, '1', '0222222', 'http://localhost:21090/api/campus-product-sys/v1.0/file/getFile?fileName=9fd561f3.png,http://localhost:21090/api/campus-product-sys/v1.0/file/getFile?http://localhost:21090/api/campus-product-sys/v1.0/file/getFile?fileName=8dd22f5img1.jpg', '011111111111', 1, '2025-12-17 21:39:40', '2025-12-19 14:40:53', 'suggestion');
INSERT INTO `feedback` VALUES (4, 11, 'test', '111111', '', NULL, 0, '2025-12-19 15:24:54', NULL, 'complaint');

SET FOREIGN_KEY_CHECKS = 1;
