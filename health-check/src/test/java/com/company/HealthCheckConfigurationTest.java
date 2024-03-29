package com.company;

import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HealthCheckConfigurationTest {

    HealthCheckConfiguration configuration;

    @Before
    public void initHealthCheckConfiguration() {
        configuration = new HealthCheckConfiguration();
    }

    @Test
    public void shouldReturnMongoClient() {
        MongoClient mongoClient = configuration.getMongoClient();
        assertNotNull(mongoClient);
    }

    @Test
    public void shouldReturnHttpClient() {
        Client httpClient = configuration.getHttpClient();
        assertNotNull(httpClient);
    }

    @Test
    public void shouldReturnDefaultName() {
        String defaultName = configuration.getDefaultName();
        assertEquals("HealthCheck", defaultName);
    }
}
