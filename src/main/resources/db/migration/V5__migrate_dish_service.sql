ALTER TABLE orders ADD COLUMN order_dishes JSONB NOT NULL DEFAULT '[]';

UPDATE orders o
SET order_dishes = subquery.order_dishes_json
FROM (
         SELECT o.order_id,
                jsonb_agg(
                        jsonb_build_object(
                                'dishId', od.dish_id,
                                'dishName', d.dish_name,
                                'dishQuantity', od.dish_quantity,
                                'dishPrice', d.price,
                                'isDeleted', false
                        )
                ) AS order_dishes_json
         FROM link_order_dish od
                  JOIN dish d ON od.dish_id = d.dish_id
                  JOIN orders o ON od.order_id = o.order_id
         GROUP BY o.order_id
     ) AS subquery
WHERE o.order_id = subquery.order_id;


DROP TABLE rating;

DROP TABLE link_ingredient_dish;

DROP TABLE link_order_dish;

DROP TABLE ingredient;

DROP TABLE dish;

DROP TABLE category;