-- One-step setup for Math Whiz (Sanjith Saha, IT24044)
-- Creates: database, table, and a dedicated MySQL user used by the app.

CREATE DATABASE IF NOT EXISTS math_whiz_db;
USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score       INT          NOT NULL,
    difficulty  VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
    played_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE USER IF NOT EXISTS 'mathwhiz'@'localhost' IDENTIFIED BY 'MathWhiz@123';
ALTER USER 'mathwhiz'@'localhost' IDENTIFIED BY 'MathWhiz@123';
GRANT ALL PRIVILEGES ON math_whiz_db.* TO 'mathwhiz'@'localhost';
FLUSH PRIVILEGES;
