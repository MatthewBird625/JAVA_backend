package app;

import com.mongodb.client.MongoClients;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Projections.*;

import org.bson.BSONObject;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import app.models.Property;
import app.models.Reviews;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import static com.mongodb.client.model.Filters.*;

public class MongoDBConnection {
   /**
    * TODO update the DATABASE_URL value below format:
    * mongodb+srv://username:password@atlas.server.address/database Goto
    * https://cloud.mongodb.com/ click connect > connect your application > Driver
    * Java > Version 4.3 or later copy connection string if you have time out
    * issues (due to firewalls) choose verion 3.3 or earlier instead
    **/
   private static final String DATABASE_URL = "mongodb+srv://s3482450:Rocksmith1@cluster0.ivvlx.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
   static MongoClient client;
   MongoDatabase database;
   MongoCollection<Document> allListings;
   MongoCollection<Document> allReviewers;
   private static MongoDBConnection mongodb = null;

   public static MongoDBConnection getConnection() {
      // check that MongoDBConnection is available (if not establish)
      if (mongodb == null) {
         mongodb = new MongoDBConnection();
      }
      return mongodb;
   }

   public MongoDBConnection() {
      System.out.println("Creating MongoDB Connection Object");

      try {
         client = MongoClients.create(DATABASE_URL);
         database = client.getDatabase("sample_airbnb");
         allListings = database.getCollection("listingsAndReviews");
         allReviewers = database.getCollection("reviewers");

      } catch (Exception e) {
         // If there is an error, lets just print the error
         System.out.println("this error!!!");
         System.err.println(e.getMessage());
      }
   }

   public static void closeConnection() {
      try {
         if (client != null) {
            client.close();
            System.out.println("Database Connection closed");
         }
      } catch (Exception e) {
         // connection close failed.
         System.err.println(e.getMessage());
      }
   }

   public boolean userLogin(String loginName, String loginId) {

      System.out.println("LOG IN ATTEMPT: " + loginId);
      SessionController session = App.getSessionController();
      // and(eq("reviews.reviewer_name", loginName),
      MongoCursor<Document> cursor = allListings
            .find(and(eq("reviews.reviewer_id", loginId), eq("reviews.reviewer_name", loginName)))
            .projection(fields(include("reviews"))).iterator();
      if (cursor == null) {
         System.out.println("No results found.");
         return false;
      } else {
         while (cursor.hasNext()) {
            System.out.println("LOGIN IS FOUND");
            session.setUserId(loginId);
            session.setUserName(loginName);
            return true;

         }

      }

      return false;
   }

   public Property searchOneProperty(String mongoId) {

      Property property = null;

      MongoCursor<Document> cursor = allListings
            .find(eq("_id", mongoId)).projection(fields(include("_id", "name", "address", "summary", "review_scores",
                  "price", "property_type", "amenities", "bedrooms", "beds", "accommodates", "host", "images")))
            .iterator();

      try {
         while (cursor.hasNext()) {
            Document record = cursor.next();
            String mongodbID = record.getString("_id");
            String name = record.get("name").toString();
            Document addressDoc = record.get("address", Document.class);
            String summary = record.getString("summary");
            String address = addressDoc.getString("street");
            String suburb = addressDoc.getString("suburb");
            String countryCode = addressDoc.getString("country_code");
            String country = addressDoc.getString("country");
            String marketCity = addressDoc.getString("market");
            Document reviewDoc = record.get("review_scores", Document.class);
            // if there are no ratings, review is set to -1. -1 review score will render no
            // review score on front end.
            int reviewScoreRating = -1;
            try {
               reviewScoreRating = reviewDoc.getInteger("review_scores_rating");
            } catch (Exception e) {
               // System.out.println("no ratings");
            }
            Decimal128 price = (Decimal128) record.get("price");
            String propertyType = record.getString("property_type");

            // Document amenitiesDoc = record.get("amenities", Document.class);
            //
            ArrayList<String> amenities = new ArrayList<String>();
            try {
               amenities = record.get("amenities", ArrayList.class);
            } catch (Exception e) {
               // no amenities
            }

            int numberBedrooms = record.getInteger("bedrooms");
            int numberBeds = record.getInteger("beds");
            int numberPeople = record.getInteger("accommodates");

            Document hostDoc = record.get("host", Document.class);

            boolean superHost = hostDoc.getBoolean("host_is_superhost");

            Document urlDoc = record.get("images", Document.class);

            String url = urlDoc.getString("picture_url");

            property = new Property(mongodbID, name, summary, address, suburb, countryCode, country, marketCity,
                  reviewScoreRating, price, propertyType, amenities, numberBedrooms, numberBeds, numberPeople,
                  superHost, url);

         }
      } finally {

         cursor.close();
      }

      return property;

   }

   public void search(String location, String market) {

      System.out.println(location + market);

      ArrayList<Property> listings = new ArrayList<Property>();

      MongoCursor<Document> cursor = allListings
            .find(and(eq("address.country", location), eq("address.market", market)))
            .projection(fields(include("_id", "name", "summary", "price", "bedrooms", "beds", "images"))).iterator();

      try {

         // String name, String summary,int numberBedrooms, int numberBeds, Decimal128
         // price
         while (cursor.hasNext()) {
            Document record = cursor.next();
            String mongodbID = record.getString("_id");
            String name = record.get("name").toString();
            String summary = record.getString("summary");
            Decimal128 price = (Decimal128) record.get("price");
            int numberBedrooms = record.getInteger("bedrooms");
            int numberBeds = record.getInteger("beds");
            Document urlDoc = record.get("images", Document.class);
            String url = urlDoc.getString("picture_url");

            listings.add(new Property(mongodbID, name, summary, price, numberBedrooms, numberBeds, url));

         }
      } catch (Exception e) {
         System.out.println(e);
      }

      finally {

         cursor.close();
      }
      SessionController session = App.getSessionController();
      session.setListingsFound(listings);

   }

   public ArrayList<Reviews> searchPropertyReviews(String mongoId) {
      ArrayList<Reviews> reviews = new ArrayList<>();
      MongoCursor<Document> cursor = allListings.find(eq("_id", mongoId)).projection(fields(include("reviews")))
            .iterator();

      try {
         while (cursor.hasNext()) {
            Document record = cursor.next();
            List<Document> reviewsDoc = record.get("reviews", List.class);

            for (int i = 0; i < reviewsDoc.size(); i++) {

               String reviewId = reviewsDoc.get(i).get("_id").toString();
               String ListingId = reviewsDoc.get(i).get("listing_id").toString();
               Date date = reviewsDoc.get(i).getDate("date");

               String reviewerId = reviewsDoc.get(i).get("reviewer_id").toString();
               String reviewerName = reviewsDoc.get(i).get("reviewer_name").toString();
               String comments = reviewsDoc.get(i).get("comments").toString();

               reviews.add(new Reviews(reviewId, date, ListingId, reviewerId, reviewerName, comments));

            }

         }
      } finally {

         cursor.close();
      }

      return reviews;
   }

   public ArrayList<Reviews> getUserReviews(String userId) {
      ArrayList<Reviews> reviews = new ArrayList<>();
      MongoCursor<Document> cursor = allListings.find(eq("reviews.reviewer_id", userId))
            .projection(fields(include("reviews"))).iterator();

      int filesChecked = 0;
      int filesAdded = 0;
      try {
         while (cursor.hasNext()) {
            Document record = cursor.next();
            List<Document> reviewsDoc = record.get("reviews", List.class);
            filesChecked++;

            for (int i = 0; i < reviewsDoc.size(); i++) {

               String reviewId = reviewsDoc.get(i).get("_id").toString();
               String ListingId = reviewsDoc.get(i).get("listing_id").toString();
               Date date = reviewsDoc.get(i).getDate("date");

               String reviewerId = reviewsDoc.get(i).get("reviewer_id").toString();
               String reviewerName = reviewsDoc.get(i).get("reviewer_name").toString();

               String comments = reviewsDoc.get(i).get("comments").toString();

               if (reviewerId.equals(userId)) {
                  filesAdded++;
                  reviews.add(new Reviews(reviewId, date, ListingId, reviewerId, reviewerName, comments));
                  System.out.println(reviewerName);
               }

            }

         }
         System.out.println("checked: " + filesChecked + "added: " + filesAdded);
      } finally {

         cursor.close();
      }

      return reviews;
   }

   public void postReview(String formParam, SessionController session) {

      System.out.println(formParam + session.getListingID() + session.getUserId() + session.getUserName());

      // BasicDBObject doc = new BasicDBObject("reviews",new
      // BasicDBObject("listing_id",session.getListingID()).append("reviewer_id",session.getUserId()).append("reviewer_name",
      // session.getUserName()).append("comments", formParam));
      // BasicDBObject query = new BasicDBObject();
      // query.put("_id",session.getListingID());
      // BasicDBObject set = new BasicDBObject("$set", doc);
      // allListings.updateOne(query, set);

   }

   public void searchLocationOnly(String location) {

      System.out.println(location);

      ArrayList<Property> listings = new ArrayList<Property>();

      MongoCursor<Document> cursor = allListings.find(eq("address.country", location))
            .projection(fields(include("_id", "name", "summary", "price", "bedrooms", "beds", "images"))).iterator();

      try {

         // String name, String summary,int numberBedrooms, int numberBeds, Decimal128
         // price
         while (cursor.hasNext()) {
            Document record = cursor.next();
            String mongodbID = record.getString("_id");
            String name = record.get("name").toString();
            String summary = record.getString("summary");
            Decimal128 price = (Decimal128) record.get("price");
            int numberBedrooms = record.getInteger("bedrooms");
            int numberBeds = record.getInteger("beds");
            Document urlDoc = record.get("images", Document.class);
            String url = urlDoc.getString("picture_url");

            listings.add(new Property(mongodbID, name, summary, price, numberBedrooms, numberBeds, url));

         }
      } catch (Exception e) {
         System.out.println(e);
      }

      finally {

         cursor.close();
      }
      SessionController session = App.getSessionController();
      session.setListingsFound(listings);

   }

   public void searchMarketOnly(String market) {

      System.out.println(market);

      ArrayList<Property> listings = new ArrayList<Property>();

      MongoCursor<Document> cursor = allListings.find(eq("address.market", market))
            .projection(fields(include("_id", "name", "summary", "price", "bedrooms", "beds", "images"))).iterator();

      try {

         // String name, String summary,int numberBedrooms, int numberBeds, Decimal128
         // price
         while (cursor.hasNext()) {
            Document record = cursor.next();
            String mongodbID = record.getString("_id");
            String name = record.get("name").toString();
            String summary = record.getString("summary");
            Decimal128 price = (Decimal128) record.get("price");
            int numberBedrooms = record.getInteger("bedrooms");
            int numberBeds = record.getInteger("beds");
            Document urlDoc = record.get("images", Document.class);
            String url = urlDoc.getString("picture_url");
            System.out.println(name);

            listings.add(new Property(mongodbID, name, summary, price, numberBedrooms, numberBeds, url));

         }
      } catch (Exception e) {
         System.out.println(e);
      }

      finally {

         cursor.close();
      }
      SessionController session = App.getSessionController();
      session.setListingsFound(listings);

   }

}
