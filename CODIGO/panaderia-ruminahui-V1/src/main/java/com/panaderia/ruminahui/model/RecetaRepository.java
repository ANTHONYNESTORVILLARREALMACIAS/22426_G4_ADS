package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class RecetaRepository implements Repository<Receta> {
    private final MongoCollection<Document> collection;

    public RecetaRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("recetas");
    }

    @Override
    public void save(Receta receta) {
        Document doc = new Document()
                .append("_id", receta.getId())
                .append("nombre", receta.getNombre())
                .append("descripcion", receta.getDescripcion());
        collection.insertOne(doc);
    }

    @Override
    public Receta findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Receta(
                doc.getString("_id"),
                doc.getString("nombre"),
                doc.getString("descripcion")
        );
    }

    @Override
    public List<Receta> findAll() {
        List<Receta> recetas = new ArrayList<>();
        for (Document doc : collection.find()) {
            recetas.add(new Receta(
                    doc.getString("_id"),
                    doc.getString("nombre"),
                    doc.getString("descripcion")
            ));
        }
        return recetas;
    }

    @Override
    public void update(Receta receta) {
        Document doc = new Document()
                .append("nombre", receta.getNombre())
                .append("descripcion", receta.getDescripcion());
        collection.updateOne(Filters.eq("_id", receta.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}