INSERT INTO boxes (id, tx_ref, weight_limit, battery_level, state,
                   total_items_weight, is_deleted, created_at, created_by, version) VALUES
                                                                                              ('aaaaaaaa-0000-0000-0000-000000000001', 'BOX-0001', 500, 90, 'IDLE',       120,   false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0),
                                                                                              ('aaaaaaaa-0000-0000-0000-000000000002', 'BOX-0002', 500, 60, 'IDLE',       120,   false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0),
                                                                                              ('aaaaaaaa-0000-0000-0000-000000000003', 'BOX-0003', 500, 15, 'IDLE',       120,   false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0),   -- below 25% battery: NOT available for loading
                                                                                              ('aaaaaaaa-0000-0000-0000-000000000004', 'BOX-0004', 250, 70, 'DELIVERING', 120, false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0);

-- Preloaded, unassigned items -- ready to be loaded into a box via POST /boxes/{id}/load
INSERT INTO items (id, name, weight_grams, code, status, box_id,
                   is_deleted, created_at, created_by, version) VALUES
                                                                    ('bbbbbbbb-0000-0000-0000-000000000001', 'Widget', 100, 'WIDGET_001', 'UNASSIGNED', NULL, false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0),
                                                                    ('bbbbbbbb-0000-0000-0000-000000000002', 'Gadget', 150, 'GADGET_001', 'UNASSIGNED', NULL, false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0),
                                                                    ('bbbbbbbb-0000-0000-0000-000000000003', 'Gizmo',  80,  'GIZMO_001',  'UNASSIGNED', NULL, false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0);

-- An already-assigned item, consistent with BOX-0004 being mid-delivery with 200g loaded
INSERT INTO items (id, name, weight_grams, code, status, box_id,
                   is_deleted, created_at, created_by, version) VALUES
    ('bbbbbbbb-0000-0000-0000-000000000004', 'Doohickey', 200, 'DOOHICKEY_001', 'ASSIGNED',
     'aaaaaaaa-0000-0000-0000-000000000004', false, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000000', 0);

-- A delivery in progress for BOX-0004, carrying the assigned item above
INSERT INTO deliveries (id, location_distance, box_set_speed, box_id, start_time,
                        arrival_time, returned_time, is_delivered, is_returned,
                        is_deleted, created_at, created_by, version) VALUES
    ('cccccccc-0000-0000-0000-000000000001', 3.5, 25, 'aaaaaaaa-0000-0000-0000-000000000004',
     CURRENT_TIMESTAMP, NULL, NULL, false, false, false, CURRENT_TIMESTAMP,
     '00000000-0000-0000-0000-000000000000', 0);

INSERT INTO delivery_items (delivery_id, item_id) VALUES
    ('cccccccc-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000004');