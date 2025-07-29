package com.panaderia.ruminahui.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private final MongoCollection<Document> collection;

    public UsuarioRepository() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        this.collection = database.getCollection("usuarios");
    }

    public String create(Usuario usuario) {
        try {
            Document doc = new Document("_id", usuario.getId())
                    .append("username", usuario.getUsername())
                    .append("nombre", usuario.getNombre())
                    .append("password", usuario.getPassword());
            collection.insertOne(doc);
            return null;
        } catch (Exception e) {
            return "Error al crear usuario: " + e.getMessage();
        }
    }

    public String update(Usuario usuario) {
        try {
            Document doc = new Document("username", usuario.getUsername())
                    .append("nombre", usuario.getNombre())
                    .append("password", usuario.getPassword());
            collection.replaceOne(new Document("_id", usuario.getId()), doc);
            return null;
        } catch (Exception e) {
            return "Error al actualizar usuario: " + e.getMessage();
        }
    }

    public String delete(String id) {
        try {
            collection.deleteOne(new Document("_id", id));
            return null;
        } catch (Exception e) {
            return "Error al eliminar usuario: " + e.getMessage();
        }
    }

    public Usuario findById(String id) {
        Document doc = collection.find(new Document("_id", id)).first();
        if (doc == null) return null;
        return new Usuario(
                doc.getString("_id"),
                doc.getString("username"),
                doc.getString("nombre"),
                doc.getString("password")
        );
    }

    public Usuario findByUsername(String username) {
        Document doc = collection.find(new Document("username", username)).first();
        if (doc == null) return null;
        return new Usuario(
                doc.getString("_id"),
                doc.getString("username"),
                doc.getString("nombre"),
                doc.getString("password")
        );
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find()) {
            usuarios.add(new Usuario(
                    doc.getString("_id"),
                    doc.getString("username"),
                    doc.getString("nombre"),
                    doc.getString("password")
            ));
        }
        return usuarios;
    }
}