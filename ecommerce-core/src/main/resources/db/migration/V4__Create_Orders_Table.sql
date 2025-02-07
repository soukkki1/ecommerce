CREATE TABLE orders (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                        total_price DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) CHECK (status IN ('PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED')) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
