package app;

import java.util.ArrayList;

import app.models.Property;

//this class stores the current logged in users userName and ID
//acts as a session variable. 


public class SessionController {

    //user dependant variables
    private String userName;
    private String userId;
    private int indexControllerListings = 0;
    private int indexControllerReviews = 0;
    private int indexControllerUser = 0;
    //non user dependant
    private ArrayList<Property> listingsFound = new ArrayList<Property>();
    private String listingID= null; 



    public String getListingID() {
        return listingID;
    }
    public void setListingID(String listingID) {
        this.listingID = listingID;
    }
    public ArrayList<Property> getListingsFound() {
        return listingsFound;
    }
    public void setListingsFound(ArrayList<Property> listingsFound) {
        this.listingsFound = listingsFound;
    }
    public int getIndexController() {
        return indexControllerListings;
    }
    public void setIndexController(int indexControllerListings) {
        this.indexControllerListings = indexControllerListings;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    
    }

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    
    }

    public int getIndexControllerReviews() {
        return indexControllerReviews;
    }
    public void setIndexControllerReviews(int indexControllerReviews) {
        this.indexControllerReviews = indexControllerReviews;
    }
    public int getIndexControllerUser() {
        return indexControllerUser;
    }
    public void setIndexControllerUser(int indexControllerUser) {
        this.indexControllerUser = indexControllerUser;
    }
    public void logOut() {
    this.userName=null;
    this.userId= null;
    this.indexControllerListings = 0;
    this.indexControllerReviews = 0;
    this.indexControllerUser = 0;
    System.out.println("LOGGED OUT");

    }



    
}
