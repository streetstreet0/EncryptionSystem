-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 17, 2023 at 07:03 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `encryptUsers`
--

-- --------------------------------------------------------

--
-- Table structure for table `cipherKeys`
--

CREATE TABLE `cipherKeys` (
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `cipherKey` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `algorithm` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cipherKeys`
--

INSERT INTO `cipherKeys` (`username`, `cipherKey`, `algorithm`) VALUES
('12-63117-71-64-15-74-8849-61-103-301051193897', 'msaXfZWu4/h5pZX0rjUfew==', 'JvkLUjHAlhI='),
('12-63117-71-64-15-74-8849-61-103-301051193897', '2yiI7h84m3V5pZX0rjUfew==', 'JvkLUjHAlhI='),
('12-63117-71-64-15-74-8849-61-103-301051193897', '4udE5eGwptw3LC809R/3jNV1TGagNBEUxaTUxjgS0S95pZX0rjUfew==', 'CwNh4prMoxQ=');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `cipherKey` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `algorithm` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`username`, `cipherKey`, `algorithm`, `message`) VALUES
('12-63117-71-64-15-74-8849-61-103-301051193897', 'EmDwMGqOuUZ5pZX0rjUfew==', 'JvkLUjHAlhI=', 'OAZJ8OIF1Qk='),
('12-63117-71-64-15-74-8849-61-103-301051193897', 'x1Br+T6kDGviI02xzkaWiEe6ZKloRhHn82LxIE+fo6V5pZX0rjUfew==', 'CwNh4prMoxQ=', 'cc49HKS2LmacWdpkKoMHIw=='),
('12-63117-71-64-15-74-8849-61-103-301051193897', '2yiI7h84m3V5pZX0rjUfew==', 'JvkLUjHAlhI=', 'Fv7jY6XzlF0Pzq8VNueLbQ=='),
('12-63117-71-64-15-74-8849-61-103-301051193897', 'H0VGxVujrJR5pZX0rjUfew==', 'JvkLUjHAlhI=', 'qBfukt6iB193C/O5mEzG3qqNtSUuKxq0');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `background` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`username`, `background`) VALUES
('12-63117-71-64-15-74-8849-61-103-301051193897', 'rHe+Ev9/1sDbFa6igJkJPg==');

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `username` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`username`, `password`) VALUES
('a', '-4429-116-39-1130-784-23-1289-104-20-866126');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
