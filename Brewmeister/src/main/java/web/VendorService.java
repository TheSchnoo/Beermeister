package web;

import java.sql.*;
import java.util.ArrayList;

public class VendorService {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public ArrayList<Vendor> getVendorsThatSellABeer(String bname) throws Exception{
        String searchString = "SELECT bv.StoreID, bv.StoreName" + //", bv.Address" +
                " FROM BeerVendor bv, beerinstock bis " +
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
