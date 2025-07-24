package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.config.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements Repository<Usuario> {
    private final MongoCollection<Document> collection;

    public UsuarioRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        collection = database.getCollection("users");
    }

    @Override
    public void save(Usuario usuario) {
        Document doc = new Document()
                .append("_id", usuario.getId())
                .append("username", usuario.getUsername())
                .append("password", usuario.getPassword())
                .append("nombre", usuario.getNombre())
                .append("email", usuario.getEmail());
        collection.insertOne(doc);
    }

    @Override
    public Usuario findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) return null;
        return new Usuario(
                doc.getString("_id"),
                doc.getString("username"),
                doc.getString("password"),
                doc.getString("nombre"),
                doc.getString("email")
        );
    }

    public Usuario findByUsername(String username) {
        Document doc = collection.find(Filters.eq("username", username)).first();
        if (doc == null) return null;
        return new Usuario(
                doc.getString("_id"),
                doc.getString("username"),
                doc.getString("password"),
                doc.getString("nombre"),
                doc.getString("email")
        );
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find()) {
            usuarios.add(new Usuario(
                    doc.getString("_id"),
                    doc.getString("username"),
                    doc.getString("password"),
                    doc.getString("nombre"),
                    doc.getString("email")
            ));
        }
        return usuarios;
    }

    @Override
    public void update(Usuario usuario) {
        Document doc = new Document()
                .append("username", usuario.getUsername())
                .append("password", usuario.getPassword())
                .append("nombre", usuario.getNombre())
                .append("email", usuario.getEmail());
        collection.updateOne(Filters.eq("_id", usuario.getId()), new Document("$set", doc));
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }
}