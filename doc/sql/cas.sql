/*
 Navicat Premium Data Transfer

 Source Server         : localhost@root
 Source Server Type    : MySQL
 Source Server Version : 50715
 Source Host           : localhost:3306
 Source Schema         : cas

 Target Server Type    : MySQL
 Target Server Version : 50715
 File Encoding         : 65001

 Date: 30/01/2020 23:33:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `id_card_num` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户身份证号',
  `expired` int(1) NULL DEFAULT NULL COMMENT '是否过期 : 0 正常； 1 过期',
  `disabled` int(1) NULL DEFAULT NULL COMMENT '是否可用：0 正常 ；1 不可用',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `id_card_num`(`id_card_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'amdin', 'admin', 'b11d5ed0c5b99693b3e2d751a7ef541f', 'e61fb780850c6b97ff1c2444e277bf45', '888888888888888888', 0, 1);
INSERT INTO `user_info` VALUES (2, 'test', 'test', '352538e12384b7ae17a2d5d03ea8aef9', 'f6b9a14f6358facb34489fc84bedda9f', '1111', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
