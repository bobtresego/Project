package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import controller.LoginController;
import database.Session;

public class LoginForm extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton goToRegisterButton;

    public LoginForm() {

        setTitle("Inventory Management System - Login");
        setSize(520, 470);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(new Color(232, 238, 245));
        add(backgroundPanel);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(31, 52, 80));
        headerPanel.setBounds(0, 0, 520, 115);
        backgroundPanel.add(headerPanel);

        JLabel title = new JLabel("INVENTORY MANAGEMENT");
        title.setBounds(30, 32, 460, 45);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(title);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        loginPanel.setBounds(60, 135, 400, 280);
        backgroundPanel.add(loginPanel);

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setBounds(30, 15, 340, 32);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 21));
        loginTitle.setForeground(new Color(31, 52, 80));
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(loginTitle);

        JLabel userLabel = new JLabel("Email");
        userLabel.setBounds(40, 58, 150, 25);
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userLabel.setForeground(new Color(55, 65, 80));
        loginPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(40, 83, 320, 36);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        loginPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(40, 126, 150, 25);
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(new Color(55, 65, 80));
        loginPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 151, 320, 36);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        loginPanel.add(passwordField);

        loginButton = new RoundedButton("LOGIN");
        loginButton.setBounds(120, 202, 160, 38);
        loginPanel.add(loginButton);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your email and password.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LoginController controller = new LoginController();
            int result = controller.login(username, password);

            if (result == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful\n\n"
                                + "Welcome " + Session.getFullName() + "\n"
                                + "Role: " + Session.getRole(),
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE
                );
                new Dashboard();
                dispose();
            }
            else if (result == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Your account is pending Admin approval.\nPlease wait until an Admin approves your account.",
                        "Account Pending",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            else if (result == -2) {
                JOptionPane.showMessageDialog(
                        this,
                        "Your account has been blocked by the Admin.\nPlease contact support.",
                        "Account Blocked",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "Wrong Email or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        passwordField.addActionListener(e -> loginButton.doClick());

        goToRegisterButton = new JButton("Don't have an account? Register here");
        goToRegisterButton.setBounds(60, 248, 280, 25);
        goToRegisterButton.setFont(new Font("Arial", Font.PLAIN, 12));
        goToRegisterButton.setForeground(new Color(37, 132, 190));
        goToRegisterButton.setContentAreaFilled(false);
        goToRegisterButton.setBorderPainted(false);
        goToRegisterButton.setFocusPainted(false);
        goToRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToRegisterButton.addActionListener(e -> {
            new RegisterForm();
            dispose();
        });
        loginPanel.add(goToRegisterButton);

        JLabel footer = new JLabel("Inventory Management System");
        footer.setBounds(60, 425, 400, 25);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 110, 120));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(footer);

        setVisible(true);
    }

    static class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(new Color(37, 132, 190));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}