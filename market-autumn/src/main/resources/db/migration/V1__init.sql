SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE public.carts (
                              id integer NOT NULL,
                              user_id integer NOT NULL
);


ALTER TABLE public.carts OWNER TO postgres;


ALTER TABLE public.carts ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.carts_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );


CREATE TABLE public.carts_products (
                                       id integer NOT NULL,
                                       cart_id integer NOT NULL,
                                       product_id integer NOT NULL,
                                       quantity integer NOT NULL
);


ALTER TABLE public.carts_products OWNER TO postgres;


ALTER TABLE public.carts_products ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.carts_products_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );



CREATE TABLE public.orders (
                               id integer NOT NULL,
                               user_id integer NOT NULL,
                               date timestamp without time zone NOT NULL,
                               price integer NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;


ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );



CREATE TABLE public.orders_products (
                                        id integer NOT NULL,
                                        order_id integer NOT NULL,
                                        product_id integer NOT NULL,
                                        quantity integer NOT NULL
);


ALTER TABLE public.orders_products OWNER TO postgres;


ALTER TABLE public.orders_products ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_products_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );




CREATE TABLE public.products (
                                 id integer NOT NULL,
                                 title character varying(150) NOT NULL,
                                 price integer NOT NULL
);


ALTER TABLE public.products OWNER TO postgres;



ALTER TABLE public.products ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.products_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );




CREATE TABLE public.roles (
                              id integer NOT NULL,
                              name character varying(150) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;



ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );




CREATE TABLE public.users (
                              id integer NOT NULL,
                              login character varying(50) NOT NULL,
                              pass character varying(300) NOT NULL,
                              name character varying(150) NOT NULL,
                              surname character varying(150) NOT NULL,
                              email character varying(150) NOT NULL,
                              role_id integer NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;



ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );




ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.carts_products
    ADD CONSTRAINT carts_products_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.carts_products
    ADD CONSTRAINT carts_products_cart_id_fk FOREIGN KEY (cart_id) REFERENCES public.carts(id) NOT VALID;



ALTER TABLE ONLY public.carts_products
    ADD CONSTRAINT carts_products_product_id_fk FOREIGN KEY (product_id) REFERENCES public.products(id) NOT VALID;



ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;



ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_order_id_fk FOREIGN KEY (order_id) REFERENCES public.orders(id) NOT VALID;



ALTER TABLE ONLY public.orders_products
    ADD CONSTRAINT orders_products_product_id_fk FOREIGN KEY (product_id) REFERENCES public.products(id) NOT VALID;



ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;


ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_role_id_fk FOREIGN KEY (role_id) REFERENCES public.roles(id) NOT VALID;


