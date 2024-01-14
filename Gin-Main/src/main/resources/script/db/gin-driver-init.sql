-- MySQL dump 10.13  Distrib 8.0.35, for macos14.0 (arm64)
-- 2024.01.14 21:38
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
-- Table structure for table `dustbin`
--

DROP TABLE IF EXISTS `dustbin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dustbin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `media_id` bigint NOT NULL COMMENT '媒体ID',
  `user_id` bigint NOT NULL COMMENT '删除者ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `type` int NOT NULL COMMENT '文件类型(1：图片，2：视频，3：电影，4：其他)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='媒体资源垃圾箱';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dustbin`
--

LOCK TABLES `dustbin` WRITE;
/*!40000 ALTER TABLE `dustbin` DISABLE KEYS */;
INSERT INTO `dustbin` VALUES (1,2,1,'file_193de7ebd97f4a4e91a64051782b5f6a',3,'2023-12-31 16:08:08','2023-12-31 16:08:08');
INSERT INTO `dustbin` VALUES (2,1,1,'file_8afb5ab1ad3b4022925dffa09f72bf4a',3,'2023-12-31 16:08:08','2023-12-31 16:08:08');
INSERT INTO `dustbin` VALUES (3,13,1,'file_8c05286035f84f44acbe5e9af7e78808',3,'2024-01-12 13:38:09','2024-01-12 13:38:09');
INSERT INTO `dustbin` VALUES (4,12,1,'file_d35a371e5dc942ab90641630d6b3f324',3,'2024-01-12 13:38:09','2024-01-12 13:38:09');
INSERT INTO `dustbin` VALUES (5,11,1,'file_9f786376361345b8b60dfb94ee225ed1',3,'2024-01-12 13:38:09','2024-01-12 13:38:09');
/*!40000 ALTER TABLE `dustbin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(20) NOT NULL COMMENT '组名',
  `user_id` bigint NOT NULL COMMENT '创建者用户ID',
  `avatar` varchar(255) DEFAULT NULL COMMENT '资源组头像',
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (7,'阿甘正传',1730617494076522496,NULL,'hhhhhjnilm','2023-12-01 16:42:44','2023-12-30 05:23:16',0);
INSERT INTO `group` VALUES (27,'fsda',1730617494076522496,NULL,'fsadf','2023-12-19 16:30:01','2023-12-19 16:30:01',0);
INSERT INTO `group` VALUES (28,'sfdsaf',1730617494076522496,NULL,'dfas','2023-12-19 16:31:04','2023-12-19 16:31:04',0);
INSERT INTO `group` VALUES (29,'fsda',1730617494076522496,NULL,'wq','2023-12-19 16:40:05','2023-12-19 16:40:05',0);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`user_id`,`group_id`,`role_id`),
  UNIQUE KEY `group_user_role_pk` (`group_id`,`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_user_role`
--

LOCK TABLES `group_user_role` WRITE;
/*!40000 ALTER TABLE `group_user_role` DISABLE KEYS */;
INSERT INTO `group_user_role` VALUES (7,1,5,'admin');
INSERT INTO `group_user_role` VALUES (7,1730617494076522496,4,'test');
INSERT INTO `group_user_role` VALUES (27,1730617494076522496,5,'test1');
INSERT INTO `group_user_role` VALUES (28,1730617494076522496,5,'test1');
INSERT INTO `group_user_role` VALUES (29,1730617494076522496,5,'test1');
/*!40000 ALTER TABLE `group_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '上传者ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `type` int DEFAULT NULL COMMENT '文件类型1：图片，2：视频，3：电影，4：其他',
  `src` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `url` varchar(255) DEFAULT NULL COMMENT 'minio地址',
  `group_id` bigint NOT NULL DEFAULT '0' COMMENT '组ID',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '文件状态',
  `self` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为私有(0:不私有，1:私有)',
  `mime_type` varchar(32) DEFAULT NULL COMMENT '媒体格式',
  `media_date` date DEFAULT NULL COMMENT '媒体创建时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='媒体资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
INSERT INTO `media` VALUES (1,1,'file_8afb5ab1ad3b4022925dffa09f72bf4a',3,NULL,NULL,0,1,0,'image/png',NULL,'2023-12-31 07:41:03','2023-12-31 16:08:08',1);
INSERT INTO `media` VALUES (2,1,'file_193de7ebd97f4a4e91a64051782b5f6a',3,NULL,NULL,0,1,0,'image/png',NULL,'2023-12-31 08:14:59','2023-12-31 16:08:08',1);
INSERT INTO `media` VALUES (3,1,'file_557aaf1ba53f45499d1e2c335dc4829d',3,NULL,NULL,0,1,0,'image/png','2022-01-01','2023-12-31 08:15:00','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (4,1,'file_dcaae7df511b4828b5d267f4db145303',NULL,NULL,NULL,0,1,0,'image/jpeg','2024-01-16','2024-01-01 10:02:35','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (5,1,'file_495597a4c1b0431fb15058c62f5be3ee',NULL,NULL,NULL,0,1,0,'image/jpeg','2024-01-15','2024-01-01 10:02:36','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (6,1,'file_ea764cabd5a743a6888c84bd96c6294b',NULL,NULL,NULL,0,1,0,'image/jpeg','2022-01-13','2024-01-01 10:02:37','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (7,1,'file_253bd7f0cd0743c59c1cd97e93988e52',NULL,NULL,NULL,0,1,0,'image/jpeg','2023-04-18','2024-01-01 10:06:13','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (8,1,'file_e6133f55deb14ea6b3d9427522993cd0',NULL,NULL,NULL,0,1,0,'image/jpeg','2023-02-07','2024-01-01 10:25:07','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (9,1,'file_e3c60f072ad547c497d2ea148304dd54',NULL,NULL,NULL,0,1,0,'image/jpeg','2023-07-24','2024-01-01 10:26:46','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (10,1,'file_44883c21855942c4b1295bbec0ed3038',NULL,NULL,NULL,0,1,0,'image/jpeg','2024-01-12','2024-01-01 10:26:50','2024-01-12 14:32:18',0);
INSERT INTO `media` VALUES (11,1,'file_9f786376361345b8b60dfb94ee225ed1',NULL,NULL,NULL,0,1,0,'image/jpeg',NULL,'2024-01-12 04:35:20','2024-01-12 13:38:09',1);
INSERT INTO `media` VALUES (12,1,'file_d35a371e5dc942ab90641630d6b3f324',NULL,NULL,NULL,0,1,0,'image/jpeg',NULL,'2024-01-12 04:47:50','2024-01-12 13:38:09',1);
INSERT INTO `media` VALUES (13,1,'file_8c05286035f84f44acbe5e9af7e78808',NULL,NULL,NULL,0,1,0,'image/jpeg',NULL,'2024-01-12 05:02:10','2024-01-12 13:38:09',1);
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_exif`
--

DROP TABLE IF EXISTS `media_exif`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_exif` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `media_id` bigint NOT NULL COMMENT '文件id',
  `width` int DEFAULT NULL,
  `height` int DEFAULT NULL,
  `mime_type` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `model` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='媒体资源信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_exif`
--

LOCK TABLES `media_exif` WRITE;
/*!40000 ALTER TABLE `media_exif` DISABLE KEYS */;
INSERT INTO `media_exif` VALUES (1,13,1920,1080,'image/jpeg','2015-12-09 15:08:53',NULL);
/*!40000 ALTER TABLE `media_exif` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(8) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父菜单ID',
  `path` varchar(32) NOT NULL DEFAULT '' COMMENT '路由地址',
  `component_path` varchar(64) NOT NULL COMMENT '组件路径',
  `component_name` varchar(32) NOT NULL COMMENT '组件名称',
  `layout_component` varchar(64) DEFAULT NULL COMMENT '布局组件',
  `slot_or_router` tinyint DEFAULT NULL COMMENT '使用插槽还是路由(0：插槽，1：路由)',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `perms` varchar(16) DEFAULT NULL COMMENT '权限标识',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '菜单状态（1正常 0停用）',
  `type` tinyint NOT NULL COMMENT '菜单类型(0:启动台菜单，1:组资源菜单，2:媒体管理菜单，3:其他)',
  `sorted` tinyint NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'组信息',0,'/group/info','/group/info/index','GroupDetail','HeaderAndSiderLayout',NULL,NULL,NULL,1,1,0,'2023-12-24 20:30:21','2023-12-29 15:35:39',NULL);
INSERT INTO `menu` VALUES (2,'组管理',0,'/group/manager','/group/manager/index','GroupManager','HeaderAndSiderLayout',NULL,NULL,NULL,1,1,1,'2023-12-24 12:45:12','2023-12-29 15:15:47',NULL);
INSERT INTO `menu` VALUES (3,'首页',0,'/group/home','/group/index','GroupHome','HeaderLayout',1,NULL,NULL,0,3,0,'2023-12-29 15:13:08','2023-12-29 15:16:09',NULL);
INSERT INTO `menu` VALUES (8,'组资源',0,'/group/resource','/group/resource/index','GroupResource','HeaderAndSiderLayout',NULL,NULL,NULL,1,1,2,'2023-12-24 12:38:53','2023-12-29 15:15:47',NULL);
INSERT INTO `menu` VALUES (9,'首页',0,'/media/home','/media/index','MediaHome','HeaderAndSiderLayout',NULL,'tdesign:home',NULL,1,2,0,'2023-12-24 12:38:53','2023-12-29 15:13:59',NULL);
INSERT INTO `menu` VALUES (10,'全部',0,'/media/mediaAll','/media/MediaAll','MediaAll','HeaderAndSiderLayout',NULL,'tabler:photo',NULL,1,2,1,'2023-12-24 12:38:54','2023-12-29 15:13:59',NULL);
INSERT INTO `menu` VALUES (11,'地点',0,'/media/place','/media/MediaPlace','MediaPlace','HeaderAndSiderLayout',NULL,'ep:place',NULL,1,2,2,'2023-12-24 12:38:54','2023-12-29 15:13:59',NULL);
INSERT INTO `menu` VALUES (12,'分类',0,'/media/classify','/media/classify/MediaClassifyList','MediaClassify','HeaderAndSiderLayout',NULL,'mingcute:classify-add-2-line',NULL,1,2,3,'2023-12-24 12:38:54','2023-12-29 15:13:59',NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限标识符',
  `description` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'sys:user_manager','用户管理','2023-11-29 16:18:30','2023-12-03 07:56:06');
INSERT INTO `permission` VALUES (2,'sys:role_manager','角色管理','2023-11-29 16:18:30','2023-12-03 07:56:27');
INSERT INTO `permission` VALUES (3,'sys:monitor','系统监控管理','2023-11-29 16:18:30','2023-12-03 07:57:22');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '描述信息',
  `type` tinyint NOT NULL COMMENT '类型（1: 系统角色，2: 组角色）',
  `create_time` datetime DEFAULT (now()) COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN','超级管理员',1,'2021-09-07 14:48:43','2023-12-03 07:20:45',1);
INSERT INTO `role` VALUES (2,'ROLE_USER','普通用户',1,'2021-09-07 14:58:35','2023-12-03 07:20:47',1);
INSERT INTO `role` VALUES (3,'ROLE_DISABLED','系统用户禁用',1,'2023-12-03 07:24:01','2023-12-03 07:54:51',1);
INSERT INTO `role` VALUES (5,'ROLE_GROUP_ADMIN','组管理员',2,'2023-12-01 16:10:26','2023-12-03 09:52:54',1);
INSERT INTO `role` VALUES (6,'ROLE_GROUP_USER','组普通用户',2,'2023-12-01 16:12:57','2023-12-03 09:52:52',1);
INSERT INTO `role` VALUES (7,'ROLE_GROUP_VISITOR','组只读用户',2,'2023-12-01 16:14:44','2023-12-03 09:52:50',1);
INSERT INTO `role` VALUES (8,'ROLE_GROUP_DISABLED','组用户禁用',2,'2023-12-03 07:21:38','2023-12-03 09:52:48',1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1,1);
INSERT INTO `role_permission` VALUES (2,1,2);
INSERT INTO `role_permission` VALUES (3,1,3);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type` varchar(64) NOT NULL COMMENT '字典类型',
  `label` varchar(64) NOT NULL,
  `value` int NOT NULL,
  `status` tinyint NOT NULL COMMENT '字典状态(0:禁用，1:启用)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (2,'menu','group',1,1,NULL,'2023-12-24 14:45:47','2023-12-24 14:52:52');
INSERT INTO `sys_dict` VALUES (3,'menu','media',2,1,NULL,'2023-12-24 14:45:47','2023-12-24 14:52:52');
INSERT INTO `sys_dict` VALUES (4,'menu','other',3,1,NULL,'2023-12-30 07:06:45','2023-12-30 07:06:45');
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(64) NOT NULL COMMENT '字典名称',
  `status` tinyint DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'menu',1,'菜单','2024-01-14 12:54:12','2024-01-14 12:54:12');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(8) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `path` varchar(128) DEFAULT '' COMMENT '路由地址',
  `component` varchar(128) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(32) DEFAULT '#' COMMENT '菜单图标',
  `perms` varchar(64) DEFAULT NULL COMMENT '权限标识',
  `status` tinyint DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电话号码',
  `email` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮件',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1730617494076522497 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$3g0ihkE.PoZQOs4xK3mQKeYEQtOFSe7dzqarhyiSth9lJCC2ZKvBa','17670930119','951930136@qq.com','2023-11-29 16:15:33','2023-11-29 16:15:33',0);
INSERT INTO `user` VALUES (2,'user','$2a$10$9kH.xMSu8RV4QLyMDUEi0u8VzRO0P6g8naes8I5BHujZInaCwv3Hy','17670930114','1185232242@qq.com','2023-11-29 16:15:33','2023-11-29 16:15:33',0);
INSERT INTO `user` VALUES (3,'visitor','$2a$10$t62Jgr8XeabCXSzYto/2gOuxl4DdXB7K.qGuBurRdpGn8ViL7tlAy','17670930113','a_176709301555@163.com','2023-11-29 16:15:33','2023-11-29 16:15:33',0);
INSERT INTO `user` VALUES (1730617494076522496,'test1','$2a$10$2GhCGAmTbgfyIpIYN6CTgOunVIx.tD9gHtCCAZGfN.tEFriygm5R2',NULL,NULL,'2023-12-01 15:58:58','2023-12-01 15:58:58',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户表id',
  `role_id` bigint NOT NULL COMMENT '角色表id',
  `create_time` datetime DEFAULT (now()) COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1,'2021-09-07 14:50:17','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (2,2,2,'2021-09-07 15:05:01','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (3,3,3,'2021-09-07 15:05:29','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (5,1730617494076522496,2,'2023-12-01 15:58:58','2023-12-01 15:58:58');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-14 21:38:15
