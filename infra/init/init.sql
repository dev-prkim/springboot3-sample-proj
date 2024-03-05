-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS paran_sample;

-- 사용자 생성 및 권한 부여
CREATE USER IF NOT EXISTS 'paran'@'%' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'paran'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON paran_sample.* TO 'paran'@'%';
GRANT ALL PRIVILEGES ON paran_sample.* TO 'paran'@'localhost';

-- 사용자 권한 적용
FLUSH PRIVILEGES;