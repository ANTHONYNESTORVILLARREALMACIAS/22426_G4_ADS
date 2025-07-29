package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.util.IDGenerator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProduccionRepository implements Repository<Produccion> {
    private final MongoCollection<Document> collection;

    public ProduccionRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("producciones");
    }

    @Override
    public void save(Produccion produccion) {
        Document doc = new Document()
                .append("_id", produccion.getId() != null ? produccion.getId() : IDGenerator.generateID("producciones"))
                .append("id_receta", produccion.getIdReceta())
                .append("cantidad", produccion.getCantidad())
                .append("fecha", produccion.getFecha());
        collection.insertOne(doc);
    }

    @Override
    public Produccion findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Produccion(
                doc.getString("_id"),
                doc.getString("id_receta"),
                doc.getDouble("cantidad"),
                doc.getString("fecha")
        );
    }

    @Override
    public List<Produccion> findAll() {
        List<Produccion> producciones = new ArrayList<>();
        for (Document doc : collection.find()) {
            producciones.add(new Produccion(
                    doc.getString("_id"),
                    doc.getString("id_receta"),
                    doc.getDouble("cantidad"),
                    doc.getString("fecha")
            ));
        }
        return producciones;
    }

    @Override
    public void update(Produccion produccion) {
        Document doc = new Document()
                .append("id_receta", produccion.getIdReceta())
                .append("cantidad", produccion.getCantidad())
                .append("fecha", produccion.getFecha());
        collection.updateOne(Filters.eq("_id", produccion.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}