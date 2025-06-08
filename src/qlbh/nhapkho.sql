-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 08, 2025 at 07:10 AM
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
-- Table structure for table `nhapkho`
--

CREATE TABLE `nhapkho` (
  `id` int(11) NOT NULL,
  `ten_san_pham` varchar(255) NOT NULL,
  `so_luong` int(11) NOT NULL,
  `gia_nhap` double NOT NULL,
  `ngay_nhap` date NOT NULL,
  `nha_cung_cap` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhapkho`
--

INSERT INTO `nhapkho` (`id`, `ten_san_pham`, `so_luong`, `gia_nhap`, `ngay_nhap`, `nha_cung_cap`) VALUES
(1, 'Cushion YSL', 50, 7500000, '2025-06-07', 'Nhãn hàng YSL'),
(2, 'Son Dior', 40, 8900000, '2025-06-07', 'Nhãn hàng Dior'),
(3, 'Phấn mắt Romand', 20, 4500000, '2025-06-07', 'Nhãn hàng Romand'),
(4, 'Má hồng 3CE', 10, 3500000, '2025-06-07', 'Nhãn hàng 3CE');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `nhapkho`
--
ALTER TABLE `nhapkho`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `nhapkho`
--
ALTER TABLE `nhapkho`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
