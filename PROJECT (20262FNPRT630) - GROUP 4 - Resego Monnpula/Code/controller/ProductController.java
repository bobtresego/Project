package controller;


import database.DBConnection;
import java.sql.*;



public class ProductController {



public boolean addProduct(
String name,
String category,
int quantity,
double price
){



boolean result=false;



try{



Connection con =
DBConnection.getConnection();



String sql =
"INSERT INTO Products(ProductName,Category,Quantity,Price) VALUES(?,?,?,?)";



PreparedStatement pst =
con.prepareStatement(sql);



pst.setString(1,name);

pst.setString(2,category);

pst.setInt(3,quantity);

pst.setDouble(4,price);



pst.executeUpdate();



result=true;



}catch(Exception e){


System.out.println(e);


}



return result;



}



}