CREATE SCHEMA IF NOT EXISTS order_service;

CREATE TABLE order_service.orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_price NUMERIC(10, 2) NOT NULL,
    notes TEXT,
    shipping_address VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE order_service.order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES order_service.orders(id),
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX idx_orders_user_id ON order_service.orders(user_id);
CREATE INDEX idx_orders_status ON order_service.orders(status);
CREATE INDEX idx_orders_created_at ON order_service.orders(created_at DESC);
CREATE INDEX idx_orders_deleted ON order_service.orders(is_deleted);
CREATE INDEX idx_order_items_order_id ON order_service.order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_service.order_items(product_id);
CREATE INDEX idx_order_items_deleted ON order_service.order_items(is_deleted);
