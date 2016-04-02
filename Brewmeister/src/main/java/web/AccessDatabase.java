package web;


import java.sql.*;
import java.util.*;

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
