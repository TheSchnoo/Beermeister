package web;

/**
 * Created by pdante on 3/31/2016.
 */
public class BeerReview {
    private String bname;
    private String review;
    private int rating;
    private int cid;
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



    public BeerReview(String bname, String review, int rating, int cid, boolean newReview) {
        this.bname = bname;
        this.review = review;
        this.rating = rating;
        this.cid = cid;
        this.newReview = newReview;
    }
    public String toTupleValueString() {
        return "(" + this.cid + ", " + this.bname + ", " + this.rating + ", " + this.review + ")";
    }
}
