ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_user_id_fkey;

ALTER TABLE orders ALTER COLUMN user_id SET NOT NULL;
