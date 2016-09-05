package web;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VendorService {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private Connection openMySqlConnection() {
        Connection mySql = null;
        try {
            URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
            mySql = DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mySql;
    }

    public ArrayList<Vendor> getVendorsThatSellABeer(String bname) throws Exception{
        String searchString =
                "SELECT bv.* " +
                        "FROM BeerVendor bv, beerinstock bis " +
                        "WHERE bv.StoreId = bis.StoreId AND bis.bname LIKE '%" + bname + "%'";

        System.out.println(searchString);

        try {
            connect = openMySqlConnection();
            preparedStatement = connect
                    .prepareStatement(searchString);
            resultSet = preparedStatement.executeQuery();
            //JSONArray beers = new JSONArray();
            ArrayList<Vendor> vendors = new ArrayList<Vendor>();
            while(resultSet.next()){
                vendors.add(convertResultSetToVendor(resultSet));
            }
            //return vendors;
            return vendors;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public String getBeersByVendor(String storeName, Map searchMap) throws Exception {
        BeerService beerService = new BeerService();
        String searchString =
                "SELECT bi.* " +
                "FROM BeerInfo bi, BeerVendor bv, BeerInStock bis " +
                "WHERE bi.BName = bis.BName AND bv.storeID = bis.storeID and bv.storeName LIKE '%" + storeName + "%' " +
                        beerService.getBeers(searchMap);

        System.out.println(searchString);
        return searchString;
    }

    public String getBeersByVendorStocked(String storeId, Map searchBeerMap) throws Exception {
        String searchString =
                "SELECT *, CASE WHEN " +
                        "(SELECT BName " +
                        "FROM BeerVendor bv, BeerInStock bis " +
                        "WHERE beerinfo.BName = bis.BName AND bv.storeID = bis.storeID " +
                        "AND bv.storeId="+storeId+") IS NULL THEN 0 ELSE 1 END AS stocked FROM beerinfo";
        if(searchBeerMap.size()>0){
            BeerService beerService = new BeerService();
            searchString = searchString + " " + beerService.getBeers(searchBeerMap);
        }

        System.out.println(searchString);
        return searchString;
    }

    public Vendor convertResultSetToVendor(ResultSet rs) throws Exception{
        int storeID = rs.getInt("storeID");
        String storeName = rs.getString("storeName");
        String address = rs.getString("address");
        Vendor newVendor = new Vendor(storeID, storeName, address);
        return newVendor;
    }

    public static Map createVendorAccount(String storeName, String password, String address) {
        ArrayList<String> createVendorAccountParams = new ArrayList<>();

        createVendorAccountParams.add(storeName);
        createVendorAccountParams.add(address);
        createVendorAccountParams.add(password);

        //Insert account into db
        AccessDatabase ad = new AccessDatabase();
        Map createAccountResult = ad.createAccount(createVendorAccountParams, "StoreName",
                AccessDatabase.BEER_VENDOR_TABLE);

        return createAccountResult;
    }

    public static Map login(String storeName, String password) {
        ArrayList<String> loginParams = new ArrayList<String>();
        loginParams.add("StoreName");
        loginParams.add(storeName);

        //Check db for match values
        AccessDatabase ad = new AccessDatabase();
        Map checkCredsResult = new HashMap<>();

        try {
            checkCredsResult = ad.checkCredentials(loginParams, password, AccessDatabase.BEER_VENDOR_TABLE);
        } catch (SQLException e) {
            //Case: some sql error occured
            checkCredsResult.put("authenticated", false);
            checkCredsResult.put("error", AccessDatabase.loginErrorTypes.sqlError);
            return checkCredsResult;
        }

        return checkCredsResult;
    }

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
}
