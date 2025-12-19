SET search_path TO user_service;

CREATE TABLE IF NOT EXISTS user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_roles_unique UNIQUE(user_id, role)
);

CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON user_roles(role);

COMMENT ON TABLE user_roles IS 'Таблица для связи пользователей с ролями (many-to-many)';
COMMENT ON COLUMN user_roles.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN user_roles.role IS 'Роль пользователя (USER, SELLER, MODERATOR, ADMIN)';