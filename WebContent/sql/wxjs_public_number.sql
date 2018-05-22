/*
Navicat MySQL Data Transfer

Source Server         : MYSQL
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : wxjs_public_number

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2018-04-18 21:33:49
*/

-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE wxjs_public_number;
-- 使用数据库
use wxjs_public_number;


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for card_address
-- ----------------------------
DROP TABLE IF EXISTS `card_address`;
CREATE TABLE `card_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(50) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of card_address
-- ----------------------------
INSERT INTO `card_address` VALUES ('1', '同仁公寓', '104.0524', '30.673157');
INSERT INTO `card_address` VALUES ('2', '长虹科技大厦', '104.0642', '30.541527');

-- ----------------------------
-- Table structure for card_record
-- ----------------------------
DROP TABLE IF EXISTS `card_record`;
CREATE TABLE `card_record` (
  `id` varchar(50) NOT NULL,
  `clock_date` varchar(20) DEFAULT NULL,
  `start_time` varchar(20) DEFAULT NULL,
  `end_time` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of card_record
-- ----------------------------
INSERT INTO `card_record` VALUES ('1300100', '2018年04月18日', '21:12:38', '21:14:01');
INSERT INTO `card_record` VALUES ('1300100', '2018年05月22日', '13:40:29', '13:42:31');

-- ----------------------------
-- Table structure for card_ticket
-- ----------------------------
DROP TABLE IF EXISTS `card_ticket`;
CREATE TABLE `card_ticket` (
  `time` varchar(15) DEFAULT NULL,
  `ticket` varchar(100) DEFAULT NULL,
  `id` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of card_ticket
-- ----------------------------
INSERT INTO `card_ticket` VALUES ('1526967368060', 'kgt8ON7yVITDhtdwci0qeasVwq45XhONlqVX-W82c-lBM-rSzwiKKJfwLLGgft0HpCx8qF0G8pIPT0ZBWFDolQ', 'ec3Sv6');

-- ----------------------------
-- Table structure for card_user
-- ----------------------------
DROP TABLE IF EXISTS `card_user`;
CREATE TABLE `card_user` (
  `usercode` varchar(10) NOT NULL,
  `userpassword` varchar(10) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `openId` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`usercode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of card_user
-- ----------------------------
INSERT INTO `card_user` VALUES ('1300100', '123', 'Test用户', 'oNBaExOt67SzKoTQ0mkTwSwxcymo');
SET FOREIGN_KEY_CHECKS=1;
