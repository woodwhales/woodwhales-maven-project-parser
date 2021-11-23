-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.27 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 woodwhales_maven_project_parser 的数据库结构
DROP DATABASE IF EXISTS `woodwhales_maven_project_parser`;
CREATE DATABASE IF NOT EXISTS `woodwhales_maven_project_parser` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `woodwhales_maven_project_parser`;

-- 导出  表 woodwhales_maven_project_parser.dependency_info 结构
DROP TABLE IF EXISTS `dependency_info`;
CREATE TABLE IF NOT EXISTS `dependency_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '项目根目录hash值',
  `project_info_id` bigint(20) unsigned NOT NULL COMMENT '工程信息表主键',
  `group_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'groupId',
  `artifact_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'artifactId',
  `version` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'version',
  `scope` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'scope',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'type',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `project_info_id` (`project_info_id`),
  KEY `root_project_key` (`root_project_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='依赖信息表';

-- 数据导出被取消选择。

-- 导出  表 woodwhales_maven_project_parser.dependency_management_info 结构
DROP TABLE IF EXISTS `dependency_management_info`;
CREATE TABLE IF NOT EXISTS `dependency_management_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '项目根目录hash值',
  `project_info_id` bigint(20) unsigned NOT NULL COMMENT '工程信息表主键',
  `group_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'groupId',
  `artifact_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'artifactId',
  `version` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'version',
  `scope` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'scope',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'type',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `project_info_id` (`project_info_id`),
  KEY `root_project_key` (`root_project_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='dependencyManagement 信息表';

-- 数据导出被取消选择。

-- 导出  表 woodwhales_maven_project_parser.exclusion_info 结构
DROP TABLE IF EXISTS `exclusion_info`;
CREATE TABLE IF NOT EXISTS `exclusion_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '项目根目录hash值',
  `dependency_info_id` bigint(20) unsigned NOT NULL COMMENT '工程信息表主键',
  `group_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'groupId',
  `artifact_id` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT 'artifactId',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `dependency_info_id` (`dependency_info_id`),
  KEY `root_project_key` (`root_project_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='依赖排除信息表';

-- 数据导出被取消选择。

-- 导出  表 woodwhales_maven_project_parser.module_info 结构
DROP TABLE IF EXISTS `module_info`;
CREATE TABLE IF NOT EXISTS `module_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '项目根目录hash值',
  `project_info_id` bigint(20) unsigned NOT NULL COMMENT '工程信息表主键',
  `module` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'module',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `project_info_id` (`project_info_id`),
  KEY `root_file_path_key` (`root_project_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模块信息表';

-- 数据导出被取消选择。

-- 导出  表 woodwhales_maven_project_parser.project_info 结构
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE IF NOT EXISTS `project_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) NOT NULL COMMENT '项目根目录hash值',
  `project_alias` varchar(200) NOT NULL COMMENT '项目别名',
  `absolute_file_path` varchar(1024) DEFAULT NULL COMMENT '文件绝对路径',
  `group_id` varchar(200) DEFAULT NULL COMMENT 'groupId',
  `artifact_id` varchar(200) DEFAULT NULL COMMENT 'artifactId',
  `version` varchar(200) DEFAULT NULL COMMENT 'version',
  `packaging` varchar(200) DEFAULT NULL COMMENT 'packaging',
  `name` varchar(500) DEFAULT NULL COMMENT 'name',
  `description` varchar(1024) DEFAULT NULL COMMENT 'description',
  `relative_file_path` varchar(1024) DEFAULT NULL COMMENT '文件相对路径',
  `parent_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '父工程标识：0-否，1-是',
  `root_project_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否为项目跟目录：0-否，1-是',
  `parent_group_id` varchar(200) DEFAULT NULL COMMENT 'parent-groupId',
  `parent_artifact_id` varchar(200) DEFAULT NULL COMMENT 'parent-artifactId',
  `parent_version` varchar(200) DEFAULT NULL COMMENT 'parent-version',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `root_file_path_key` (`root_project_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='maven工程信息表';

-- 数据导出被取消选择。

-- 导出  表 woodwhales_maven_project_parser.properties_info 结构
DROP TABLE IF EXISTS `properties_info`;
CREATE TABLE IF NOT EXISTS `properties_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `root_project_key` varchar(200) NOT NULL COMMENT '项目根目录hash值',
  `project_info_id` bigint(20) unsigned NOT NULL COMMENT '工程信息表主键',
  `prop_key` varchar(1024) NOT NULL COMMENT 'key',
  `prop_value` text NOT NULL COMMENT 'value',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `project_info_id` (`project_info_id`),
  KEY `root_file_path_key` (`root_project_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置信息表';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
