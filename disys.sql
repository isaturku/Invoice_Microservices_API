-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 05, 2022 at 05:06 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `disys`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `fname` varchar(30) DEFAULT NULL,
  `lname` varchar(30) DEFAULT NULL,
  `address` varchar(30) DEFAULT NULL,
  `zip` int(11) DEFAULT NULL,
  `country` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `fname`, `lname`, `address`, `zip`, `country`) VALUES
(1, 'John', 'Doe', '1. Strasse', 1080, 'AT'),
(2, 'Jane', 'Doe', '2. Strasse', 8010, 'AT');

-- --------------------------------------------------------

--
-- Table structure for table `invoice_log`
--

DROP TABLE IF EXISTS `invoice_log`;
CREATE TABLE `invoice_log` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `pdf_name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `invoice_log`
--

INSERT INTO `invoice_log` (`id`, `customer_id`, `pdf_name`) VALUES
(1, 1, 'Doe_1658665198999.pdf'),
(2, 1, 'Doe_1658665695969.pdf'),
(3, 2, 'Doe_1658688699546.pdf'),
(4, 1, 'Doe_1659630064347.pdf'),
(5, 1, 'Doe_1659637163086.pdf'),
(6, 2, 'Doe_1659637163537.pdf'),
(7, 1, 'Doe_1659640112536.pdf'),
(8, 1, 'Doe_1659640203267.pdf'),
(9, 1, 'Doe_1659640363028.pdf'),
(10, 1, 'Doe_1659640887955.pdf'),
(11, 2, 'Doe_1659640919253.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
CREATE TABLE `station` (
  `station_id` int(11) NOT NULL,
  `available` tinyint(1) DEFAULT NULL,
  `latitude` decimal(8,6) DEFAULT NULL,
  `longitude` decimal(8,6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `station`
--

INSERT INTO `station` (`station_id`, `available`, `latitude`, `longitude`) VALUES
(1, 1, '48.205113', '16.383420'),
(2, 1, '48.238836', '16.379323'),
(3, 1, '48.196068', '16.337239');

-- --------------------------------------------------------

--
-- Table structure for table `station_usage`
--

DROP TABLE IF EXISTS `station_usage`;
CREATE TABLE `station_usage` (
  `id` int(11) NOT NULL,
  `station_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `kwh` int(11) NOT NULL,
  `datetime` datetime NOT NULL,
  `accountedFor` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `station_usage`
--

INSERT INTO `station_usage` (`id`, `station_id`, `customer_id`, `kwh`, `datetime`, `accountedFor`) VALUES
(1, 2, 1, 231, '2022-07-16 18:53:58', 1),
(2, 1, 2, 911, '2022-07-16 18:53:58', 1),
(3, 2, 1, 432, '2022-07-16 19:26:32', 1),
(4, 1, 1, 2412, '2022-07-16 20:34:46', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `invoice_log`
--
ALTER TABLE `invoice_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `station`
--
ALTER TABLE `station`
  ADD PRIMARY KEY (`station_id`);

--
-- Indexes for table `station_usage`
--
ALTER TABLE `station_usage`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idcustomer` (`customer_id`),
  ADD KEY `idstation` (`station_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `invoice_log`
--
ALTER TABLE `invoice_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `station`
--
ALTER TABLE `station`
  MODIFY `station_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `station_usage`
--
ALTER TABLE `station_usage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `station_usage`
--
ALTER TABLE `station_usage`
  ADD CONSTRAINT `idcustomer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `idstation` FOREIGN KEY (`station_id`) REFERENCES `station` (`station_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
