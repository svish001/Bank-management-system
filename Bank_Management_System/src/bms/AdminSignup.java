package bms;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.Random;

public class AdminSignup extends JFrame implements ActionListener {

    private JTextField employeeNameField, usernameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> designationCombo;
    private JButton submitButton, cancelButton;
    private String adminId;

    public AdminSignup() {
        super("Admin Registration");

        // Generate random admin ID
        Random random = new Random();
        adminId = "ADM" + String.format("%04d", random.nextInt(10000));

        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        // Title
        JLabel title = new JLabel("Admin Registration Form");
        title.setFont(new Font("Raleway", Font.BOLD, 28));
        title.setBounds(190, 20, 400, 40);
        add(title);

        // Admin ID Display
        JLabel adminIdLabel = new JLabel("Admin ID:");
        adminIdLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        adminIdLabel.setBounds(100, 80, 200, 30);
        add(adminIdLabel);

        JLabel adminIdDisplay = new JLabel(adminId);
        adminIdDisplay.setFont(new Font("Raleway", Font.BOLD, 20));
        adminIdDisplay.setBounds(300, 80, 200, 30);
        add(adminIdDisplay);

        // Employee Name
        JLabel nameLabel = new JLabel("Employee Name:");
        nameLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        nameLabel.setBounds(100, 120, 200, 30);
        add(nameLabel);

        employeeNameField = new JTextField();
        employeeNameField.setFont(new Font("Arial", Font.BOLD, 14));
        employeeNameField.setBounds(300, 120, 300, 30);
        add(employeeNameField);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        userLabel.setBounds(100, 160, 200, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField.setBounds(300, 160, 300, 30);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        passLabel.setBounds(100, 200, 200, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField.setBounds(300, 200, 300, 30);
        add(passwordField);

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        confirmPassLabel.setBounds(100, 240, 200, 30);
        add(confirmPassLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPasswordField.setBounds(300, 240, 300, 30);
        add(confirmPasswordField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        emailLabel.setBounds(100, 280, 200, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.BOLD, 14));
        emailField.setBounds(300, 280, 300, 30);
        add(emailField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        phoneLabel.setBounds(100, 320, 200, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.BOLD, 14));
        phoneField.setBounds(300, 320, 300, 30);
        add(phoneField);

        // Designation
        JLabel designationLabel = new JLabel("Designation:");
        designationLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        designationLabel.setBounds(100, 360, 200, 30);
        add(designationLabel);

        String[] designations = {"Manager", "Assistant Manager", "Banking Officer", "System Administrator"};
        designationCombo = new JComboBox<>(designations);
        designationCombo.setFont(new Font("Arial", Font.BOLD, 14));
        designationCombo.setBounds(300, 360, 300, 30);
        add(designationCombo);

        // Submit Button
        submitButton = new JButton("SUBMIT");
        submitButton.setBackground(Color.WHITE);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBounds(300, 410, 100, 30);
        submitButton.addActionListener(this);
        add(submitButton);

        // Cancel Button
        cancelButton = new JButton("CANCEL");
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBounds(450, 410, 100, 30);
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Background color
        getContentPane().setBackground(new Color(222, 255, 228));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == submitButton) {
                String employeeName = employeeNameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String email = emailField.getText();
                String phone = phoneField.getText();
                String designation = (String) designationCombo.getSelectedItem();

                // Validation
                if (employeeName.isEmpty() || username.isEmpty() || password.isEmpty() ||
                        email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the required fields");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                    return;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address");
                    return;
                }

                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid 10-digit phone number");
                    return;
                }

                // Check if username already exists
                Con c = new Con();
                String checkQuery = "SELECT * FROM admin_users WHERE username = '" + username + "'";
                if (c.statement.executeQuery(checkQuery).next()) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.");
                    return;
                }

                // Insert into database
                String query = "INSERT INTO admin_users (admin_id, username, password, employee_name, " +
                        "email, phone, designation) VALUES ('" +
                        adminId + "', '" + username + "', '" + password + "', '" +
                        employeeName + "', '" + email + "', '" + phone + "', '" +
                        designation + "')";

                c.statement.executeUpdate(query);

                JOptionPane.showMessageDialog(null,
                        "Admin Registration Successful!\nYour Admin ID is: " + adminId);

                setVisible(false);
                new Login();

            } else if (ae.getSource() == cancelButton) {
                setVisible(false);
                new Login();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}