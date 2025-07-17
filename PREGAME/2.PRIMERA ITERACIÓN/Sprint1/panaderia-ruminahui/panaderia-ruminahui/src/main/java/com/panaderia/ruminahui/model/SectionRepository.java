package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SectionRepository implements Repository<Section> {
    private final MongoCollection<Document> collection;

    public SectionRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("sections");
    }

    @Override
    public void save(Section section) {
        Document doc = new Document()
                .append("_id", section.getId())
                .append("name", section.getName())
                .append("description", section.getDescription());
        collection.insertOne(doc);
    }

    @Override
    public Section findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Section(doc.getString("_id"), doc.getString("name"), doc.getString("description"));
    }

    @Override
    public List<Section> findAll() {
        List<Section> sections = new ArrayList<>();
        for (Document doc : collection.find()) {
            sections.add(new Section(doc.getString("_id"), doc.getString("name"), doc.getString("description")));
        }
        return sections;
    }

    @Override
    public void update(Section section) {
        Document doc = new Document()
                .append("name", section.getName())
                .append("description", section.getDescription());
        collection.updateOne(Filters.eq("_id", section.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}