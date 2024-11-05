package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Login class represents the login interface for the Bank Management System (ATM).
 * It handles both user and admin authentication with database integration.
 */
public class Login extends JFrame implements ActionListener {

    // UI Components
    private JLabel welcomeLabel, cardNumberLabel, pinLabel, userTypeLabel;
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JButton signInButton, clearButton, signUpButton, registerAdminButton, closeButton;
    private JComboBox<String> userTypeCombo;

    /**
     * Constructor to initialize the Login form components and layout.
     */
    public Login() {
        super("Bank Management System - ATM");

        // Frame setup
        setSize(850, 480);
        setLocation(450, 200);
        setUndecorated(true);
        setLayout(null);

        // Initialize UI components
        createUIComponents();

        // Set frame visible
        setVisible(true);
    }

    /**
     * Creates and initializes all UI components
     */
    private void createUIComponents() {
        // ATM Icon at the top
        ImageIcon atmIcon = new ImageIcon(ClassLoader.getSystemResource("icon/bank.png"));
        Image atmImage = atmIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel atmIconLabel = new JLabel(new ImageIcon(atmImage));
        atmIconLabel.setBounds(350, 10, 100, 100);
        add(atmIconLabel);

        // Close button at top right
        closeButton = new JButton("×");
        closeButton.setBounds(800, 10, 40, 40);
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.WHITE);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));
        add(closeButton);

        // Welcome label
        welcomeLabel = new JLabel("WELCOME TO ATM");
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setFont(new Font("AvantGarde", Font.BOLD, 38));
        welcomeLabel.setBounds(230, 125, 450, 40);
        add(welcomeLabel);

        // User Type Selection
        userTypeLabel = new JLabel("Login As:");
        userTypeLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        userTypeLabel.setForeground(Color.BLACK);
        userTypeLabel.setBounds(150, 160, 375, 30);
        add(userTypeLabel);

        userTypeCombo = new JComboBox<>(new String[]{"Customer", "Admin"});
        userTypeCombo.setBounds(325, 160, 230, 30);
        userTypeCombo.setFont(new Font("Arial", Font.BOLD, 14));
        userTypeCombo.addActionListener(this);
        userTypeCombo.setBackground(Color.WHITE);
        add(userTypeCombo);

        // Card Number/Username label and field
        cardNumberLabel = new JLabel("Card No:");
        cardNumberLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        cardNumberLabel.setForeground(Color.BLACK);
        cardNumberLabel.setBounds(150, 220, 375, 30);
        add(cardNumberLabel);

        cardNumberField = new JTextField(15);
        cardNumberField.setBounds(325, 220, 230, 30);
        cardNumberField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardNumberField);

        // PIN/Password label and field
        pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        pinLabel.setForeground(Color.BLACK);
        pinLabel.setBounds(150, 280, 375, 30);
        add(pinLabel);

        pinField = new JPasswordField(15);
        pinField.setBounds(325, 280, 230, 30);
        pinField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinField);

        // Button Panel setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(300, 330, 230, 90);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 10));

        // Sign In button
        signInButton = createStyledButton("SIGN IN");
        buttonPanel.add(signInButton);

        // Clear button
        clearButton = createStyledButton("CLEAR");
        buttonPanel.add(clearButton);

        // Sign Up button
        signUpButton = createStyledButton("SIGN UP");
        buttonPanel.add(signUpButton);

        // Register Admin button
        registerAdminButton = createStyledButton("REGISTER");
        registerAdminButton.setVisible(false);
        buttonPanel.add(registerAdminButton);

        add(buttonPanel);

        // Decorative card image
        ImageIcon cardIcon = new ImageIcon(ClassLoader.getSystemResource("icon/card.png"));
        Image cardImage = cardIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel cardIconLabel = new JLabel(new ImageIcon(cardImage));
        cardIconLabel.setBounds(630, 350, 100, 100);
        add(cardIconLabel);

        // Background image
        ImageIcon backgroundIcon = new ImageIcon(ClassLoader.getSystemResource("icon/backbg.png"));
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(850, 480, Image.SCALE_DEFAULT);
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, 850, 480);
        add(backgroundLabel);

        // Update UI based on initial selection
        updateUIForUserType();
    }

    /**
     * Creates a styled button with consistent appearance
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Updates UI elements based on selected user type
     */
    private void updateUIForUserType() {
        String selectedType = (String) userTypeCombo.getSelectedItem();
        if ("Admin".equals(selectedType)) {
            cardNumberLabel.setText("Username:");
            pinLabel.setText("Password:");
            signUpButton.setVisible(false);
            registerAdminButton.setVisible(true);
        } else {
            cardNumberLabel.setText("Card No:");
            pinLabel.setText("PIN:");
            signUpButton.setVisible(true);
            registerAdminButton.setVisible(false);
        }
    }

    /**
     * Handles all button click events
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == signInButton) {
                handleSignIn();
            } else if (ae.getSource() == clearButton) {
                handleClear();
            } else if (ae.getSource() == signUpButton) {
                handleSignUp();
            } else if (ae.getSource() == registerAdminButton) {
                handleAdminSignUp();
            } else if (ae.getSource() == userTypeCombo) {
                updateUIForUserType();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the sign-in process
     */
    private void handleSignIn() throws Exception {
        String userType = (String) userTypeCombo.getSelectedItem();
        String username = cardNumberField.getText().trim();
        String password = new String(pinField.getPassword());

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter " + (userType.equals("Admin") ? "username and password" : "card number and PIN"));
            return;
        }

        if ("Admin".equals(userType)) {
            handleAdminLogin(username, password);
        } else {
            handleCustomerLogin(username, password);
        }
    }

    /**
     * Handles admin authentication
     */
    private void handleAdminLogin(String username, String password) {
        try {
            Con c = new Con();
            String query = "SELECT * FROM admin_users WHERE username = ? AND password = ?";
            PreparedStatement pst = c.connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                setVisible(false);
                new AdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Admin Credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error during admin login: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles customer authentication
     */
    private void handleCustomerLogin(String cardNumber, String pin) throws Exception {
        Con c = new Con();
        String query = "SELECT * FROM login WHERE card_number = ? AND pin = ?";
        PreparedStatement pst = c.connection.prepareStatement(query);
        pst.setString(1, cardNumber);
        pst.setString(2, pin);
        ResultSet resultSet = pst.executeQuery();

        if (resultSet.next()) {
            setVisible(false);
            new main_class(pin);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Incorrect Card Number or PIN",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears input fields
     */
    private void handleClear() {
        cardNumberField.setText("");
        pinField.setText("");
        cardNumberField.requestFocus();
    }

    /**
     * Opens customer signup form
     */
    private void handleSignUp() {
        setVisible(false);
        new Signup();
    }

    /**
     * Opens admin registration form
     */
    private void handleAdminSignUp() {
        setVisible(false);
        new AdminSignup();
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Login());
    }
}