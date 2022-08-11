CREATE ROLE debezium WITH REPLICATION LOGIN PASSWORD 'dbzpassword';
CREATE ROLE debezium_replication_group;
GRANT debezium_replication_group TO appuser;
GRANT debezium_replication_group TO debezium;
