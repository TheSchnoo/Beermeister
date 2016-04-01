package web;

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

@Controller
public class WebController {

    @RequestMapping(value = "/beers", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> searchBeers (@RequestParam(value="bname", required = false) String bname,
                                     @RequestParam(value="type", required = false) String btype,
                                     @RequestParam(value="vendor", required = false) String vendor,
                                     @RequestParam(value="ibu", required = false) String ibu,
                                     @RequestParam(value="abv", required = false) String abv,
                                     @RequestParam(value="rating", required = false) String rating,
                                     @RequestParam(value="description", required = false) String description,
                                     @RequestParam(value="breweryName", required = false) String breweryName,
                                     @RequestParam(value="storeName", required = false) String storeName,
                                     HttpServletResponse httpResponse) throws IOException {
        ArrayList<BeerInfo> beers;

        if(storeName == null) {

            Map<String, String> searchBeerMap = new HashMap<>();

            searchBeerMap.put("bname", bname);
            searchBeerMap.put("type", btype);
            searchBeerMap.put("ibu", ibu);
            searchBeerMap.put("abv", abv);
            //searchBeerMap.put("averageRating", rating);
            searchBeerMap.put("description", description);
            searchBeerMap.put("breweryName", breweryName);

            AccessDatabase accessDB = new AccessDatabase();
            try {
                beers = accessDB.searchBeers(searchBeerMap);
            } catch (Exception e) {
                beers = null;
            }


        }
        else{
            VendorService vendorService = new VendorService();
            try {
                beers = vendorService.getBeersByVendor(storeName);
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
    ArrayList<BeerInfo> recs (
            @RequestParam(value="userid", required = false) String userid,
            HttpServletResponse httpResponse) throws IOException {
        AccessDatabase accessDB = new AccessDatabase();
        ArrayList<BeerInfo> beers;
        try {
            beers = accessDB.getRecommendations(Integer.parseInt(userid));
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            beers = null;
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return beers;
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> revs (
            @RequestParam(value="userid", required = false) String userid,
            @RequestParam(value="bname", required = false) String bname,
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

    // Get vendors by beer name
    @RequestMapping(value = "/vendors", method = RequestMethod.GET)
    public
    @ResponseBody
    ArrayList<BeerInfo> vendors (
            @RequestParam(value="bname", required = false) String bname,
            HttpServletResponse httpResponse) throws IOException {
//        VendorService vendorService = new VendorService();
//        ArrayList<Vendor> vendors;
//
//        try {
//            vendors = vendorService.getVendorsThatSellABeer(bname);
//        }

        //TODO: ADD FUNCTIONALITY FOR GETTING VENDORS THAT SELL A BEER

//        try {
//            httpResponse.setStatus(HttpServletResponse.SC_OK);
//        } catch (Exception e) {
//            reviews = null;
//            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
        return new ArrayList<BeerInfo>();
    }


//    POST REQUESTS
//++++++++++++++++++++++++++++++++++
    @RequestMapping(value = "/beers", method = RequestMethod.POST)
    public @ResponseBody JSONObject postBeer(@RequestBody String body,
                                             @RequestParam(value="bname", required=false) String beerName) throws JSONException {
        // Add a beer
        if(beerName == null){
            JSONObject bodyJSON = new JSONObject(body);
            String bname = bodyJSON.getString("bname");
            String breweryName = bodyJSON.getString("breweryName");
            String type = bodyJSON.getString("type");
            Double abv = bodyJSON.getDouble("abv");
            Double ibu = bodyJSON.getDouble("ibu");
            String description = bodyJSON.getString("description");
            Boolean brewed = bodyJSON.getBoolean("brewed");

            BeerInfo newBI = new BeerInfo(bname, breweryName, type, abv, ibu, description, brewed);

            AccessDatabase accessDB = new AccessDatabase();

            try {
                accessDB.addBeer(newBI);
            } catch (Exception e) {
                return new JSONObject().append("created", false);
            }

            return new JSONObject().append("created", true);
        }

        // Update a beer
        else {
            JSONObject bodyJSON = new JSONObject(body);
            AccessDatabase accessDB = new AccessDatabase();

            try {
//                accessDB.updateBeerToDB(beerName, bodyJSON);
            } catch (Exception e) {
                return new JSONObject().append("updated", false);
            }

            return new JSONObject().append("updated", true);
        }
//    ArrayList<BeerInfo> beers (
//            @RequestParam(value="bname", required = false) String bname,
//            HttpServletResponse httpResponse) throws IOException {
//        AccessDatabase accessDB = new AccessDatabase();
//        ArrayList<BeerInfo> reviews;
//
//        try {
//            httpResponse.setStatus(HttpServletResponse.SC_OK);
//        } catch (Exception e) {
//            reviews = null;
//            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//        return new ArrayList<BeerInfo>();
//    }

    }

}
