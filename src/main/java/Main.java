import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); //sacar la clave de mongo del .env
        String connectionString = dotenv.get("MONGO_URL");
        MongoClient mongoClient= MongoClients.create(connectionString); //conexión cliente

        MongoDatabase db=mongoClient.getDatabase("BoardFinderDB"); //conexión base de datos

        new App().startThread(db); //inicio de la app
    }
}