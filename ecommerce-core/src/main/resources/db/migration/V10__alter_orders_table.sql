ALTER TABLE orders ADD COLUMN payment_status VARCHAR(10) CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED')) NOT NULL;
