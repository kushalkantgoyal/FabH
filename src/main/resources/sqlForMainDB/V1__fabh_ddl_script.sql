USE fabh_db;

SET FOREIGN_KEY_CHECKS=0;

--
-- Table structure for table users
--

CREATE TABLE users (
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created_time DATETIME NOT NULL,
  updated_time DATETIME NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) DEFAULT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  mobile_number VARCHAR(15) NOT NULL UNIQUE
);
  
--
-- Table structure for table transactions
--

CREATE TABLE transactions (
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created_time DATETIME NOT NULL,
  updated_time DATETIME NOT NULL,
  user_id BIGINT(20) NOT NULL,
  transaction_type INTEGER NOT NULL,
  amount DOUBLE NOT NULL,
  current_balance DOUBLE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id)
);

SET FOREIGN_KEY_CHECKS=1;