package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaRepository implements Repository<DetalleReceta> {
    private final MongoCollection<Document> collection;

    public DetalleRecetaRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("detalles_receta");
    }

    @Override
    public void save(DetalleReceta detalle) {
        Document doc = new Document()
                .append("_id", detalle.getId())
                .append("id_receta", detalle.getIdReceta())
                .append("id_materia", detalle.getIdMateria())
                .append("cantidad", detalle.getCantidad())
                .append("unidad_medida", detalle.getUnidadMedida());
        collection.insertOne(doc);
    }

    @Override
    public DetalleReceta findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new DetalleReceta(
                doc.getString("_id"),
                doc.getString("id_receta"),
                doc.getString("id_materia"),
                doc.getDouble("cantidad"),
                doc.getString("unidad_medida")
        );
    }

    @Override
    public List<DetalleReceta> findAll() {
        List<DetalleReceta> detalles = new ArrayList<>();
        for (Document doc : collection.find()) {
            detalles.add(new DetalleReceta(
                    doc.getString("_id"),
                    doc.getString("id_receta"),
                    doc.getString("id_materia"),
                    doc.getDouble("cantidad"),
                    doc.getString("unidad_medida")
            ));
        }
        return detalles;
    }

    @Override
    public void update(DetalleReceta detalle) {
        Document doc = new Document()
                .append("id_receta", detalle.getIdReceta())
                .append("id_materia", detalle.getIdMateria())
                .append("cantidad", detalle.getCantidad())
                .append("unidad_medida", detalle.getUnidadMedida());
        collection.updateOne(Filters.eq("_id", detalle.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}