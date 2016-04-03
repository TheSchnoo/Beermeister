package web;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Pdante on 2016-04-01.
 */
public class BeerReviewService {

    public BeerReview convertResultSetToBeerReview(ResultSet rs){

        try{
            String bname = rs.getString("BName");
            int cid = rs.getInt("cid");
            int bRate = rs.getInt("brate");
            String review = rs.getString("review");
            String reviewerName = rs.getString("CName");
            boolean newReview = false;

            BeerReview newBR = new BeerReview(bname, review, bRate, cid, newReview, reviewerName);


            return newBR;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public String getReviews(String bname) throws Exception{
        String searchString =
                "Select Rates.*, Customer.cname " +
                "from Rates, Customer " +
                        "Where BName like '%" + bname + "%' and rates.cid=customer.cid;";
        return searchString;
    }
}
