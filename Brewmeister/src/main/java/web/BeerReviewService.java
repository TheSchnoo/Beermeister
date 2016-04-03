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
            boolean newReview = false;

            BeerReview newBR = new BeerReview(bname, review, bRate, cid, newReview);


            return newBR;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public String getReviews(String bname) throws Exception{
        String searchString = "Select * from Rates Where BName like '%" + bname + "%'";
        return searchString;
    }
}
