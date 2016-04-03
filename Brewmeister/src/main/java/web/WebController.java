package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class WebController {

    @RequestMapping(value = "/beers", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> searchBeers(@RequestParam(value = "bname", required = false) String bname,
                                    @RequestParam(value = "type", required = false) String btype,
                                    @RequestParam(value = "vendor", required = false) String vendor,
                                    @RequestParam(value = "ibu", required = false) String ibu,
                                    @RequestParam(value = "abv", required = false) String abv,
                                    @RequestParam(value = "rating", required = false) String rating,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "breweryName", required = false) String breweryName,
                                    @RequestParam(value = "storeId", required = false) String storeId,
                                    HttpServletResponse httpResponse) throws IOException {
        ArrayList<BeerInfo> beers;
        AccessDatabase accessDatabase = new AccessDatabase();

        Map<String, String> searchBeerMap = new HashMap<>();

        searchBeerMap.put("bname", bname);
        searchBeerMap.put("btype", btype);
        searchBeerMap.put("ibu", ibu);
        searchBeerMap.put("abv", abv);
//            searchBeerMap.put("averageRating", rating);
        searchBeerMap.put("description", description);
        searchBeerMap.put("breweryName", breweryName);

        if (storeId == null) {

            BeerService beerService = new BeerService();
            try {
                beers = accessDatabase.searchBeers(beerService.getBeers(searchBeerMap));
            } catch (Exception e) {
                beers = null;
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
            @RequestParam(value = "cid", required = false) int cid,
            HttpServletResponse httpResponse) throws IOException {

        AccessDatabase accessDB = new AccessDatabase();
        ArrayList<BeerInfo> beers;
        try {
            BeerService beerService = new BeerService();
            beers = accessDB.getRecommendations(beerService.getUnratedBeers(cid));
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            beers = null;
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
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

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public
    @ResponseBody
    Map logout(
            @RequestParam(value = "sessionId", required = true) String sessionId,
            HttpServletResponse httpResponse) throws IOException {

        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return CustomerAccountService.logout(sessionId);
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
    ArrayList<BeerInfo> revs(
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "bname", required = false) String bname,
            HttpServletResponse httpResponse) throws IOException {
        AccessDatabase accessDB = new AccessDatabase();
        ArrayList<BeerInfo> reviews;

        //TODO: ADD FUNCTIONALITY

//        try {
//            if(userid==null){
//                //TODO search reviews by beer name
//            }
//            else{
//                //TODO search reviews by a user
//            }
//            httpResponse.setStatus(HttpServletResponse.SC_OK);
//        } catch (Exception e) {
//            reviews = null;
//            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
        return new ArrayList<BeerInfo>();
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
                returnStatus.put("status", e);
                returnStatus.put("created", false);
                System.out.println("AddBeerError:" + e);
                return returnStatus;
            }

            if (rowsAffected == 0) {
                returnStatus.put("status", "beer already exists");
                returnStatus.put("created", false);
            } else {
                returnStatus.put("status", "beer added");
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
                returnStatus.put("status", e);
                returnStatus.put("created", false);
                System.out.println("UpdateBeerError:" + e);
                return returnStatus;
            }

            if (rowsAffected == 0) {
                returnStatus.put("status", "no such beer");
                returnStatus.put("created", false);
            } else {
                returnStatus.put("status", "beer updated");
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
            returnStatus.put("status", "beer already in inventory");
            returnStatus.put("created", false);
        } else {
            returnStatus.put("status", "beer added to inventory");
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
        int rating = bodyJSON.getInt("brate");
        String review = bodyJSON.getString("review");
        int cid = bodyJSON.getInt("cid");
        boolean newReview = bodyJSON.getBoolean("newReview");

        BeerReview newBR = new BeerReview(bname, review, rating, cid, newReview);

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


