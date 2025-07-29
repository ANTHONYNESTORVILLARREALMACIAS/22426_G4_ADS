package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.util.IDGenerator;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class StockRepository implements Repository<Stock> {
    private final MongoCollection<Document> collection;

    public StockRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("stock");
    }

    @Override
    public void save(Stock stock) {
        Document doc = new Document()
                .append("_id", stock.getId() != null ? stock.getId() : IDGenerator.generateID("stocks"))
                .append("id_materia", stock.getIdMateria())
                .append("cantidad", stock.getCantidad())
                .append("unidad_medida", stock.getUnidadMedida())
                .append("fecha", stock.getFecha());
        collection.insertOne(doc);
    }

    @Override
    public Stock findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return documentToStock(doc);
    }

    @Override
    public List<Stock> findAll() {
        List<Stock> stocks = new ArrayList<>();
        for (Document doc : collection.find()) {
            stocks.add(documentToStock(doc));
        }
        return stocks;
    }

    @Override
    public void update(Stock stock) {
        Document doc = new Document()
                .append("id_materia", stock.getIdMateria())
                .append("cantidad", stock.getCantidad())
                .append("unidad_medida", stock.getUnidadMedida())
                .append("fecha", stock.getFecha());
        collection.updateOne(Filters.eq("_id", stock.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public List<Stock> searchStock(String query, String seccionId) {
        MongoCollection<Document> collection = MongoDBConfig.getDatabase().getCollection("stock");
        Bson filter = Filters.and(
                query.isEmpty() ? Filters.empty() : Filters.regex("id_materia", query, "i"),
                seccionId == null ? Filters.empty() : Filters.eq("id_materia",
                        Filters.in("id", MongoDBConfig.getDatabase().getCollection("materias_primas")
                                .find(Filters.eq("id_seccion", seccionId))
                                .map(doc -> doc.getString("_id"))
                                .into(new ArrayList<>()))
                )
        );
        return collection.find(filter).map(this::documentToStock).into(new ArrayList<>());
    }

    private Stock documentToStock(Document doc) {
        return new Stock(
                doc.getString("_id"),
                doc.getString("id_materia"),
                doc.getDouble("cantidad"),
                doc.getString("unidad_medida"),
                doc.getString("fecha")
        );
    }
}