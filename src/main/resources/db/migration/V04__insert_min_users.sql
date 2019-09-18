INSERT INTO user(name, password) VALUES ('admin', '$2a$10$6t7ueTPA.z.gTRhgBB8iy.8YKe75fHRv5HIpk3lDe6cc9IXE5oA0e');
INSERT INTO user(name, password) VALUES ('cleverton', '$2a$10$6t7ueTPA.z.gTRhgBB8iy.8YKe75fHRv5HIpk3lDe6cc9IXE5oA0e');

INSERT INTO user_role (user_id, role_id ) VALUES (SELECT id FROM USER WHERE name = 'admin',
    SELECT id FROM role WHERE name = 'ADMIN');
INSERT INTO user_role (user_id, role_id ) VALUES (SELECT id FROM USER WHERE name = 'cleverton',
    SELECT id FROM role WHERE name = 'MANAGER');

INSERT INTO user_privilege (user_id, privilege_id ) VALUES (SELECT id FROM USER WHERE name = 'cleverton',
    SELECT id FROM privilege WHERE name = 'UPDATE_VEHICLE');
INSERT INTO user_privilege (user_id, privilege_id ) VALUES (SELECT id FROM USER WHERE name = 'cleverton',
    SELECT id FROM privilege WHERE name = 'REMOVE_VEHICLE');