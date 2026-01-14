-- =====================================
-- Clean existing data (order matters)
-- =====================================

-- Delete role mappings first (FK safe)
DELETE FROM user_roles
WHERE user_id IN (
    SELECT id FROM users
    WHERE username IN ('admin', 'user', 'ops')
)
OR role_id IN (
    SELECT id FROM roles
    WHERE name IN ('ADMIN', 'USER', 'OPS')
);

-- Delete users
DELETE FROM users
WHERE username IN ('admin', 'user', 'ops');

-- Delete roles
DELETE FROM roles
WHERE name IN ('ADMIN', 'USER', 'OPS');

-- Insert roles
INSERT INTO roles (name)
VALUES 
  ('ADMIN'),
  ('USER'),
  ('OPS');

-- Insert users
INSERT INTO users (username, password, enabled)
VALUES
  ('admin', '$2a$10$20bp9c1NYB9hJ1zBelSkt.e57Cu5BFao9rQD6hnUrvQZka8DqjLkm', true),
  ('user',  '$2a$10$GWBxZYUmFOKf3/gSbBtUd.3KDM9Gjs9Fz.h5jzeH9GOCXDa5AOHbm', true),
  ('ops',   '$2a$10$LlQtDCkq1MmONFpiwGrTkuj3cFck8RO5RPJlOVNdUr5Gma7UVjPv.', true);

-- Assign roles to users (no hardcoded IDs)
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r
  ON (
       (u.username = 'admin' AND r.name = 'ADMIN') OR
       (u.username = 'user'  AND r.name = 'USER')  OR
       (u.username = 'ops'   AND r.name = 'OPS')
     );