package web;


import java.sql.*;
import java.util.*;

import org.json.JSONObject;

public class AccessDatabase {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public final static String CUSTOMER_TABLE = "Customer";
    public final static String BEER_VENDOR_TABLE = "BeerVendor";

    //keep track of the current highest Id of each object type in the db
    //id gets incremented even when an insert fails, need to account for this
    public static int numCustomersInserted = 0;
    public static int numBeerVendorsInserted = 0;

    public static enum loginErrorTypes {
        noAccountFound, wrongPassword, sqlError;
    }

    public AccessDatabase(){
    }

    public ArrayList<BeerInfo> searchBeers(String searchString) throws Exception {
        open();
        System.out.println(searchString);

        //Beer search by vendor
        if(searchString.contains("SELECT")){
            preparedStatement = connect
                    .prepareStatement(searchString);
        }

        // Normal beer search
        else {
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM beerinfo " + searchString);
        }
        resultSet = preparedStatement.executeQuery();

        BeerService bs = new BeerService();

        ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();

        while(resultSet.next()){
            listBeers.add(bs.convertResultSetToBeerInfo(resultSet));
        }
        return listBeers;
    }

    public ArrayList<BeerInfo> searchBeersByVendor(String searchString) throws Exception {
        open();
        System.out.println(searchString);

        //Beer search by vendor
        if(searchString.contains("SELECT")){
            preparedStatement = connect
                    .prepareStatement(searchString);
        }

        // Normal beer search
        else {
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM beerinfo " + searchString);
        }
        resultSet = preparedStatement.executeQuery();

        BeerService bs = new BeerService();

        ArrayList<BeerInfo> listBeers = new ArrayList<BeerInfo>();

        while(resultSet.next()){
            String bname = resultSet.getString("BName");
            String breweryName = resultSet.getString("BreweryName");
            String type = resultSet.getString("BType");
            float abv = resultSet.getFloat("ABV");
            float ibu = resultSet.getFloat("IBU");
            String description = resultSet.getString("Description");
            Boolean brewed = resultSet.getBoolean("Brewed");
            double averageRating = resultSet.getDouble("AvgRating");
            Boolean stocked = resultSet.getBoolean("stocked");

            BeerInfo newBI = new BeerInfo(bname, breweryName, type, abv, ibu,
                    description, averageRating, brewed, stocked);
            listBeers.add(newBI);
        }
        return listBeers;
    }

    public ArrayList<BeerInfo> getRecommendations(int userid) throws Exception {
        open();
        try{
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM beerinfo");
            resultSet = preparedStatement.executeQuery();

            BeerService bs = new BeerService();

            ArrayList<BeerInfo> listBeers = new ArrayList<>();

            while(resultSet.next()){
                listBeers.add(bs.convertResultSetToBeerInfo(resultSet));
            }
            return listBeers;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public int insertToDB(String table, String values) throws Exception {
        open();
        try{
            System.out.println(("INSERT INTO " + table + " VALUES " + values));

            preparedStatement = connect
                    .prepareStatement("INSERT INTO " + table + " VALUES " + values);
            int insertSuccess = preparedStatement.executeUpdate();
            BeerService beerService = new BeerService();
            return insertSuccess;

        } catch (Exception e) {
            System.out.println("Error: " + e);
            throw e;
        } finally {
            close();
        }
    }

    public int updateToDB(String table, Map<String, Object> updateMap, String parameter) throws Exception {
        if(updateMap.size()==0){
            return 0;
        }
        open();
        String searchString = "Update " + table + " SET ";
        int i = 0;
        for(Map.Entry<String,Object> entry : updateMap.entrySet()){
            if(entry.getValue().getClass().equals(String.class)){
                searchString = searchString + entry.getKey() + "='" + entry.getValue() + "'";
            }
            else{
                searchString = searchString + entry.getKey() + "=" + entry.getValue();
            }

            if(i!=updateMap.size()-1){
                searchString = searchString + ", ";
            }
            i++;
        }

        if(parameter!=null){
            searchString=searchString + " WHERE " + parameter;
        }

        try{
            System.out.print(searchString);
            preparedStatement = connect
                    .prepareStatement(searchString);
            int updateSuccess = preparedStatement.executeUpdate();

            return updateSuccess;

        } catch (Exception e) {
            System.out.println("Error:" + e);
            throw e;
        } finally {
            close();
        }
    }

    public int deleteTuple(String table, Map<String, Object> deleteMap) throws Exception {
        open();
        String searchString = "DELETE FROM " + table + " WHERE ";
        int i = 0;
        for(Map.Entry<String,Object> entry : deleteMap.entrySet()){
            if(entry.getValue().getClass().equals(String.class)){
                searchString = searchString + entry.getKey() + " LIKE '%" + entry.getValue() + "%'";
            }
            else{
                searchString = searchString + entry.getKey() + "=" + entry.getValue();
            }

            if(i!=deleteMap.size()-1){
                searchString = searchString + " AND ";
            }
            i++;
        }

        try{
            System.out.print(searchString);

            preparedStatement = connect
                    .prepareStatement(searchString);
            int updateSuccess = preparedStatement.executeUpdate();

            return updateSuccess;

        } catch (Exception e) {
            System.out.println("Error:" + e);
            throw e;
        } finally {
            close();
        }
    }

    private void open(){
        if(connect==null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager
                        .getConnection("jdbc:mysql://localhost/beerinfo?"
                                + "user=sqluser&password=sqluserpw");
            } catch (Exception e) {
                System.out.println("Cannot connect to DB from AccessDatabase object");
                e.printStackTrace();
            }
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

    public Map createAccount(ArrayList<String> createAccountParams, String tableName) {

        Map createAccountResponse = new HashMap();
        String insertAccountString = this.generateInsertString(createAccountParams, tableName);

        //because the id increments in db regardless of whether insert works
        if (tableName.equals(CUSTOMER_TABLE)) {
            numCustomersInserted++;
        } else if (tableName.equals(BEER_VENDOR_TABLE)) {
            numBeerVendorsInserted++;
        }

        try {
            int createAccountResult = insertNewEntry(insertAccountString);
        } catch (Exception e) {
            createAccountResponse.put("created", false);
            return createAccountResponse;
        }

        createAccountResponse.put("created", true);

        if (tableName.equals(CUSTOMER_TABLE)) {
            createAccountResponse.put("cid", numCustomersInserted);
        } else if (tableName.equals(BEER_VENDOR_TABLE)) {
            createAccountResponse.put("storeId", numBeerVendorsInserted);
        }
        close();
        return createAccountResponse;
    }

    public Map checkCredentials(ArrayList<String> checkCredentialsParams, String password, String tableName)
            throws SQLException{

        Map checkCredentialResponse = new HashMap();
        String searchAccountString;
        if(tableName.equals(CUSTOMER_TABLE)) {
            searchAccountString = this.generateSearchString(checkCredentialsParams, CUSTOMER_TABLE);
        } else {
            searchAccountString = this.generateSearchString(checkCredentialsParams, BEER_VENDOR_TABLE);
        }

        ResultSet searchResult;

        try {
            searchResult = queryDatabase(searchAccountString);
        } catch (Exception e) {
            checkCredentialResponse.put("matchFound", false);
            checkCredentialResponse.put("error", AccessDatabase.loginErrorTypes.sqlError);
            return checkCredentialResponse;
        }
        //TODO: need more checks here
        //return noAccountFound if size of result is 0

        while(searchResult.next()){
            String tempPassword;
            if (tableName.equals(CUSTOMER_TABLE)) {
                tempPassword = resultSet.getString("CPassword");
            } else {
                tempPassword = resultSet.getString("SPassword");
            }
            if (tempPassword.equals(password)) {
                checkCredentialResponse.put("authenticated", true);
                if (tableName.equals(CUSTOMER_TABLE)) {
                    checkCredentialResponse.put("cid", resultSet.getString("CID"));
                } else {
                    checkCredentialResponse.put("storeId", resultSet.getString("StoreID"));
                }
                return checkCredentialResponse;
            }
        }

        checkCredentialResponse.put("authenticated", false);
        checkCredentialResponse.put("error", AccessDatabase.loginErrorTypes.wrongPassword);

        close();

        return checkCredentialResponse;
    }

    private String generateInsertString(ArrayList<String> insertParams, String tableName) {

        if (insertParams.isEmpty()) {
            //return an error? should this ever happen?
        }

        boolean multipleParams = false;

        String insertString = "INSERT INTO " + tableName + " VALUES (";

        if (tableName.equals(CUSTOMER_TABLE) || tableName.equals(BEER_VENDOR_TABLE)) {
            //insert NULL for id, table will change it to next available id number upon insertion
            insertString += "'" + "0" + "'";
            multipleParams = true;
        }

        for (String temp : insertParams) {

            if (multipleParams) {
                insertString += ", ";
            }
            multipleParams = true;
            if (temp.length() == 0) {
                insertString += "null";
            } else {
                insertString += "'" + temp + "'";
            }
        }

        insertString += ")";

        return insertString;
    }

    private String generateSearchString(ArrayList<String> searchParams, String tableName) {
        String queryString = "SELECT * FROM " + tableName; //change * later to be customizable

        if (searchParams.isEmpty()) {
            return queryString;
        } else {
            queryString += " WHERE ";
        }

        boolean multipleParams = false;

        for (int i = 0; i < searchParams.size(); i++) {
            //searchParams[i] is sql attribute label
            if (i % 2 == 0) {
                if (multipleParams) {
                    queryString += " AND ";
                }
                if (searchParams.get(i+1) != null) {
                    queryString += searchParams.get(i);
                }
            //searchParams[i] is sql attribute value
            } else {
                String tempKey = searchParams.get(i - 1);
                switch (tempKey) {
                    case "cname":
                    case "cpassword":
                    case "storeName":
                    case "password":
                        queryString += " like " + "'" + searchParams.get(i) + "'";
                        break;
                }
            }
            multipleParams = true;
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
