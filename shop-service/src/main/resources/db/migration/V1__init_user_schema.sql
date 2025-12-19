CREATE SCHEMA IF NOT EXISTS user_service;

SET search_path TO user_service;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role' AND typnamespace = (SELECT oid FROM pg_namespace WHERE nspname = 'user_service')) THEN
        CREATE TYPE user_role AS ENUM ('USER', 'SELLER', 'MODERATOR', 'ADMIN');
    END IF;
END
$$;