package app.models;

import java.util.Date;

public class Reviews {


    String id;
    Date date;
    String listingId;
    String reviewerId;
    String reviewerName;
    String comments;


    public Reviews(String id, Date date, String listingId, String reviewerId, String reviewerName, String comments){
        this.id = id;
        this.date=date;
        this.listingId= listingId;
        this.reviewerId= reviewerId;
        this.reviewerName=reviewerName;
        this.comments=comments;

    }


    public String getId() {
        return id;
    }


   
    public Date getDate() {
        return date;
    }


    public String getListingId() {
        return listingId;
    }
    
    public String getReviewerId() {
        return reviewerId;
    }


    public String getReviewerName() {
        return reviewerName;
    }



    public String getComments() {
        return comments;
    }



        
    
}
