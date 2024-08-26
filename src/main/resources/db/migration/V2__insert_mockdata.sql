INSERT INTO users (username, firstname, lastname, password, user_email, avatar_url, point, user_role)
VALUES ('user1', 'user', 'user', '$2a$10$.wLQ2xMfPLjfhCG6cnw.PupQunIZhv6F09ChvsxwvJgnAT.JZUtxq', 'admin@email.com',
        'https://freepngimg.com/thumb/google/66726-customer-account-google-service-button-search-logo.png',
        100, 'sys_admin'),
        ('driver', 'driver', 'driver', '$2a$10$.wLQ2xMfPLjfhCG6cnw.PupQunIZhv6F09ChvsxwvJgnAT.JZUtxq', 'driver@email.com',
        'https://cdn.icon-icons.com/icons2/2335/PNG/512/food_delivery_guy_avatar_people_icon_142370.png',
        100, 'driver');
-- password:   123456


INSERT INTO user_address (user_id, address, is_default)
VALUES (1, '123 Main St, Blackburn VIC 3130', true),
       (1, '456 Elm St', false);


INSERT INTO ingredient (name, unit)
VALUES ('Sugar', 'grams'),
       ('Flour', 'grams'),
       ('Butter', 'grams'),
       ('Eggs', 'pieces');


INSERT INTO restaurant (name, description, address, contact_number, abn,
                        owner_name, owner_mobile, owner_address, owner_email, owner_crn, average_rating)
VALUES ('Restaurant A', 'A description of Restaurant A', '123 Main St, Blackburn VIC 3130', '123-456-7890',
        '12345678901', 'John Doe', '987-654-3210', '456 Elm St', 'john@example.com', '123456789A', 4.5),
       ('Restaurant B', 'A description of Restaurant B', '789 Oak St',
        '987-654-3210', '12345678902', 'Jane Smith', '123-456-7890', '123 Main St',
        'jane@example.com', '123456789A', 4.2);


INSERT INTO opening_hours (restaurant_id, day_of_week, opening_time, closing_time)
VALUES (1, 'monday', '09:00 AM', '05:00 PM'),
       (1, 'tuesday', '09:00 AM', '05:00 PM'),
       (1, 'wednesday', '09:00 AM', '05:00 PM'),
       (1, 'thursday', '09:00 AM', '05:00 PM'),
       (1, 'friday', '09:00 AM', '08:00 PM'),
       (1, 'saturday', '10:00 AM', '08:00 PM'),
       (1, 'sunday', 'Closed', 'Closed');


INSERT INTO category (restaurant_id, category_name)
VALUES (1, 'Main Dishes'),
       (1, 'Soups'),
       (1, 'Salads'),
       (1, 'Desserts'),
       (1, 'Beverages'),
       (1, 'Appetizers'),
       (1, 'Side Dishes'),
       (1, 'Breakfast'),
       (1, 'Lunch'),
       (1, 'Dinner');


INSERT INTO dish (restaurant_id, dish_name, description, price, image_url, rating, availability, category_id)
VALUES (1, 'Pasta Carbonara', 'Classic Italian pasta dish with bacon and eggs', 12.99,
        'https://plus.unsplash.com/premium_photo-1671547330493-b07d377085ca?auto=format&fit=crop&q=60&w=900&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cGFzdGF8ZW58MHx8MHx8fDA%3D',
        4.5, true, 1),
       (1, 'Margherita Pizza', 'Traditional pizza with tomato sauce, mozzarella, and basil', 9.99,
        'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&q=60&w=900&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGl6emF8ZW58MHx8MHx8fDA%3D',
        4.2, true, 1),
       (1, 'Sushi Roll', 'Assorted sushi rolls with fresh seafood', 14.99,
        'https://plus.unsplash.com/premium_photo-1668143362936-ce8a84410659?auto=format&fit=crop&q=60&w=900&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8c3VzaGklMjByb2xsfGVufDB8fDB8fHww',
        4.7, true, 1),
       (1, 'Teriyaki Chicken', 'Grilled chicken with teriyaki sauce and steamed rice', 10.99,
        'https://images.unsplash.com/photo-1609183480237-ccbb2d7c5772?auto=format&fit=crop&q=60&w=900&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8VGVyaXlha2klMjBDaGlja2VufGVufDB8fDB8fHww',
        4.0, true, 1);

INSERT INTO link_ingredient_dish (dish_id, ingredient_id, quantity_value, quantity_unit)
VALUES (1, 1, 200, 'grams'),
       (1, 2, 50, 'grams'),
       (2, 3, 100, 'grams'),
       (2, 4, 150, 'grams'),
       (3, 1, 200, 'grams'),
       (3, 2, 50, 'grams'),
       (4, 3, 100, 'grams'),
       (4, 4, 150, 'grams');


INSERT INTO rating (user_id, dish_id, rating_value, comments)
VALUES (1, 1, 4.5, 'Delicious dish!'),
       (1, 1, 3.8, 'Good but could be better'),
       (1, 2, 5.0, 'Highly recommended'),
       (1, 2, 4.2, 'Great flavor'),
       (1, 3, 2.5, 'Not my taste'),
       (1, 4, 4.7, 'Amazing presentation');


INSERT INTO orders (user_id, restaurant_id, order_status, order_type, table_number, number_of_people, pickup_time, address, total_price,
                    discount, note, phone)
VALUES (1, 1, 'pending', 'delivery', NULL, NULL, NULL, '123 Main St, Blackburn VIC 3130', 50.00, 0.00, 'No special instructions', '0430000000'),
       (1, 1, 'preparing', 'pickup', NULL, NULL, NULL, NULL, 35.50, 0.00, 'Extra ketchup packets', NULL),
       (1, 2, 'finished', 'dine_in', 1, 2, NULL, NULL, 75.25, 10.00, 'Call upon arrival', '0430000001'),
       (1, 1, 'cancelled', 'dine_in', NULL, NULL, NULL, NULL, 75.25, 10.00, 'Call upon arrival', '0430000002');


INSERT INTO booking (user_id, table_number, reservation_time, status, restaurant_id)
VALUES (1, 1, '2023-10-31 19:30:00+00', 'confirmed', 1);


INSERT INTO link_order_dish (order_id, dish_id, dish_quantity)
VALUES (1, 1, 2),
       (1, 3, 1),
       (2, 2, 3),
       (2, 4, 1),
       (3, 1, 1),
       (4, 4, 2);



