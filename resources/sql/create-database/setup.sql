
CREATE USER keycloak PASSWORD 'keycloak';
CREATE DATABASE keycloak;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;

CREATE USER onetrade WITH PASSWORD 'onetrade';
GRANT CONNECT ON DATABASE flowable TO onetrade;
GRANT USAGE ON SCHEMA public TO onetrade;
