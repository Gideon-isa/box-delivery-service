-- Permissions
INSERT INTO permissions (code, description) VALUES
                                                ('BOX_CREATE',  'Create a new box'),
                                                ('BOX_LOAD',    'Load items into a box'),
                                                ('BOX_READ',    'Read box details, items and battery level'),
                                                ('ITEM_CREATE', 'Create a new standalone item');

-- Roles
INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('OPERATOR'),
                             ('VIEWER');

-- ADMIN: full access
INSERT INTO role_permissions (role_name, permission_code) VALUES
                                                              ('ADMIN', 'BOX_CREATE'),
                                                              ('ADMIN', 'BOX_LOAD'),
                                                              ('ADMIN', 'BOX_READ'),
                                                              ('ADMIN', 'ITEM_CREATE');

-- OPERATOR: can create items, load and read boxes, cannot create new boxes
INSERT INTO role_permissions (role_name, permission_code) VALUES
                                                              ('OPERATOR', 'BOX_LOAD'),
                                                              ('OPERATOR', 'BOX_READ'),
                                                              ('OPERATOR', 'ITEM_CREATE');

-- VIEWER: read-only
INSERT INTO role_permissions (role_name, permission_code) VALUES
    ('VIEWER', 'BOX_READ');

-- Demo user role assignments -- these keycloak_user_id values must match the
-- "sub" claims of the demo users preloaded into the Keycloak realm export.
INSERT INTO user_role_assignments (keycloak_user_id, role_name) VALUES
                                                                    ('11111111-1111-1111-1111-111111111111', 'ADMIN'),    -- demo-admin / demo-admin
                                                                    ('22222222-2222-2222-2222-222222222222', 'OPERATOR'), -- demo-operator / demo-operator
                                                                    ('33333333-3333-3333-3333-333333333333', 'VIEWER');   -- demo-viewer / demo-viewer