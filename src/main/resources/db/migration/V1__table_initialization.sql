CREATE
EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE user_role AS ENUM ('customer', 'merchant', 'sys_admin', 'driver');
CREATE TYPE booking_status AS ENUM ('pending', 'confirmed', 'cancelled');
CREATE TYPE order_status AS ENUM ('pending', 'preparing', 'finished', 'cancelled', 'in_transit', 'delivered', 'delayed');
CREATE TYPE order_type AS ENUM ('dine_in', 'delivery', 'pickup');
CREATE TYPE week AS ENUM ('monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday');
CREATE TYPE payment_status AS ENUM ('unpaid', 'paid', 'failed');

CREATE TABLE users
(
    user_id          serial PRIMARY KEY       NOT NULL UNIQUE,
    username         varchar(255)             NOT NULL,
    firstname        varchar(255)             NOT NULL,
    lastname         varchar(255)             NOT NULL,
    password         varchar(255)             NOT NULL,
    user_email       varchar(255)             NOT NULL UNIQUE,
    avatar_url       varchar(255)             NOT NULL,
    point            integer,
    user_role        user_role                NOT NULL,
    created_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    google_open_id   varchar(255)                      UNIQUE,
    facebook_open_id varchar(255)                      UNIQUE
);

CREATE TABLE user_address
(
    user_address_id serial PRIMARY KEY       NOT NULL UNIQUE,
    user_id         integer                  NOT NULL REFERENCES users (user_id),
    address         varchar(255)             NOT NULL,
    is_default      boolean                  NOT NULL DEFAULT FALSE,
    created_time    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ingredient
(
    ingredient_id serial PRIMARY KEY       NOT NULL UNIQUE,
    name          varchar(255)             NOT NULL,
    unit          varchar(50)              NOT NULL,
    created_time  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurant
(
    restaurant_id  serial PRIMARY KEY       NOT NULL UNIQUE,
    name           varchar(255)             NOT NULL,
    description    text                     NOT NULL,
    address        varchar(255)             NOT NULL,
    contact_number varchar(255)             NOT NULL,
    abn            varchar(255)             NOT NULL,
    owner_name     varchar(255)             NOT NULL,
    owner_mobile   varchar(255)             NOT NULL,
    owner_address  varchar(255)             NOT NULL,
    owner_email    varchar(255)             NOT NULL,
    owner_crn      varchar(255)             NOT NULL,
    average_rating decimal(3, 1),
    created_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE opening_hours
(
    opening_hours_id serial PRIMARY KEY       NOT NULL UNIQUE,
    restaurant_id    integer REFERENCES restaurant (restaurant_id),
    day_of_week      week                     NOT NULL,
    opening_time     varchar(255)             NOT NULL,
    closing_time     varchar(255)             NOT NULL,
    created_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dish
(
    dish_id       serial PRIMARY KEY       NOT NULL UNIQUE,
    restaurant_id integer                  NOT NULL,
    dish_name     varchar(255)             NOT NULL,
    description   varchar(255)             NOT NULL,
    price         decimal(5, 2)            NOT NULL,
    image_url     varchar(255)             NOT NULL,
    category      varchar(255)             NOT NULL,
    rating        decimal(3, 1),
    availability  boolean                  NOT NULL,
    created_time  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE link_ingredient_dish
(
    link_ingredient_dish_id serial PRIMARY KEY       NOT NULL UNIQUE,
    dish_id                 integer                  NOT NULL REFERENCES dish (dish_id),
    ingredient_id           integer                  NOT NULL REFERENCES ingredient (ingredient_id),
    quantity_value          decimal(10, 2)           NOT NULL,
    quantity_unit           varchar(50)              NOT NULL,
    created_time            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rating
(
    rating_id    serial PRIMARY KEY       NOT NULL UNIQUE,
    user_id      integer                  NOT NULL REFERENCES users (user_id),
    dish_id      integer                  NOT NULL REFERENCES dish (dish_id),
    rating_value decimal                  NOT NULL,
    comments     varchar,
    created_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders
(
    order_id     serial PRIMARY KEY       NOT NULL UNIQUE,
    user_id      integer REFERENCES users (user_id),
    restaurant_id      integer REFERENCES restaurant (restaurant_id) NOT NULL,
    order_status order_status             NOT NULL,
    order_type   order_type               NOT NULL,
    table_number integer,
    number_of_people integer,
    pickup_time  TIMESTAMP WITH TIME ZONE,
    address      varchar(255),
    total_price  decimal(10, 2)           NOT NULL,
    discount     decimal(10, 2)           NOT NULL,
    note         varchar(255),
    phone         varchar(255),
    created_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE booking
(
    booking_id       serial PRIMARY KEY       NOT NULL UNIQUE,
    user_id          integer                  NOT NULL REFERENCES users (user_id),
    table_number     integer                  NOT NULL,
    reservation_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status           booking_status           NOT NULL,
    restaurant_id    integer                  NOT NULL REFERENCES restaurant (restaurant_id),
    created_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE link_order_dish
(
    link_order_dish_id serial PRIMARY KEY       NOT NULL UNIQUE,
    order_id           integer                  NOT NULL REFERENCES orders (order_id),
    dish_id            integer                  NOT NULL REFERENCES dish (dish_id),
    dish_quantity      integer                  NOT NULL,
    created_time       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);


create TABLE category
(
    category_id serial primary key NOT NULL UNIQUE,
    restaurant_id integer NOT NULL,
    category_name varchar(255) NOT NULL UNIQUE,
    created_time       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments
(
    payment_id     serial PRIMARY KEY          NOT NULL UNIQUE,
    order_id       integer REFERENCES orders (order_id),
    payment_status payment_status              NOT NULL,
    payment_method varchar(255),
    stripe_payment_id varchar(255)             NOT NULL ,
    amount         decimal(10, 2)              NOT NULL,
    currency       varchar(3)                  NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE dish
    ADD COLUMN category_id integer REFERENCES category(category_id);

ALTER TABLE dish DROP COLUMN IF EXISTS category;

