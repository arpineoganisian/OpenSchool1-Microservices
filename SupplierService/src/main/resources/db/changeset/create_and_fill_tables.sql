CREATE TABLE category (
                          id      INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                          name    VARCHAR(255) NOT NULL
);

CREATE TABLE product (
                         id          INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                         category_id INT REFERENCES category (id) ON DELETE SET NULL,
                         name        VARCHAR(255) NOT NULL,
                         description TEXT,
                         price       NUMERIC(10, 2) NOT NULL
);

INSERT INTO category(name)
VALUES ('Electronics'),
       ('Home'),
       ('Sports'),
       ('Clothing'),
       ('Shoes'),
       ('Books');

INSERT INTO product(category_id, name, description, price)
VALUES (1, 'Camera', 'This digital camera can be used for making videos for YouTube features with 4K video resolution', 112.99),
       (1, 'Microwave', 'The best microwave oven for your kitchen', 89.99),
       (2, 'Table Cover', 'Flower jacquard damask tablecloths are made of high quality polyester fabric material. Tablecloth touch soft and skin-friendly, rectangle tables can be well protected.', 15.99),
       (2, 'Curtains', 'The best curtains for your living room', 29.99),
       (3, 'Yoga Mat', null, 19.56),
       (3, 'Yoga Pants', 'The best yoga pants for your yoga classes', 19.99),
       (4, 'T-Shirt', 'The best t-shirt for your daily use', 9.99),
       (5, 'Nike Air Max', 'The Nike Air Max 97 Men’s Shoe keeps the sneaker favorite going strong with the same design details that made it famous: water-ripple lines, reflective piping and full-length Max Air cushioning.', 169.99),
       (5, 'Nike Air Force', 'The best shoes for your daily use', 129.99),
       (6, 'The Alchemist', 'The Alchemist is a novel by Brazilian author Paulo Coelho that was first published in 1988. Originally written in Portuguese, it became a widely translated international bestseller.', 26),
       (6, 'Math Refresher for Adults', 'Math Refresher for Adults: The Perfect Solution', 36.57),
       (6, 'The Da Vinci Code', 'The best book for your daily reading', 19.99);
