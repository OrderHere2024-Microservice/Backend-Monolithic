ALTER TABLE orders
    ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;