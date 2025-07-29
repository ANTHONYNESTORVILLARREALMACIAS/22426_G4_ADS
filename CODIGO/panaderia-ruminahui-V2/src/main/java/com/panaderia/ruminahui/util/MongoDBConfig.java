package com.panaderia.ruminahui.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConfig {
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "panaderia_ruminahui";
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // Private constructor to prevent instantiation
    private MongoDBConfig() {
    }

    // Get MongoClient instance (singleton)
    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            synchronized (MongoDBConfig.class) {
                if (mongoClient == null) {
                    mongoClient = MongoClients.create(MONGO_URI);
                }
            }
        }
        return mongoClient;
    }

    // Get MongoDatabase instance
    public static MongoDatabase getDatabase() {
        if (database == null) {
            synchronized (MongoDBConfig.class) {
                if (database == null) {
                    database = getMongoClient().getDatabase(DATABASE_NAME);
                }
            }
        }
        return database;
    }

    // Close MongoClient (optional, call on application shutdown)
    public static void close() {
        if (mongoClient != null) {
            synchronized (MongoDBConfig.class) {
                if (mongoClient != null) {
                    mongoClient.close();
                    mongoClient = null;
                    database = null;
                }
            }
        }
    }
}