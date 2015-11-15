import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by emhah on 11/15/2015.
 */
public class TweetMain {
    private static String UserName = null;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient( "167.205.35.19" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("husain");
        MongoCollection<Document> User = database.getCollection("user");



        System.out.println("Selamat datang di Tweet Mongo");
        System.out.println("1. Login");
        System.out.println("0. keluar");
        System.out.print("Pilihan: ");
        int ins = input.nextInt();
        input.nextLine();
        while (ins != 0){
            if (ins == 1) {
                System.out.println("Login/Register");
                System.out.print("Username: ");
                String username = input.nextLine();
                System.out.print("Password: ");
                String password = input.nextLine();
                Document myUsername = User.find(eq("username", username)).first();
                if (myUsername != null) {
                    if (myUsername.get("password").toString().equals(password)){
                        UserName = username;
                        ins = 0;
                    } else {
                        System.out.println("Password Salah");
                        ins = 1;
                    }
                } else {
                    System.out.println("User Belum Ada, dan akan di registrasi");
                    Document AnUser = new Document("username", username)
                            .append("password", password);
                    User.insertOne(AnUser);
                    ins = 1;
                }
            }else{
                System.out.println("pilih 1 atau 0!");
                System.out.print("Pilihan: ");
                ins = input.nextInt();
                input.nextLine();
            }
        }
        if (UserName != null){
            System.out.println("Selamat Datang di Twitsanda " + UserName);
//            System.out.println("");
//            System.out.print("Query: ");
//            String inputs = input.nextLine();
//            String[] query;
//            while (true){
//                if ((query = CommandRegexes.follow.match(inputs)) != null){
//                    if(twissandra_engine.follow(query[0],UserName))
//                        System.out.println("Anda Berhasil Mengikuti "+query[0]);
//                    else
//                        System.out.println("Anda Gagal Mengikuti "+query[0]);
//                }else if ((query = CommandRegexes.EXIT.match(inputs)) != null){
//                    System.out.print("Bye....... ");
//                    break;
//                }else if ((query = CommandRegexes.TIMELINE.match(inputs)) != null){
//                    twissandra_engine.show_timeline(UserName);
//                }else if ((query = CommandRegexes.TWEET.match(inputs)) != null){
//                    twissandra_engine.tweet(UserName,query[0]);
//                }else if ((query = CommandRegexes.MYTWEET.match(inputs)) != null){
//                    twissandra_engine.show_userline(UserName);
//                }
//                System.out.print("Query: ");
//                inputs = input.nextLine();
//            }
        }
    }
}
