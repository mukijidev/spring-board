-- V3: 글과 댓글의 작성자

ALTER TABLE post
    ADD COLUMN author_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE comment
    ADD COLUMN author_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id);