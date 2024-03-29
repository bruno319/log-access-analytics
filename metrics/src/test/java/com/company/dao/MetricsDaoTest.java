package com.company.dao;

import com.company.db.EmbeddedMongoDb;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class MetricsDaoTest {

    MongoClient mongoClient;
    EmbeddedMongoDb db;

    @Before
    public void configureMongoForTests() {
        db = new EmbeddedMongoDb();
        db.start("localhost", 27018);
        mongoClient = db.getMongoClient();
        persistSomeDocs();
    }

    @After
    public void closeConnections() {
        db.close();
    }

    private void persistSomeDocs() {
        MongoCollection<Document> collection = mongoClient.getDatabase("logs").getCollection("logs");
        IntStream.range(0, 5).forEach(i -> collection.insertOne(createDocument("id", 1528732598000L, "url/1", 1)));
        IntStream.range(0, 3).forEach(i -> collection.insertOne(createDocument("id", 992274998000L, "url/2", 2)));
    }

    private Document createDocument(String id, long timestamp, String url, int region) {
        return new Document("uuid", id)
                .append("timestamp", new Date(timestamp))
                .append("url", url)
                .append("region", region);
    }

    @Test
    public void shouldFetchLogsFromDbGroupedByUrl() {
        MetricsDao metricsDao = new MetricsDao(mongoClient);
        Iterable<Document> result = metricsDao.getLogsGroupedByUrl();

        List<Document> resultAsList = iterableToList(result);
        Document firstDocument = resultAsList.get(0);

        assertEquals(2, resultAsList.size());
        assertEquals(firstDocument.getInteger("count"), Integer.valueOf(5));
    }

    @Test
    public void shouldFetchLogsFromDbGroupedByUrlAndRegion() {
        MetricsDao metricsDao = new MetricsDao(mongoClient);
        Iterable<Document> result = metricsDao.getLogsGroupedByRegionAndUrl();

        List<Document> resultAsList = iterableToList(result);
        Document firstDocument = resultAsList.get(0);

        assertEquals(2, resultAsList.size());
        assertEquals(firstDocument.getInteger("count"), Integer.valueOf(5));
        assertEquals(firstDocument.getInteger("region"), Integer.valueOf(1));
    }

    @Test
    public void shouldFetchLogsWithUrlsAccessedBetweenTwoDates() {
        LocalDate date1 = LocalDate.of(2001, 1, 1);
        LocalDate date2 = LocalDate.of(2001, 12, 31);

        MetricsDao metricsDao = new MetricsDao(mongoClient);
        Iterable<Document> result = metricsDao.getLogsGroupedByUrlsAccessedBetween(date1, date2);

        List<Document> resultAsList = iterableToList(result);
        Document firstDocument = resultAsList.get(0);

        assertEquals(1, resultAsList.size());
        assertEquals(firstDocument.getInteger("count"), Integer.valueOf(3));
    }

    @Test
    public void shouldFetchLogsGroupedByMinuteOfTheDay() {
        MetricsDao metricsDao = new MetricsDao(mongoClient);
        Iterable<Document> result = metricsDao.getLogsGroupedByMinute();

        List<Document> resultAsList = iterableToList(result);
        Document firstDocument = resultAsList.get(0);

        assertEquals(1, resultAsList.size());
        assertEquals(firstDocument.getInteger("count"), Integer.valueOf(8));
    }

    private List<Document> iterableToList(Iterable<Document> result) {
        List<Document> resultAsList = new ArrayList<>();
        result.forEach(resultAsList::add);
        return resultAsList;
    }
}
