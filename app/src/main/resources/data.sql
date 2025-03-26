INSERT INTO role(id, name) VALUES (1, 'ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name) VALUES (2, 'USER')  ON CONFLICT DO NOTHING;

INSERT INTO "user"(id, username, password) VALUES ('30146443-fd6c-4c16-afd6-9aaca822a54a',
                                                   'admin',
                                                   '$2a$10$g7swg4PRzaJ0/p5.BisFz.5O1knKfGeL0khLDKJ62Y6ZmvO9dQjte')  ON CONFLICT DO NOTHING;

INSERT INTO user_role(user_id, role_id) VALUES ('30146443-fd6c-4c16-afd6-9aaca822a54a', 1) ON CONFLICT DO NOTHING;