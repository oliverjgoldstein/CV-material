#!/bin/sh
set -eu

if [ -z "${TWISENT_DB_USER:-}" ] || [ -z "${TWISENT_DB_PASSWORD:-}" ] || \
   [ -z "${TWITTER_CONSUMER_KEY:-}" ] || [ -z "${TWITTER_CONSUMER_SECRET:-}" ] || \
   [ -z "${TWITTER_ACCESS_TOKEN:-}" ] || [ -z "${TWITTER_ACCESS_TOKEN_SECRET:-}" ]; then
  printf '%s\n' 'Set all required database and Twitter environment variables before starting.' >&2
  exit 1
fi

export PATH="${PATH}:/usr/local/mysql/bin"

RED='\033[0;31m'
GREEN='\033[1;32m'
NC='\033[0m' # No Color

printf "${RED} Make sure you have the enum package installed (in the scripts directory perhaps run setup.py)"
printf 'Make sure you have mysql-python installed: "pip install MySQL-python"'
printf 'Make sure you have TWEEPY 3.4 and WEB PY Installed (Or later maybe although not tested)'

printf 'To create the database type: '

printf '

mysql -u ${TWISENT_DB_USER} -p (then enter ${TWISENT_DB_PASSWORD})
CREATE DATABASE dashbrd;
USE dashbrd;

Then:

-- phpMyAdmin SQL Dump
-- version 4.4.1.1
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Aug 16, 2016 at 05:39 PM
-- Server version: 5.5.42
-- PHP Version: 5.6.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `dashbrd`
--

-- --------------------------------------------------------

--
-- Table structure for table `session_id`
--

CREATE TABLE `session_id` (
  `session_id` varchar(36) NOT NULL,
  `time_stamp` varchar(100) NOT NULL,
  `data` text NOT NULL,
  `session_expired` tinyint(1) NOT NULL,
  `keyword_list` text,
  `environ` text NOT NULL,
  `ip` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `session_id`
--
ALTER TABLE `session_id`
  ADD UNIQUE KEY `session_id` (`session_id`);
\n\n\n\n\n'

printf 'Database Settings:'
printf '
dbn     = "mysql"
db      = "dashbrd"
user    = "${TWISENT_DB_USER}"
pw      = "${TWISENT_DB_PASSWORD}"\n '

printf "${GREEN}\n\n\n######################Starting Unit Test.###############################\n\n\n\n"

python2.7 ./scripts/Unit_Test.py

printf 'Starting Server.\n\nURL: '

python2.7 Server_Start.py 127.0.0.1:8080


BLUE='\033[1;36m'
printf "${BLUE}\n\nComplete.\n\n${NC}"
