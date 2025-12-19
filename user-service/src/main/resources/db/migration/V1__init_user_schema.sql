-- ============================================================================
-- Create schema
-- ============================================================================
CREATE SCHEMA IF NOT EXISTS user_service;


-- ============================================================================
-- Create users table
-- ============================================================================
CREATE TABLE IF NOT EXISTS user_service.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    profile_image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT true NOT NULL,
    email_verified BOOLEAN DEFAULT false NOT NULL,
    is_deleted BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);


-- Create indexes on users table
CREATE INDEX IF NOT EXISTS idx_users_email ON user_service.users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON user_service.users(username);
CREATE INDEX IF NOT EXISTS idx_users_deleted ON user_service.users(is_deleted);


-- Add comments
COMMENT ON TABLE user_service.users IS 'Таблица пользователей системы';
COMMENT ON COLUMN user_service.users.username IS 'Уникальное имя пользователя';
COMMENT ON COLUMN user_service.users.email IS 'Электронная почта пользователя';
COMMENT ON COLUMN user_service.users.password_hash IS 'Хеш пароля пользователя';
COMMENT ON COLUMN user_service.users.is_active IS 'Активен ли пользователь';
COMMENT ON COLUMN user_service.users.email_verified IS 'Подтверждена ли почта';
COMMENT ON COLUMN user_service.users.is_deleted IS 'Мягкое удаление пользователя';


-- ============================================================================
-- Create user_roles table (many-to-many)
-- ============================================================================
CREATE TABLE IF NOT EXISTS user_service.user_roles (
    user_id BIGINT NOT NULL REFERENCES user_service.users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);


-- Create indexes on user_roles table
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_service.user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON user_service.user_roles(role);


-- Add comments
COMMENT ON TABLE user_service.user_roles IS 'Таблица для связи пользователей с ролями (many-to-many)';
COMMENT ON COLUMN user_service.user_roles.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN user_service.user_roles.role IS 'Роль пользователя (USER, SELLER, MODERATOR, ADMIN)';
