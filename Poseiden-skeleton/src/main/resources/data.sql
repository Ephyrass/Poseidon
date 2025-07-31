-- Données d'initialisation pour l'application Poseidon

-- Suppression des utilisateurs existants pour éviter les doublons
DELETE FROM users;

-- Insertion des utilisateurs avec mots de passe hashés BCrypt
-- Mot de passe : "Password123!"
INSERT INTO users (username, password, fullname, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb97VSqyPOXneoH.BCLrELstMWfKvYoKpTJmKW/FG', 'Administrator', 'ADMIN'),
('user', '$2a$10$N.zmdr9k7uOCQb97VSqyPOXneoH.BCLrELstMWfKvYoKpTJmKW/FG', 'Standard User', 'USER');

-- Données d'exemple pour BidList (utiliser les noms de colonnes générés par Hibernate)
INSERT INTO bidlist (account, type, bid_quantity) VALUES
('Test Account', 'Type1', 10.0),
('Test Account2', 'Type2', 20.0);

-- Données d'exemple pour Trade (utiliser les noms de colonnes générés par Hibernate)
INSERT INTO trade (account, type, buy_quantity) VALUES
('Trade Account', 'Type1', 10.0),
('Trade Account2', 'Type2', 20.0);

-- Données d'exemple pour CurvePoint (utiliser les noms de colonnes générés par Hibernate)
INSERT INTO curvepoint (curve_id, term, "value") VALUES
(1, 10.0, 100.0),
(2, 20.0, 200.0);

-- Données d'exemple pour Rating (utiliser les noms de colonnes générés par Hibernate)
INSERT INTO rating (moodys_rating, sandprating, fitch_rating, order_number) VALUES
('Aaa', 'AAA', 'AAA', 1),
('Aa1', 'AA+', 'AA+', 2);

-- Données d'exemple pour RuleName (utiliser les noms de colonnes générés par Hibernate)
INSERT INTO rulename (name, description, json, template, sql_str, sql_part) VALUES
('Rule Name 1', 'Description 1', 'Json 1', 'Template 1', 'SQL 1', 'SQL Part 1'),
('Rule Name 2', 'Description 2', 'Json 2', 'Template 2', 'SQL 2', 'SQL Part 2');
