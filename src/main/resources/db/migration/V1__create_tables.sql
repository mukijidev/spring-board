-- V1: 글과 댓글 테이블.
-- 파일 이름 규칙: V + 버전 + 밑줄 두 개(__) + 설명. 한 번 실행된 파일은 절대 고치지 않고 V2를 새로 만든다.

CREATE TABLE post (
                      id          BIGINT        NOT NULL AUTO_INCREMENT,
                      title       VARCHAR(100)  NOT NULL,
                      content     VARCHAR(5000) NOT NULL,
                      created_at  DATETIME(6)   NOT NULL,   -- LocalDateTime. (6) = 마이크로초까지 저장
                      updated_at  DATETIME(6),
                      PRIMARY KEY (id),
                      INDEX idx_post_created_at (created_at) -- 목록의 ORDER BY created_at DESC를 빠르게
);

CREATE TABLE comment (
                         id          BIGINT        NOT NULL AUTO_INCREMENT,
                         post_id     BIGINT        NOT NULL,
                         content     VARCHAR(500)  NOT NULL,
                         created_at  DATETIME(6)   NOT NULL,
                         PRIMARY KEY (id),
    -- 외래 키. MySQL은 post_id에 인덱스를 자동으로 만들어 줘서 "글의 댓글 목록" 조회도 빠르다
                         CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post (id)
);