package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.util.IDGenerator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaRepository implements Repository<MateriaPrima> {
    private final MongoCollection<Document> collection;

    public MateriaPrimaRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("materias_primas");
    }

    @Override
    public void save(MateriaPrima materia) {
        Document doc = new Document()
                .append("_id", materia.getId() != null ? materia.getId() : IDGenerator.generateID("materias_primas"))
                .append("nombre", materia.getNombre())
                .append("descripcion", materia.getDescripcion())
                .append("stock_minimo", materia.getStockMinimo())
                .append("unidad_medida", materia.getUnidadMedida())
                .append("id_seccion", materia.getIdSeccion());
        collection.insertOne(doc);
    }

    @Override
    public MateriaPrima findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return documentToMateriaPrima(doc);
    }

    @Override
    public List<MateriaPrima> findAll() {
        List<MateriaPrima> materias = new ArrayList<>();
        for (Document doc : collection.find()) {
            materias.add(documentToMateriaPrima(doc));
        }
        return materias;
    }

    @Override
    public void update(MateriaPrima materia) {
        Document doc = new Document()
                .append("nombre", materia.getNombre())
                .append("descripcion", materia.getDescripcion())
                .append("stock_minimo", materia.getStockMinimo())
                .append("unidad_medida", materia.getUnidadMedida())
                .append("id_seccion", materia.getIdSeccion());
        collection.updateOne(Filters.eq("_id", materia.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public static MateriaPrima documentToMateriaPrima(Document doc) {
        return new MateriaPrima(
                doc.getString("_id"),
                doc.getString("nombre"),
                doc.getString("descripcion"),
                doc.getDouble("stock_minimo"),
                doc.getString("unidad_medida"),
                doc.getString("id_seccion")
        );
    }
}