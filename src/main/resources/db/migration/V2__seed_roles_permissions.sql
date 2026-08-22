-- Permissions
INSERT INTO permissions (code, description) VALUES
                                                ('BOX_CREATE',  'Create a new box'),
                                                ('BOX_LOAD',    'Load items into a box'),
                                                ('BOX_READ',    'Read box details, items and battery level'),
                                                ('BOX_GET',    'Get box'),
                                                ('BOX_DELETE', 'delete box'),
                                                ('BOX_DISPATCH', 'Dispatch box to remote location'),
                                                ('BOX_RETURN', 'initiate returning of box from location to base'),
                                                ('BOX_RETURNED', 'box returned to based'),
                                                ('BOX_AVAILABLE', 'Get available boxes'),
                                                ('ITEM_CREATE', 'Create a new standalone item'),
                                                ('ITEM_DELETE', 'Delete item'),
                                                ('ITEM_READ', 'read items'),
                                                ('ITEM_RETURNED', 'read items'),
                                                ('ITEM_UPDATE', 'Update item'),
                                                ('DELIVERY_GET', 'Get delivery record'),
                                                ('DELIVERY_DELIVERED', 'Accept delivery at destination'),
                                                ('DELIVERY_ESTIMATE', 'Estimate delivery time');


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
                                                              ('ADMIN', 'BOX_DISPATCH'),
                                                              ('ADMIN', 'BOX_RETURN'),
                                                              ('ADMIN', 'BOX_RETURNED'),
                                                              ('ADMIN', 'BOX_GET'),
                                                              ('ADMIN', 'BOX_AVAILABLE'),
                                                              ('ADMIN', 'BOX_DELETE'),
                                                              ('ADMIN', 'ITEM_CREATE'),
                                                              ('ADMIN', 'ITEM_DELETE'),
                                                              ('ADMIN', 'ITEM_UPDATE'),
                                                              ('ADMIN', 'ITEM_RETURNED'),
                                                              ('ADMIN', 'ITEM_READ'),
                                                              ('ADMIN', 'DELIVERY_GET'),
                                                              ('ADMIN', 'DELIVERY_DELIVERED'),
                                                              ('ADMIN', 'DELIVERY_ESTIMATE');



-- OPERATOR: can create items, load and read boxes, cannot create new boxes
INSERT INTO role_permissions (role_name, permission_code) VALUES
                                                              ('OPERATOR', 'BOX_LOAD'),
                                                              ('OPERATOR', 'BOX_READ'),
                                                              ('OPERATOR', 'BOX_AVAILABLE'),
                                                              ('OPERATOR', 'BOX_DISPATCH'),
                                                              ('OPERATOR', 'BOX_RETURNED'),
                                                              ('OPERATOR', 'BOX_RETURN'),
                                                              ('OPERATOR', 'ITEM_CREATE'),
                                                              ('OPERATOR', 'ITEM_RETURNED'),
                                                              ('OPERATOR', 'ITEM_READ'),
                                                              ('OPERATOR', 'ITEM_UPDATE'),
                                                              ('OPERATOR', 'DELIVERY_GET'),
                                                              ('OPERATOR', 'DELIVERY_ESTIMATE');

-- VIEWER: read-only
INSERT INTO role_permissions (role_name, permission_code) VALUES
    ('VIEWER', 'BOX_READ'),
    ('VIEWER', 'BOX_RETURN'),
    ('VIEWER', 'DELIVERY_GET'),
    ('VIEWER', 'DELIVERY_DELIVERED');

-- Demo user role assignments -- these keycloak_user_id values must match the
-- "sub" claims of the demo users preloaded into the Keycloak realm export.
INSERT INTO user_role_assignments (keycloak_user_id, role_name) VALUES
                                                                    ('11111111-1111-1111-1111-111111111111', 'ADMIN'),    -- demo-admin / demo-admin
                                                                    ('22222222-2222-2222-2222-222222222222', 'OPERATOR'), -- demo-operator / demo-operator
                                                                    ('33333333-3333-3333-3333-333333333333', 'VIEWER');   -- demo-viewer / demo-viewer