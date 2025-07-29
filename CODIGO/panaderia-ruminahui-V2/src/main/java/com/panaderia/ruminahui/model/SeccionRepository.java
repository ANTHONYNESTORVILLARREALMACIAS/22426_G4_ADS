package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class SeccionRepository {
    private final MongoCollection<Document> collection;

    public SeccionRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        this.collection = database.getCollection("secciones");
    }

    public String create(Seccion seccion) {
        try {
            Document doc = new Document("_id", IDGenerator.generateID("secciones"))
                    .append("nombre", seccion.getNombre())
                    .append("descripcion", seccion.getDescripcion());
            collection.insertOne(doc);
            return null;
        } catch (Exception e) {
            return "Error al crear sección: " + e.getMessage();
        }
    }

    public String update(Seccion seccion) {
        try {
            Document doc = new Document("nombre", seccion.getNombre())
                    .append("descripcion", seccion.getDescripcion());
            collection.replaceOne(eq("_id", seccion.getId()), doc);
            return null;
        } catch (Exception e) {
            return "Error al actualizar sección: " + e.getMessage();
        }
    }

    public String delete(String id) {
        try {
            collection.deleteOne(eq("_id", id));
            return null;
        } catch (Exception e) {
            return "Error al eliminar sección: " + e.getMessage();
        }
    }

    public Seccion findById(String id) {
        Document doc = collection.find(eq("_id", id)).first();
        if (doc == null) return null;
        return new Seccion(
                doc.getString("_id"),
                doc.getString("nombre"),
                doc.getString("descripcion")
        );
    }

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

    public List<Seccion> findByName(String searchText) {
        List<Seccion> secciones = new ArrayList<>();
        Document query = new Document();
        if (searchText != null && !searchText.isEmpty()) {
            query.append("nombre", new Document("$regex", searchText).append("$options", "i"));
        }
        for (Document doc : collection.find(query)) {
            secciones.add(new Seccion(
                    doc.getString("_id"),
                    doc.getString("nombre"),
                    doc.getString("descripcion")
            ));
        }
        return secciones;
    }
}