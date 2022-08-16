-- Use this role for debezium CDC
CREATE ROLE debezium WITH REPLICATION LOGIN PASSWORD 'dbzpassword';

-- Emit record INSERT, UPDATE, DELETE as event
CREATE PUBLICATION dbz_publication FOR ALL TABLES;

-- For creating snapshot, SELECT should be permitted
GRANT SELECT ON ALL TABLES IN SCHEMA public TO debezium;
