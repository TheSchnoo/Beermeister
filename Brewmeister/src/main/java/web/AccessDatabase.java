package web;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class AccessDatabase {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private final String CUSTOMERTABLE = "Customer";
    private final String SESSIONTABLE = "CustomerSession";

    public static int numAccounts = 0;


    public AccessDatabase(){

    }

    public ArrayList<BeerInfo> searchBeers(Map<String, String> searchBeerMap) throws Exception {
        String searchString;
        try {
            searchString = "WHERE ";
            int i=0;
            for(Map.Entry<String,String> entry : searchBeerMap.entrySet()){
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
                else {
                    searchString = searchString + entry.getKey() + " LIKE " + "'%" + entry.getValue() + "%'";
                }
                i++;
            }
            if(i==0){
                searchString = "";
            }
            System.out.println(searchString);

            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM beerinfo " + searchString);
            resultSet = preparedStatement.executeQuery();
            //JSONArray beers = new JSONArray();
            ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();
            while(resultSet.next()){
                listBeers.add(convertResultSetToBeerInfo(resultSet));
            }
            //return beers;
            return listBeers;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public ArrayList<BeerInfo> getRecommendations(int userid) throws Exception {
        try{
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM beerinfo");
            resultSet = preparedStatement.executeQuery();
            //JSONArray beers = new JSONArray();
            ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();
            while(resultSet.next()){
                listBeers.add(convertResultSetToBeerInfo(resultSet));
            }
            //return beers;
            return listBeers;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public Boolean addBeer(BeerInfo beer) throws Exception {
        try{
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement("INSERT INTO BeerInfo VALUES " + beer.toTupleValueString());
            resultSet = preparedStatement.executeQuery();
            //JSONArray beers = new JSONArray();
            ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();
            while(resultSet.next()){
                listBeers.add(convertResultSetToBeerInfo(resultSet));
            }
            //return beers;
            return true;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public Boolean updateBeerToDB(String bname, JSONObject jobj) throws Exception {
        try{
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement("Update BeerInfo SET " + "Description=" + jobj.getString("Description") + ", " +
                            "Brewed=" + jobj.getString("Brewed") + " WHERE " + "BName=" + bname);
            resultSet = preparedStatement.executeQuery();
            //JSONArray beers = new JSONArray();
            ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();
            while(resultSet.next()){
                listBeers.add(convertResultSetToBeerInfo(resultSet));
            }
            //return beers;
            return true;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    //  Helper for cleaning a string for queries
    private String splitAndReplace(String inString){
        return inString.split(" ")[0].replace("%20", " ");
    }

    // Convert a ResultSet to a BeerInfo object
    public BeerInfo convertResultSetToBeerInfo(ResultSet rs){

        VendorService vs = new VendorService();

        try{
            String bname = rs.getString("BName");
            String breweryName = rs.getString("BreweryName");
            String type = rs.getString("Type");
            float abv = rs.getFloat("ABV");
            float ibu = rs.getFloat("IBU");
            String description = rs.getString("Description");
//            Boolean brewed = rs.getBoolean("Brewed");
//            String averageRating; = rs.getString("")
//		  String imageLocation;

            ArrayList<Vendor> vendors = vs.getVendorsThatSellABeer(bname);


            BeerInfo newBI = new BeerInfo(bname, breweryName, type, abv, ibu, description, true, vendors);

//            return obj;
            return newBI;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public Map createAccount(Map<String, String> createAccountMap) {

        Map createAccountResponse = new HashMap();
        String insertAccountString = this.generateInsertString(createAccountMap, CUSTOMERTABLE);

        try {
            int createAccountResult = insertNewEntry(insertAccountString);
        } catch (Exception e) {
            createAccountResponse.put("created", false);
            return createAccountResponse;
        }

        numAccounts++;
        createAccountResponse.put("created", true);
        createAccountResponse.put("uuid", numAccounts);
        close();
        return createAccountResponse;
    }

    public Map checkCredentials(Map<String, String> checkCredentials, String password) throws SQLException{

        Map checkCredentialResponse = new HashMap();

        String searchAccountString = this.generateSearchString(checkCredentials, CUSTOMERTABLE);
        ResultSet searchResult;

        try {
            searchResult = queryDatabase(searchAccountString);
        } catch (Exception e) {
            checkCredentialResponse.put("matchFound", false);
            checkCredentialResponse.put("error", CustomerAccountService.loginErrorTypes.sqlError);
            return checkCredentialResponse;
        }
        //TODO: need more checks here
        /*int sizeResult;
        try {
            sizeResult = resultSet.getFetchSize();
        } catch (SQLException e) {
            sizeResult = 0;
        }

        if (sizeResult == 0) {
            checkCredentialResponse.put("matchFound", false);
            checkCredentialResponse.put("error", CustomerAccountService.loginErrorTypes.noAccountFound);
            return checkCredentialResponse;
        }*/

        while(searchResult.next()){
            String cPassword = resultSet.getString("CPassword");
            if (cPassword.equals(password)) {
                checkCredentialResponse.put("matchFound", true);
                checkCredentialResponse.put("CID", resultSet.getString("CID"));
                return checkCredentialResponse;
            }
        }

        checkCredentialResponse.put("matchFound", true);
        checkCredentialResponse.put("error", CustomerAccountService.loginErrorTypes.wrongPassword);

        close();

        return checkCredentialResponse;
    }

    public Map createCustomerSession(Map<String, String> createSessionParams) {

        Map createSessionResponse = new HashMap<String, Integer>();

        String insertNewSessionString = generateInsertString(createSessionParams, SESSIONTABLE);
        int insertResult;

        try {
            int createAccountResult = insertNewEntry(insertNewSessionString);
        } catch (Exception e) {
            createSessionResponse.put("created", false);
            return createSessionResponse;
        }

        createSessionResponse.put("created", true);
        return createSessionResponse;
    }

    private String generateInsertString(Map<String, String> searchParams, String tableName) {

        if (searchParams.isEmpty()) {
            //return an error? should this ever happen?
        }

        boolean multipleParams = false;

        String insertString = "INSERT INTO " + tableName + " VALUES (";

        if (tableName.equals(CUSTOMERTABLE)) {
            //insert NULL for id, table will change it to next available id number upon insertion
            insertString += "'" + "0" + "'";
            multipleParams = true;
        }

        for(Map.Entry<String,String> entry : searchParams.entrySet()) {

            if (entry.getValue() == null){
                continue;
            }

            if (multipleParams) {
                insertString += ", ";
            }

            multipleParams = true;

            String tempKey = entry.getKey();

            //need to treat null values differently depending on what key is
            if (entry.getValue() == null) {
                switch (tempKey) {
                    case "cname":
                    case "cpassword":
                        insertString += "NULL"; //TODO: not right way to handle, temporary only
                        break;
                }
            } else {
                insertString += "'" + entry.getValue() + "'";
            }
        }

        insertString += ")";

        return insertString;
    }

    private String generateSearchString(Map<String, String> searchParams, String tableName) {
        String queryString = "SELECT * FROM " + tableName; //change * later to be customizable

        if (searchParams.isEmpty()) {
            return queryString;
        } else {
            queryString += " WHERE ";
        }

        boolean multipleParams = false;

        for(Map.Entry<String,String> entry : searchParams.entrySet()) {
            if (entry.getValue() == null){
                continue;
            }
            if (multipleParams) {
                queryString += " AND ";
            }
            multipleParams = true;

            String tempKey = entry.getKey();

            switch (tempKey) {
                case "cname":
                case "cpassword":
                    queryString += tempKey + " like " + "'" + entry.getValue() + "'";
                    break;
            }
        }

        return queryString;
    }

    private ResultSet queryDatabase(String queryString) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            close();
            throw e;
        }
        return resultSet;
    }

    private int insertNewEntry(String insertString) throws Exception {
        int result;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect.prepareStatement(insertString);
            result = preparedStatement.executeUpdate();
        } catch (Exception e) {
            close();
            throw e;
        }
        return result;
    }
}
