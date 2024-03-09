package com.demo.accountservice.util;

import com.demo.accountservice.service.OutboxService;
import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.OPERATION;

@Component
@Slf4j
public class DebeziumUtil {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final EmbeddedEngine engine;
    private final OutboxService outboxService;

    @PostConstruct
    public void start() {
        executor.execute(engine);
    }

    @PreDestroy
    public void stop() {
        if (engine != null) {
            engine.stop();
        }
    }

    public DebeziumUtil(Configuration configuration, OutboxService outboxService) {
        this.engine = EmbeddedEngine.create()
                .using(configuration)
                .notifying(this::handleEvent)
                .build();
        this.outboxService = outboxService;
    }

    private void handleEvent(SourceRecord sourceRecord) {
        Struct sourceRecordValue = (Struct) sourceRecord.value();

        var crudOperation = (String) sourceRecordValue.get(OPERATION);

        if (sourceRecordValue != null && (crudOperation == "c" || crudOperation == "u")) {
            Struct struct = (Struct) sourceRecordValue.get(AFTER);
            Map<String, Object> payload = struct.schema().fields().stream()
                    .filter(field -> struct.get(field) != null)
                    .collect(Collectors.toMap(Field::name, field -> struct.get(field)));

            outboxService.debeziumDatabaseChange(payload);
        }
    }
}
