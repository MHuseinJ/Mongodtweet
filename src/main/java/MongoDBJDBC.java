/**
 * Created by emhah on 11/15/2015.
 */
import com.mongodb.MongoClient;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MongoDBJDBC {

    public static void main( String args[] ) {

        try{

            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient( "167.205.35.19" , 27017 );
            Mongo_Engine mongo_engine = new Mongo_Engine("167.205.35.19", "husain");
            // Now connect to your databases
            //DB db = mongoClient.getDB( "husain" );
            MongoDatabase database = mongoClient.getDatabase("husain");

            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = database.getCollection("user");
            System.out.println(collection.count());
            Document myDoc = collection.find().first();
            System.out.println(myDoc.toJson());


        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
