-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 06, 2025 at 08:39 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlbh`
--

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `image`, `category`, `price`, `quantity`, `status`, `description`) VALUES
('SP0001', 'Cushion Dior', 'SP0001.png', 'Cushion', 7000000, 20, 'Còn hàng', ''),
('SP0002', 'Cushion 3CE', 'SP0002.png', 'Cushion', 899999, 15, 'Còn hàng', 'Chất mỏng nhẹ'),
('SP0003', 'Cushion Epoir', 'SP0003.png', 'Cushion', 400000, 10, 'Còn hàng', 'Không bết rít'),
('SP0004', 'Cushion YSL', 'SP0004.png', 'Cushion', 999999, 2, 'Cháy hàng', 'Lớp nền tự nhiên, căng bóng'),
('SP0005', 'Phấn mắt 3CE', 'SP0005.png', 'Phấn mắt', 450000, 26, 'Còn hàng', 'Tone màu ấm'),
('SP0006', 'Phấn mắt Peripera', 'SP0006.png', 'Phấn mắt', 399999, 1, 'Cháy hàng', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
