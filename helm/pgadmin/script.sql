CREATE USER onetrade WITH PASSWORD 'onetrade';
GRANT CONNECT ON DATABASE "flowable" TO onetrade;
GRANT USAGE ON SCHEMA public TO onetrade;

GRANT SELECT ON pgn_cle TO onetrade;
GRANT SELECT ON pgn_cle_request TO onetrade;
GRANT SELECT ON pgn_cle_modification TO onetrade;

-- Revoke:
-- REVOKE SELECT ON export_remittance FROM onetrade;
