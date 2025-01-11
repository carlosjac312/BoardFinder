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
    private ObjectId user_id;

    public Functions(MongoDatabase mongoDatabase){
        userCollection=mongoDatabase.getCollection("users");
        gameCollection=mongoDatabase.getCollection("games");
        chatCollection=mongoDatabase.getCollection("chats");
    }
    public void getUserByid() {

        // Buscar el documento por _id
        Document document = new Document("_id", user_id);

        FindIterable<Document> resultDocument = userCollection.find(document);
        // Return the name of the first returned document
        System.out.println(resultDocument.first().toJson());
    }
    public Boolean getUser(String username, String password) {

        Document document = new Document("username", username).append("password",password);
        try {
            FindIterable<Document> resultDocument = userCollection.find(document);
            Document firstDocument = resultDocument.first(); // Obtener el primer documento encontrado
            user_id = firstDocument.getObjectId("_id");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Boolean addUser(String username, String password) {
        try {
            Document exist=new Document("username",username);
            FindIterable<Document> resultDocument = userCollection.find(exist); //If user does not exist mongo error will trigger
            return false;                                                       //And the code on the catch will be executed
        } catch (Exception e) {
            ArrayList<String> games_id = new ArrayList<>();
            ArrayList<String> chats_id = new ArrayList<>();

            Document document=new Document("username",username);
            document.append("password",password);
            document.append("games_id",games_id);
            document.append("chats_id",chats_id);

            userCollection.insertOne(document);
            return true;
        }
    }

    public void addUserToGame() {

    }
    public void getGame(String gamename) {
        Document document = new Document("gamename", gamename);

        FindIterable<Document> resultDocument = gameCollection.find(document);
    }
    public void addGame(String gamename,String date,String location, String descripcion,int max_players) {
        ArrayList<ObjectId> players_id = new ArrayList<>();
        players_id.add(user_id);
        // Crear subdocumento para gameinfo
        Document gameInfo = new Document("location", location)
                .append("date", date)
                .append("description", descripcion == null || descripcion.isEmpty() ? "" : descripcion);

        Document document=new Document("gamename",gamename);
        document.append("gameinfo",gameInfo);
        document.append("owner_id",user_id);
        document.append("max_players",max_players);
        document.append("players_id",players_id);
        document.append("full",false);

        gameCollection.insertOne(document); //Añadir el juego
        //Añadir el id del juego a la lista de juegos del creador
        ObjectId idGame = document.getObjectId("_id");
        Document filter=new Document("_id",user_id);
        Document update=new Document("$push", new Document("games_id", idGame));
        userCollection.updateOne(filter,update);
    }
    public void deleteGame() {

    }
}
