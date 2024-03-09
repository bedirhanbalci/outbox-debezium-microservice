package com.demo.accountservice.config.debezium;

import io.debezium.config.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class DebeziumConnectorConfig {

    @Bean
    public Configuration configuration() {
        return Configuration.create()
                .with("name", "outbox-postgres")
                .with("database.server.name", "outbox-poc")
                .with("database.hostname", "localhost")
                .with("database.port", "5432")
                .with("database.user", "postgres")
                .with("database.password", "test")
                .with("database.dbname", "outbox_poc")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("skipped.operations", "t,d")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "offset.dat")
                .with("offset.flush.interval.ms", 60000)
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", "schistory.dat")
                .with("topic.prefix", "test")
                .with("decimal.handling.mode", "string")
                .with("wal_level", "logical")
                .with("plugin.name", "pgoutput")
                .with("table.include.list", "public.outboxs")
                .with("tasks.max", "1")
                .with("tombstones.on.delete", "false")
                .with("route.topic.regex", "")
                .build();
    }
}