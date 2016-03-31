package web;

import org.json.HTTP;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                                     @RequestParam(value="breweryname", required = false) String breweryName,
                                     HttpServletResponse httpResponse) throws IOException {

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

        httpResponse.setStatus(HttpServletResponse.SC_OK);

        return beers;
    }

    @RequestMapping("/recommendedbeers")
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


    @RequestMapping("/signup")
    public
    @ResponseBody
    Map createAccount (
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse httpResponse) throws IOException {

            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return CustomerAccountService.createAccount(username, password);
    }

    @RequestMapping("/login")
    public
    @ResponseBody
    Map login (
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse httpResponse) throws IOException {

            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return CustomerAccountService.login(username, password);
    }

    @RequestMapping("/logout")
    public
    @ResponseBody
    Map logout (
            @RequestParam(value = "sessionId", required = true) String sessionId,
            HttpServletResponse httpResponse) throws IOException {

            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return CustomerAccountService.logout(sessionId);
    }
}
