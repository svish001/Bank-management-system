package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SystemSettings extends JFrame implements ActionListener {

    private JTextField minBalanceField, transactionLimitField;
    private JCheckBox enableEmailNotifications, enableSMSNotifications;
    private JComboBox<String> themeSelector;
    private JButton saveButton, cancelButton;

    public SystemSettings() {
        setTitle("System Settings");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Minimum Balance:"), gbc);

        gbc.gridx = 1;
        minBalanceField = new JTextField(10);
        mainPanel.add(minBalanceField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Transaction Limit:"), gbc);

        gbc.gridx = 1;
        transactionLimitField = new JTextField(10);
        mainPanel.add(transactionLimitField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Theme:"), gbc);

        gbc.gridx = 1;
        themeSelector = new JComboBox<>(new String[]{"Light", "Dark", "System Default"});
        mainPanel.add(themeSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        enableEmailNotifications = new JCheckBox("Enable Email Notifications");
        mainPanel.add(enableEmailNotifications, gbc);

        gbc.gridy = 4;
        enableSMSNotifications = new JCheckBox("Enable SMS Notifications");
        mainPanel.add(enableSMSNotifications, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save Settings");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to frame
        add(new JLabel("System Settings", SwingConstants.CENTER), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadSettings();
        setVisible(true);
    }

    private void loadSettings() {
        try {
            Con c = new Con();
            String query = "SELECT * FROM system_settings LIMIT 1";
            ResultSet rs = c.statement.executeQuery(query);

            if (rs.next()) {
                minBalanceField.setText(rs.getString("min_balance"));
                transactionLimitField.setText(rs.getString("transaction_limit"));
                themeSelector.setSelectedItem(rs.getString("theme"));
                enableEmailNotifications.setSelected(rs.getBoolean("email_notifications"));
                enableSMSNotifications.setSelected(rs.getBoolean("sms_notifications"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSettings() {
        try {
            Con c = new Con();
            String query = "UPDATE system_settings SET " +
                    "min_balance = '" + minBalanceField.getText() + "', " +
                    "transaction_limit = '" + transactionLimitField.getText() + "', " +
                    "theme = '" + themeSelector.getSelectedItem() + "', " +
                    "email_notifications = " + enableEmailNotifications.isSelected() + ", " +
                    "sms_notifications = " + enableSMSNotifications.isSelected();

            c.statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Settings saved successfully!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving settings: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            saveSettings();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}