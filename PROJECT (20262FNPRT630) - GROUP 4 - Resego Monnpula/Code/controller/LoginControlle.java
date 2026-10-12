package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;
import database.Session;

public class LoginController {

    /**
     * Attempts to log a user in.
     * 
     * @return  1  = Login Successful
     *          0  = Wrong Email or Password
     *         -1  = Account Pending Admin Approval
     *         -2  = Account Blocked
     */
    public int login(String email, String password) {

        // Select the 'status' column as well so we can check it
        String sql = "SELECT UserID, UserName, userRole, "
                + "FirstName, LastName, status "
                + "FROM users "
                + "WHERE UserName = ? AND Password = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)
        ) {

            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    String status = rs.getString("status");

                    // 1. Check if the account is still pending approval
                    if ("Pending".equalsIgnoreCase(status)) {
                        return -1;
                    }

                    // 2. Check if the account has been blocked
                    if ("Blocked".equalsIgnoreCase(status) 
                            || "Rejected".equalsIgnoreCase(status)) {
                        return -2;
                    }

                    // 3. If status is 'Approved', log them in
                    Session.setCurrentUser(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("userRole"),
                            rs.getString("FirstName"),
                            rs.getString("LastName")
                    );

                    return 1;
                }
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        // 4. Wrong email or password
        return 0;
    }
}