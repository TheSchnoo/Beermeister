package web;

import java.sql.*;
import java.util.ArrayList;

public class VendorService {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public ArrayList<Vendor> getVendorsThatSellABeer(String bname) throws Exception{
        String searchString = "SELECT bv.* FROM BeerVendor bv, beerinstock bis " +
                "WHERE bv.StoreID = bis.StoreID AND bis.bname LIKE '%" + bname + "%'";

        System.out.println(searchString);

        try {

            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
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

    public ArrayList<BeerInfo> getBeersByVendor(String storeName) throws SQLException, ClassNotFoundException {
        String searchString = "SELECT bi.*" +
                " FROM BeerInfo bi, BeerVendor bv, BeerInStock bis " +
                "WHERE bi.BName = bis.BName AND bv.storeID = bis.storeID and bv.storeName like '%" + storeName + "%'";

        System.out.println(searchString);

        try {

            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement(searchString);
            resultSet = preparedStatement.executeQuery();
            ArrayList<BeerInfo> beers = new ArrayList<BeerInfo>();
            AccessDatabase accessDB = new AccessDatabase();
            while(resultSet.next()){
                beers.add(accessDB.convertResultSetToBeerInfo(resultSet));
            }
            //return vendors;
            return beers;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public Vendor convertResultSetToVendor(ResultSet rs) throws Exception{
        int storeID = rs.getInt("storeID");
        String storeName = rs.getString("storeName");
//        String address = rs.getString("address");
        Vendor newVendor = new Vendor(storeID, storeName); //, address);
        return newVendor;
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
