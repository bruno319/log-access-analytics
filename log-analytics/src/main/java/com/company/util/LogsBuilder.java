package com.company.util;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogsBuilder {

    public static List<Document> build(String text) {
        List<Document> documents = new ArrayList<>();

        Stream<String> lines = text.lines();
        lines.forEach(l -> {
            List<String> dataLog = Arrays.stream(l.split(" "))
                    .filter(d -> !d.isBlank())
                    .collect(Collectors.toList());

            documents.add(
                new Document("uuid", dataLog.get(2))
                    .append("endpoint", dataLog.get(0))
                    .append("timestamp", new Date(Long.valueOf(dataLog.get(1))))
                    .append("region", dataLog.get(3))
            );
        });

        return documents;
    }

}