package web;

import com.oracle.tools.packager.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> start(@RequestParam(value = "bname", required = false) String bname) {
        System.out.println("Start");
        Connection connect = mySqlConnection();
        try {
            if(connect == null) {
                System.out.println("Null connect");
            }
            if(connect.isClosed()) {
                System.out.println("Connection closed");
                return null;
            }

            ResultSet resultSet = connect.prepareStatement("SELECT * FROM beerinfo;").executeQuery();

            BeerService bs = new BeerService();

            ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();

            while(resultSet.next()){
                listBeers.add(bs.convertResultSetToBeerInfo(resultSet));
            }
            connect.close();
            return listBeers;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection mySqlConnection() {
        Connection mySql = null;
        System.out.println(System.getenv("CLEARDB_DATABASE_URL"));
        try {
            URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
            mySql = DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            System.out.println("Issue");
            e.printStackTrace();
        }
        return mySql;
    }

    @RequestMapping(value = "/beers", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> searchBeers(@RequestParam(value = "bname", required = false) String bname,
                                    @RequestParam(value = "type", required = false) String btype,
                                    @RequestParam(value = "vendor", required = false) String vendor,
                                    @RequestParam(value = "ibu", required = false) String ibu,
                                    @RequestParam(value = "abv", required = false) String abv,
                                    @RequestParam(value = "averageRating", required = false) String rating,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "breweryName", required = false) String breweryName,
                                    @RequestParam(value = "storeId", required = false) String storeId,
                                    @RequestParam(value = "storeName", required = false) String storeName,
                                    HttpServletResponse httpResponse) throws IOException {
        System.out.println("Moki print");
        Log.debug("Moki log");
        ArrayList<BeerInfo> beers = new ArrayList<>();
        AccessDatabase accessDatabase = new AccessDatabase();

        Map<String, String> searchBeerMap = new HashMap<>();

        searchBeerMap.put("bname", bname);
        searchBeerMap.put("btype", btype);
        searchBeerMap.put("ibu", ibu);
        searchBeerMap.put("abv", abv);
        searchBeerMap.put("avgRating", rating);
        searchBeerMap.put("description", description);
        searchBeerMap.put("breweryName", breweryName);

        if (storeId == null) {
            if(storeName == null) {

                try {
                    BeerService beerService = new BeerService();
                    beers = accessDatabase.searchBeers(beerService.getBeers(searchBeerMap));
                } catch (Exception e) {
                    beers = null;
                }
            }
            else {
                try{
                    VendorService vs = new VendorService();
                    beers = accessDatabase.searchBeersByVendorNoStock(vs.getBeersByVendor(storeName, searchBeerMap));
                } catch (Exception e){
                    System.out.print(e);
                }
            }
        } else {
            try {
                VendorService vendorService = new VendorService();
                beers = accessDatabase.searchBeersByVendor(vendorService.getBeersByVendorStocked(storeId, searchBeerMap));
            } catch (Exception e) {
                beers = null;
                e.printStackTrace();
            }
        }
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return beers;
    }

    @RequestMapping(value = "/recommendedbeers", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> recs(
            @RequestParam(value = "cid", required = false) Integer cid,
            HttpServletResponse httpResponse) throws IOException {

        AccessDatabase accessDB = new AccessDatabase();
        ArrayList<BeerInfo> beers;

        // Get 4 beers not rated by user
        if(cid!=null) {
            try {
                BeerService beerService = new BeerService();
                beers = accessDB.getRecommendations(beerService.getUnratedBeers(cid));
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                if(beers==null || beers.size()==0){
                    beers = accessDB.getHighestRatedBeers(4);
                }
            } catch (Exception e) {
                beers = null;
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        // Get 4 top rated beers
        else {
            beers = accessDB.getHighestRatedBeers(4);
        }
        return beers;

    }

    @RequestMapping(value = "/most-rated-beer", method = RequestMethod.GET)
    public
    @ResponseBody ArrayList mostRated(HttpServletResponse httpResponse) throws IOException {

        AccessDatabase accessDB = new AccessDatabase();
        ArrayList mostRatedBeer;
        try {
            mostRatedBeer = accessDB.getMostRated();
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            mostRatedBeer = null;
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return mostRatedBeer;
    }

    @RequestMapping(value = "/customer-signup", method = RequestMethod.POST)
    public
    @ResponseBody
    Map createAccount(
            @RequestBody String signupRequestBody,
            HttpServletResponse httpResponse) throws IOException, JSONException {

        JSONObject bodyJSON = new JSONObject(signupRequestBody);
        String tempUsername = bodyJSON.getString("username");
        String tempPassword = bodyJSON.getString("password");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return CustomerAccountService.createAccount(tempUsername, tempPassword);
    }

    @RequestMapping(value = "/customer-login", method = RequestMethod.GET)
    public
    @ResponseBody
    Map login(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return CustomerAccountService.login(username, password);
    }

    @RequestMapping(value = "/customer-delete", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map logout(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return CustomerAccountService.deleteAccount(username, password);
    }

    @RequestMapping(value = "/vendor-signup", method = RequestMethod.POST)
    public
    @ResponseBody
    Map createVendorAccount(
            @RequestBody String signupRequestBody,
            HttpServletResponse httpResponse) throws IOException, JSONException {

        JSONObject bodyJSON = new JSONObject(signupRequestBody);
        String tempStoreName = bodyJSON.getString("storeName");
        String tempPassword = bodyJSON.getString("password");
        String tempAddress = bodyJSON.getString("address");

        if (tempAddress == null) {
            tempAddress = "";
        }

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return VendorService.createVendorAccount(tempStoreName, tempPassword, tempAddress);
    }

    @RequestMapping(value = "/vendor-login", method = RequestMethod.GET)
    public
    @ResponseBody
    Map vendorLogin(
            @RequestParam(value = "storeName", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return VendorService.login(username, password);
    }


    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerReview> revs(
            @RequestParam(value = "bname", required = true) String bname,
            HttpServletResponse httpResponse) throws IOException {
        ArrayList<BeerReview> reviews;

        AccessDatabase accessDatabase = new AccessDatabase();

        BeerReviewService beerReviewService = new BeerReviewService();
        try {
            reviews = accessDatabase.searchReviews(beerReviewService.getReviews(bname));
        } catch (Exception e) {
            System.out.println("Review error:" + e);
            reviews = null;
        }


        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return reviews;
    }

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    public
    @ResponseBody
    BeerReview revs (
            @RequestParam(value="cid", required = true) int cid,
            @RequestParam(value="bname", required = true) String bname,
            HttpServletResponse httpResponse) throws Exception {
        AccessDatabase accessDB = new AccessDatabase();
        return accessDB.checkForReview(cid, bname);
    }
    //    POST REQUESTS
//++++++++++++++++++++++++++++++++++

    //    Creating and updating beers
    @RequestMapping(value = "/beers", method = RequestMethod.POST)

    public
    @ResponseBody
    Map postBeer(@RequestBody String body,
                 @RequestParam(value = "bname",
                         required = false) String beerName) throws JSONException {

        Map<String, Object> returnStatus = new HashMap<>();
        int rowsAffected = 0;

        // Add a beer
        if (beerName == null) {
            JSONObject bodyJSON = new JSONObject(body);
            String bname = bodyJSON.getString("bname");
            String breweryName = bodyJSON.getString("breweryName");
            String type = bodyJSON.getString("type");
            Double abv = bodyJSON.getDouble("abv");
            Double ibu = bodyJSON.getDouble("ibu");
            String description = bodyJSON.getString("description");
            Boolean brewed = bodyJSON.getBoolean("brewed");

            BeerInfo newBI = new BeerInfo(bname, breweryName, type, abv, ibu, description, 0, true);

            AccessDatabase accessDB = new AccessDatabase();

            System.out.println(newBI.toTupleValueString());

            try {
                rowsAffected = accessDB.insertToDB("BeerInfo", newBI.toTupleValueString());
            } catch (Exception e) {
                returnStatus.put("status", "Brewery does not exist or beer already exists");
                returnStatus.put("created", false);
                return returnStatus;
            }

            if (rowsAffected == 0) {
                returnStatus.put("status", "Beer already exists");
                returnStatus.put("created", false);
            } else {
                returnStatus.put("status", "Beer added");
                returnStatus.put("created", true);
            }

            return returnStatus;
        }

        // Update a beer
        else {
            HashMap updateMap = new HashMap();
            JSONObject bodyJSON = new JSONObject(body);
            if(body.contains("brewed")){
                updateMap.put("Brewed", bodyJSON.getBoolean("brewed"));
            }
            if(body.contains("description")){
                updateMap.put("Description", bodyJSON.getString("description"));
            }
            AccessDatabase accessDB = new AccessDatabase();

            try {
                beerName = "BName LIKE '%" + beerName + "%'";
                rowsAffected = accessDB.updateToDB("BeerInfo", updateMap, beerName);
            } catch (Exception e) {
                returnStatus.put("status", "Brewery does not exist");
                returnStatus.put("created", false);
                System.out.println("UpdateBeerError:" + e);
                return returnStatus;
            }

            if (rowsAffected == 0) {
                returnStatus.put("status", "No such beer");
                returnStatus.put("created", false);
            } else {
                returnStatus.put("status", "Beer updated");
                returnStatus.put("created", true);
            }

            return returnStatus;
        }
    }

    //    Add Beer to vendor inventory
    @RequestMapping(value = "/vendors", method = RequestMethod.POST)
    public
    @ResponseBody
    Map addBeerToVendor(@RequestParam(value = "bname", required = false) String bname,
                        @RequestParam(value = "storeId", required = false) String storeID) throws JSONException {
        Map<String, Object> returnStatus = new HashMap<>();
        int rowsAffected = 0;
        try {
            AccessDatabase accessDatabase = new AccessDatabase();
            // Add beer to vendor inventory
            String insertValues = "('" + bname + "', " + storeID + ")";
            rowsAffected = accessDatabase.insertToDB("BeerInStock", insertValues);
        } catch (Exception e) {
            returnStatus.put("status", e);
            returnStatus.put("created", false);
            System.out.println("AddToInventoryError:" + e);
            return returnStatus;
        }

        if (rowsAffected == 0) {
            returnStatus.put("status", "Beer already in inventory");
            returnStatus.put("created", false);
        } else {
            returnStatus.put("status", "Beer added to inventory");
            returnStatus.put("created", true);
        }

        return returnStatus;
    }

    //    Remove beer from vendor inventory
    @RequestMapping(value = "/vendors", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map removeBeerFromVendorStock(@RequestParam(value = "bname", required = false) String bname,
                                  @RequestParam(value = "storeid", required = false) int storeID) throws JSONException {

        Map<String, Object> returnStatus = new HashMap<>();
        int rowsAffected = 0;

        try {
            AccessDatabase accessDatabase = new AccessDatabase();

            // Remove beer from vendor inventory
            HashMap<String, Object> removeBeerTuple = new HashMap<>();
            removeBeerTuple.put("BName", bname);
            removeBeerTuple.put("StoreID", storeID);
            rowsAffected = accessDatabase.deleteTuple("BeerInStock", removeBeerTuple);
        } catch (Exception e) {
            returnStatus.put("status", e);
            returnStatus.put("created", false);
            System.out.println("UpdateInventoryError:" + e);
            return returnStatus;
        }

        if (rowsAffected == 0) {
            returnStatus.put("status", "beer not in inventory");
            returnStatus.put("created", false);
        } else {
            returnStatus.put("status", "beer removed from inventory");
            returnStatus.put("created", true);
        }

        return returnStatus;
    }
    // Add or Update a Review
    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public @ResponseBody Map postRating(@RequestBody String body) throws JSONException {

        JSONObject bodyJSON = new JSONObject(body);
        String bname = bodyJSON.getString("bname");
        int rating = bodyJSON.getInt("rating");
        String review = bodyJSON.getString("review");
        int cid = bodyJSON.getInt("cid");
        boolean newReview = bodyJSON.getBoolean("newReview");
        String reviewerName = bodyJSON.getString("reviewerName");

        BeerReview newBR = new BeerReview(bname, review, rating, cid, newReview, reviewerName);

        AccessDatabase accessDB = new AccessDatabase();
        Map<String,Boolean> returnMap = new HashMap<>();
        try {
            accessDB.addOrModifyReview(newBR);
            returnMap.put("created", true);
        } catch (Exception e) {
            returnMap.put("created", false);
        }

        return returnMap;
    }
}


