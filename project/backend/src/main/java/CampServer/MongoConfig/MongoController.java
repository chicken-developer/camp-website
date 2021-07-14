package CampServer.MongoConfig;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

public class MongoController {
    public static MongoDatabase database;
    public void InitField()
    {
        //If want to init field from code
    }
    public static void Connect()
    {
        ConnectionString connectionString = new ConnectionString(MongoParams.connectionStr);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(MongoParams.databaseName);

        //Test connection
        MongoIterable<String> list = database.listCollectionNames();
        for (String name : list) {
            System.out.println(name);
        }
    }

    public static MongoCollection<Document> GetCollection(String name)
    {
        return database.getCollection(name);
    }


}
