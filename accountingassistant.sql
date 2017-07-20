CREATE DATABASE IF NOT EXISTS accountingassistant
  DEFAULT CHARSET utf8
  COLLATE utf8_general_ci;

USE accountingassistant;

CREATE TABLE user (
  username      VARCHAR(24) NOT NULL,
  password      CHAR(32)    NOT NULL,
  email         VARCHAR(48) NOT NULL,
  avatar        VARCHAR(32),
  activateToken CHAR(32),
  activateCode  CHAR(6),
  activateTime  TIMESTAMP DEFAULT current_timestamp ON UPDATE current_timestamp,
  resetToken    CHAR(32),
  resetCode     CHAR(6),
  resetTime     TIMESTAMP DEFAULT current_timestamp ON UPDATE current_timestamp,
  UNIQUE (email),
  PRIMARY KEY (username)
)
  ENGINE = innodb;

CREATE TABLE device (
  uuid      CHAR(32)    NOT NULL,
  username  VARCHAR(24) NOT NULL,
  name      VARCHAR(24) NOT NULL,
  token     CHAR(32)    NOT NULL,
  tokenTime TIMESTAMP DEFAULT current_timestamp ON UPDATE current_timestamp,
  syncTime  TIMESTAMP DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (uuid),
  CONSTRAINT device_ibfk_1 FOREIGN KEY (username) REFERENCES user (username)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;

CREATE TABLE log (
  id       INT         NOT NULL AUTO_INCREMENT,
  username VARCHAR(24) NOT NULL,
  time     TIMESTAMP            DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (id),
  CONSTRAINT log_ibfk_1 FOREIGN KEY (username) REFERENCES user (username)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;

CREATE TABLE book (
  id       INT          NOT NULL AUTO_INCREMENT,
  username VARCHAR(24)  NOT NULL,
  name     VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT book_ibfk_1 FOREIGN KEY (username) REFERENCES user (username)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;

CREATE TABLE account (
  id      INT     NOT NULL AUTO_INCREMENT,
  bookid  INT     NOT NULL,
  type    TINYINT NOT NULL,
  balance FLOAT   NOT NULL,
  name    VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT account_ibfk_1 FOREIGN KEY (bookid) REFERENCES book (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;

CREATE TABLE record (
  id        INT      NOT NULL AUTO_INCREMENT,
  accountid INT      NOT NULL,
  expense   BOOLEAN  NOT NULL,
  amount    FLOAT    NOT NULL,
  remark    VARCHAR(255),
  type      TINYINT  NOT NULL,
  time      DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT record_ibfk_1 FOREIGN KEY (accountid) REFERENCES account (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;

CREATE TABLE dirty (
  id      INT      NOT NULL AUTO_INCREMENT,
  uuid    CHAR(32) NOT NULL,
  rid     INT      NOT NULL,
  type    TINYINT           DEFAULT 0,
  deleted BOOLEAN,
  time    TIMESTAMP         DEFAULT current_timestamp ON UPDATE current_timestamp,
  PRIMARY KEY (id),
  CONSTRAINT dirty_ibfk_1 FOREIGN KEY (uuid) REFERENCES device (uuid)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = innodb;