package web;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

public class CustomerAccountService {


    public static Map createAccount(String username, String password) {
        Map response = new HashMap();
        Map<String, String> createAccountMap = new HashMap<>();

        createAccountMap.put("cname", username);
        createAccountMap.put("cpassword", password);

        //Insert account into db
        AccessDatabase ad = new AccessDatabase();
        Map operationResult = ad.createAccount(createAccountMap);

        if (operationResult.containsValue("success")) {
            response.put("created", true);
            response.put("uuid", 1);
        } else {
            response.put("created", false);
        }
        return response;
    }

    public static Map login(String username, String password) {
        Map response = new HashMap();
        Map<String, String> loginMap = new HashMap<>();

        loginMap.put("cname", username);
        loginMap.put("cpassword", password);

        return response;
    }

    public static Map logout(String sessionId) {
        Map response = new HashMap();
        Map<String, String> loginMap = new HashMap<>();


        return response;
    }
}
