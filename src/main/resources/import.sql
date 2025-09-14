INSERT INTO users (id, username, password, role, points, multiplication_enabled, addition_enabled, addition_max_digits) VALUES (1, 'Admin', '123456', 'ADMIN', 100.00, false, false, 0);
INSERT INTO users (id, username, password, role, points, multiplication_enabled, addition_enabled, addition_max_digits) VALUES (2, 'Santiago', '', 'CHILD', 25.50, true, false, 0);
INSERT INTO users (id, username, password, role, points, multiplication_enabled, addition_enabled, addition_max_digits) VALUES (3, 'Catalina', '', 'CHILD', 0.00, true, true, 1);

INSERT INTO game_config ( id, description, time_limit_seconds, enable_timer, range_ini, range_fin, range_tables, percentage_to_points, enable_all_tables, number_questions, number_answers) VALUES (1, 'MULTIPLICATION', 60, true, 2, 5, '4,6,7,8,9,12', 20.00, false, 20, 4);
INSERT INTO game_config ( id, description, time_limit_seconds, enable_timer, range_ini, range_fin, range_tables, percentage_to_points, enable_all_tables, number_questions, number_answers) VALUES (2, 'ADDITION', 0, false, 0, 0, '', 20.00, false, 10, 4);
