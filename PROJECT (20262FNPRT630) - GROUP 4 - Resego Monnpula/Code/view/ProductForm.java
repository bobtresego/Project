package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

import database.DBConnection;

public class ProductForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField;
    private JTextField categoryField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField searchField;

    private JButton addButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton showAllButton;
    private JButton logoutButton;

    private int selectedID = 0;

    private Dashboard previousDashboard;

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    private static final int CONTENT_LEFT = 70;
    private static final int CONTENT_WIDTH = 760;

    private final Color BACKGROUND = new Color(240, 245, 250);
    private final Color DARK_BLUE = new Color(40, 70, 120);
    private final Color HEADER_BLUE = new Color(32, 57, 92);
    private final Color BUTTON_BLUE = new Color(40, 120, 220);
    private final Color SHOW_ALL_BLUE = new Color(70, 130, 180);
    private final Color WHITE = Color.WHITE;

    public ProductForm() {
        this(null);
    }

    public ProductForm(Dashboard previousDashboard) {

        this.previousDashboard = previousDashboard;

        setTitle("Product Management");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(panel);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(HEADER_BLUE);
        headerPanel.setBounds(0, 0, WINDOW_WIDTH, 75);
        panel.add(headerPanel);

        JButton backButton = new RoundedButton("Back");
        backButton.setBounds(15, 22, 75, 30);
        styleBackButton(backButton);
        headerPanel.add(backButton);

        backButton.addActionListener(e -> {
            dispose();
            if (previousDashboard != null) {
                previousDashboard.setVisible(true);
            } else {
                new Dashboard();
            }
        });

        JLabel mainHeading = new JLabel("INVENTORY MANAGEMENT SYSTEM");
        mainHeading.setBounds(120, 10, 650, 35);
        mainHeading.setFont(new Font("Arial", Font.BOLD, 25));
        mainHeading.setForeground(WHITE);
        mainHeading.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(mainHeading);

        JLabel welcome = new JLabel("Welcome Admin");
        welcome.setBounds(250, 43, 400, 25);
        welcome.setFont(new Font("Arial", Font.PLAIN, 15));
        welcome.setForeground(new Color(225, 230, 240));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(welcome);

        logoutButton = new RoundedButton("Logout");
        logoutButton.setBounds(800, 22, 75, 30);
        styleLogoutButton(logoutButton);
        headerPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                if (previousDashboard != null) {
                    previousDashboard.dispose();
                }
                dispose();
                new LoginForm();
            }
        });

        JLabel title = new JLabel("Product Management");
        title.setBounds(250, 85, 400, 40);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(DARK_BLUE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title);

        JLabel searchHeading = new JLabel("Search for product");
        searchHeading.setBounds(CONTENT_LEFT, 130, 220, 25);
        searchHeading.setFont(new Font("Arial", Font.BOLD, 15));
        searchHeading.setForeground(DARK_BLUE);
        panel.add(searchHeading);

        searchField = new JTextField();
        searchField.setBounds(CONTENT_LEFT, 158, 275, 35);
        styleTextField(searchField);
        panel.add(searchField);

        searchButton = new RoundedButton("SEARCH");
        searchButton.setBounds(360, 158, 115, 35);
        styleButton(searchButton);
        panel.add(searchButton);

        showAllButton = new RoundedButton("SHOW ALL");
        showAllButton.setBounds(490, 158, 115, 35);
        styleShowAllButton(showAllButton);
        panel.add(showAllButton);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{
                "ID",
                "Product Name",
                "Category",
                "Quantity",
                "Price"
        });

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(190, 200, 215));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(210, 225, 245));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(DARK_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setReorderingAllowed(false);

        TableColumn idColumn = table.getColumnModel().getColumn(0);
        TableColumn nameColumn = table.getColumnModel().getColumn(1);
        TableColumn categoryColumn = table.getColumnModel().getColumn(2);
        TableColumn quantityColumn = table.getColumnModel().getColumn(3);
        TableColumn priceColumn = table.getColumnModel().getColumn(4);

        idColumn.setPreferredWidth(70);
        nameColumn.setPreferredWidth(200);
        categoryColumn.setPreferredWidth(190);
        quantityColumn.setPreferredWidth(140);
        priceColumn.setPreferredWidth(160);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        idColumn.setCellRenderer(centerRenderer);
        quantityColumn.setCellRenderer(centerRenderer);
        priceColumn.setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(CONTENT_LEFT, 210, CONTENT_WIDTH, 220);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 195, 215)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll);

        RoundedSectionPanel managePanel = new RoundedSectionPanel();
        managePanel.setBounds(CONTENT_LEFT, 445, CONTENT_WIDTH, 175);
        panel.add(managePanel);

        JLabel manageHeading = new JLabel("Add or Delete Product");
        manageHeading.setBounds(180, 10, 400, 30);
        manageHeading.setFont(new Font("Arial", Font.BOLD, 20));
        manageHeading.setForeground(DARK_BLUE);
        manageHeading.setHorizontalAlignment(SwingConstants.CENTER);
        managePanel.add(manageHeading);

        int fieldWidth = 170;
        int fieldGap = 16;
        int firstX = 15;
        int secondX = firstX + fieldWidth + fieldGap;
        int thirdX = secondX + fieldWidth + fieldGap;
        int fourthX = thirdX + fieldWidth + fieldGap;
        int labelY = 48;
        int fieldY = 75;

        JLabel nameLabel = new JLabel("Product Name");
        nameLabel.setBounds(firstX, labelY, fieldWidth, 22);
        styleLabel(nameLabel);
        managePanel.add(nameLabel);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(secondX, labelY, fieldWidth, 22);
        styleLabel(categoryLabel);
        managePanel.add(categoryLabel);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setBounds(thirdX, labelY, fieldWidth, 22);
        styleLabel(quantityLabel);
        managePanel.add(quantityLabel);

        JLabel priceLabel = new JLabel("Price");
        priceLabel.setBounds(fourthX, labelY, fieldWidth, 22);
        styleLabel(priceLabel);
        managePanel.add(priceLabel);

        nameField = new JTextField();
        nameField.setBounds(firstX, fieldY, fieldWidth, 35);
        styleTextField(nameField);
        managePanel.add(nameField);

        categoryField = new JTextField();
        categoryField.setBounds(secondX, fieldY, fieldWidth, 35);
        styleTextField(categoryField);
        managePanel.add(categoryField);

        quantityField = new JTextField();
        quantityField.setBounds(thirdX, fieldY, fieldWidth, 35);
        styleTextField(quantityField);
        managePanel.add(quantityField);

        priceField = new JTextField();
        priceField.setBounds(fourthX, fieldY, fieldWidth, 35);
        styleTextField(priceField);
        managePanel.add(priceField);

        addButton = new RoundedButton("ADD");
        addButton.setBounds(250, 122, 120, 38);
        styleButton(addButton);
        managePanel.add(addButton);

        deleteButton = new RoundedButton("DELETE");
        deleteButton.setBounds(390, 122, 120, 38);
        styleShowAllButton(deleteButton);
        managePanel.add(deleteButton);

        loadProducts();

        addButton.addActionListener(e -> addProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        searchButton.addActionListener(e -> searchProduct());

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            selectedID = 0;
            loadProducts();
        });

        searchField.addActionListener(e -> searchProduct());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });

        setVisible(true);
    }

    private void loadProducts() {

        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Unable to connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Products");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Category"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                });
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load products.\n\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {

        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || category.isEmpty()
                || quantityText.isEmpty() || priceText.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all product fields.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity < 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity cannot be negative.",
                        "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (price < 0) {
                JOptionPane.showMessageDialog(this,
                        "Price cannot be negative.",
                        "Invalid Price", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Connection con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Unable to connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO Products (ProductName, Category, Quantity, Price) VALUES (?, ?, ?, ?)"
            );

            pst.setString(1, name);
            pst.setString(2, category);
            pst.setInt(3, quantity);
            pst.setDouble(4, price);

            pst.executeUpdate();

            pst.close();
            con.close();

            JOptionPane.showMessageDialog(this,
                    "Product Added Successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            loadProducts();
            clearFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Quantity must be a whole number\nand Price must be a valid number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding product.\n\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {

        if (selectedID == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product from the table first.",
                    "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete\nthe selected product?",
                "Delete Product", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Unable to connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "DELETE FROM Products WHERE ProductID = ?"
            );

            pst.setInt(1, selectedID);

            int rows = pst.executeUpdate();

            pst.close();
            con.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Product Deleted Successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                selectedID = 0;
                loadProducts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Product could not be deleted.",
                        "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting product.\n\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectRow() {

        int row = table.getSelectedRow();

        if (row >= 0) {
            selectedID = Integer.parseInt(table.getValueAt(row, 0).toString());
            nameField.setText(table.getValueAt(row, 1).toString());
            categoryField.setText(table.getValueAt(row, 2).toString());
            quantityField.setText(table.getValueAt(row, 3).toString());
            priceField.setText(table.getValueAt(row, 4).toString());
        }
    }

    private void searchProduct() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            loadProducts();
            return;
        }

        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Unable to connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM Products WHERE ProductName LIKE ? OR Category LIKE ?"
            );

            pst.setString(1, "%" + search + "%");
            pst.setString(2, "%" + search + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Category"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                });
            }

            rs.close();
            pst.close();
            con.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No product found.",
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error searching for product.\n\n" + e.getMessage(),
                    "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
        table.clearSelection();
        selectedID = 0;
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(DARK_BLUE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 215)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(BUTTON_BLUE);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleShowAllButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(SHOW_ALL_BLUE);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBackground(new Color(200, 70, 70));
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleBackButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 95, 120));
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static class RoundedSectionPanel extends JPanel {

        public RoundedSectionPanel() {
            setOpaque(false);
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(205, 212, 222));
            g2.fill(new RoundRectangle2D.Double(
                    4, 5, getWidth() - 8, getHeight() - 8, 18, 18
            ));

            g2.setColor(new Color(225, 233, 243));
            g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth() - 5, getHeight() - 5, 18, 18
            ));

            g2.setColor(new Color(195, 207, 222));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Double(
                    0.5, 0.5, getWidth() - 6, getHeight() - 6, 18, 18
            ));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(), 18, 18
            ));

            g2.setFont(getFont());
            g2.setColor(getForeground());

            FontMetrics fm = g2.getFontMetrics();

            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(getText(), x, y);

            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}