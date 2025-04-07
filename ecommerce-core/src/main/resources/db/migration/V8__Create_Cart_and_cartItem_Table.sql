CREATE TABLE cart (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid()
);


CREATE TABLE cart_item (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           product_id UUID NOT NULL,
                           quantity INT NOT NULL CHECK (quantity >= 0),
                           price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                           cart_id UUID,

                           CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);
