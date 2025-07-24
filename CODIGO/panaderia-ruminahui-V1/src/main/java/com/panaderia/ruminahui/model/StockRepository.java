package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

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
                .append("_id", stock.getId())
                .append("id_materia", stock.getIdMateria())
                .append("cantidad", stock.getCantidad())
                .append("fecha", stock.getFecha());
        collection.insertOne(doc);
    }

    @Override
    public Stock findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Stock(
                doc.getString("_id"),
                doc.getString("id_materia"),
                doc.getDouble("cantidad"),
                doc.getString("fecha")
        );
    }

    @Override
    public List<Stock> findAll() {
        List<Stock> stocks = new ArrayList<>();
        for (Document doc : collection.find()) {
            stocks.add(new Stock(
                    doc.getString("_id"),
                    doc.getString("id_materia"),
                    doc.getDouble("cantidad"),
                    doc.getString("fecha")
            ));
        }
        return stocks;
    }

    @Override
    public void update(Stock stock) {
        Document doc = new Document()
                .append("id_materia", stock.getIdMateria())
                .append("cantidad", stock.getCantidad())
                .append("fecha", stock.getFecha());
        collection.updateOne(Filters.eq("_id", stock.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}