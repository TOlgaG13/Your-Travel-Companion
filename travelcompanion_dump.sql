 SET NAMES utf8mb4;
 SET TIME_ZONE='+00:00';
 SET UNIQUE_CHECKS=0 ;
 SET FOREIGN_KEY_CHECKS=0 ;
 SET SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
 SET SQL_NOTES=0 ;

DROP TABLE IF EXISTS `companions`;
CREATE TABLE `companions` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `created_at` datetime(6) DEFAULT NULL,
                              `trip_id` bigint DEFAULT NULL,
                              `user_id` bigint DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FKr2ckvbp5qo9gl4r0aq9pqn52b` (`trip_id`),
                              KEY `FKj4b9yynoel66kvsx17r02r63l` (`user_id`),
                              CONSTRAINT `FKj4b9yynoel66kvsx17r02r63l` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                              CONSTRAINT `FKr2ckvbp5qo9gl4r0aq9pqn52b` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `companions` VALUES
                             (62,NULL,26,1),(78,NULL,31,1),(81,'2025-04-20 11:54:39.237807',31,4),
                             (82,NULL,32,4),(86,'2025-04-20 11:59:50.231028',26,12),(87,'2025-04-20 11:59:52.400955',31,12),
                             (88,'2025-04-20 11:59:54.190794',32,12),(90,NULL,34,12),(91,'2025-04-20 12:39:56.857849',34,1),
                             (93,NULL,35,5),(94,'2025-04-23 14:05:43.193324',34,5),(95,'2025-04-23 14:05:50.027305',32,5);

DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `countries` VALUES
                            (1,'France'),(2,'Italy'),(3,'Germany'),(4,'Spain'),(5,'Poland'),
                            (6,'Ukraine'),(7,'USA'),(8,'Canada'),(9,'Japan'),(10,'Australia');

DROP TABLE IF EXISTS `trips`;
CREATE TABLE `trips` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `created` datetime(6) DEFAULT NULL,
                         `description` text,
                         `destination` varchar(255) DEFAULT NULL,
                         `end_date` date DEFAULT NULL,
                         `start_date` date DEFAULT NULL,
                         `country_id` bigint DEFAULT NULL,
                         `user_id` bigint DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FKitftb1gqfe4bgp6u8ewfq1vfe` (`country_id`),
                         KEY `FK8wb14dx6ed0bpp3planbay88u` (`user_id`),
                         CONSTRAINT `FK8wb14dx6ed0bpp3planbay88u` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                         CONSTRAINT `FKitftb1gqfe4bgp6u8ewfq1vfe` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `trips` VALUES
                        (26,'2025-04-19 10:02:40.102263','Trip Description','City A','2025-10-10','2025-09-09',9,1),
                        (31,'2025-04-20 11:53:03.341930','Example','Dnipro','2025-06-10','2025-06-06',6,1),
                        (32,'2025-04-20 11:55:00.392144','Text','Berlin','2025-08-19','2025-08-08',3,4),
                        (34,'2025-04-20 12:01:05.147269','Hello world','Milan','2025-09-09','2025-08-08',2,12),
                        (35,'2025-04-23 14:05:28.039603','Paris!','Paris','2025-09-18','2025-09-09',1,5);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `address` varchar(255) DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `login` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `phone` varchar(255) DEFAULT NULL,
                         `role` enum('ADMIN','USER') DEFAULT NULL,
                         `time` datetime(6) DEFAULT NULL,
                         `type` enum('FORM','GOOGLE') DEFAULT NULL,
                         `active` bit(1) NOT NULL,
                         `blocked` bit(1) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users` VALUES
                        (1,'12345','olgapracticeprofile@gmail.com','olgapracticeprofile','pass1','1234','USER','2025-04-01 17:06:32.991591','GOOGLE',b'0',b'0'),
                        (4,'Test Street 14','test@example.com','TestUser','pass2','111-112','USER','2025-04-08 17:33:10.000000','FORM',b'0',b'0'),
                        (5,'dfsg542','admin@gmail.com','admin','pass3','9798','ADMIN',NULL,NULL,b'1',b'0'),
                        (12,'qwer5756765','o1227573@gmail.com','Patric','pass4','4546345656','USER',NULL,'FORM',b'1',b'0');

