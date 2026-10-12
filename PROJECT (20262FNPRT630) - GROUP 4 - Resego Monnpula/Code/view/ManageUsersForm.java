package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import controller.ManageUsersController;
import controller.ManageUsersController.UserData;

public class ManageUsersForm extends JFrame {

    JTable userTable;
    DefaultTableModel tableModel;
    ManageUsersController controller;

    public ManageUsersForm() {
        setTitle("Manage Users - Admin");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        controller = new ManageUsersController();

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(new Color(232, 238, 245));
        add(backgroundPanel);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(31, 52, 80));
        headerPanel.setBounds(0, 0, 950, 80);
        backgroundPanel.add(headerPanel);

        JLabel title = new JLabel("MANAGE USERS");
        title.setBounds(30, 20, 890, 40);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(title);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBounds(30, 100, 890, 350);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        backgroundPanel.add(tablePanel);

        String[] columns = {"ID", "First Name", "Last Name", "Username (Email)", "Role", "Status"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(37, 132, 190));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        userTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("Approved".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(39, 174, 96));
                } else if ("Pending".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(243, 156, 18));
                } else if ("Blocked".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(192, 57, 43));
                } else {
                    c.setForeground(Color.BLACK);
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBounds(30, 470, 890, 60);
        buttonPanel.setOpaque(false);
        backgroundPanel.add(buttonPanel);

        JButton approveButton = createStyledButton("Approve", new Color(46, 204, 113));
        JButton rejectButton = createStyledButton("Reject", new Color(231, 76, 60));
        JButton blockButton = createStyledButton("Block", new Color(241, 196, 15));
        JButton deleteButton = createStyledButton("Delete", new Color(192, 57, 43));
        JButton refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        JButton closeButton = createStyledButton("Close", new Color(100, 110, 120));

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(blockButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        refreshButton.addActionListener(e -> loadUserData());

        closeButton.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        approveButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

            if ("Approved".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "User is already approved.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (controller.updateUserStatus(userId, "Approved")) {
                JOptionPane.showMessageDialog(this, "User Approved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to approve user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        rejectButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

            if (!"Pending".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Only pending users can be rejected.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to reject this user?", "Confirm Reject", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.updateUserStatus(userId, "Rejected")) {
                    JOptionPane.showMessageDialog(this, "User Rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to reject user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        blockButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

            if (!"Approved".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Only approved users can be blocked.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to block this user?", "Confirm Block", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.updateUserStatus(userId, "Blocked")) {
                    JOptionPane.showMessageDialog(this, "User Blocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to block user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to PERMANENTLY DELETE this user from the database?\nThis action cannot be undone.",
                    "Confirm Permanent Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(this, "User Deleted Permanently.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadUserData();

        setVisible(true);
    }

    private void loadUserData() {
        tableModel.setRowCount(0);
        List<UserData> users = controller.getAllUsers();

        for (UserData user : users) {
            tableModel.addRow(new Object[]{
                user.id,
                user.firstName,
                user.lastName,
                user.username,
                user.role,
                user.status
            });
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsersForm());
    }
}