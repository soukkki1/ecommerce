CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) CHECK (role IN ('ADMIN', 'CUSTOMER', 'SELLER')),
                       created_at TIMESTAMP DEFAULT now()
);