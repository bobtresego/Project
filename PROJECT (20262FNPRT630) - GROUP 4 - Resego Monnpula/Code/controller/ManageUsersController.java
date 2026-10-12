package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DBConnection;

public class ManageUsersController {

    // =====================================================
    // INNER CLASS: UserData
    // This is a simple container to hold user info for the table
    // =====================================================
    public static class UserData {
        public int id;
        public String firstName;
        public String lastName;
        public String username;
        public String role;
        public String status;

        public UserData(int id, String firstName, String lastName, String username, String role, String status) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.role = role;
            this.status = status;
        }
    }

    // =====================================================
    // METHOD: getAllUsers
    // Fetches all users from the database EXCEPT Admins
    // =====================================================
    public List<UserData> getAllUsers() {
        List<UserData> userList = new ArrayList<>();
        
        // We exclude 'Admin' so the Admin doesn't accidentally block/delete themselves
        String sql = "SELECT UserID, FirstName, LastName, UserName, userRole, status "
                   + "FROM users "
                   + "WHERE userRole != 'Admin' "
                   + "ORDER BY UserID DESC";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                userList.add(new UserData(
                    rs.getInt("UserID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("UserName"),
                    rs.getString("userRole"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return userList;
    }

    // =====================================================
    // METHOD: updateUserStatus
    // Updates the status of a user (Approve, Reject, Block)
    // =====================================================
    public boolean updateUserStatus(int userId, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE UserID = ?";
        
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {
            pst.setString(1, newStatus);
            pst.setInt(2, userId);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // METHOD: deleteUser
    // Permanently removes a user from the database
    // =====================================================
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE UserID = ?";
        
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}