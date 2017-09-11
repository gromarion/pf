ALTER TABLE users ADD roles varchar(255);
UPDATE users SET roles = 'EVALUATOR' WHERE users.id IN (SELECT id from users where roles IS NULL);