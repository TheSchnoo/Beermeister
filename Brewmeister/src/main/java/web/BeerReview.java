package web;

/**
 * Created by pdante on 3/31/2016.
 */
public class BeerReview {
    String bname;
    String review;
    int rating;
    int cid;

    public BeerReview(String bname, String review, int rating, int cid) {
        this.bname = bname;
        this.review = review;
        this.rating = rating;
        this.cid = cid;
    }
    public String toTupleValueString() {
        return "(" + this.cid + ", " + this.bname + ", " + this.rating + ", " + this.review + ")";
    }
}
