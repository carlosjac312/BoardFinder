package Mongo;

import com.mongodb.client.*;
import io.github.cdimascio.dotenv.Dotenv;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;
//This is sample code suplemented by Mongo
public class MongoClientConnection {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("MONGO_URL");
        MongoClient mongoClient=MongoClients.create(connectionString);

        MongoDatabase db=mongoClient.getDatabase("BoardFinderDB");
        MongoCollection<Document> collection = db.getCollection("experiment");

        // El ID que deseas buscar
        String id = "6772d9d7a8890d6437857a4f"; // Ejemplo de ObjectId en formato hexadecimal

        // Convertir el ID a ObjectId
        ObjectId objectId = new ObjectId(id);

        // Buscar el documento por _id
        Document document = new Document("_id", objectId);

        FindIterable<Document> resultDocument = collection.find(document);
        // Return the name of the first returned document
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
