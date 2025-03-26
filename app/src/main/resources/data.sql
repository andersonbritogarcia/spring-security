CREATE TABLE IF NOT EXISTS public.role
(
    id   integer NOT NULL,
    name character varying(10) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."user"
(
    id       uuid NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(256) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uk_user_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS public.user_role
(
    user_id uuid NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public."user" (id) MATCH SIMPLE
                                                                                     ON UPDATE NO ACTION
                                                                                     ON DELETE NO ACTION,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.role (id) MATCH SIMPLE
                                                                                   ON UPDATE NO ACTION
                                                                                   ON DELETE NO ACTION
);

INSERT INTO public.role(id, name) VALUES (1, 'ADMIN') ON CONFLICT(id) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO public.role(id, name) VALUES (2, 'USER')  ON CONFLICT(id) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO public."user"(id, username, password)
VALUES ('0195d477-89ff-7b6d-ba54-94fdfb5a6ac6','admin','$2a$10$g7swg4PRzaJ0/p5.BisFz.5O1knKfGeL0khLDKJ62Y6ZmvO9dQjte') ON CONFLICT DO NOTHING;

INSERT INTO public."user"(id, username, password)
VALUES ('0195d477-b5bd-7635-b41a-6426965d62e9','user','$2a$10$g7swg4PRzaJ0/p5.BisFz.5O1knKfGeL0khLDKJ62Y6ZmvO9dQjte') ON CONFLICT DO NOTHING;

INSERT INTO public."user"(id, username, password)
VALUES ('0195d477-db64-7ece-a042-032fc2e3004d','master','$2a$10$g7swg4PRzaJ0/p5.BisFz.5O1knKfGeL0khLDKJ62Y6ZmvO9dQjte') ON CONFLICT DO NOTHING;

INSERT INTO public.user_role(user_id, role_id) VALUES ('0195d477-89ff-7b6d-ba54-94fdfb5a6ac6', 1) ON CONFLICT DO NOTHING;
INSERT INTO public.user_role(user_id, role_id) VALUES ('0195d477-b5bd-7635-b41a-6426965d62e9', 2) ON CONFLICT DO NOTHING;
INSERT INTO public.user_role(user_id, role_id) VALUES ('0195d477-db64-7ece-a042-032fc2e3004d', 1) ON CONFLICT DO NOTHING;
INSERT INTO public.user_role(user_id, role_id) VALUES ('0195d477-db64-7ece-a042-032fc2e3004d', 2) ON CONFLICT DO NOTHING;