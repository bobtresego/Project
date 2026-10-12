package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import database.DBConnection;

import model.User;

public class RegisterController {

    public boolean register(User user) {
        
        // Insert all fields including userRole and status
        // userRole is set to 'Staff' and status is set to 'Pending'
        String sql = "INSERT INTO users (FirstName, LastName, UserName, Password, userRole, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {

            // Set the values from the User object
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getUsername());
            pst.setString(4, user.getPassword());
            
            // Automatically set the role to 'Staff'
            pst.setString(5, "Staff");
            
            // Automatically set the status to 'Pending'
            // The Admin must approve this user before they can log in
            pst.setString(6, "Pending");

            // Execute the insert
            int rowsAffected = pst.executeUpdate();

            // If rowsAffected > 0, the insert was successful
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}