CREATE DATABASE  IF NOT EXISTS `schooldb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `schooldb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: schooldb
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrollment` (
  `idenrollment` int(11) NOT NULL AUTO_INCREMENT,
  `lesson_idlesson` int(11) NOT NULL,
  `student_idstudent` int(11) NOT NULL,
  `grade` int(11) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`idenrollment`),
  KEY `fk_enrollment_lesson1_idx` (`lesson_idlesson`),
  KEY `fk_enrollment_student1_idx` (`student_idstudent`),
  CONSTRAINT `fk_enrollment_lesson1` FOREIGN KEY (`lesson_idlesson`) REFERENCES `lesson` (`idlesson`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_enrollment_student1` FOREIGN KEY (`student_idstudent`) REFERENCES `student` (`idstudent`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lesson` (
  `idlesson` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idlesson`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
INSERT INTO `lesson` VALUES (1,'ATL','Arxes Technologias Logismikou'),(2,'SYTH','Sygxrona Themata'),(3,'SADE','Systimata Aksiologisis'),(4,'PROG','Programming');
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prerequisite`
--

DROP TABLE IF EXISTS `prerequisite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prerequisite` (
  `lesson_idlesson` int(11) NOT NULL,
  `lesson_idlesson1` int(11) NOT NULL,
  PRIMARY KEY (`lesson_idlesson`,`lesson_idlesson1`),
  UNIQUE KEY `lesson_idlesson1_UNIQUE` (`lesson_idlesson1`),
  KEY `fk_prerequisite_lesson2_idx` (`lesson_idlesson1`),
  CONSTRAINT `fk_prerequisite_lesson1` FOREIGN KEY (`lesson_idlesson`) REFERENCES `lesson` (`idlesson`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_prerequisite_lesson2` FOREIGN KEY (`lesson_idlesson1`) REFERENCES `lesson` (`idlesson`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prerequisite`
--

LOCK TABLES `prerequisite` WRITE;
/*!40000 ALTER TABLE `prerequisite` DISABLE KEYS */;
INSERT INTO `prerequisite` VALUES (1,3),(1,4);
/*!40000 ALTER TABLE `prerequisite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `idstudent` int(11) NOT NULL,
  `AM` varchar(45) DEFAULT NULL,
  `examino` int(11) DEFAULT NULL,
  PRIMARY KEY (`idstudent`),
  KEY `fk_student_users_idx` (`idstudent`),
  CONSTRAINT `fk_student_users` FOREIGN KEY (`idstudent`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `idteacher` int(11) NOT NULL,
  `eidikotita` varchar(45) DEFAULT NULL,
  `endiaferonta` varchar(45) DEFAULT NULL,
  `tilefono` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idteacher`),
  CONSTRAINT `fk_teacher_users1` FOREIGN KEY (`idteacher`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'Software','Programming','12345'),(2,'Algebra','Mathematical Analysis','203000'),(3,'Networks ','Wireless Communications & Bluetooth','2003003');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teaches`
--

DROP TABLE IF EXISTS `teaches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teaches` (
  `idteaches` int(11) NOT NULL AUTO_INCREMENT,
  `lesson_idlesson` int(11) NOT NULL,
  `teacher_idteacher` int(11) NOT NULL,
  PRIMARY KEY (`idteaches`,`lesson_idlesson`,`teacher_idteacher`),
  KEY `fk_teaches_lesson1_idx` (`lesson_idlesson`),
  KEY `fk_teaches_teacher1_idx` (`teacher_idteacher`),
  CONSTRAINT `fk_teaches_lesson1` FOREIGN KEY (`lesson_idlesson`) REFERENCES `lesson` (`idlesson`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_teaches_teacher1` FOREIGN KEY (`teacher_idteacher`) REFERENCES `teacher` (`idteacher`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teaches`
--

LOCK TABLES `teaches` WRITE;
/*!40000 ALTER TABLE `teaches` DISABLE KEYS */;
/*!40000 ALTER TABLE `teaches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `idusers` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idusers`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'np','np123','np@teicrete.gr','Papas','Nikos'),(2,'dd','dd1234','dd@teicrete.gr','Dimitriou','Dimitris'),(3,'masa','masa1111','maria@teicrete.gr','Sakka','Maria'),(4,'foo','foo','foo@tei','Fotilas','Fotis'),(5,'al','lall233','al@teicrete.gr','Antoniadou','Loukia'),(6,'sma','stha4343','sm@aaa.de','Stathopoulou','Maria');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'schooldb'
--

--
-- Dumping routines for database 'schooldb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-08  0:37:40
