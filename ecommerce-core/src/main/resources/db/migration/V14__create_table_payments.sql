CREATE TABLE payments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          order_id UUID NOT NULL,
                          user_id UUID NOT NULL,
                          amount DOUBLE PRECISION NOT NULL,
                          status VARCHAR(10) CHECK (status IN ('PENDING', 'PAID', 'FAILED')) NOT NULL,
                          payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);