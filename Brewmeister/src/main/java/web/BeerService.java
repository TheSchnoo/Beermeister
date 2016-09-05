package web;

import org.json.JSONObject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BeerService {

    public String getBeers(Map<String, String> searchBeerMap) throws Exception {
        String searchString;
        searchString = "WHERE ";
        int i=0;
        for(Map.Entry<String,String> entry : searchBeerMap.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            if(entry.getValue() == null){
                continue;
            }
            if(i>0){
                searchString = searchString + " AND ";
            }
            if(entry.getKey()=="ibu"){
                float value = Float.parseFloat(entry.getValue());
                if(value < 9){
                    searchString = searchString + entry.getKey() + " < " + 9;
                }
                else if(value >= 80){
                    searchString = searchString + entry.getKey() + " >= " + 80;
                }
                else {
                    searchString = searchString + entry.getKey() + " BETWEEN " + value + " AND " + (value + (float) 9);
                }
            }
            else if(entry.getKey()=="abv"){
                float value = Float.parseFloat(entry.getValue());
                float upperRange = value + (float) 0.99;
                if(value < 4){
                    searchString = searchString + entry.getKey() + " < " + 4;
                }
                else if(value >= 7){
                    searchString = searchString + entry.getKey() + " >= " + 7;
                }
                else{
                    searchString = searchString + entry.getKey() + " BETWEEN " + value + " AND " + upperRange;
                }
            }
            else if(entry.getKey()=="avgRating"){
                Double value = Double.parseDouble(entry.getValue());
                searchString = searchString + entry.getKey() + " > " + entry.getValue();
                }
            else {
                searchString = searchString + entry.getKey() + " LIKE '%" + entry.getValue() + "%'";
            }
            i++;
        }
        if(i==0){
            searchString = "";
        }

        System.out.println(searchString);
        return searchString;
    }

    // Convert a ResultSet to a BeerInfo object
    public BeerInfo convertResultSetToBeerInfo(ResultSet rs){

        VendorService vs = new VendorService();

        try{
            String bname = rs.getString("BName");
            String breweryName = rs.getString("BreweryName");
            String type = rs.getString("BType");
            double abv = Math.floor(rs.getDouble("ABV")*100.0)/100.0;
            float ibu = rs.getFloat("IBU");
            String description = rs.getString("Description");
            Boolean brewed = rs.getBoolean("Brewed");
            double averageRating = rs.getDouble("AvgRating");

            System.out.println(bname + breweryName + type + abv + ibu + description + brewed + averageRating);

            ArrayList<Vendor> vendors = vs.getVendorsThatSellABeer(bname);


            System.out.println(bname + breweryName + type + abv + ibu + description + brewed + averageRating);


            BeerInfo newBI = new BeerInfo(bname, breweryName, type, abv, ibu, description,
                    averageRating, brewed, vendors);

            return newBI;
        }
        catch (Exception e){
            System.out.println("Error in converting to BeerInfo:"+ e);
            e.printStackTrace();
        }
        return null;
    }

    public String getUnratedBeers(int cid){
        String searchString =
                "SELECT bi.* " +
                        "FROM beerinfo bi " +
                        "WHERE not exists " +
                        "(SELECT beerinfo.*, r.cid " +
                        "FROM BeerInfo, rates r, customer c " +
                        "WHERE r.bname = bi.bname AND c.cid = " + cid + " AND r.cid=c.cid) " +
                        "ORDER BY bi.AvgRating DESC LIMIT 4";
        return searchString;
    }
}
