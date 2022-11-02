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
public class ReviewsPage implements Handler {

   // URL of this page relative to http://localhost:7000/
   public static final String URL = "/ReviewsPage";
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
            session.setIndexControllerReviews(session.getIndexControllerUser() + 10);
         }
      } catch (Exception e) {
      }

      try {
         if (context.formParam("back10").equals("back10")) {
            session.setIndexControllerReviews(session.getIndexControllerUser() - 10);
         }
      } catch (Exception e) {
      }

      if (session.getUserId() == null) {
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

         html = html + "<h1> REVIEW PAGE</h1>";

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
      }
      html = html + "</div>";
      html = html + "</div>";

      // END LEFT DIVISION

      // BEGIN RIGHT DIVISION
      html = html + " <form action='/' method='post'>";
      html = html + "  <input type='hidden' name='back' value= 'back'>";

      html = html + " <button type='submit'>back</button>";

      html = html + "</form> ";
      html = html + "<div style='margin-left: 10%; width = 90%; background: purple;'> ";

      ArrayList<Reviews> reviews = getUserReviews(session.getUserId(), session);
      html = html + "<h2>Reviews: </h2>";

      if (reviews.size() == 0) {

         html = html + "<p>no reviews made</p>";

      } else {
         try {
            for (int i = session.getIndexControllerUser(); i < session.getIndexControllerUser() + 10; i++) {
               html = html + "<div class = 'listingReview'>";
               html = html + "<p> <b>name:</b> " + reviews.get(i).getReviewerName() + "</p>";
               html = html + "<p> <b>date:</b> " + reviews.get(i).getDate() + "</p>";
               html = html + "<p> <b>comment:</b> " + reviews.get(i).getComments() + "</p>";
               html = html + "</div>";

            }
         } catch (Exception e) {

         }
         if (session.getIndexControllerReviews() > 0) {
            html = html + " <form action='/Listing' method='post'>";
            html = html + "  <input type='hidden' name='back10' value= 'back10'>";

            html = html + " <button type='submit'>back 10</button>";

            html = html + "</form> ";
         }
         html = html + " <form action='/ReviewsPage' method='post'>";
         html = html + "  <input type='hidden' name='next10' value= 'next10'>";

         html = html + " <button type='submit'>next 10</button>";

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

      // DO NOT MODIFY THIS
      // Makes Javalin render the webpage

      context.html(html);

      // end html rendering and logic
   }

   // functions

   private ArrayList<Reviews> getUserReviews(String userId, SessionController session) {
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      ArrayList<Reviews> reviews = new ArrayList<Reviews>();
      reviews = mongodb.getUserReviews(session.getUserId());
      return reviews;
   }

}
