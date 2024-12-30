package functions;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Functions {
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> gameCollection;
    private MongoCollection<Document> chatCollection;

    public Functions(MongoDatabase mongoDatabase){
        userCollection=mongoDatabase.getCollection("users");
        gameCollection=mongoDatabase.getCollection("games");
        chatCollection=mongoDatabase.getCollection("chats");
    }
    public void getUser(String user_id) {
        ObjectId objectId = new ObjectId(user_id);

        // Buscar el documento por _id
        Document document = new Document("_id", objectId);

        FindIterable<Document> resultDocument = userCollection.find(document);
        // Return the name of the first returned document
        System.out.println(resultDocument.first().toJson());
    }
    public void addUser(String username, String password) {
        ArrayList<String> games_id = new ArrayList<>();
        ArrayList<String> chats_id = new ArrayList<>();

        Document document=new Document("username",username);
        document.append("password",password);
        document.append("games_id",games_id);
        document.append("chats_id",chats_id);

        userCollection.insertOne(document);
    }
    public void addUserToGame() {

    }
    public void getGame() {

    }public void addGame(String gamename, String owner_id, int max_players) {
        ArrayList<String> players_id = new ArrayList<>();


        Document document=new Document("gamename",gamename);
        document.append("owner_id",owner_id);
        document.append("max_players",max_players);
        document.append("players_id",players_id);
        //TERMINAR ESTO

        gameCollection.insertOne(document);

    }
    public void deleteGame() {

    }
}
