/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : gin-driver

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 28/03/2024 23:49:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dustbin
-- ----------------------------
DROP TABLE IF EXISTS `dustbin`;
CREATE TABLE `dustbin`  (
                            `id` bigint(0) NOT NULL AUTO_INCREMENT,
                            `file_id` bigint(0) NOT NULL COMMENT '文件ID',
                            `user_id` bigint(0) NOT NULL COMMENT '删除者ID',
                            `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
                            `type` int(0) NOT NULL COMMENT '文件类型(0：其他，1：照片)',
                            `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                            `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '媒体资源垃圾箱' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
                         `id` bigint(0) NOT NULL AUTO_INCREMENT,
                         `md5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'md5',
                         `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名称',
                         `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                         `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '文件状态',
                         `self` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否为私有(0:不私有，1:私有)',
                         `type` int(0) NOT NULL DEFAULT 0 COMMENT '文件类型(0：其他，1：照片)',
                         `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                         `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                         `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '软删除',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
                          `id` bigint(0) NOT NULL AUTO_INCREMENT,
                          `group_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组名',
                          `user_id` bigint(0) NOT NULL COMMENT '创建者用户ID',
                          `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源组头像',
                          `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
                          `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                          `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                          `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '软删除',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_user_role
-- ----------------------------
DROP TABLE IF EXISTS `group_user_role`;
CREATE TABLE `group_user_role`  (
                                    `group_id` bigint(0) NOT NULL COMMENT '组ID',
                                    `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                    `role_id` bigint(0) NOT NULL COMMENT '用户组角色ID',
                                    `group_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组内用户昵称',
                                    PRIMARY KEY (`user_id`, `group_id`, `role_id`) USING BTREE,
                                    UNIQUE INDEX `group_user_role_pk`(`group_id`, `user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for md5_file
-- ----------------------------
DROP TABLE IF EXISTS `md5_file`;
CREATE TABLE `md5_file`  (
                             `md5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `src` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '存放路径',
                             `object_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象存储名称',
                             `ref` int(0) NOT NULL DEFAULT 1 COMMENT '引用次数',
                             `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '软删除',
                             PRIMARY KEY (`md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'md5文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for media
-- ----------------------------
DROP TABLE IF EXISTS `media`;
CREATE TABLE `media`  (
                          `id` bigint(0) NOT NULL AUTO_INCREMENT,
                          `file_id` bigint(0) NOT NULL COMMENT '文件ID',
                          `width` int(0) NULL DEFAULT NULL,
                          `height` int(0) NULL DEFAULT NULL,
                          `mime_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                          `original_date_time` datetime(0) NULL DEFAULT NULL COMMENT '媒体拍摄时间',
                          `adcode` int(0) NULL DEFAULT NULL COMMENT '行政区域编码',
                          `longitude` double(50, 6) NULL DEFAULT NULL COMMENT '经度',
                          `latitude` double(50, 6) NULL DEFAULT NULL COMMENT '纬度',
                          `model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 218 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '媒体资源' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
