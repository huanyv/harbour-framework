-- MySQL dump 10.13  Distrib 5.7.37, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.7.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','123456'),(2,'lisi','3333'),(3,'lisi','lisi'),(5,'user','1234'),(6,'user2','1234'),(7,'user3','1111'),(8,'user4','2222'),(9,'user5','44444');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_book`
--

DROP TABLE IF EXISTS `t_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(50) DEFAULT NULL,
  `author` varchar(10) DEFAULT NULL,
  `pubcomp` varchar(50) DEFAULT NULL,
  `pubdate` datetime DEFAULT NULL,
  `bcount` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_book`
--

LOCK TABLES `t_book` WRITE;
/*!40000 ALTER TABLE `t_book` DISABLE KEYS */;
INSERT INTO `t_book` VALUES (1,'Java从入门到放弃','张三','清华大学','2022-02-28 00:00:00',111,77.60),(2,'西游记','吴承恩','清华大学','2022-02-28 00:00:00',77,30.68),(3,'儒林外史','吴敬梓','清华大学','2022-01-02 00:00:00',88,18.80),(4,'聊斋志异','蒲松龄','清华大学','2022-01-03 00:00:00',88,21.00),(5,'史记','司马迁','清华大学','2022-01-04 00:00:00',88,278.25),(6,'资治通鉴','司马光','清华大学','2022-01-05 00:00:00',89,524.00),(7,'Java核心技术 卷I：基础知识','Cay S','机械工业出版社','2022-01-06 00:00:00',88,92.50),(8,'Java核心技术卷II：高级特性','Cay S','机械工业出版社','2022-01-07 00:00:00',88,111.20),(9,'Java多线程编程核心技术','高洪岩','清华大学','2022-01-08 00:00:00',88,47.50),(10,'诗经','孔子','清华大学','2022-01-09 00:00:00',88,44.00),(11,'唐诗三百首','蘅塘居士','清华大学','2022-01-10 00:00:00',121,49.30),(13,'西游记','吴承恩','清华大学','2022-01-12 00:00:00',88,47.50),(15,'深入理解Java虚拟机','周志明','清华大学','2022-01-14 00:00:00',88,57.90),(16,'MySQL','张三','北京大学','2022-04-04 00:00:00',100,50.80),(18,'资治通鉴','张三','清华大学','2022-02-22 00:00:00',100,50.80),(19,'cHl7z7a','神采奕奕','大彻大悟','2022-01-04 00:00:00',91341,163.25),(20,'DD7p','郑人争年','不卑不亢','2022-01-25 00:00:00',753,98.08),(21,'M3hZq','凡桃俗李','不破不立','2022-11-27 00:00:00',4790,185.88),(22,'Pr1YFBCUms','心回意转','正本清源','2022-02-17 00:00:00',3622,125.15),(23,'O0y3GH3','喜怒无常','磕头如捣','2022-02-04 00:00:00',6874,122.62),(34,'PM-1','PM-1','PM-1','2018-04-02 00:00:00',5,85.47),(35,'测试','测试','测试','2022-09-16 00:00:00',22,11.00),(36,'qqq','qqq','qqq','2022-09-16 00:00:00',12,12.00),(38,'三体','刘慈心','上路','2022-11-17 00:00:00',100,66.00),(46,'asas','asas','as','2023-01-06 00:00:00',12,21.00),(51,'大柳塔','vsdv','dvsv','2023-01-16 00:00:00',22,12.00);
/*!40000 ALTER TABLE `t_book` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-22 17:14:33
