-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS note_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE note_db;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 메모 테이블
CREATE TABLE IF NOT EXISTS notes (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     title VARCHAR(100) NOT NULL,
                                     content TEXT NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_notes_user FOREIGN KEY (user_id)
                                         REFERENCES users(id)
                                         ON DELETE CASCADE
);

-- 인덱스
CREATE INDEX idx_notes_user_id ON notes(user_id);
