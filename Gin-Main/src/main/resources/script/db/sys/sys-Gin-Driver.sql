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

use `gin-driver`;

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
                        `create_time` datetime NOT NULL DEFAULT (now()) COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                        `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `unique_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN','系统超级管理员',1,'2021-09-07 14:48:43','2024-02-05 07:03:58',1);
INSERT INTO `role` VALUES (2,'ROLE_USER','系统普通用户',1,'2021-09-07 14:58:35','2024-02-05 07:03:58',1);
INSERT INTO `role` VALUES (3,'ROLE_VISITOR','系统游客',1,'2024-02-05 07:03:58','2024-02-06 12:42:42',1);
INSERT INTO `role` VALUES (4,'ROLE_DISABLED','系统用户禁用',1,'2023-12-03 07:24:01','2024-02-06 12:42:42',1);
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
                            `label` varchar(64) NOT NULL COMMENT '字典名称',
                            `value` int NOT NULL COMMENT '字典值',
                            `status` tinyint NOT NULL DEFAULT '1' COMMENT '字典状态(0:禁用，1:启用)',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `sys_dict_dict_type_index` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (2,'menu','group',1,1,'组菜单','2023-12-24 14:45:47','2024-02-01 15:25:05');
INSERT INTO `sys_dict` VALUES (3,'menu','media',2,1,'媒体菜单','2023-12-24 14:45:47','2024-02-01 15:25:12');
INSERT INTO `sys_dict` VALUES (5,'menu','startMenu',3,1,'启动台菜单','2024-01-20 02:40:40','2024-02-01 15:25:24');
INSERT INTO `sys_dict` VALUES (9,'menu','sys',4,1,'系统管理','2024-01-30 14:10:03','2024-01-30 14:10:03');
INSERT INTO `sys_dict` VALUES (10,'menu','common',5,1,'系统常用菜单','2024-01-31 13:27:33','2024-02-01 15:25:48');
INSERT INTO `sys_dict` VALUES (11,'menu','other',6,1,'其他菜单','2024-02-01 15:25:59','2024-02-01 15:25:59');
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
                                 `code` varchar(10) NOT NULL COMMENT '字段类型编码',
                                 `name` varchar(64) NOT NULL COMMENT '字典名称',
                                 `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `sys_dict_type_code_uindex` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'menu','菜单',1,'','2024-01-14 12:54:12','2024-01-20 03:24:34');
INSERT INTO `sys_dict_type` VALUES (3,'qq','dd',1,NULL,'2024-01-21 16:41:39','2024-01-21 16:41:39');
INSERT INTO `sys_dict_type` VALUES (4,'dsd','saa',0,NULL,'2024-01-21 16:42:25','2024-01-21 16:42:25');
INSERT INTO `sys_dict_type` VALUES (9,'f f','f',1,NULL,'2024-01-21 17:30:12','2024-01-21 17:30:12');
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
                        `user_account` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录用户名',
                        `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
                        `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                        `avatar` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
                        `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话号码',
                        `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1754871140588908545 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin','$2a$10$3g0ihkE.PoZQOs4xK3mQKeYEQtOFSe7dzqarhyiSth9lJCC2ZKvBa','Rin.jpg','17670930119','951930136@qq.com','2023-11-29 16:15:33','2024-02-06 13:58:16',0);
INSERT INTO `user` VALUES (2,'user','user','$2a$10$9kH.xMSu8RV4QLyMDUEi0u8VzRO0P6g8naes8I5BHujZInaCwv3Hy',NULL,'17670930114','1185232242@qq.com','2023-11-29 16:15:33','2024-02-06 13:58:16',0);
INSERT INTO `user` VALUES (3,'visitor','visitor','$2a$10$t62Jgr8XeabCXSzYto/2gOuxl4DdXB7K.qGuBurRdpGn8ViL7tlAy',NULL,'17670930113','a_176709301555@163.com','2023-11-29 16:15:33','2024-02-06 13:58:16',0);
INSERT INTO `user` VALUES (1754143408636805120,'hhh','hhh','$2a$10$.3CRwd68xSBfp/LBhhZ5vuYEhNo.YtD7J1W1lQGuLV85toyUHFtnK',NULL,NULL,NULL,'2024-02-04 14:02:34','2024-02-06 13:58:16',0);
INSERT INTO `user` VALUES (1754144201012088832,'asdf','asdf333','$2a$10$KZbralfOwjO2SJmQFYfp5eOPlFMO.0olgmllbQblwy9v7nH25qAgq','20230409203930_e2985b7ee9d5495a9e669da01661b2fd.jpg',NULL,NULL,'2024-02-04 14:05:42','2024-02-06 14:14:37',0);
INSERT INTO `user` VALUES (1754357804558409728,'干饭菌','干饭菌','$2a$10$YcCOtOhITCRj8/NBMipK1OQzkeIil3UGL7hkWUgKQ1D/JeK8jQfdS',NULL,NULL,NULL,'2024-02-05 04:14:30','2024-02-06 13:58:23',0);
INSERT INTO `user` VALUES (1754871140588908544,'dio','nmd','$2a$10$6LSvBaZYymLiQrXMy6cg2.keSIw3a8ZRazAzDFNWex9kaP2G.R1zO','八卦图_71c82f4d8c8c4449bd294cec93501b7b.ico',NULL,NULL,'2024-02-06 14:14:18','2024-02-06 14:49:04',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1,'2021-09-07 14:50:17','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (2,2,2,'2021-09-07 15:05:01','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (3,3,3,'2021-09-07 15:05:29','2023-11-29 16:16:40');
INSERT INTO `user_role` VALUES (6,1754143408636805120,2,'2024-02-04 14:02:34','2024-02-04 14:02:34');
INSERT INTO `user_role` VALUES (7,1754144201012088832,2,'2024-02-04 14:05:42','2024-02-04 14:05:42');
INSERT INTO `user_role` VALUES (8,1754357804558409728,2,'2024-02-05 04:14:30','2024-02-05 04:14:30');
INSERT INTO `user_role` VALUES (9,1754871140588908544,2,'2024-02-06 14:14:18','2024-02-06 14:14:18');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
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
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(8) NOT NULL COMMENT '菜单名称',
                        `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父菜单ID',
                        `path` varchar(64) NOT NULL COMMENT '路由地址',
                        `component_path` varchar(64) DEFAULT NULL COMMENT '组件路径',
                        `component_name` varchar(32) NOT NULL COMMENT '组件名称',
                        `layout_component_id` bigint DEFAULT NULL COMMENT '布局组件ID',
                        `icon` varchar(128) DEFAULT NULL COMMENT '菜单图标',
                        `role_id` bigint DEFAULT NULL COMMENT '角色权限标识',
                        `hidden` tinyint NOT NULL DEFAULT '0' COMMENT '是否隐藏(1:隐藏，0:不隐藏)',
                        `status` tinyint NOT NULL DEFAULT '0' COMMENT '菜单状态（1正常 0停用）',
                        `type` bigint NOT NULL COMMENT '菜单类型(字典ID)',
                        `sorted` tinyint NOT NULL DEFAULT '0' COMMENT '排序',
                        `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'组信息',0,'/group/info','/group/info/index','GroupDetail',3,NULL,NULL,0,1,2,0,NULL,'2023-12-24 20:30:21','2024-01-28 11:15:20');
INSERT INTO `menu` VALUES (2,'组管理',0,'/group/manager','/group/manager/index','GroupManager',3,NULL,NULL,0,1,2,1,NULL,'2023-12-24 12:45:12','2024-01-28 10:56:08');
INSERT INTO `menu` VALUES (3,'首页',0,'/group/home','/group/index','GroupHome',2,NULL,NULL,0,0,2,0,NULL,'2023-12-29 15:13:08','2024-01-27 16:21:05');
INSERT INTO `menu` VALUES (8,'组资源',0,'/group/resource','/group/resource/index','GroupResource',3,NULL,NULL,0,1,2,2,NULL,'2023-12-24 12:38:53','2024-01-27 16:21:05');
INSERT INTO `menu` VALUES (9,'首页',0,'/media/home','/media/index','MediaHome',3,'tdesign:home',2,0,0,3,0,NULL,'2023-12-24 12:38:53','2024-02-04 12:10:56');
INSERT INTO `menu` VALUES (10,'全部',0,'/media/all','/media/all/index','MediaAll',3,'tabler:photo',2,0,1,3,1,NULL,'2023-12-24 12:38:54','2024-02-03 13:51:00');
INSERT INTO `menu` VALUES (11,'地点',0,'/media/place','/media/place/index','MediaPlace',3,'ep:place',2,0,1,3,2,NULL,'2023-12-24 12:38:54','2024-02-03 13:49:58');
INSERT INTO `menu` VALUES (12,'分类',0,'/media/classify','','MediaClassify',3,'mingcute:classify-add-2-line',2,0,1,3,3,NULL,'2023-12-24 12:38:54','2024-01-31 12:45:13');
INSERT INTO `menu` VALUES (13,'媒体',0,'/media/all',NULL,'MediaHome',NULL,'tabler:photo',2,0,1,5,0,NULL,'2024-01-29 14:13:45','2024-02-05 03:56:17');
INSERT INTO `menu` VALUES (14,'菜单管理',0,'/sys/menu',NULL,'MenuManager',NULL,'solar:menu-dots-bold',1,0,1,5,0,NULL,'2024-01-30 12:38:30','2024-02-06 11:49:03');
INSERT INTO `menu` VALUES (15,'字典类型',0,'/sys/dictType',NULL,'DictTypeManager',NULL,'streamline:dictionary-language-book',1,0,1,5,0,NULL,'2024-01-30 12:39:14','2024-02-05 03:54:05');
INSERT INTO `menu` VALUES (16,'分类详情',12,'/media/classify/list','/media/classify/MediaClassifyList','MediaClassifyList',3,NULL,2,1,1,3,0,NULL,'2024-01-30 13:16:22','2024-01-31 12:48:32');
INSERT INTO `menu` VALUES (18,'日期',12,'/media/classify/date','/media/classify/classifyDate/index','ClassifyDate',3,NULL,2,0,1,3,0,NULL,'2024-01-31 12:22:03','2024-01-31 12:32:27');
INSERT INTO `menu` VALUES (19,'地点',12,'/media/classify/classifyPlace','/media/classify/classifyPlace/index','ClassifyPlace',3,NULL,2,0,1,3,0,NULL,'2024-01-31 12:22:58','2024-02-06 12:01:55');
INSERT INTO `menu` VALUES (20,'首页',0,'/index','/index','Index',2,'pixelarticons:home',2,1,1,10,0,NULL,'2024-01-31 13:28:41','2024-02-06 11:50:31');
INSERT INTO `menu` VALUES (21,'用户管理',0,'/sys/user/manager','/sys/user/userManage','UserManage',2,'fa6-solid:user',1,0,1,9,0,NULL,'2024-02-01 12:33:54','2024-02-05 03:50:37');
INSERT INTO `menu` VALUES (22,'组管理',0,'/sys/group/manager','/sys/group/GroupManager','GroupManager',2,'flowbite:users-group-outline',2,0,1,9,0,NULL,'2024-02-01 12:35:48','2024-02-06 12:25:36');
INSERT INTO `menu` VALUES (24,'布局组件',0,'/sys/layoutComponent',NULL,'LayoutComponent',NULL,'gala:menu-left',1,0,1,5,0,NULL,'2024-02-01 12:51:19','2024-02-06 12:26:04');
INSERT INTO `menu` VALUES (26,'用户中心',0,'/user/center','/user/center/index','UserCenter',2,'mingcute:user-2-line',2,0,1,10,0,NULL,'2024-02-01 15:27:43','2024-02-06 11:49:56');
INSERT INTO `menu` VALUES (27,'垃圾箱',0,'/media/dustbin','/media/dustbin/index','MediaDustbin',3,'mi:delete',2,0,1,3,0,NULL,'2024-02-03 12:08:21','2024-02-03 12:08:21');
INSERT INTO `menu` VALUES (28,'字典类型',0,'/sys/dictType','/sys/dictType/index','DictType',2,'streamline:dictionary-language-book-solid',1,0,1,9,0,NULL,'2024-02-06 11:22:12','2024-02-06 11:36:09');
INSERT INTO `menu` VALUES (29,'字典',0,'/sys/dict','/sys/dict/index','Dict',2,'arcticons:dictionary',1,1,1,9,0,NULL,'2024-02-06 11:37:03','2024-02-06 12:23:43');
INSERT INTO `menu` VALUES (30,'布局组件',0,'/sys/layoutComponent','/sys/layoutComponent/index','LayoutComponent',2,'gala:menu-left',1,0,1,9,0,NULL,'2024-02-06 11:38:43','2024-02-06 12:24:17');
INSERT INTO `menu` VALUES (31,'菜单',0,'/sys/menu','/sys/menu/index','Menu',2,'solar:menu-dots-bold',1,0,1,9,0,NULL,'2024-02-06 11:47:37','2024-02-06 11:48:56');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `layout_component`
--

DROP TABLE IF EXISTS `layout_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `layout_component` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `name` varchar(32) NOT NULL COMMENT '组件名称',
                                    `path` varchar(50) NOT NULL COMMENT '组件路径',
                                    `has_slot` tinyint NOT NULL DEFAULT '0' COMMENT '是否存在组件插槽，0:不存在，1:存在',
                                    `has_router` tinyint NOT NULL DEFAULT '1' COMMENT '是否存在路由插槽，0:不存在，1:存在',
                                    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组件布局';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `layout_component`
--

LOCK TABLES `layout_component` WRITE;
/*!40000 ALTER TABLE `layout_component` DISABLE KEYS */;
INSERT INTO `layout_component` VALUES (2,'HeaderLayout','/headerLayout/HeaderLayout',1,1,'头部布局','2024-01-27 16:06:22','2024-01-27 16:06:22');
INSERT INTO `layout_component` VALUES (3,'HeaderAndSiderLayout','/headerAndSiderLayout/HeaderAndSiderLayout',0,1,'头部+侧边栏布局','2024-01-27 16:07:27','2024-01-27 16:07:27');
INSERT INTO `layout_component` VALUES (4,'SiderLayout','/siderLayout/SiderLayout',1,1,'侧边栏布局','2024-01-27 16:09:21','2024-01-28 01:54:26');
/*!40000 ALTER TABLE `layout_component` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-06 22:54:25
