/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.UuidRepresentation;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
/**
 *
 * @author adwisatya
 */
public class Mongo_Engine {
    private String hostname;
    private String keyspace;
    private MongoCollection<Document> User;
    private MongoCollection<Document> Followers;
    private MongoCollection<Document> Friends;
    private MongoCollection<Document> Timeline;
    private MongoCollection<Document> Userline;
    private MongoCollection<Document> Tweets;

    public Mongo_Engine(String _hostname, String _keyspace) {

        hostname = _hostname;
        keyspace = _keyspace;
        MongoClient mongoClient = new MongoClient( hostname , 27017 );
        MongoDatabase database = mongoClient.getDatabase(keyspace);
        User = database.getCollection("user");
        Followers = database.getCollection("followers");
        Friends = database.getCollection("friends");
        Timeline = database.getCollection("timeline");
        Userline = database.getCollection("userline");
        Tweets = database.getCollection("tweets");
    }

    public boolean show_user(String username) {
        Document myUsername = User.find(eq("username", username)).first();
        if (myUsername != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is_user_exist(String username) {
        Document myUsername = User.find(eq("username", username)).first();
        if (myUsername != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean check_password(String username, String password) {
        Document myUsername = User.find(and(eq("username", username), eq("password",password))).first();
        if (myUsername != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean show_follower(String username, String target) {
        Document MyFollower = Followers.find(and(eq("username", username), eq("follower",target))).first();
        if (MyFollower != null) {
            return true;
        } else {
            return false;
        }
    }

    public void register_user(String username, String password) {
        Document AnUser = new Document("username", username)
                .append("password", password);
        User.insertOne(AnUser);
    }
    public void input_follower(String username, String follower, Date sejak) {
        Document Afollower = new Document("username", username)
                .append("follower", follower)
                .append("since", sejak);
        Followers.insertOne(Afollower);
    }
    public void input_friend(String username, String friend, Date sejak) {
        Document AFriend =  new Document("username", username)
                .append("friend", friend)
                .append("since", sejak);
        Friends.insertOne(AFriend);
    }

    public boolean follow(String target, String sumber) {
        try {
            Date sejak = new Date();
            if (show_user(target)) {
                if (!show_follower(target, sumber)) {
                    input_follower(target,sumber,sejak);
                    input_friend(sumber,target,sejak);
                    return true;
                } else {
                    System.out.println("Kamu udah follow dia");
                    return false;
                }
            } else {
                System.out.println("Yang kamu mau follow gak ada");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Anda Gagal Mengikuti "+target);
            return false;
        }
    }

    public void tweet(String username, String tweet) {
        try {
            UUID tweet_id = UUID.randomUUID();
            TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();
            UUID time_uuid = uuidGenerator.generate();
            String follower;
            add_tweet(tweet_id,username,tweet);
            insert_to_userline(tweet_id, username, time_uuid);
            insert_to_timeline(tweet_id, username, time_uuid);
            for (Document cur : Followers.find(eq("username",username))) {
                follower = cur.get("follower").toString();
                insert_to_timeline(tweet_id, follower, time_uuid);
            }
            System.out.println("Tweeted!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insert_to_userline(UUID tweet_id, String username, UUID time_uuid) {
        Document Uline = new Document("username", username)
            .append("time", time_uuid)
            .append("tweet_id", tweet_id);
        Userline.insertOne(Uline);
    }

    public void insert_to_timeline(UUID tweet_id, String username, UUID  time_uuid) {
        Document Tline = new Document("username", username)
              .append("time", time_uuid)
              .append("tweet_id", tweet_id);
        Timeline.insertOne(Tline);
    }

    public void add_tweet(UUID tweet_id, String username, String Body) {
        Document ATweet = new Document("tweet_id", tweet_id)
                .append("username", username)
                .append("body", Body);
        Tweets.insertOne(ATweet);
    }
//
//    public void show_tweet(String username) {
//        try {
//            ResultSet result = session.execute("SELECT * from tweets WHERE username='" + username + "'");
//            if (result.getAvailableWithoutFetching() > 0) {
//                System.out.println(username + " tweet's:");
//                for (Row row : result) {
//                    System.out.println(row.getString("body"));
//                }
//            } else {
//                System.out.println("Tidak ada tweet dari " + username);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void show_userline(String username){
//        ResultSet result = session.execute("SELECT * from userline WHERE username='"+username+"'");
//        for(Row row : result){
//            show_tweetid(row.getUUID("tweet_id"));
//        }
//    }
//    public void show_timeline(String username){
//        ResultSet result = session.execute("SELECT * from timeline WHERE username='"+username+"'");
//        for(Row row : result){
//            show_tweetid(row.getUUID("tweet_id"));
//        }
//    }
//
//    public void show_tweetid(UUID tweet_id){
//        ResultSet result = session.execute("SELECT * from tweets WHERE tweet_id="+tweet_id+"");
//        for(Row row : result){
//            System.out.println(row.getString("body"));
//
//        }
//    }
//    public void teminate_connection(){
//        cluster.close();
//    }
//    public String toMD5(String input) throws NoSuchAlgorithmException{
//        /* taken from http://www.mkyong.com/java/java-md5-hashing-example/ */
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(input.getBytes());
//
//        byte byteData[] = md.digest();
//
//        //convert the byte to hex format method 1
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < byteData.length; i++) {
//            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//        }
//        return sb.toString();
//    }
}
