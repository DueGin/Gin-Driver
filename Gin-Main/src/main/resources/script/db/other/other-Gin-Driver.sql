-- MySQL dump 10.13  Distrib 8.0.35, for macos14.0 (arm64)
--
-- Host: 127.0.0.1    Database: gin-driver
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `md5_file_id` bigint DEFAULT NULL COMMENT 'md5文件ID',
                         `file_id` bigint NOT NULL COMMENT '文件ID',
                         `width` int DEFAULT NULL,
                         `height` int DEFAULT NULL,
                         `mime_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                         `original_date_time` datetime DEFAULT NULL COMMENT '媒体拍摄时间',
                         `adcode` int DEFAULT NULL COMMENT '行政区域编码',
                         `longitude` double(50,6) DEFAULT NULL COMMENT '经度',
                         `latitude` double(50,6) DEFAULT NULL COMMENT '纬度',
                         `model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=435 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='媒体资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `md5_file`
--

DROP TABLE IF EXISTS `md5_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `md5_file` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `md5` varchar(128) NOT NULL,
                            `content_type` varchar(32) DEFAULT NULL COMMENT '文件内容类型',
                            `object_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '对象存储名称',
                            `ref` int NOT NULL DEFAULT '1' COMMENT '引用次数',
                            `src` varchar(512) NOT NULL COMMENT '存放路径',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `md5_file_md5_content_type_uindex` (`md5`,`content_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1776877594929688579 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='md5文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dustbin`
--

DROP TABLE IF EXISTS `dustbin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dustbin` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `file_id` bigint NOT NULL COMMENT '文件ID',
                           `user_id` bigint NOT NULL COMMENT '删除者ID',
                           `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
                           `type` int NOT NULL COMMENT '文件类型(0：其他，1：照片)',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='媒体资源垃圾箱';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `md5_file_id` bigint NOT NULL COMMENT 'md5文件ID',
                        `name` varchar(128) NOT NULL COMMENT '文件名称',
                        `user_id` bigint NOT NULL COMMENT '用户ID',
                        `status` tinyint NOT NULL DEFAULT '1' COMMENT '文件状态',
                        `self` tinyint NOT NULL DEFAULT '0' COMMENT '是否为私有(0:不私有，1:私有)',
                        `type` int NOT NULL DEFAULT '0' COMMENT '文件类型(0：其他，1：系统，2：照片)',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `group_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组名',
                         `user_id` bigint NOT NULL COMMENT '创建者用户ID',
                         `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资源组头像',
                         `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '简介',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_user_role`
--

DROP TABLE IF EXISTS `group_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_user_role` (
                                   `group_id` bigint NOT NULL COMMENT '组ID',
                                   `user_id` bigint NOT NULL COMMENT '用户ID',
                                   `role_id` bigint NOT NULL COMMENT '用户组角色ID',
                                   `group_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组内用户昵称',
                                   PRIMARY KEY (`user_id`,`group_id`,`role_id`) USING BTREE,
                                   UNIQUE KEY `group_user_role_pk` (`group_id`,`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-07 17:12:04
