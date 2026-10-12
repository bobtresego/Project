package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import controller.RegisterController;
import model.User;

public class RegisterForm extends JFrame {

    JTextField firstNameField;
    JTextField lastNameField;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerButton;
    JButton backToLoginButton;

    public RegisterForm() {

        setTitle("Inventory Management System - Register");
        setSize(520, 600);
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

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        registerPanel.setBounds(60, 135, 400, 420);
        backgroundPanel.add(registerPanel);

        JLabel registerTitle = new JLabel("Create Account");
        registerTitle.setBounds(30, 15, 340, 32);
        registerTitle.setFont(new Font("Arial", Font.BOLD, 21));
        registerTitle.setForeground(new Color(31, 52, 80));
        registerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        registerPanel.add(registerTitle);

        JLabel fNameLabel = new JLabel("First Name");
        fNameLabel.setBounds(40, 55, 150, 25);
        fNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        fNameLabel.setForeground(new Color(55, 65, 80));
        registerPanel.add(fNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(40, 80, 320, 36);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        registerPanel.add(firstNameField);

        JLabel lNameLabel = new JLabel("Last Name");
        lNameLabel.setBounds(40, 120, 150, 25);
        lNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        lNameLabel.setForeground(new Color(55, 65, 80));
        registerPanel.add(lNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(40, 145, 320, 36);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        registerPanel.add(lastNameField);

        JLabel userLabel = new JLabel("Username (Gmail)");
        userLabel.setBounds(40, 185, 150, 25);
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userLabel.setForeground(new Color(55, 65, 80));
        registerPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(40, 210, 320, 36);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        registerPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(40, 250, 150, 25);
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setForeground(new Color(55, 65, 80));
        registerPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 275, 320, 36);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(185, 195, 210)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        registerPanel.add(passwordField);

        registerButton = new RoundedButton("REGISTER");
        registerButton.setBounds(120, 330, 160, 38);
        registerPanel.add(registerButton);

        registerButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();

            if (firstName.isEmpty() || lastName.isEmpty()
                    || username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields.",
                        "Registration Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            User newUser = new User(firstName, lastName, username, password);

            RegisterController controller = new RegisterController();
            boolean success = controller.register(newUser);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful!\n\n"
                                + "Your account is now pending Admin approval.\n"
                                + "Please wait for an Admin to approve your account before you can log in.",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                new LoginForm();
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed.\nPossible reasons:\n- Database connection error\n- Email/Username already exists",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        backToLoginButton = new JButton("Already have an account? Login here");
        backToLoginButton.setBounds(60, 380, 280, 25);
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backToLoginButton.setForeground(new Color(37, 132, 190));
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginButton.addActionListener(e -> {
            new LoginForm();
            dispose();
        });
        registerPanel.add(backToLoginButton);

        JLabel footer = new JLabel("Inventory Management System");
        footer.setBounds(60, 565, 400, 25);
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
            g2.fillRoundRect(
                    0, 0,
                    getWidth(), getHeight(),
                    20, 20
            );
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm());
    }
}