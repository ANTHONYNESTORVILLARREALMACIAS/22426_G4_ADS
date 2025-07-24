package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SeccionRepository implements Repository<Seccion> {
    private final MongoCollection<Document> collection;

    public SeccionRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("secciones");
    }

    @Override
    public void save(Seccion seccion) {
        Document doc = new Document()
                .append("_id", seccion.getId())
                .append("nombre", seccion.getNombre())
                .append("descripcion", seccion.getDescripcion());
        collection.insertOne(doc);
    }

    @Override
    public Seccion findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Seccion(
                doc.getString("_id"),
                doc.getString("nombre"),
                doc.getString("descripcion")
        );
    }

    @Override
    public List<Seccion> findAll() {
        List<Seccion> secciones = new ArrayList<>();
        for (Document doc : collection.find()) {
            secciones.add(new Seccion(
                    doc.getString("_id"),
                    doc.getString("nombre"),
                    doc.getString("descripcion")
            ));
        }
        return secciones;
    }

    @Override
    public void update(Seccion seccion) {
        Document doc = new Document()
                .append("nombre", seccion.getNombre())
                .append("descripcion", seccion.getDescripcion());
        collection.updateOne(Filters.eq("_id", seccion.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}