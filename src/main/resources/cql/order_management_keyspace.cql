//Order Management schema.
CREATE KEYSPACE order_management WITH replication = { 'class': 'NetworkTopologyStrategy', ‘Cassandra’: 3};

ALTER KEYSPACE order_management WITH replication = { 'class': 'NetworkTopologyStrategy', 'Cassandra': 3, 'Analytics': 1, 'Solr' : 1};
