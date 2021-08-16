-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: agiles_login
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activo`
--

DROP TABLE IF EXISTS `activo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activo` (
  `id_act` varchar(20) NOT NULL,
  `ced_usu_act` varchar(10) NOT NULL,
  `id_ite_act` varchar(3) NOT NULL,
  PRIMARY KEY (`id_act`),
  KEY `ced_usu_act` (`ced_usu_act`),
  KEY `id_ite_act` (`id_ite_act`),
  CONSTRAINT `activo_ibfk_1` FOREIGN KEY (`ced_usu_act`) REFERENCES `usuario` (`ced_usu`),
  CONSTRAINT `activo_ibfk_2` FOREIGN KEY (`id_ite_act`) REFERENCES `item` (`id_ite`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activo`
--

LOCK TABLES `activo` WRITE;
/*!40000 ALTER TABLE `activo` DISABLE KEYS */;
INSERT INTO `activo` VALUES ('2','456','002'),('3','789','003'),('4','123','002'),('5','123','003'),('7861038061875','123','001');
/*!40000 ALTER TABLE `activo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `ced_adm` varchar(10) NOT NULL,
  `pas_adm` varchar(10) NOT NULL,
  `nom_adm` varchar(15) NOT NULL,
  `ape_adm` varchar(15) NOT NULL,
  `rol_adm` varchar(20) NOT NULL,
  PRIMARY KEY (`ced_adm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES ('123','123','Admin','Istrador','superadmin'),('456','456','Revi','Sor','admin');
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_proceso`
--

DROP TABLE IF EXISTS `detalle_proceso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_proceso` (
  `id_pro_det` int NOT NULL,
  `id_act_det` varchar(20) NOT NULL,
  `rev_act_det` tinyint(1) NOT NULL,
  `est_act_det` varchar(15) NOT NULL,
  `obs_act_det` text,
  `ced_adm_rev_det` varchar(10) DEFAULT NULL,
  KEY `id_pro_det` (`id_pro_det`),
  KEY `id_act_det` (`id_act_det`),
  KEY `ced_adm_rev_det` (`ced_adm_rev_det`),
  CONSTRAINT `detalle_proceso_ibfk_1` FOREIGN KEY (`id_pro_det`) REFERENCES `proceso` (`id_pro`),
  CONSTRAINT `detalle_proceso_ibfk_2` FOREIGN KEY (`id_act_det`) REFERENCES `activo` (`id_act`),
  CONSTRAINT `detalle_proceso_ibfk_3` FOREIGN KEY (`ced_adm_rev_det`) REFERENCES `administrador` (`ced_adm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_proceso`
--

LOCK TABLES `detalle_proceso` WRITE;
/*!40000 ALTER TABLE `detalle_proceso` DISABLE KEYS */;
INSERT INTO `detalle_proceso` VALUES (1,'2',1,'CORRECTO','','123'),(1,'3',0,'','',NULL),(3,'7861038061875',1,'CORRECTO','','123'),(3,'4',0,'','',NULL),(3,'5',1,'CORRECTO','','456'),(3,'3',0,'','',NULL),(2,'2',1,'CORRECTO','','123'),(2,'7861038061875',1,'OBSERVACION','Averiada','123'),(2,'4',1,'CORRECTO','','123'),(2,'5',1,'OBSERVACION','Mal estado','123'),(1,'7861038061875',1,'CORRECTO','','123'),(1,'4',1,'CORRECTO','','456'),(1,'5',1,'CORRECTO','','123'),(4,'7861038061875',1,'OBSERVACION','Mal estado','456'),(4,'4',1,'CORRECTO','','456'),(4,'5',1,'CORRECTO','','456'),(4,'2',1,'CORRECTO','','456');
/*!40000 ALTER TABLE `detalle_proceso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id_ite` varchar(3) NOT NULL,
  `nom_ite` varchar(25) NOT NULL,
  `des_ite` text,
  PRIMARY KEY (`id_ite`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES ('001','Silla','Silla de MiComisariato'),('002','Computadora','Intel i7, 16GB de RAM ...'),('003','Mesa','Mesa de MiComisariato');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proceso`
--

DROP TABLE IF EXISTS `proceso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proceso` (
  `id_pro` int NOT NULL AUTO_INCREMENT,
  `nom_pro` varchar(30) NOT NULL,
  `fec_cre_pro` date NOT NULL,
  `est_pro` varchar(12) NOT NULL,
  `ced_adm_cre_pro` varchar(10) NOT NULL,
  PRIMARY KEY (`id_pro`),
  KEY `ced_adm_cre_pro` (`ced_adm_cre_pro`),
  CONSTRAINT `proceso_ibfk_1` FOREIGN KEY (`ced_adm_cre_pro`) REFERENCES `administrador` (`ced_adm`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proceso`
--

LOCK TABLES `proceso` WRITE;
/*!40000 ALTER TABLE `proceso` DISABLE KEYS */;
INSERT INTO `proceso` VALUES (1,'Proceso 1','2021-08-12','FINALIZADO','123'),(2,'Proceso 2','2021-08-12','FINALIZADO','123'),(3,'Proceso 3','2021-08-14','INICIADO','123'),(4,'Proceso 4','2021-08-16','FINALIZADO','123');
/*!40000 ALTER TABLE `proceso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `ced_usu` varchar(10) NOT NULL,
  `nom_usu` varchar(15) NOT NULL,
  `ape_usu` varchar(15) NOT NULL,
  PRIMARY KEY (`ced_usu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('123','Richard','Carrion'),('456','Abraham','Miranda'),('789','Alejandro','Barrera');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-16 12:37:46
