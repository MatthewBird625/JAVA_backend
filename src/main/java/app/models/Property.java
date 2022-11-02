package app.models;

import java.util.ArrayList;

import org.bson.types.Decimal128;

//a class to store property details after they are found in mongodb


public class Property {

    private String mongodb;
    private String name;
    private String summary;
    private String address;
    private String suburb;
    private String countryCode;
    private String country;
    private String market;
    private int reviewScoreRating;
    private Decimal128 price;
    private String propertyType;
    private ArrayList<String> amenities;
    private int numberBedrooms;
    private int numberBeds;
    private int numberPeople;
    private boolean superHost;
    private String url;


    public Property(){}
  
    //search results constructor
    public Property(String id, String name, String summary, Decimal128 price, int numberBedrooms, int numberBeds, String url){
        this.mongodb= id;
        this.name= name;
        this.summary= summary;
        this.numberBedrooms = numberBedrooms;
        this.numberBeds = numberBeds;
        this.price = price;
        this.url = url;


    }
   
    
    //listing constructor
    public Property(String mongodb, String name, String summary, String address, String suburb, String countryCode, String country, String market, int reviewScoreRating, Decimal128 price, String propertyType, ArrayList<String> amenitites, int numberBedrooms, int numberBeds,
     int numberPeople, boolean superHost, String url){
        this.mongodb = mongodb;
        this.name = name;
        this. summary = summary;
        this.address = address;
        this.suburb= suburb;
        this.countryCode=countryCode;
        this.country = country;
        this.market = market;
        this.reviewScoreRating= reviewScoreRating;
        this.price= price;
        this.propertyType = propertyType;
        this.propertyType = propertyType;
        this.amenities = amenitites;
        this.numberBedrooms = numberBedrooms;
        this.numberPeople = numberPeople;
        this.superHost = superHost;
        this.url = url;
        this.numberBeds=numberBeds;
      


    }; 
    public String getMongodb() {
        return mongodb;
    }

    public String getName() {
        return name;
    }
 
    public String getSummary() {
        return summary;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public int getReviewScoreRating() {
        return reviewScoreRating;
    }

    public Decimal128 getPrice() {
        return price;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    
    }

    public int getNumberBedrooms() {
        return numberBedrooms;
    }
    public String getSuburb() {
        return suburb;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public int getNumberPeople() {
        return numberPeople;
    }


    public boolean isSuperHost() {
        return superHost;
    }

    public String getUrl() {
        return url;
    }

    public String getMarket() {
        return market;
    }
   
    public int getBeds() {
        return numberBeds;
    }
   


    
}