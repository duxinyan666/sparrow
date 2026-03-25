-- PostgreSQL 初始化脚本
-- 自动在容器首次启动时执行

-- 示例表：用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_email ON users(email);

-- 示例表：订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status SMALLINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_no ON orders(order_no);
CREATE INDEX IF NOT EXISTS idx_status ON orders(status);

-- 插入示例数据
INSERT INTO users (username, email) VALUES 
    ('alice', 'alice@example.com'),
    ('bob', 'bob@example.com'),
    ('charlie', 'charlie@example.com')
ON CONFLICT (username) DO UPDATE SET email = EXCLUDED.email;

INSERT INTO orders (user_id, order_no, amount, status) VALUES
    (1, 'ORD-2026-001', 99.99, 1),
    (1, 'ORD-2026-002', 199.99, 0),
    (2, 'ORD-2026-003', 299.99, 2)
ON CONFLICT (order_no) DO UPDATE SET amount = EXCLUDED.amount;
