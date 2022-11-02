package app;

import java.util.ArrayList;

import app.models.Property;
import app.models.Reviews;
import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin by writing the raw HTML into a Java
 * String object
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class Listing implements Handler {

   // URL of this page relative to http://localhost:7000/
   public static final String URL = "/Listing";
   public static String usernames[] = { "halil", "bill" };
   public static String passwords[] = { "halilpass", "billpass" };
   String loginName;
   String loginId;
   ArrayList<Property> listingsFound = new ArrayList<Property>();

   @Override
   public void handle(Context context) throws Exception {
      // import session controller- reset to no user.
      SessionController session = App.getSessionController();
      try {
         if (context.formParam("next10").equals("next10")) {
            session.setIndexControllerReviews(session.getIndexControllerReviews() + 10);
         }
      } catch (Exception e) {
      }

      try {
         if (context.formParam("back10").equals("back10")) {
            session.setIndexControllerReviews(session.getIndexControllerReviews() - 10);
         }
      } catch (Exception e) {
      }
      if (session.getListingID() == null) {
         context.redirect("/");
      }

      String html = null;
      // Create a simple HTML webpage in a String
      // Add some Header information
      html = html + "<head>" + "<title>Listing page</title>\n";

      // Add some CSS (external file)
      html = html + "<link rel='stylesheet' type='text/css' href='common.css' />\n";

      // Add the body
      html = html + "<body>\n";

      // create left/right division

      html = html + "<div style='width: 100%;'>";

      // BEGIN LEFT DIVISION
      html = html
            + " <div style='width: 200px;  height: 3600px; float: left; background: green; border-width: 5px; border-color: rgb(88, 88, 88);'>";

      html = html + "<div>";

      if (session.getUserId() == null) {

         html = html + "<h1> LISTING PAGE</h1>";

         html = html + " <form action='/' method='post'>";

         html = html + "<div class='container'>";
         html = html + "  <label for='userName'><b>userName</b></label>";
         html = html + "  <input type='text' placeholder='Enter userName' name='userName'>";

         html = html + " <label for='id'><b>mongoId</b></label>";
         html = html + " <input type='text' placeholder='Enter mongoId' name='mongoId' >";

         html = html + " <button type='submit'>Login</button>";

         html = html + "</div>";

         html = html + "</div>";
         html = html + "</form> ";

      } else {
         html = html + "<h1> User Panel: </h1>";
         html = html + "<h3> welcome " + session.getUserName() + " </h3>";
         html = html + " <form action='/ReviewsPage' method='post'>";

         html = html + " <button type='submit'>My Reviews</button>";

         html = html + "</form> ";
         html = html + " <form action='/Listing' method='post'>";
         html = html + " <input type='hidden' name='logout' value = 'logout' >";
         html = html + " <button type='submit'>log out</button>";

         html = html + "</form> ";
   
      }
      html = html + "</div>";
      html = html + "</div>";
      

      // END LEFT DIVISION
      if(context.formParam("logout") != null){
         session.logOut();
         context.redirect("/Listing");
      }

      // BEGIN RIGHT DIVISION
      html = html + " <form action='/' method='post'>";
      html = html + "  <input type='hidden' name='back' value= 'back'>";

      html = html + " <button type='submit'>back</button>";

      html = html + "</form> ";
      html = html + "<div style='margin-left: 10%; width = 90%; background: purple;'> ";
      if (session.getListingID() == null) {
         context.redirect("/");
      }

      Property property = getProperty(session.getListingID());
      ArrayList<Reviews> reviews = getReviews(session.getListingID());
      String hostStatus = "not a super host";
      if (property.isSuperHost())
         hostStatus = "is a super host!";

      html = html + "<div class= 'float-child-left-one'>";
      html = html + "<img src='" + property.getUrl() + "' alt='Property Image' style='width:300px;height:300px;'>";
      html = html + "</div>";
      // end left picture div
      // begin right div for text info
      html = html + "<div class ='float-child-right-one'>";
      html = html + "<h2>" + property.getName() + "</h2>";
      html = html + "<p>" + property.getSummary() + "</p>";
      html = html + "<p> <b>address: </b> " + property.getAddress() + "</p>";
      html = html + "<p> <b>suburb: </b> " + property.getSuburb() + "</p>";
      html = html + "<p> <b>country: </b> " + property.getCountry() + "</p>";
      html = html + "<p> <b>country code: </b> " + property.getCountryCode() + "</p>";
      if(property.getReviewScoreRating() != -1)
      html = html + "<p> <b>rating: </b> " + property.getReviewScoreRating() + "</p>";
      else
      html = html + "<p> <b>rating: </b> no reviews </p>";
      html = html + "<p> <b>price: </b> " + property.getPrice() + "</p>";
      html = html + "<p> <b>property type: </b> " + property.getPropertyType() + "</p>";
      html = html + "<p> <b>bedrooms:</b> " + property.getNumberBedrooms() + "</p>";
      html = html + "<p> <b>beds:</b> " + property.getBeds() + "</p>";
      html = html + "<p> <b>people accommodated: </b> " + property.getNumberPeople() + "</p>";
      html = html + "<p> <b>super host: </b> " + hostStatus + "</p>";

      ArrayList<String> amenities = property.getAmenities();
      html = html + "<h4>AMENITIES: </h4>";
      for (int i = 0; i < amenities.size(); i++) {

         html = html + "<p>" + amenities.get(i) + "</p>";

      }

      if(session.getUserId() != null){
         html = html + "<h4>leave a review </h4>";
         html = html + "<div class ='formbox'>";

         html = html + " <form action='/Listing' method='post'>";
   
         html = html + "<div class='container'>";
         html = html + " <textarea id='userReview' name='userReview' rows='4' cols='50'></textarea>";
         html = html + "</div>";
         html = html + " <button type='submit'>submit Review</button>";
         html = html + "</form> ";
        


      }

      html = html + "<h4>Reviews: </h4>";
      try{

      if (reviews.size() == 0) {

         html = html + "<p>no reviews made</p>";

      } else {
         for (int i = session.getIndexControllerReviews(); i < session.getIndexControllerReviews() + 10; i++) {
            html = html + "<div class = 'listingReview'>";
            html = html + "<p> <b>name:</b> " + reviews.get(i).getReviewerName() + "</p>";
            html = html + "<p> <b>date:</b> " + reviews.get(i).getDate() + "</p>";
            html = html + "<p> <b>comment:</b> " + reviews.get(i).getComments() + "</p>";
            html = html + "</div>";

         }
       
         html = html + " <form action='/Listing' method='post'>";
         html = html + "  <input type='hidden' name='next10' value= 'next10'>";

         html = html + " <button type='submit'>next 10</button>";

         html = html + "</form> ";

      }
   }catch(Exception e){
      //this catch catches when array list reaches end of reviews but for loop exceeds index

   }
   if (session.getIndexControllerReviews() > 0) {
      html = html + " <form action='/Listing' method='post'>";
      html = html + "  <input type='hidden' name='back10' value= 'back10'>";

      html = html + " <button type='submit'>back 10</button>";

      html = html + "</form> ";
   }

      html = html + "</div>";

      // end listing div
      html = html + "</div>";

      // END RIGHT DIVISION
      html = html + "</div>";

      // collect form parameters

    

      loginName = context.formParam("userName");
      loginId = context.formParam("mongoId");

      // DO NOT MODIFY THIS
      // Makes Javalin render the webpage

      context.html(html);
      boolean loggedIn = false;

      //post review
      if(context.formParam("userReview")!= null){
         postReview(context.formParam("userReview"), session);
         
      }

      // do login
      

      if (loginName != null && loginId != null)
         if (!loginName.equals("") && !loginId.equals(""))
            loggedIn = LoginCheck();
      if (loggedIn == true) {
         System.out.println("logged in " + session.getUserName());
         context.redirect("/Listing");
      }

      // DO NOT MODIFY THIS
      // Makes Javalin render the webpage

      context.html(html);

      // end html rendering and logic
   }

   // functions

   private void postReview(String formParam, SessionController session) {


      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      mongodb.postReview(formParam, session);

   }

   private ArrayList<Reviews> getReviews(String listingID) {
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      ArrayList<Reviews> reviews = mongodb.searchPropertyReviews(listingID);
      return reviews;
   }

   private Property getProperty(String listingID) {
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      Property property = mongodb.searchOneProperty(listingID);
      return property;
   }

   public boolean LoginCheck() {
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      boolean login = mongodb.userLogin(loginName, loginId);
      return login;
   }

}
