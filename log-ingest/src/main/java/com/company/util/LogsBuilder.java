package com.company.util;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import com.google.common.flogger.StackSize;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogsBuilder {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static List<Document> build(String text) {
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
        logger.atInfo().log("Destructuring text to build log documents");
        List<Document> documents = new ArrayList<>();

        Stream<String> lines = Arrays.stream(text.split("\\r?\\n"));
        lines.forEach(l -> {
            List<String> dataLog = Arrays.stream(l.split(" "))
                    .map(String::trim)
                    .filter(d -> !d.isEmpty())
                    .collect(Collectors.toList());

            documents.add(
                    new Document("uuid", dataLog.get(2))
                            .append("url", dataLog.get(0))
                            .append("timestamp", new Date(Long.valueOf(dataLog.get(1))))
                            .append("region", Integer.valueOf(dataLog.get(3)))
            );
        });
        return documents;
    }

}
