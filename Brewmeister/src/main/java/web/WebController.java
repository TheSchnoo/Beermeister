package web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @RequestMapping("/beers")
    public
    @ResponseBody
//    String[] searchParams = new String[@RequestParam.split('=').length];
    ArrayList<BeerInfo> searchBeers (@RequestParam(value="bname", required = false) String bname,
                                     @RequestParam(value="type", required = false) String btype,
                                     @RequestParam(value="vendor", required = false) String vendor,
                                     @RequestParam(value="ibu", required = false) String ibu,
                                     @RequestParam(value="abv", required = false) String abv,
                                     @RequestParam(value="rating", required = false) String rating,
//                                     @RequestParam(value="description", required = false) String description,
                                     @RequestParam(value="breweryname", required = false) String breweryName) {

        Map<String,String> searchBeerMap = new HashMap<>();

        searchBeerMap.put("bname", bname);
        searchBeerMap.put("type", btype);
        searchBeerMap.put("ibu", ibu);
        searchBeerMap.put("abv", abv);
        searchBeerMap.put("averageRating", rating);
//        searchBeerMap.put("Description", description);
        searchBeerMap.put("breweryName", breweryName);

        AccessDatabase accessDB = new AccessDatabase();
        ArrayList<BeerInfo> beers;
        try {
            beers = accessDB.searchBeers(searchBeerMap);
        } catch (Exception e) {
            beers = null;
        }

        return beers;
    }
}
