-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 22, 2019 at 12:07 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `voyager`
--

-- --------------------------------------------------------

--
-- Table structure for table `accessoryassignement`
--

CREATE TABLE `accessoryassignement` (
  `accassign_id` int(11) NOT NULL,
  `assign_id` int(11) NOT NULL,
  `accessory_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `accessoryassignement`
--

INSERT INTO `accessoryassignement` (`accassign_id`, `assign_id`, `accessory_id`) VALUES
(26, 35, 1),
(27, 35, 2),
(28, 36, 1),
(29, 36, 2),
(30, 37, 1),
(31, 37, 2),
(32, 38, 1),
(33, 38, 2),
(34, 39, 1),
(35, 39, 2),
(39, 35, 1),
(41, 44, 1),
(42, 44, 2),
(43, 45, 1);

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `address_id` int(11) NOT NULL,
  `streetaddr` varchar(50) NOT NULL,
  `city` varchar(25) NOT NULL,
  `state` varchar(15) NOT NULL,
  `country` varchar(25) NOT NULL,
  `zipcode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`address_id`, `streetaddr`, `city`, `state`, `country`, `zipcode`) VALUES
(1, '4331 Thurmon Tanner Prky', 'Flowery Branch', 'Georgia', 'United States of America', '30542');

-- --------------------------------------------------------

--
-- Table structure for table `computeraccessories`
--

CREATE TABLE `computeraccessories` (
  `accessory_id` int(11) NOT NULL,
  `accessory_alias` varchar(25) NOT NULL,
  `amount_in_stock` int(11) NOT NULL,
  `total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `computeraccessories`
--

INSERT INTO `computeraccessories` (`accessory_id`, `accessory_alias`, `amount_in_stock`, `total`) VALUES
(1, 'Laptop Bag', 17, 16),
(2, 'Air-Card', 10, 10);

-- --------------------------------------------------------

--
-- Table structure for table `computermakes`
--

CREATE TABLE `computermakes` (
  `make_id` int(11) NOT NULL,
  `make_alias` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `computermakes`
--

INSERT INTO `computermakes` (`make_id`, `make_alias`) VALUES
(3, 'Apple'),
(2, 'HP'),
(1, 'Lenovo');

-- --------------------------------------------------------

--
-- Table structure for table `computermodels`
--

CREATE TABLE `computermodels` (
  `model_id` int(11) NOT NULL,
  `make_id` int(11) NOT NULL,
  `model_alias` varchar(20) NOT NULL,
  `amount_in_stock` int(11) NOT NULL DEFAULT '1',
  `total` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `computermodels`
--

INSERT INTO `computermodels` (`model_id`, `make_id`, `model_alias`, `amount_in_stock`, `total`) VALUES
(1, 1, 'E555', 1, 2),
(2, 2, 'Probook 400', 1, 1),
(3, 1, 'E560', 1, 1),
(4, 1, 'E70', 1, 1),
(5, 2, '450 GN', 1, 1),
(6, 3, 'Macbook Air', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `computersystem`
--

CREATE TABLE `computersystem` (
  `sys_id` int(11) NOT NULL,
  `computer_name` varchar(15) NOT NULL,
  `serialnum` varchar(35) NOT NULL,
  `model_id` int(11) NOT NULL,
  `system_type` set('Laptop','Desktop') NOT NULL,
  `is_assigned` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `computersystem`
--

INSERT INTO `computersystem` (`sys_id`, `computer_name`, `serialnum`, `model_id`, `system_type`, `is_assigned`) VALUES
(1, 'kdmitchell16', 'PF-123456', 1, 'Laptop', b'0'),
(2, 'kdmitchell16', 'PF-001234', 1, 'Laptop', b'0'),
(3, 'bqdover16', 'PF-098765', 1, 'Laptop', b'0'),
(4, 'StockDTBCDE', 'PF-ABCDE', 4, 'Desktop', b'0'),
(5, 'StockDTBCDE', '5CNABCDE', 5, 'Laptop', b'1'),
(6, 'StockDT3987', 'PF-123987', 1, 'Laptop', b'1'),
(7, 'StockDTT123', 'PF-DT123', 4, 'Laptop', b'1'),
(8, 'AdminLT', '5CNHPLT', 2, 'Laptop', b'1'),
(9, 'mkhuek', 'PF-8675309', 3, 'Laptop', b'0'),
(10, 'StockDT2345', 'APPLE12345', 6, 'Laptop', b'0');

-- --------------------------------------------------------

--
-- Table structure for table `laptopassignment`
--

CREATE TABLE `laptopassignment` (
  `assign_id` int(11) NOT NULL,
  `sys_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `date_issued` date NOT NULL,
  `date_returned` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `laptopassignment`
--

INSERT INTO `laptopassignment` (`assign_id`, `sys_id`, `user_id`, `date_issued`, `date_returned`) VALUES
(35, 2, 1, '2019-04-19', '2019-04-22'),
(36, 1, 3, '2019-04-19', '2019-04-19'),
(37, 1, 3, '2019-04-19', '2019-04-19'),
(38, 3, 2, '2019-04-19', '2019-04-22'),
(39, 1, 1, '2019-04-19', '2019-04-22'),
(40, 4, 1, '2019-04-22', '2019-04-22'),
(41, 5, 1, '2019-04-22', NULL),
(42, 6, 1, '2019-04-22', NULL),
(43, 7, 4, '2019-04-22', NULL),
(44, 8, 2, '2019-04-22', NULL),
(45, 9, 2, '2019-04-22', '2019-04-22'),
(46, 9, 3, '2019-04-22', '2019-04-22');

-- --------------------------------------------------------

--
-- Table structure for table `login_table`
--

CREATE TABLE `login_table` (
  `login_id` int(11) NOT NULL,
  `user_name` varchar(30) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `login_table`
--

INSERT INTO `login_table` (`login_id`, `user_name`, `password`) VALUES
(1, 'admin', 'b5a5b4c716efd475a68c3fadfc58f57629fe99bcdd00f3127b8c7c5848141c2967755d3e96f30b2a566556a396edf6800068add5a161f410f3c0dc00f32517fe');

-- --------------------------------------------------------

--
-- Table structure for table `user_table`
--

CREATE TABLE `user_table` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(15) NOT NULL,
  `worksite_id` int(11) DEFAULT NULL,
  `first_name` varchar(25) NOT NULL,
  `last_name` varchar(35) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_table`
--

INSERT INTO `user_table` (`user_id`, `user_name`, `worksite_id`, `first_name`, `last_name`) VALUES
(1, 'kdmitchell', 1, 'Kevin', 'Mitchell'),
(2, 'bqdover', 1, 'Ben', 'Dover'),
(3, 'mkhuck', 1, 'Mike', 'Huck'),
(4, 'kxrainer', 1, 'Kyle', 'Rainer');

-- --------------------------------------------------------

--
-- Table structure for table `worksite`
--

CREATE TABLE `worksite` (
  `worksite_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `worksite_name` varchar(50) NOT NULL,
  `department` enum('DD','BH','Admin','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `worksite`
--

INSERT INTO `worksite` (`worksite_id`, `address_id`, `worksite_name`, `department`) VALUES
(1, 1, 'Thurmon Tanner', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accessoryassignement`
--
ALTER TABLE `accessoryassignement`
  ADD PRIMARY KEY (`accassign_id`),
  ADD KEY `fk_assign` (`assign_id`),
  ADD KEY `fk_acc` (`accessory_id`);

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `computeraccessories`
--
ALTER TABLE `computeraccessories`
  ADD PRIMARY KEY (`accessory_id`);

--
-- Indexes for table `computermakes`
--
ALTER TABLE `computermakes`
  ADD PRIMARY KEY (`make_id`),
  ADD UNIQUE KEY `make_alias` (`make_alias`);

--
-- Indexes for table `computermodels`
--
ALTER TABLE `computermodels`
  ADD PRIMARY KEY (`model_id`),
  ADD KEY `make_id` (`make_id`);

--
-- Indexes for table `computersystem`
--
ALTER TABLE `computersystem`
  ADD PRIMARY KEY (`sys_id`),
  ADD KEY `fk_model` (`model_id`);

--
-- Indexes for table `laptopassignment`
--
ALTER TABLE `laptopassignment`
  ADD PRIMARY KEY (`assign_id`),
  ADD KEY `fk_systemAssign` (`sys_id`),
  ADD KEY `fk_userAssign` (`user_id`);

--
-- Indexes for table `login_table`
--
ALTER TABLE `login_table`
  ADD PRIMARY KEY (`login_id`);

--
-- Indexes for table `user_table`
--
ALTER TABLE `user_table`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `fk_site` (`worksite_id`);

--
-- Indexes for table `worksite`
--
ALTER TABLE `worksite`
  ADD PRIMARY KEY (`worksite_id`),
  ADD KEY `fk_address` (`address_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accessoryassignement`
--
ALTER TABLE `accessoryassignement`
  MODIFY `accassign_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `address_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `computeraccessories`
--
ALTER TABLE `computeraccessories`
  MODIFY `accessory_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `computermakes`
--
ALTER TABLE `computermakes`
  MODIFY `make_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `computermodels`
--
ALTER TABLE `computermodels`
  MODIFY `model_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `computersystem`
--
ALTER TABLE `computersystem`
  MODIFY `sys_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `laptopassignment`
--
ALTER TABLE `laptopassignment`
  MODIFY `assign_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `login_table`
--
ALTER TABLE `login_table`
  MODIFY `login_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user_table`
--
ALTER TABLE `user_table`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `worksite`
--
ALTER TABLE `worksite`
  MODIFY `worksite_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accessoryassignement`
--
ALTER TABLE `accessoryassignement`
  ADD CONSTRAINT `fk_acc` FOREIGN KEY (`accessory_id`) REFERENCES `computeraccessories` (`accessory_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_assign` FOREIGN KEY (`assign_id`) REFERENCES `laptopassignment` (`assign_id`) ON UPDATE CASCADE;

--
-- Constraints for table `computermodels`
--
ALTER TABLE `computermodels`
  ADD CONSTRAINT `fk_make` FOREIGN KEY (`make_id`) REFERENCES `computermakes` (`make_id`) ON UPDATE CASCADE;

--
-- Constraints for table `computersystem`
--
ALTER TABLE `computersystem`
  ADD CONSTRAINT `fk_model` FOREIGN KEY (`model_id`) REFERENCES `computermodels` (`model_id`) ON UPDATE CASCADE;

--
-- Constraints for table `laptopassignment`
--
ALTER TABLE `laptopassignment`
  ADD CONSTRAINT `fk_systemAssign` FOREIGN KEY (`sys_id`) REFERENCES `computersystem` (`sys_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_userAssign` FOREIGN KEY (`user_id`) REFERENCES `user_table` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user_table`
--
ALTER TABLE `user_table`
  ADD CONSTRAINT `fk_site` FOREIGN KEY (`worksite_id`) REFERENCES `worksite` (`worksite_id`) ON UPDATE CASCADE;

--
-- Constraints for table `worksite`
--
ALTER TABLE `worksite`
  ADD CONSTRAINT `fk_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
