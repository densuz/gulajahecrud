/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 10.4.13-MariaDB : Database - dbrest
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dbrest` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `dbrest`;

/*Table structure for table `produk` */

DROP TABLE IF EXISTS `produk`;

CREATE TABLE `produk` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `nama_produk` varchar(30) DEFAULT NULL,
  `tipe_produk` varchar(30) DEFAULT NULL,
  `harga` int(50) DEFAULT NULL,
  `stok` int(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `produk` */

insert  into `produk`(`id`,`nama_produk`,`tipe_produk`,`harga`,`stok`) values 
(1,'Ardiles','Sandal',500000,13),
(3,'Super 25','Baju',35000,16),
(5,'Uyta','Celana',117000,34),
(6,'Jeas Ver','Celana',235000,13),
(7,'Vander','Celana',125000,42),
(8,'Super 25','Baju',35000,16),
(10,'Uyta','Celana',117000,34);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
