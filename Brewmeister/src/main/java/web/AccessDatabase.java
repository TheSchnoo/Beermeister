package web;


import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import web.BeerInfo;

public class AccessDatabase {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public ArrayList<BeerInfo> searchBeers(Map<String, String> searchBeerMap) throws Exception {
        String searchString = "";
        try {
            if(searchBeerMap.size() <= 1){
                // !!! fill in here
            }
            else{
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
                        searchString = searchString + entry.getKey() + " BETWEEN " + value + " AND " + (value + (float) 9);
                    }
                    if(entry.getKey()=="abv"){
                        float value = Float.parseFloat(entry.getValue());
                        float upperRange = value + (float) 1;
                        if(value == 0){
                            upperRange = value + (float) 4;
                        }
                        searchString = searchString + entry.getKey() + " BETWEEN " + value + " AND " + upperRange;
                    }
                    else {
                        searchString = searchString + entry.getKey() + " LIKE " + "'%" + entry.getValue() + "%'";
                    }
                    i++;
                }
                System.out.println(searchString);
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
            String type = rs.getString("Type");
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
