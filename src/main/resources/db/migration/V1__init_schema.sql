CREATE TABLE boxes (
                       id                        UUID PRIMARY KEY,
                       tx_ref                    VARCHAR(20) NOT NULL UNIQUE,
                       weight_limit        DOUBLE PRECISION NOT NULL CHECK (weight_limit > 0 AND weight_limit <= 500),
                       battery_level        DOUBLE PRECISION NOT NULL CHECK (battery_level BETWEEN 0 AND 100),
                       state                     VARCHAR(20) NOT NULL,
                       total_items_weight  DOUBLE PRECISION NOT NULL DEFAULT 0,
                       is_deleted                BOOLEAN NOT NULL DEFAULT false,
                       created_at                TIMESTAMP NOT NULL,
                       created_by                UUID NOT NULL,
                       modified_at               TIMESTAMP,
                       modified_by               UUID,
                       version                   BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_boxes_id ON boxes (id);
CREATE INDEX idx_boxes_tx_ref ON boxes (tx_ref);

CREATE INDEX idx_boxes_state_battery ON boxes (state, battery_level);

-- Items (own aggregate, referenced by box_id, not embedded)
CREATE TABLE items (
                       id           UUID PRIMARY KEY,
                       name         VARCHAR(100) NOT NULL,
                       weight_grams DOUBLE PRECISION NOT NULL CHECK (weight_grams > 0),
                       code         VARCHAR(50) NOT NULL UNIQUE,
                       status       VARCHAR(20) NOT NULL,
                       box_id       UUID REFERENCES boxes (id) ON DELETE SET NULL,
                       is_deleted   BOOLEAN NOT NULL DEFAULT false,
                       created_at   TIMESTAMP NOT NULL,
                       created_by   UUID NOT NULL,
                       modified_at  TIMESTAMP,
                       modified_by  UUID,
                       version      BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_items_id ON items (id);
CREATE INDEX idx_items_code ON items (code);
CREATE INDEX idx_items_box_id ON items (box_id);
CREATE INDEX idx_items_status ON items (status);

-- Deliveries
CREATE TABLE deliveries (
                            id                 UUID PRIMARY KEY,
                            location_distance  DOUBLE PRECISION NOT NULL CHECK (location_distance > 0),
                            box_set_speed       DOUBLE PRECISION NOT NULL CHECK (box_set_speed > 0),
                            box_id             UUID NOT NULL REFERENCES boxes (id),
                            start_time         TIMESTAMP NOT NULL,
                            arrival_time       TIMESTAMP,
                            returned_time      TIMESTAMP,
                            is_delivered       BOOLEAN NOT NULL DEFAULT false,
                            is_returned        BOOLEAN NOT NULL DEFAULT false,
                            is_deleted         BOOLEAN NOT NULL DEFAULT false,
                            created_at         TIMESTAMP NOT NULL,
                            created_by         UUID NOT NULL,
                            modified_at        TIMESTAMP,
                            modified_by        UUID,
                            version            BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_deliveries_box_id ON deliveries (box_id);
CREATE INDEX idx_deliveries_id ON deliveries (id);

-- Delivery -> Item references (plain @ElementCollection, no identity of its own)
CREATE TABLE delivery_items (
                                delivery_id UUID NOT NULL REFERENCES deliveries (id) ON DELETE CASCADE,
                                item_id     UUID NOT NULL,
                                PRIMARY KEY (delivery_id, item_id)
);

-- RBAC (application-managed, independent of Keycloak) -- unaffected by domain changes
CREATE TABLE permissions (
                             code        VARCHAR(64) PRIMARY KEY,
                             description VARCHAR(255)
);

CREATE TABLE roles (
                       name VARCHAR(64) PRIMARY KEY
);

CREATE TABLE role_permissions (
                                  role_name       VARCHAR(64) NOT NULL REFERENCES roles (name) ON DELETE CASCADE,
                                  permission_code VARCHAR(64) NOT NULL REFERENCES permissions (code) ON DELETE CASCADE,
                                  PRIMARY KEY (role_name, permission_code)
);

CREATE TABLE user_role_assignments (
                                       keycloak_user_id UUID NOT NULL,
                                       role_name        VARCHAR(64) NOT NULL REFERENCES roles (name) ON DELETE CASCADE,
                                       PRIMARY KEY (keycloak_user_id, role_name)
);