package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.DBConnection;
import database.Session;

public class Dashboard extends JFrame {

    private final Color DARK_BLUE = new Color(31, 52, 80);
    private final Color BLUE = new Color(45, 135, 195);
    private final Color BACKGROUND = new Color(244, 247, 250);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(45, 55, 70);
    private final Color RED = new Color(190, 65, 65);

    private final Color PRODUCTS_COLOR = new Color(225, 240, 250);
    private final Color STOCK_COLOR = new Color(222, 244, 237);
    private final Color CATEGORY_COLOR = new Color(237, 230, 249);
    private final Color VALUE_COLOR = new Color(252, 241, 218);
    private final Color REPORTS_COLOR = new Color(224, 238, 247);
    private final Color USERS_COLOR = new Color(245, 230, 240);
    private final Color SALES_COLOR = new Color(225, 243, 237);
    private final Color SALES_ICON_COLOR = new Color(35, 150, 105);

    private JPanel mainPanel;
    private JPanel productTablePanel;

    private JLabel totalProductsLabel;
    private JLabel totalQuantityLabel;
    private JLabel totalCategoriesLabel;
    private JLabel totalValueLabel;

    private List<ProductData> products = new ArrayList<>();

    private javax.swing.Timer refreshTimer;

    public Dashboard() {

        setTitle("Inventory Management System - Dashboard");
        setSize(1350, 950);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createDashboard();
        refreshDashboard();

        refreshTimer = new javax.swing.Timer(3000, e -> refreshDashboard());
        refreshTimer.start();

        setVisible(true);
    }

    private void createDashboard() {

        mainPanel = new JPanel(null);
        mainPanel.setBackground(BACKGROUND);
        add(mainPanel);

        boolean isAdmin = Session.isAdmin();

        JPanel header = new JPanel(null);
        header.setBackground(DARK_BLUE);
        header.setBounds(0, 0, 1350, 95);
        mainPanel.add(header);

        JButton backButton = new RoundedButton("Back");
        backButton.setBounds(20, 30, 80, 35);
        styleBackButton(backButton);
        header.add(backButton);
        backButton.addActionListener(e -> goBackToLogin());

        JLabel title = new JLabel("INVENTORY SYSTEM MANAGEMENT");
        title.setBounds(300, 10, 750, 35);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title);

        String welcomeText = "Welcome " + Session.getFullName() + " (" + Session.getRole() + ")";

        JLabel welcome = new JLabel(welcomeText);
        welcome.setBounds(300, 50, 750, 25);
        welcome.setFont(new Font("Arial", Font.PLAIN, 15));
        welcome.setForeground(new Color(190, 210, 225));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(welcome);

        JButton logoutButton = new RoundedButton("Logout");
        logoutButton.setBounds(1230, 30, 100, 34);
        styleLogoutButton(logoutButton);
        header.add(logoutButton);
        logoutButton.addActionListener(e -> logout());

        JLabel dashboardLabel = new JLabel("Dashboard Overview");
        dashboardLabel.setBounds(35, 110, 400, 30);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 22));
        dashboardLabel.setForeground(TEXT);
        mainPanel.add(dashboardLabel);

        int smallCardWidth = 320;
        int smallCardHeight = 105;
        int bigCardWidth = 320;
        int bigCardHeight = 230;

        int gap = 20;

        int col1 = 35;
        int col2 = col1 + smallCardWidth + gap;
        int col3 = col2 + smallCardWidth + gap;
        int col4 = col3 + smallCardWidth + gap;

        int row1 = 150;
        int row2 = row1 + smallCardHeight + gap;
        int row3 = row2 + smallCardHeight + gap;

        JPanel productsCard = createDashboardCard(col1, row1, smallCardWidth, smallCardHeight, PRODUCTS_COLOR);
        mainPanel.add(productsCard);

        DashboardIcon productsIcon = new DashboardIcon(DashboardIcon.PRODUCTS, BLUE);
        productsIcon.setBounds(25, 25, 60, 55);
        productsCard.add(productsIcon);

        JLabel productsTitle = createCardTitle("TOTAL PRODUCTS");
        productsTitle.setBounds(105, 25, 200, 22);
        productsCard.add(productsTitle);

        totalProductsLabel = createCardValue("0");
        totalProductsLabel.setBounds(105, 53, 200, 35);
        productsCard.add(totalProductsLabel);

        if (isAdmin) {
            makeCardClickable(productsCard, () -> openProductForm());
        } else {
            makeCardRestricted(productsCard);
        }

        JPanel stockCard = createDashboardCard(col2, row1, smallCardWidth, smallCardHeight, STOCK_COLOR);
        mainPanel.add(stockCard);

        DashboardIcon stockIcon = new DashboardIcon(DashboardIcon.STOCK, new Color(35, 155, 95));
        stockIcon.setBounds(25, 25, 60, 55);
        stockCard.add(stockIcon);

        JLabel stockTitle = createCardTitle("TOTAL STOCK");
        stockTitle.setBounds(105, 25, 200, 22);
        stockCard.add(stockTitle);

        totalQuantityLabel = createCardValue("0");
        totalQuantityLabel.setForeground(new Color(35, 155, 95));
        totalQuantityLabel.setBounds(105, 53, 200, 35);
        stockCard.add(totalQuantityLabel);

        if (isAdmin) {
            makeCardClickable(stockCard, () -> openStockForm());
        } else {
            makeCardRestricted(stockCard);
        }

        JPanel categoryCard = createDashboardCard(col1, row2, smallCardWidth, smallCardHeight, CATEGORY_COLOR);
        mainPanel.add(categoryCard);

        DashboardIcon categoryIcon = new DashboardIcon(DashboardIcon.CATEGORY, new Color(125, 80, 175));
        categoryIcon.setBounds(25, 25, 60, 55);
        categoryCard.add(categoryIcon);

        JLabel categoryTitle = createCardTitle("CATEGORIES");
        categoryTitle.setBounds(105, 25, 200, 22);
        categoryCard.add(categoryTitle);

        totalCategoriesLabel = createCardValue("0");
        totalCategoriesLabel.setForeground(new Color(125, 80, 175));
        totalCategoriesLabel.setBounds(105, 53, 200, 35);
        categoryCard.add(totalCategoriesLabel);

        if (isAdmin) {
            makeCardClickable(categoryCard, () -> { });
        } else {
            makeCardRestricted(categoryCard);
        }

        JPanel valueCard = createDashboardCard(col2, row2, smallCardWidth, smallCardHeight, VALUE_COLOR);
        mainPanel.add(valueCard);

        DashboardIcon valueIcon = new DashboardIcon(DashboardIcon.VALUE, new Color(205, 145, 30));
        valueIcon.setBounds(25, 25, 60, 55);
        valueCard.add(valueIcon);

        JLabel valueTitle = createCardTitle("INVENTORY VALUE");
        valueTitle.setBounds(105, 25, 200, 22);
        valueCard.add(valueTitle);

        totalValueLabel = createCardValue("R0.00");
        totalValueLabel.setForeground(new Color(190, 130, 20));
        totalValueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalValueLabel.setBounds(105, 53, 200, 35);
        valueCard.add(totalValueLabel);

        if (isAdmin) {
            makeCardClickable(valueCard, () -> { });
        } else {
            makeCardRestricted(valueCard);
        }

        JPanel reportsCard = createDashboardCard(col3, row1, bigCardWidth, bigCardHeight, REPORTS_COLOR);
        mainPanel.add(reportsCard);

        DashboardIcon reportsIcon = new DashboardIcon(DashboardIcon.REPORTS, new Color(30, 120, 175));
        reportsIcon.setBounds(25, 75, 90, 80);
        reportsCard.add(reportsIcon);

        JLabel reportsTitle = new JLabel("REPORTS");
        reportsTitle.setBounds(125, 50, 180, 35);
        reportsTitle.setFont(new Font("Arial", Font.BOLD, 22));
        reportsTitle.setForeground(DARK_BLUE);
        reportsCard.add(reportsTitle);

        JLabel reportsDescription = new JLabel("<html>View inventory reports<br>and transaction information</html>");
        reportsDescription.setBounds(125, 90, 180, 50);
        reportsDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        reportsDescription.setForeground(new Color(95, 105, 115));
        reportsCard.add(reportsDescription);

        JLabel clickReports = new JLabel("Open Reports");
        clickReports.setBounds(125, 160, 150, 25);
        clickReports.setFont(new Font("Arial", Font.BOLD, 13));
        clickReports.setForeground(BLUE);
        reportsCard.add(clickReports);

        if (isAdmin) {
            makeCardClickable(reportsCard, () -> openReportsPage());
            addHoverEffect(reportsCard, REPORTS_COLOR, new Color(211, 230, 242));
        } else {
            makeCardRestricted(reportsCard);
        }

        JPanel salesCard = createDashboardCard(col4, row1, bigCardWidth, bigCardHeight, SALES_COLOR);
        mainPanel.add(salesCard);

        DashboardIcon salesIcon = new DashboardIcon(DashboardIcon.SALES, SALES_ICON_COLOR);
        salesIcon.setBounds(25, 75, 90, 80);
        salesCard.add(salesIcon);

        JLabel salesTitle = new JLabel("SALES");
        salesTitle.setBounds(125, 50, 180, 35);
        salesTitle.setFont(new Font("Arial", Font.BOLD, 22));
        salesTitle.setForeground(new Color(30, 95, 75));
        salesCard.add(salesTitle);

        JLabel salesDescription = new JLabel("<html>Manage stock<br>sales transactions</html>");
        salesDescription.setBounds(125, 90, 180, 50);
        salesDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        salesDescription.setForeground(new Color(95, 105, 115));
        salesCard.add(salesDescription);

        JLabel clickSales = new JLabel("Open Sales");
        clickSales.setBounds(125, 160, 150, 25);
        clickSales.setFont(new Font("Arial", Font.BOLD, 13));
        clickSales.setForeground(SALES_ICON_COLOR);
        salesCard.add(clickSales);

        makeCardClickable(salesCard, () -> openSalesPage());
        addHoverEffect(salesCard, SALES_COLOR, new Color(210, 237, 227));

        if (isAdmin) {

            JPanel usersCard = createDashboardCard(col2, row3, bigCardWidth * 2 + gap, smallCardHeight, USERS_COLOR);
            mainPanel.add(usersCard);

            DashboardIcon usersIcon = new DashboardIcon(DashboardIcon.USERS, new Color(155, 80, 145));
            usersIcon.setBounds(25, 25, 60, 55);
            usersCard.add(usersIcon);

            JLabel usersTitle = new JLabel("MANAGE USERS");
            usersTitle.setBounds(105, 20, 400, 25);
            usersTitle.setFont(new Font("Arial", Font.BOLD, 18));
            usersTitle.setForeground(new Color(100, 50, 95));
            usersCard.add(usersTitle);

            JLabel usersDescription = new JLabel("Approve, block or delete registered users");
            usersDescription.setBounds(105, 48, 500, 20);
            usersDescription.setFont(new Font("Arial", Font.PLAIN, 12));
            usersDescription.setForeground(new Color(95, 105, 115));
            usersCard.add(usersDescription);

            JLabel clickUsers = new JLabel("Manage Users");
            clickUsers.setBounds(105, 72, 150, 22);
            clickUsers.setFont(new Font("Arial", Font.BOLD, 13));
            clickUsers.setForeground(new Color(155, 80, 145));
            usersCard.add(clickUsers);

            makeCardClickable(usersCard, () -> openManageUsersPage());
            addHoverEffect(usersCard, USERS_COLOR, new Color(235, 210, 230));
        }

        productTablePanel = createWhitePanel(35, 545, 1290, 340);
        mainPanel.add(productTablePanel);

        JLabel tableTitle = new JLabel("Product Overview");
        tableTitle.setBounds(25, 15, 400, 30);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 22));
        tableTitle.setForeground(TEXT);
        productTablePanel.add(tableTitle);

        JLabel tableDescription = new JLabel("Current products and stock information");
        tableDescription.setBounds(25, 48, 500, 22);
        tableDescription.setFont(new Font("Arial", Font.PLAIN, 13));
        tableDescription.setForeground(new Color(120, 130, 140));
        productTablePanel.add(tableDescription);

        createProductTable();
    }

    private void makeCardClickable(JPanel card, Runnable action) {

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        };

        attachMouseListenerRecursive(card, listener);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void makeCardRestricted(JPanel card) {

        Color dimmed = dim(card.getBackground());
        card.setBackground(dimmed);
        card.setName("restricted");

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        Dashboard.this,
                        "Access Denied!\n\nYour account does not have permission to open this page.\nPlease contact the administrator.",
                        "Restricted",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        };

        attachMouseListenerRecursive(card, listener);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private Color dim(Color original) {
        int r = (int) (original.getRed() * 0.65);
        int g = (int) (original.getGreen() * 0.65);
        int b = (int) (original.getBlue() * 0.65);
        return new Color(r, g, b);
    }

    private void addHoverEffect(JPanel card, Color normalColor, Color hoverColor) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverColor);
                card.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(normalColor);
                card.repaint();
            }
        });
    }

    private void attachMouseListenerRecursive(Component component, MouseListener listener) {
        component.addMouseListener(listener);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                attachMouseListenerRecursive(child, listener);
            }
        }
    }

    private void openProductForm() {
        try {
            if (refreshTimer != null) refreshTimer.stop();
            new ProductForm(this);
            setVisible(false);
        } catch (Exception e) {
            if (refreshTimer != null) refreshTimer.start();
            JOptionPane.showMessageDialog(this,
                    "Unable to open Product Form.\n\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openStockForm() {
        try {
            if (refreshTimer != null) refreshTimer.stop();
            new StockForm(this);
            setVisible(false);
        } catch (Exception e) {
            if (refreshTimer != null) refreshTimer.start();
            JOptionPane.showMessageDialog(this,
                    "Unable to open Stock Form.\n\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSalesPage() {
        if (refreshTimer != null) refreshTimer.stop();
        try {
            new SalesForm(this);
            setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to open Sales page.\n\n" + e.getMessage(),
                    "Sales Error", JOptionPane.ERROR_MESSAGE);
            refreshTimer.start();
        }
    }

    private void openManageUsersPage() {
        if (refreshTimer != null) refreshTimer.stop();
        try {
            new ManageUsersForm();
            setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to open Manage Users page.\n\nPlease make sure that ManageUsersForm.java exists in the view package.\n\nError: " + e.getMessage(),
                    "Manage Users Error", JOptionPane.ERROR_MESSAGE);
            refreshTimer.start();
        }
    }

    private void openReportsPage() {
        if (refreshTimer != null) refreshTimer.stop();
        try {
            ReportForm reportForm = new ReportForm(this);
            reportForm.setVisible(true);
            setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to open Report Form.\n\nPlease make sure that ReportForm.java exists in the view package.\n\nError: " + e.getMessage(),
                    "Reports Error", JOptionPane.ERROR_MESSAGE);
            refreshTimer.start();
        }
    }

    private void goBackToLogin() {
        if (refreshTimer != null) refreshTimer.stop();
        Session.clear();
        dispose();
        new LoginForm();
    }

    private JPanel createDashboardCard(int x, int y, int width, int height, Color backgroundColor) {
        JPanel card = new RoundedPanel(18);
        card.setLayout(null);
        card.setBackground(backgroundColor);
        card.setBounds(x, y, width, height);
        return card;
    }

    private void createProductTable() {

        String[] columns = {"Product", "Category", "Quantity", "Price", "Stock Value"};

        JTable table = new JTable(new Object[0][5], columns);
        configureTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25, 80, 1240, 240);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(215, 220, 225)));

        productTablePanel.add(scrollPane);
        productTablePanel.putClientProperty("table", table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        if (modelRow >= 0 && modelRow < products.size()) {
                            ProductData product = products.get(modelRow);
                            showProductDetails(product);
                        }
                    }
                }
            }
        });
    }

    private void configureTable(JTable table) {

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setGridColor(new Color(220, 225, 230));
        table.setShowGrid(true);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(false);

        setColumnWidths(table);
    }

    private void setColumnWidths(JTable table) {
        if (table.getColumnModel().getColumnCount() < 5) return;

        table.getColumnModel().getColumn(0).setPreferredWidth(340);
        table.getColumnModel().getColumn(1).setPreferredWidth(280);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(220);
    }

    private void refreshDashboard() {
        loadProductsFromDatabase();
        updateStatistics();
        updateProductTable();
    }

    private void loadProductsFromDatabase() {

        List<ProductData> newProducts = new ArrayList<>();

        String sql = "SELECT ProductID, ProductName, Category, Quantity, Price FROM Products ORDER BY ProductID";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            if (connection == null) {
                System.out.println("Database connection is null.");
                return;
            }

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet result = statement.executeQuery()
            ) {
                while (result.next()) {
                    newProducts.add(new ProductData(
                            result.getInt("ProductID"),
                            result.getString("ProductName"),
                            result.getString("Category"),
                            result.getInt("Quantity"),
                            result.getDouble("Price")
                    ));
                }
            }

            products = newProducts;

        } catch (Exception e) {
            System.out.println("Dashboard database error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (Exception ignored) {}
            }
        }
    }

    private void updateStatistics() {

        int totalProducts = products.size();
        int totalQuantity = 0;
        double totalValue = 0;
        Set<String> categories = new HashSet<>();

        for (ProductData product : products) {
            totalQuantity += product.quantity;
            totalValue += product.getValue();

            if (product.category != null && !product.category.trim().isEmpty()) {
                categories.add(product.category.trim());
            }
        }

        if (totalProductsLabel != null)
            totalProductsLabel.setText(String.valueOf(totalProducts));
        if (totalQuantityLabel != null)
            totalQuantityLabel.setText(String.valueOf(totalQuantity));
        if (totalCategoriesLabel != null)
            totalCategoriesLabel.setText(String.valueOf(categories.size()));
        if (totalValueLabel != null)
            totalValueLabel.setText(String.format("R%,.2f", totalValue));
    }

    private void updateProductTable() {

        if (productTablePanel == null) return;

        JTable table = (JTable) productTablePanel.getClientProperty("table");
        if (table == null) return;

        String[] columns = {"Product", "Category", "Quantity", "Price", "Stock Value"};

        Object[][] data = new Object[products.size()][5];

        for (int i = 0; i < products.size(); i++) {
            ProductData product = products.get(i);
            data[i][0] = product.name;
            data[i][1] = product.category;
            data[i][2] = product.quantity;
            data[i][3] = String.format("R%.2f", product.price);
            data[i][4] = String.format("R%.2f", product.getValue());
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        configureTable(table);
    }

    private void showProductDetails(ProductData product) {

        if (product == null) return;

        String details = "Product Details\n\n"
                + "Product ID: " + product.id + "\n"
                + "Product Name: " + safe(product.name) + "\n"
                + "Category: " + safe(product.category) + "\n"
                + "Quantity: " + product.quantity + "\n"
                + "Price: " + String.format("R%.2f", product.price) + "\n"
                + "Stock Value: " + String.format("R%.2f", product.getValue());

        JOptionPane.showMessageDialog(this, details, "Product Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (refreshTimer != null) refreshTimer.stop();
            Session.clear();
            dispose();
            new LoginForm();
        }
    }

    private JLabel createCardTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(100, 110, 120));
        return label;
    }

    private JLabel createCardValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(BLUE);
        return label;
    }

    private JPanel createWhitePanel(int x, int y, int width, int height) {
        JPanel panel = new RoundedPanel(18);
        panel.setLayout(null);
        panel.setBackground(WHITE);
        panel.setBounds(x, y, width, height);
        return panel;
    }

    private static class ProductData {

        int id;
        String name;
        String category;
        int quantity;
        double price;

        ProductData(int id, String name, String category, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.quantity = quantity;
            this.price = price;
        }

        double getValue() {
            return quantity * price;
        }
    }

    private static class DashboardIcon extends JPanel {

        static final int PRODUCTS = 1;
        static final int STOCK = 2;
        static final int CATEGORY = 3;
        static final int VALUE = 4;
        static final int REPORTS = 5;
        static final int SALES = 6;
        static final int USERS = 7;

        private final int type;
        private final Color iconColor;

        DashboardIcon(int type, Color iconColor) {
            this.type = type;
            this.iconColor = iconColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(iconColor);

            if (type == PRODUCTS) {
                g2.fillRoundRect(6, 7, 16, 14, 3, 3);
                g2.fillRoundRect(25, 7, 16, 14, 3, 3);
                g2.fillRoundRect(15, 24, 18, 14, 3, 3);
            }
            else if (type == STOCK) {
                Polygon top = new Polygon();
                top.addPoint(24, 5); top.addPoint(41, 14);
                top.addPoint(24, 23); top.addPoint(7, 14);
                g2.fillPolygon(top);
                g2.fillRect(8, 14, 15, 20);
                g2.fillRect(25, 14, 15, 20);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(24, 6, 24, 34);
            }
            else if (type == CATEGORY) {
                g2.fillRoundRect(7, 7, 14, 14, 3, 3);
                g2.fillRoundRect(25, 7, 14, 14, 3, 3);
                g2.fillRoundRect(7, 25, 14, 14, 3, 3);
                g2.fillRoundRect(25, 25, 14, 14, 3, 3);
            }
            else if (type == VALUE) {
                g2.fillOval(6, 5, 34, 34);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 23));
                g2.drawString("R", 16, 30);
            }
            else if (type == REPORTS) {
                g2.fillRoundRect(8, 5, 38, 50, 5, 5);
                g2.setColor(new Color(224, 238, 247));
                Polygon fold = new Polygon();
                fold.addPoint(33, 5); fold.addPoint(46, 18);
                fold.addPoint(33, 18);
                g2.fillPolygon(fold);
                g2.setColor(new Color(30, 120, 175));
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(15, 26, 39, 26);
                g2.drawLine(15, 34, 39, 34);
                g2.drawLine(15, 42, 39, 42);
                g2.fillRect(52, 41, 5, 14);
                g2.fillRect(60, 32, 5, 23);
                g2.fillRect(68, 22, 5, 33);
            }
            else if (type == SALES) {
                g2.fillRoundRect(8, 5, 40, 50, 5, 5);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(17, 16, 39, 16);
                g2.setColor(iconColor);
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.drawString("R", 20, 39);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(15, 46, 41, 46);
                g2.fillRect(54, 39, 5, 16);
                g2.fillRect(62, 31, 5, 24);
                g2.fillRect(70, 23, 5, 32);
                Polygon arrow = new Polygon();
                arrow.addPoint(79, 18); arrow.addPoint(87, 27);
                arrow.addPoint(79, 36);
                g2.fillPolygon(arrow);
            }
            else if (type == USERS) {
                g2.fillOval(22, 10, 18, 18);
                g2.fillRoundRect(20, 30, 22, 20, 5, 5);
                g2.fillOval(50, 15, 14, 14);
                g2.fillRoundRect(48, 32, 18, 16, 5, 5);
            }

            g2.dispose();
        }
    }

    private static class RoundedPanel extends JPanel {

        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {

        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setMargin(new Insets(0, 5, 0, 5));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color buttonColor = getBackground();
            if (getModel().isPressed()) buttonColor = buttonColor.darker();
            else if (getModel().isRollover()) buttonColor = buttonColor.brighter();
            g2.setColor(buttonColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void styleBackButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 95, 120));
        button.setForeground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(RED);
        button.setForeground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}