package web;

import javax.servlet.http.HttpServlet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class CustomerAccountService {


    public static enum loginErrorTypes {
        noAccountFound, wrongPassword, sqlError;
    }


    public static Map createAccount(String username, String password) {
        ArrayList<String> createAccountParams = new ArrayList<>();

        createAccountParams.add(username);
        createAccountParams.add(password);

        //Insert account into db
        AccessDatabase ad = new AccessDatabase();
        Map createAccountResult = ad.createAccount(createAccountParams, AccessDatabase.CUSTOMER_TABLE);

        return createAccountResult;
    }

    public static Map login(String username, String password) {
        Map response = new HashMap();
        ArrayList<String> loginParams = new ArrayList<String>();

        loginParams.add("cname");
        loginParams.add(username);
        //loginParams.add("cpassword");
        //loginParams.add(password);

        //Check db for match values
        AccessDatabase ad = new AccessDatabase();
        Map checkCredsResult;
        try {
            checkCredsResult = ad.checkCredentials(loginParams, password);
        } catch (SQLException e) {
            //Case: some sql error occured
            response.put("authenticated", false);
            response.put("error", loginErrorTypes.sqlError);
            return response;
        }

        if (checkCredsResult.get("matchFound").equals(true)) {
            if (checkCredsResult.containsKey("error")) {
                //Case: no account with given username found
                response.put("authenticated", false);
                response.put("error", loginErrorTypes.noAccountFound);
                return response;
            }
        } else {
            //Case: account found but wrong password provided
            response.put("authenticated", false);
            response.put("error", loginErrorTypes.wrongPassword);
        }

        //If reach this point login was legit
        //Can now create a session id

        //Insert account into db
        ArrayList<String> sessionInsertParams = new ArrayList<>();
        String sessionId = CustomerAccountService.generateSessionId();
        sessionInsertParams.add(checkCredsResult.get("CID").toString());
        sessionInsertParams.add(sessionId);
        Map insertSessionResult = ad.createCustomerSession(sessionInsertParams);

        if (insertSessionResult.get("created").equals(false)) {
            response.put("authenticated", false);
            response.put("error", loginErrorTypes.sqlError);
            return response;
        }

        response.put("authenticated", true);
        response.put("cid", checkCredsResult.get("CID"));
        response.put("sessionId", sessionId);

        return response;
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
