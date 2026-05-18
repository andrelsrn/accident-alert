CREATE TABLE investigation_history (
                                       id BIGSERIAL PRIMARY KEY,

                                       investigation_id BIGINT NOT NULL,

                                       old_status VARCHAR(50),

                                       new_status VARCHAR(50),

                                       comment TEXT,

                                       change_by_user_id BIGINT NOT NULL,

                                       created_at TIMESTAMP NOT NULL,

                                       CONSTRAINT fk_investigation_history_investigation
                                           FOREIGN KEY (investigation_id)
                                               REFERENCES investigations(id),

                                       CONSTRAINT fk_investigation_history_user
                                           FOREIGN KEY (change_by_user_id)
                                               REFERENCES users(id)
);

CREATE INDEX idx_investigation_history_investigation
    ON investigation_history(investigation_id);

CREATE INDEX idx_investigation_history_created_at
    ON investigation_history(created_at);