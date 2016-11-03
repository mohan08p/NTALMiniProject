-- phpMyAdmin SQL Dump
-- version 3.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 04, 2016 at 01:06 PM
-- Server version: 5.5.25a
-- PHP Version: 5.4.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ntal`
--

-- --------------------------------------------------------

--
-- Table structure for table `ntal`
--

CREATE TABLE IF NOT EXISTS `ntal` (
  `srno` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `comment` varchar(400) NOT NULL,
  PRIMARY KEY (`srno`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `ntal`
--

INSERT INTO `ntal` (`srno`, `name`, `comment`) VALUES
(1, 'Meghna ', 'A remarkable first camera for any DSLR / Photography enthusiast. The coupled offer of the second lens makes this a clear winner over the Nikon D3300 even if it sports features which may be better or advanced compared to this one, however its not just about the immediate camera needs which you should consider.'),
(2, 'Neha Mundada', 'BT 112 is an ultimate delight, 12W RMS speaker is best in class of its range.. I was a little curious to know whether the design will come as it looks in Photo representation- It looks great. The small necklace type of the cord makes it handy to carry around and thus defines the utility of a portable speaker.. All in all a very good product worth the buy.. Go for it!'),
(14, 'Kevin', 'Yo'),
(15, 'Hacker', '<script>alert(0);</script>'),
(16, 'ashgajtwdj', '<script>alert(9);</script>');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
