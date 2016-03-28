package beermeister;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class AccessDatabase {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  public void readDataBase() throws Exception {
    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://localhost/beerinfo?"
              + "user=sqluser&password=sqluserpw");
      
      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      // Result set get the result of the SQL query
      resultSet = statement
          .executeQuery("select * from test.test");
      writeResultSet(resultSet);

      // PreparedStatements can use variables and are more efficient
      preparedStatement = connect
          .prepareStatement("insert into  test.test values (default, ?)");
      // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
      // Parameters start with 1
      preparedStatement.setString(1, "wes");
      preparedStatement.executeUpdate();

      preparedStatement = connect
          .prepareStatement("SELECT id FROM test");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);

      // Remove again the insert comment
      preparedStatement = connect
      .prepareStatement("delete from test.test where name= ? ; ");
      preparedStatement.setString(1, "wes");
      preparedStatement.executeUpdate();
      
      resultSet = statement
      .executeQuery("select * from test.test");
      writeMetaData(resultSet);
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }

  private void writeMetaData(ResultSet resultSet) throws SQLException {
    //   Now get some metadata from the database
    // Result set get the result of the SQL query
    
    System.out.println("The columns in the table are: ");
    
    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
    }
  }

  private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String user = resultSet.getString("BName");
//      String name = "";
//      if(resultSet.isLast() && !resultSet.isFirst()){
//    	  name = resultSet.getString(2);
//      }
      System.out.println("BName: " + user); //+ " name: " + name);
     
    }
  }
  
  public JSONArray searchBeers(String[] params) throws Exception {
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
	      JSONArray beers = new JSONArray();
	      while(resultSet.next()){
	    	  beers.put(convertResultSetToJSONString_Beer(resultSet));
	      }
	      return beers;
	      
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
  
  public JSONArray getVendors(String st) throws Exception{
//	  splitAndReplace(st);
	  try{
		  Class.forName("com.mysql.jdbc.Driver");
		  
	      connect = DriverManager
	          .getConnection("jdbc:mysql://localhost/beerinfo?"
	              + "user=sqluser&password=sqluserpw");
	      preparedStatement = connect
	              .prepareStatement("SELECT * FROM beervendor ");
	      resultSet = preparedStatement.executeQuery();
	      JSONArray vendors = new JSONArray();
	      while(resultSet.next()){
	    	  vendors.put(convertResultSetToJSONString_Vendor(resultSet));
	      }
	      return vendors;
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
  }
  
  private String splitAndReplace(String inString){
		return inString.split(" ")[0].replace("%20", " ");
	}
  
  private JSONObject convertResultSetToJSONString_Beer(ResultSet rs){
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
		  
		  return returnJSON;
	  }
	  catch (Exception e){
		  System.out.println(e);
	  }
	  return null;
  }
  
  private JSONObject convertResultSetToJSONString_Vendor(ResultSet rs){
	  try{
		  String name = rs.getString("StoreName");
		  JSONObject returnJSON = new JSONObject();
		  returnJSON.put("storeName", name);
		  
		  return returnJSON;
	  }
	  catch (Exception e){
		  System.out.println(e);
	  }
	  return null;
  }

}