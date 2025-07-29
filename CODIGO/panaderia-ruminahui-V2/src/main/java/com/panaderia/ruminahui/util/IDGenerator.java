package com.panaderia.ruminahui.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class IDGenerator {
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "panaderia_ruminahui";
    private static final String COLLECTION_NAME = "counters";

    public static String generateID(String collectionName) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> counters = database.getCollection(COLLECTION_NAME);

            Bson filter = eq("_id", collectionName);
            Bson update = inc("sequence", 1);
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER)
                    .upsert(true);

            Document result = counters.findOneAndUpdate(filter, update, options);
            int sequence = result != null ? result.getInteger("sequence") : 1;

            String prefix;
            switch (collectionName) {
                case "secciones": prefix = "SEC"; break;
                case "materias_primas": prefix = "MAT"; break;
                case "stocks": prefix = "STK"; break;
                case "recetas": prefix = "REC"; break;
                case "detalles_receta": prefix = "DET"; break;
                case "producciones": prefix = "PRO"; break;
                case "usuarios": prefix = "USR"; break;
                default: prefix = "ID";
            }
            return String.format("%s-%03d", prefix, sequence);
        }
    }
}