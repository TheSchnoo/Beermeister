package web;


import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import web.BeerInfo;

public class AccessDatabase {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public ArrayList<BeerInfo> searchBeers(String[] params) throws Exception {
        String searchString = "";
        try {
            if(params.length <= 1){
                // !!! fill in here
            }
            else{
                searchString = "WHERE ";
                for(int i = 1; i < params.length; i++){
                    String[] split = params[i].split("=");
                    searchString = searchString + split[0] + " LIKE " + "'%"+splitAndReplace(split[1])+"%'";
                    if(i+1 < params.length){
                        searchString = searchString + " AND ";
                    }
                }
            }

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
                //beers.put(convertResultSetToJSONString_Beer(resultSet));
                listBeers.add(convertResultSetToJSONString_Beer(resultSet));
            }
            //return beers;
            return listBeers;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public String getBeersFromBrewery(String brewery) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/beerinfo?"
                            + "user=sqluser&password=sqluserpw");
            preparedStatement = connect
                    .prepareStatement("SELECT bname, BreweryName FROM beerinfo WHERE BreweryName LIKE '%'+?+'%';");
            preparedStatement.setString(1, brewery);
            resultSet = preparedStatement.executeQuery();
            String ans = "";
            while(resultSet.next()){
                ans = ans + "BeerName: " + resultSet.getString(1) + " Brewery: " + resultSet.getString(2);
            }
            return ans;

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

    private String splitAndReplace(String inString){
        return inString.split(" ")[0].replace("%20", " ");
    }

    //private JSONObject convertResultSetToJSONString_Beer(ResultSet rs){
    private BeerInfo convertResultSetToJSONString_Beer(ResultSet rs){
        try{
            String name = rs.getString("BName");
            String brewery = rs.getString("BreweryName");
            String type = rs.getString("BType");
            float abv = rs.getFloat("ABV");
            float ibu = rs.getFloat("IBU");
//		  String averageRating;
//		  int beerID;
//		  String imageLocation;
            JSONObject returnJSON = new JSONObject();
            returnJSON.put("name", name);
            returnJSON.put("brewery", brewery);
            returnJSON.put("type", type);
            returnJSON.put("abv", abv);
            returnJSON.put("ibu", ibu);

            BeerInfo newBI = new BeerInfo(name, brewery, type, abv, ibu);

            //return returnJSON;
            return newBI;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;

    }
}
