-- 1. Создаем товар №1
INSERT INTO products (id, name, article)
VALUES (1, 'laptop ASUS', 'LAP-001');

-- 2. Создаем зону склада №1
INSERT INTO warehouse_zones (id, zone_name)
VALUES (1, 'Main warehouse');

-- 3. Добавляем остаток.
INSERT INTO stocks (product_id, warehouse_zone_id, quantity)
VALUES (1, 1, 3);

-- 4. Создаем черновик расходной накладной №1
INSERT INTO invoices (type, status)
VALUES ('OUTGOING', 'DRAFT');

-- 5. Привязываем к накладной №1 потребность в 5 штуках товара №1
INSERT INTO invoice_items (invoice_id, product_id, quantity)
VALUES (1, 1, 5);