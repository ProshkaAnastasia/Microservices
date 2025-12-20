CREATE SCHEMA IF NOT EXISTS product_service;

CREATE TABLE product_service.categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE product_service.products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL REFERENCES product_service.categories(id),
    image_url VARCHAR(255),
    rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    review_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX idx_category_name ON product_service.categories(name);
CREATE INDEX idx_category_deleted ON product_service.categories(is_deleted);
CREATE INDEX idx_product_category_id ON product_service.products(category_id);
CREATE INDEX idx_product_name ON product_service.products(name);
CREATE INDEX idx_product_deleted ON product_service.products(is_deleted);
CREATE INDEX idx_product_created_at ON product_service.products(created_at DESC);
