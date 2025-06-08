-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 08, 2025 at 08:55 AM
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
-- Table structure for table `doanhthu`
--

CREATE TABLE `doanhthu` (
  `maDon` varchar(20) NOT NULL,
  `tenSanPham` varchar(100) NOT NULL,
  `ngayBan` date NOT NULL,
  `soLuong` int(11) NOT NULL,
  `giaTien` int(11) NOT NULL,
  `tongTien` int(11) NOT NULL,
  `ghiChu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `doanhthu`
--

INSERT INTO `doanhthu` (`maDon`, `tenSanPham`, `ngayBan`, `soLuong`, `giaTien`, `tongTien`, `ghiChu`) VALUES
('DH001', 'Má hồng Into You', '2025-06-01', 2, 350000, 700000, 'Mua online'),
('DH002', 'Son Fwee', '2025-06-02', 5, 399000, 1995000, ''),
('DH003', 'Cushion Dior', '2025-06-03', 3, 500000, 1500000, 'Khuyến mãi tháng 6'),
('DH004', 'Phấn mắt Peripera', '2025-06-04', 20, 290000, 5800000, 'Khách sỉ');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `doanhthu`
--
ALTER TABLE `doanhthu`
  ADD PRIMARY KEY (`maDon`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
