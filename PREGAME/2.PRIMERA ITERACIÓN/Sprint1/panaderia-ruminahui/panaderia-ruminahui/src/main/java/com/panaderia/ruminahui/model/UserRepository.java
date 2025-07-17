package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository implements Repository<User> {
    private final MongoCollection<Document> collection;

    public UserRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("users");
    }

    @Override
    public void save(User user) {
        Document doc = new Document()
                .append("_id", user.getId())
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        collection.insertOne(doc);
    }

    @Override
    public User findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new User(doc.getString("_id"), doc.getString("username"), doc.getString("password"));
    }

    public User findByUsername(String username) {
        Document doc = collection.find(Filters.eq("username", username)).first();
        if (doc == null) return null;
        return new User(doc.getString("_id"), doc.getString("username"), doc.getString("password"));
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(new User(doc.getString("_id"), doc.getString("username"), doc.getString("password")));
        }
        return users;
    }

    @Override
    public void update(User user) {
        Document doc = new Document()
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        collection.updateOne(Filters.eq("_id", user.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}