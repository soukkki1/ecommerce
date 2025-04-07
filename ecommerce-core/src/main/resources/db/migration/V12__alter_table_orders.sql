DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                        total_price DOUBLE PRECISION NOT NULL,
                        order_status VARCHAR(20) CHECK (order_status IN ('PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED')) NOT NULL,
                        payment_status VARCHAR(20) CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED')) NOT NULL,

                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
