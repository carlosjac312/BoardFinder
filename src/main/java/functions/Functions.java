package functions;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Functions {
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> gameCollection;
    private ObjectId user_id;
    private Document userData;

    public Functions(MongoDatabase mongoDatabase){ //constructor crea los accesos a coleciones
        userCollection=mongoDatabase.getCollection("users");
        gameCollection=mongoDatabase.getCollection("games");
    }

    public Boolean getUser(String username, String password) { //login

        Document document = new Document("username", username).append("password",password);
        try {
            FindIterable<Document> resultDocument = userCollection.find(document); //usa un documento para buscar en la bd
            userData = resultDocument.first(); // Obtener el primer documento encontrado
            user_id = userData.getObjectId("_id");
            return true; //existe el user
        } catch (Exception e) {
            return false; //no existe
        }
    }
    public Boolean addUser(String username, String password) { //register
        Document exist = new Document("username", username);
        FindIterable<Document> resultDocument = userCollection.find(exist);

        if (resultDocument.first() != null) {
            // El usuario ya existe
            return false;
        }

        // Crear el usuario si no existe
        ArrayList<String> games_id = new ArrayList<>();
        ArrayList<String> chats_id = new ArrayList<>();

        Document document = new Document("username", username);
        document.append("password", password);
        document.append("games_id", games_id);
        document.append("chats_id", chats_id);

        // Insertar el documento en la colección
        userCollection.insertOne(document);

        // Guardar el ObjectId generado
        user_id = document.getObjectId("_id");
        userData=userCollection.find(new Document("_id", user_id)).first();
        return true;
    }

    public void addUserToGame(ObjectId gameid) { //añadir usuario a partida
        Document gamefilter = new Document("_id", gameid);

        // Obtener el juego actual para comprobar los jugadores
        Document game = gameCollection.find(gamefilter).first();
        if (game != null) {
            List<String> players = game.getList("players", String.class);
            int maxPlayers = game.getInteger("max_players");

            // Si el número de jugadores está a 1 de alcanzar el máximo, marcar como full
            if (players.size() == maxPlayers - 1) {
                Document gameupdate = new Document("$push", new Document("players", userData.get("username")))
                        .append("$set", new Document("full", true));
                gameCollection.updateOne(gamefilter, gameupdate);
            } else {
                Document gameupdate = new Document("$push", new Document("players", userData.get("username")));
                gameCollection.updateOne(gamefilter, gameupdate);
            }

            Document playerfilter = new Document("_id", user_id);
            Document playerupdate = new Document("$push", new Document("games_id", gameid));
            userCollection.updateOne(playerfilter, playerupdate);
            //Actualizar los datos locales
            ArrayList<ObjectId> games_id = (ArrayList<ObjectId>) userData.get("games_id");
            games_id.add(gameid);
            userData.put("games_id", games_id);
        } else {
            System.out.println("Error: No se encontró el juego con el ID proporcionado.");
        }
    }
    public ArrayList<Document> getGame(String gamename) {//search
        Document document = new Document()
                .append("gamename", new Document("$regex", "^" + gamename).append("$options", "i")) //regex ^ para que busque los q empiezan por esa letra y la i para que ignore mayusculas y minusculas
                .append("players", new Document("$nin", Arrays.asList(userData.get("username")))) // nin es para evitar los juegos en los que ya esta el user
                .append("full", false);
        FindIterable<Document> resultDocument = gameCollection.find(document);
        ArrayList<Document> docsgames=new ArrayList<>();

        for(Document game : resultDocument){ //añadir cada documnet al array
            docsgames.add(game);
        }
        return docsgames;
    }
    public ArrayList<Document> getAllGames(int pagina){
        Document filter = new Document()
                .append("players", new Document("$nin", Arrays.asList(userData.get("username")))) //filtros
                .append("full", false);
        FindIterable<Document> resultDocument = gameCollection.find(filter).skip(pagina).limit(15);
        ArrayList<Document> docsgames=new ArrayList<>();

        for(Document game : resultDocument){
            docsgames.add(game);
        }
        return docsgames;
    }
    public void addGame(String gamename,String date,String location, String descripcion,int max_players) { //add
        ArrayList<String> players = new ArrayList<>();
        players.add((String) userData.get("username"));
        // Crear subdocumento para gameinfo
        Document gameInfo = new Document("location", location)
                .append("date", date)
                .append("description", descripcion == null || descripcion.isEmpty() ? "" : descripcion); //descripción opcional

        Document document=new Document("gamename",gamename);
        document.append("gameinfo",gameInfo);
        document.append("owner",userData.get("username"));
        document.append("max_players",max_players);
        document.append("players",players);
        document.append("full",false);

        gameCollection.insertOne(document); //Añadir el juego
        //Añadir el id del juego a la lista de juegos del creador
        ObjectId idGame = document.getObjectId("_id");
        Document filter=new Document("_id",user_id);
        Document update=new Document("$push", new Document("games_id", idGame));
        userCollection.updateOne(filter,update);
        ArrayList<ObjectId> games_id = (ArrayList<ObjectId>) userData.get("games_id");
        games_id.add(idGame);

        userData.put("games_id", games_id);
    }
    public void deleteGame(ObjectId gameId) { //delete
        Document filter = new Document("_id", gameId);
        Document game = gameCollection.find(filter).first();
        if (game != null) {
            List<String> players = game.getList("players", String.class); //borrar el juego de los jugadores primero
            for(String a: players){
                Document userfilter = new Document("username", a);
                Document update = new Document("$pull", new Document("games_id", gameId));
                userCollection.updateOne(userfilter, update);
            }
            gameCollection.deleteOne(filter); //borrar el juego
        } else {
            System.out.println("Error: No se encontró el juego con el ID proporcionado.");
        }
    }
    public void exitgame(ObjectId gameId){ //update
        Document gamefilter = new Document("_id", gameId);
        Document userfilter = new Document("username", userData.get("username"));
        Document game = gameCollection.find(gamefilter).first();
        List<String> players = game.getList("players", String.class);
        int maxPlayers = game.getInteger("max_players");

        // Si el número de jugadores está a 1 de alcanzar el máximo, marcar como full
        if (players.size() == maxPlayers) {
            Document updategame = new Document("$pull", new Document("players", userData.get("username")))
                    .append("$set", new Document("full", false));
            gameCollection.updateOne(gamefilter, updategame); //quitar el juagador del juego y si estaba lleno ponerlo a no lleno
        } else {
            Document updategame = new Document("$pull", new Document("players", userData.get("username")));
            gameCollection.updateOne(gamefilter, updategame); //quitar el jugador del juego
        }
        Document updateplayer = new Document("$pull", new Document("games_id", gameId));
        userCollection.updateOne(userfilter,updateplayer); //quitar el juego del user
        List<ObjectId> juegos = userData.getList("games_id", ObjectId.class);
        juegos.remove(gameId);
        userData.put("games_id", juegos); //actualizar datos locales
    }
    public ArrayList<Document> getUsergames() {
        List<ObjectId> games;
        try {
            games=(List<ObjectId>) userData.get("games_id");
        } catch (Exception e) {
            games=new ArrayList<>();
        }
        Document gamesList = new Document("_id", new Document("$in", games)); //filtro
        FindIterable<Document> result = gameCollection.find(gamesList); //se usa la listas de id que tiene el usario en sus datos

        ArrayList<Document> docsgames=new ArrayList<>();

        for(Document game : result){
            docsgames.add(game);
        }
        return docsgames;
    }
    public String getUserDataname(){
        return userData.getString("username");
    }
}
