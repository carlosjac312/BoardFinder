package Mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import functions.Functions;
import io.github.cdimascio.dotenv.Dotenv;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Objects;
//This is sample code suplemented by Mongo
public class MongoClientConnection {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("MONGO_URL");
        MongoClient mongoClient=MongoClients.create(connectionString);

        MongoDatabase db=mongoClient.getDatabase("BoardFinderDB");

        MongoCollection<Document> collection=db.getCollection("games");


        Document document = new Document("gamename", new Document("$regex", "^c").append("$options", "i"));

        FindIterable<Document> resultDocument = collection.find(document);
        System.out.println(resultDocument.first().toJson());


    }
}

/*
        MongoDatabase db=mongoClient.getDatabase("BoardFinderDB");
        MongoCollection<Document> collection = db.getCollection("experiment");

GET: (BY ID)
        // El ID que deseas buscar
        String id = "64c12345abcd67890ef12345"; // Ejemplo de ObjectId en formato hexadecimal

        // Convertir el ID a ObjectId
        ObjectId objectId = new ObjectId(id);

        // Buscar el documento por _id
        Document document = new Document("_id", objectId);

        FindIterable<Document> resultDocument = collection.find(document);
        // Return the name of the first returned document
        System.out.println(resultDocument.first().toJson());

EL GET ALL es un .find() a secas luego se puede hacer un for each con un toJson() y listo
collection.find(Filters.eq("campo", "valor")); Esto es para poner filtros
FindIterable<Document> paginatedDocuments = collection.find().skip(10).limit(15); Esto es para skipear y poner maximo
Filters.regex("gamename", "^Mon", "i") con esto solo buscara los que EMPIECENEN por MON la i es para que no distiga masyuculas de minusculas


ADD:
        Document document=new Document("name","Carlos");
        document.append("Dato1","A");
        document.append("Dato2","B");
        document.append("Dato3","C");

        col.insertOne(document);
        // Obtener el _id generado
        ObjectId idGenerado = doc.getObjectId("_id");
        System.out.println("ID generado: " + idGenerado.toHexString());
 */
