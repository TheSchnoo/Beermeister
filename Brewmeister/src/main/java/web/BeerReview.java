package web;

/**
 * Created by pdante on 3/31/2016.
 */
public class BeerReview {
    private String bname;
    private String review;
    private int rating;
    private int cid;
    private String reviewerName;
    private boolean newReview;

    public String getBname() {
        return bname;
    }

    public String getReview() {
        return review;
    }

    public int getRating() {
        return rating;
    }

    public int getCid() {
        return cid;
    }

    public boolean isNewReview() {
        return newReview;
    }

    public String getReviewerName() {
        return reviewerName;
    }



    public BeerReview(String bname, String review, int rating, int cid, boolean newReview, String reviewerName) {

        this.bname = bname;
        this.review = review;
        this.rating = rating;
        this.cid = cid;
        this.newReview = newReview;
        this.reviewerName = reviewerName;
    }
    public BeerReview(String bname, String review, int rating, int cid, String reviewerName) {
        this.bname = bname;
        this.review = review;
        this.rating = rating;
        this.cid = cid;
        this.newReview = false;
        this.reviewerName = reviewerName;
    }
    public String toTupleValueString() {
        return "(" + this.cid + ", '" + this.bname + "', " + this.rating + ", '" + this.review + "')";
    }
}
