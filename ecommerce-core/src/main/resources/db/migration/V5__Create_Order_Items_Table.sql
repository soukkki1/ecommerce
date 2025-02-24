CREATE TABLE order_items (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
                             product_id UUID REFERENCES products(id) ON DELETE CASCADE,
                             quantity INT NOT NULL,
                             price DECIMAL(10,2) NOT NULL
);