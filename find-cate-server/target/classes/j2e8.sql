/*
Navicat MySQL Data Transfer

Source Server         : demo
Source Server Version : 50639
Source Host           : localhost:3306
Source Database       : j2e8

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-11-28 23:32:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for commity
-- ----------------------------
DROP TABLE IF EXISTS `commity`;
CREATE TABLE `commity` (
  `commity_id` int(11) NOT NULL AUTO_INCREMENT,
  `commity_content` varchar(255) DEFAULT NULL,
  `commity_user_id` int(11) DEFAULT NULL,
  `commity_time` datetime DEFAULT NULL,
  `commity_like` int(11) DEFAULT '0',
  `commity_dislike` int(11) DEFAULT '0',
  `commity_shop_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`commity_id`),
  KEY `commity_user_id` (`commity_user_id`),
  KEY `commity_shop_id` (`commity_shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `food_id` int(11) NOT NULL AUTO_INCREMENT,
  `food_name` varchar(20) DEFAULT NULL,
  `food_price` float(10,1) DEFAULT NULL,
  `food_photo` varchar(100) DEFAULT NULL,
  `shop_id` int(11) DEFAULT NULL,
  `food_type_id` int(11) DEFAULT NULL,
  `food_like` int(11) DEFAULT '0',
  `food_dislike` int(11) DEFAULT '0',
  PRIMARY KEY (`food_id`),
  KEY `food_menu_id` (`shop_id`),
  KEY `food_type_id` (`food_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT,
  `reply_content` varchar(255) DEFAULT NULL,
  `reply_time` datetime DEFAULT NULL,
  `reply_user_id` int(11) DEFAULT NULL,
  `reply_commity_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`reply_id`),
  KEY `reply_user_id` (`reply_user_id`),
  KEY `repy_commity_id` (`reply_commity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `shop_id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_shopper_id` int(11) DEFAULT NULL,
  `shop_name` varchar(25) DEFAULT NULL,
  `shop_telenumber` varchar(30) DEFAULT NULL,
  `shop_address` varchar(100) DEFAULT NULL,
  `shop_photo` varchar(100) DEFAULT NULL,
  `shop_type_id` int(11) DEFAULT NULL,
  `shop_like` int(11) DEFAULT '0',
  `shop_dislike` int(11) DEFAULT '0',
  `shop_active` int(1) DEFAULT '0',
  `shop_lng` double DEFAULT NULL,
  `shop_lat` double DEFAULT NULL,
  PRIMARY KEY (`shop_id`),
  KEY `shop_type_id` (`shop_type_id`),
  KEY `shop_user_id` (`shop_shopper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for type_shop
-- ----------------------------
DROP TABLE IF EXISTS `type_shop`;
CREATE TABLE `type_shop` (
  `type_id` int(11) NOT NULL,
  `shop_type_id` int(11) NOT NULL,
  PRIMARY KEY (`type_id`,`shop_type_id`),
  KEY `shop_type_id` (`shop_type_id`),
  CONSTRAINT `type_shop_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `type` (`type_id`),
  CONSTRAINT `type_shop_ibfk_2` FOREIGN KEY (`shop_type_id`) REFERENCES `shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL,
  `user_telenumber` varchar(20) DEFAULT NULL,
  `user_photo` varchar(100) DEFAULT NULL,
  `user_email` varchar(30) DEFAULT '',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
