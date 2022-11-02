package app;

import java.util.ArrayList;

import app.models.Property;
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
public class Index implements Handler {

   // URL of this page relative to http://localhost:7000/
   public static final String URL = "/";
   public static String usernames[] = { "halil", "bill" };
   public static String passwords[] = { "halilpass", "billpass" };
   String loginName;
   String loginId;
   ArrayList<Property> listingsFound = new ArrayList<Property>();

   @Override
   public void handle(Context context) throws Exception {
      // import session controller- reset to no user.

      SessionController session = App.getSessionController();
      listingsFound = session.getListingsFound();

      String html = null;
      // Create a simple HTML webpage in a String
      // Add some Header information
      html = html + "<head>" + "<title>Homepage</title>\n";

      // Add some CSS (external file)
      html = html + "<link rel='stylesheet' type='text/css' href='common.css' />\n";

      // Add the body
      html = html + "<body>\n";

      // create left/right division

      html = html + "<div style='width: 100%;'>";

      // BEGIN LEFT DIVISION
      html = html
            + " <div style='width: 200px;  height: 4600px; float: left; background: green; border-width: 5px; border-color: rgb(88, 88, 88);'>";

      html = html + "<div>";

      if (session.getUserId() == null) {

         html = html + "<h1> AIRBnB- please login to continue </h1>";

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
         html = html + " <form action='/' method='post'>";
         html = html + " <input type='hidden' name='logout' value = 'logout' >";
         html = html + " <button type='submit'>log out</button>";

         html = html + "</form> ";
      }
      html = html + "</div>";
      html = html + "</div>";

      // END LEFT DIVISION

      // BEGIN RIGHT DIVISION
      html = html + "<div style='margin-left: 10%; width = 90%; background: purple;'> ";
      html = html + "<div>";

      html = html + "<div class ='formbox'>";

      html = html + " <form action='/' method='post'>";

      html = html + "<div class='container'>";
      html = html + "  <label for='country'><b>country</b></label>";
      html = html + "  <input type='text' placeholder='country' name='location'>";
      html = html + "  <label for='market'><b>market/city</b></label>";
      html = html + "  <input type='text' placeholder='city' name='market'>";

      html = html + "   <label for='people'>minimum people (1-10)</label>";
      html = html + "  <input type='number' id='people' name='people' min='1' max='10'>";

      html = html + " <button type='submit'>search</button>";
      html = html + "</form> ";
      html = html + "</div>";
      html = html + "</div>";
      html = html + "</div>";
      html = html + "</div>";

      // END SEARCH CRITERIA DIVISIONS
      if(context.formParam("logout") != null){
         session.logOut();
         context.redirect("/");
      }

      try{

      if (listingsFound.size() != 0) {
         for (int i = session.getIndexController(); i < session.getIndexController() + 10; i++) {
            // left div for picture

            html = html + "<div class= 'float-child-left'>";
            html = html + "<img src='" + listingsFound.get(i).getUrl()
                  + "' alt='Property Image' style='width:250px;height:250px;'>";
            html = html + "</div>";
            // end left picture div
            // begin right div for text info
            html = html + "<div class ='float-child-right'>";
            html = html + "<h3>" + listingsFound.get(i).getName() + "</h3>";
            html = html + "<p>" + listingsFound.get(i).getSummary() + "</p>";
            html = html + "<p> <b>bedrooms:</b> " + listingsFound.get(i).getNumberBedrooms() + "</p>";
            html = html + "<p> <b>beds:</b> " + listingsFound.get(i).getBeds() + "</p>";
            html = html + "<p> <b>price:</b> " + listingsFound.get(i).getPrice() + "</p>";

            html = html + " <form action='/' method='post'>";

            html = html + "<div class='container'>";

            html = html + "  <input type='hidden' name='propertyIndex' value= '" + listingsFound.get(i).getMongodb()
                  + "'>";

            html = html + " <button type='submit'>View Property</button>";

            html = html + "</div>";
            html = html + "</form> ";

            // end listing div
            html = html + "</div>";

         }
      }
   }catch(Exception e){
      //catch for if loop exceeds index (at end of array) 
   }

      // END RIGHT DIVISION
     
         html = html + " <div syle = 'padding-left: 500px'>";
         html = html + " <form action='/' method='post'>";
         html = html + "  <input type='hidden' name='next10' value= 'next10'>";

         html = html + " <button type='submit'>next 10</button>";

         html = html + "</form> ";
         html = html + "</div>";
     
      
      html = html + "</div>";

      

      // collect form parameters

      loginName = context.formParam("userName");
      loginId = context.formParam("mongoId");
      String location = context.formParam("location");
      String market = context.formParam("market");


      if (context.formParam("next10") != null) {
         session.setIndexController(session.getIndexController() + 10);
         context.redirect("/");
      }
      int people = 0;
      try {
         people = Integer.parseInt(context.formParam("people"));
      } catch (Exception e) {
      }
      // DO NOT MODIFY THIS
      // Makes Javalin render the webpage

      context.html(html);
      boolean loggedIn = false;

      // do login

      if (loginName != null && loginId != null)
         if (!loginName.equals("") && !loginId.equals(""))
            loggedIn = LoginCheck();
      if (loggedIn == true) {
         System.out.println("logged in " + session.getUserName());
         context.redirect("/");
      }

      //search location only
      try{
      if(!location.equals("") && market.equals("")){
         System.out.println("location search only");
         searchLocationOnly(location);
         context.redirect("/");

      }
   }catch(Exception e){

   }
   //search market only
   try{
      if(location.equals("") && !market.equals("")){
         System.out.println("market search only");
         searchMarketOnly(market);
         context.redirect("/");

      }
   }catch(Exception e){

   }

      //search all boxes
      try {
         if (!location.equals("") && !market.equals("")) {

            System.out.println("ALL SEARCH");

            searchData(location, market, people);
            context.redirect("/");
         }
      } catch (Exception e) {
      }

      // list property

      if (context.formParam("propertyIndex") != null) {

         session.setListingID(context.formParam("propertyIndex"));
         context.redirect("/Listing");
      }

      // DO NOT MODIFY THIS
      // Makes Javalin render the webpage

      context.html(html);

      // end html rendering and logic
   }

   // functions

   private void searchLocationOnly(String location) {
     
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      mongodb.searchLocationOnly(location);
   }

   private void searchMarketOnly(String market) {
     
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      System.out.println("calling search");
      mongodb.searchLocationOnly(market);
   }

   public boolean LoginCheck() {

      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      boolean login = mongodb.userLogin(loginName, loginId);
      return login;
   }

   // perform mongodb search and return related apartments\

   public boolean searchData(String location, String market, int people) {
      System.out.println("SEARCHING");
      MongoDBConnection mongodb = MongoDBConnection.getConnection();
      mongodb.search(location, market);

      return true;
   }
}
