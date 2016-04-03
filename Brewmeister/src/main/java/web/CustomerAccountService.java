package web;

import javax.servlet.http.HttpServlet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class CustomerAccountService {

    public static Map createAccount(String username, String password) {
        ArrayList<String> createAccountParams = new ArrayList<>();

        createAccountParams.add(username);
        createAccountParams.add(password);

        //Insert account into db
        AccessDatabase ad = new AccessDatabase();
        Map createAccountResult = ad.createAccount(createAccountParams, "CName", AccessDatabase.CUSTOMER_TABLE);

        return createAccountResult;
    }

    public static Map login(String username, String password) {
        ArrayList<String> loginParams = new ArrayList<String>();

        loginParams.add("CName");
        loginParams.add(username);

        //Check db for match values
        AccessDatabase ad = new AccessDatabase();
        Map checkCredsResult = new HashMap<>();

        try {
            checkCredsResult = ad.checkCredentials(loginParams, password, AccessDatabase.CUSTOMER_TABLE);
        } catch (SQLException e) {
            //Case: some sql error occured
            checkCredsResult.put("authenticated", false);
            checkCredsResult.put("error", AccessDatabase.loginErrorTypes.sqlError);
            return checkCredsResult;
        }

        return checkCredsResult;
    }

    public static Map logout(String sessionId) {
        Map response = new HashMap();
        Map<String, String> loginMap = new HashMap<>();


        return response;
    }

    private static String generateSessionId() {
        Random randomNumberGenerator = new Random();
        int randomInt = randomNumberGenerator.nextInt(100);
        String sessionId = "ABC" + String.valueOf(randomInt);
        return sessionId;
    }
}
