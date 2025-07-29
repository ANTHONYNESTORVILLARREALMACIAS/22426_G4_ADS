package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.util.IDGenerator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaRepository implements Repository<DetalleReceta> {
    private final MongoCollection<Document> collection;
    private final MongoDatabase database;

    public DetalleRecetaRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        this.database = database;
        collection = database.getCollection("detalles_receta");
    }

    @Override
    public void save(DetalleReceta detalle) {
        Document doc = new Document()
                .append("_id", detalle.getId() != null ? detalle.getId() : IDGenerator.generateID("detalles_receta"))
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
        return documentToDetalleReceta(doc);
    }

    @Override
    public List<DetalleReceta> findAll() {
        List<DetalleReceta> detalles = new ArrayList<>();
        for (Document doc : collection.find()) {
            detalles.add(documentToDetalleReceta(doc));
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

    public List<DetalleReceta> findByRecetaId(String recetaId) {
        return collection.find(Filters.eq("id_receta", recetaId))
                .map(this::documentToDetalleReceta)
                .into(new ArrayList<>());
    }

    private DetalleReceta documentToDetalleReceta(Document doc) {
        return new DetalleReceta(
                doc.getString("_id"),
                doc.getString("id_receta"),
                doc.getString("id_materia"),
                doc.getDouble("cantidad"),
                doc.getString("unidad_medida")
        );
    }
}