package controller;


import database.DBConnection;
import java.sql.*;



public class SalesController {



public boolean makeSale(
String productName,
int quantity,
double totalPrice
){


boolean result=false;



try{



Connection con =
DBConnection.getConnection();




// Insert sale

String sql =
"INSERT INTO Sales(ProductName,Quantity,TotalPrice) VALUES(?,?,?)";



PreparedStatement pst =
con.prepareStatement(sql);



pst.setString(1,productName);

pst.setInt(2,quantity);

pst.setDouble(3,totalPrice);



pst.executeUpdate();






// Reduce stock

String update =
"UPDATE Products SET Quantity = Quantity - ? WHERE ProductName=?";



PreparedStatement updateStock =
con.prepareStatement(update);



updateStock.setInt(1,quantity);

updateStock.setString(2,productName);



updateStock.executeUpdate();





result=true;



}catch(Exception e){


System.out.println(e);


}



return result;



}


}