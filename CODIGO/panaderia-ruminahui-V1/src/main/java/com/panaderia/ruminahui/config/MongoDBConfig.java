package com.panaderia.ruminahui.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.logging.Logger;

public class MongoDBConfig {
    private static final Logger LOGGER = Logger.getLogger(MongoDBConfig.class.getName());
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyToConnectionPoolSettings(builder -> builder.maxSize(20))
                        .build();
                mongoClient = MongoClients.create(settings);
                database = mongoClient.getDatabase("panaderia_ruminahui");
                database.listCollectionNames().first(); // Test connection
                LOGGER.info("Successfully connected to MongoDB database: panaderia_ruminahui");
                initializeAdminUser();
            } catch (MongoException e) {
                LOGGER.severe("Failed to connect to MongoDB: " + e.getMessage());
                throw new RuntimeException("Failed to connect to MongoDB: " + e.getMessage(), e);
            }
        }
        return database;
    }

    private static void initializeAdminUser() {
        MongoCollection<Document> collection = database.getCollection("users");
        Document admin = collection.find(Filters.eq("username", "admin")).first();
        if (admin == null) {
            Document adminDoc = new Document()
                    .append("_id", "admin-" + java.util.UUID.randomUUID().toString())
                    .append("username", "admin")
                    .append("password", "admin123")
                    .append("nombre", "Administrador")
                    .append("email", "admin@panaderia.com");
            collection.insertOne(adminDoc);
            LOGGER.info("Admin user created: username=admin, password=admin123, nombre=Administrador, email=admin@panaderia.com");
        } else {
            LOGGER.info("Admin user already exists: username=admin");
        }
    }

    public static void close() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
                LOGGER.info("MongoDB connection closed successfully");
            } catch (Exception e) {
                LOGGER.severe("Error closing MongoDB connection: " + e.getMessage());
            }
        }
    }
}