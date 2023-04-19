INSERT INTO products (title, price)
VALUES ('Bread', 50),
       ('Milk', 80),
       ('Orange', 100),
       ('Cheese', 250),
       ('Tea', 50),
       ('Spaghetti', 70),
       ('Chips', 100),
       ('Juice', 120),
       ('Gum', 30),
       ('Pepsi', 100),
       ('Fanta', 90),
       ('Tomato', 150),
       ('Cucumber', 80),
       ('Chocolate', 60),
       ('Cookie', 70),
       ('Sprite', 90),
       ('Coffee', 200),
       ('Chicken', 220),
       ('Pork', 250),
       ('Beef', 300),
       ('Apple', 60),
       ('PineApple', 250),
       ('Mandarin', 120),
       ('Olives', 120),
       ('Potato', 40)
;


INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_MANAGER'),
       ('ROLE_CLIENT');

INSERT INTO users (login, pass, name, surname, email, role_id)
VALUES ('userAdmin', '{bcrypt}$2a$10$ZImkEeZvRTBX8ElYp/A1guX4rH3YBCFLXePTu.z9AylnUaIGksOBy', 'Anton', 'Skomorokhin', 'admin@mail.ru', 1),
       ('userManager', '{bcrypt}$2a$10$MpaVvecJx/f3sKs/X/wyF.D1yoxTT//fgOtsQIWSdcvqzjI12g7a6', 'Sergey', 'Golovin', 'manager@mail.ru', 2),
       ('userClient', '{bcrypt}$2a$10$8MyaJc6K3vF4da/1r.35w.mOxTo2PZJVvgjJ/MTXZ5patw4CTjIJ2', 'Roman', 'Sidorov', 'customer@mail.ru', 3)
;






