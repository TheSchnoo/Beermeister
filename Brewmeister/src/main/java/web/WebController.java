package web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
public class WebController {

    @RequestMapping("/searchbeers")
    public
    @ResponseBody
//    String[] searchParams = new String[@RequestParam.split('=').length];

    ArrayList<BeerInfo> searchBeers (@RequestParam(value="BName", required = false) String bname,
                                     @RequestParam(value="BType", required = false) String btype,
                                     @RequestParam(value="FName", required = false) String fname,
                                     @RequestParam(value="IBU", required = false) String ibu,
                                     @RequestParam(value="ABV", required = false) String abv,
                                     @RequestParam(value="Description", required = false) String description,
                                     @RequestParam(value="breweryname", required = false) String breweryName) {

        AccessDatabase accessDB = new AccessDatabase();
        String[] searchParams = new String[1];
        searchParams[0] = breweryName;
        ArrayList<BeerInfo> beers;
        try {
            beers = accessDB.searchBeers(searchParams);
        } catch (Exception e) {
            beers = null;
        }

        return beers;
    }
}
