-- Données d'initialisation pour l'application Poseidon
-- Les utilisateurs sont maintenant créés via DataInitializationService

-- Données d'exemple pour BidList
DELETE FROM bidlist;
INSERT INTO bidlist (account, type, bid_quantity) VALUES
('Test Account', 'Type1', 10.0),
('Test Account2', 'Type2', 20.0);

-- Données d'exemple pour Trade
DELETE FROM trade;
INSERT INTO trade (account, type, buy_quantity) VALUES
('Trade Account', 'Type1', 10.0),
('Trade Account2', 'Type2', 20.0);

-- Données d'exemple pour CurvePoint
DELETE FROM curvepoint;
INSERT INTO curvepoint (curve_id, term, "value") VALUES
(1, 10.0, 100.0),
(2, 20.0, 200.0);

-- Données d'exemple pour Rating
DELETE FROM rating;
INSERT INTO rating (moodys_rating, sandprating, fitch_rating, order_number) VALUES
('Aaa', 'AAA', 'AAA', 1),
('Aa1', 'AA+', 'AA+', 2);

-- Données d'exemple pour RuleName
DELETE FROM rulename;
INSERT INTO rulename (name, description, json, template, sql_str, sql_part) VALUES
('Rule Name 1', 'Description 1', 'Json 1', 'Template 1', 'SQL 1', 'SQL Part 1'),
('Rule Name 2', 'Description 2', 'Json 2', 'Template 2', 'SQL 2', 'SQL Part 2');
