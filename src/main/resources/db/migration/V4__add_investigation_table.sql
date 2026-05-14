CREATE TABLE investigations (
                                id BIGSERIAL PRIMARY KEY,

                                accident_id BIGINT NOT NULL UNIQUE,

                                assigned_technician_id BIGINT NOT NULL,

                                root_cause TEXT,

                                observation TEXT NOT NULL,

                                status VARCHAR(50),

                                created_at TIMESTAMP NOT NULL,

                                CONSTRAINT fk_investigation_accident
                                    FOREIGN KEY (accident_id)
                                        REFERENCES accidents(id),

                                CONSTRAINT fk_investigation_technician
                                    FOREIGN KEY (assigned_technician_id)
                                        REFERENCES users(id)
);