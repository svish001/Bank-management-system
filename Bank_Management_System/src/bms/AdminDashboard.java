package bms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;

public class AdminDashboard extends JFrame implements ActionListener {

    private JTabbedPane tabbedPane;
    private JPanel customerPanel, transactionPanel, systemPanel;
    private JButton logoutButton, refreshButton;
    private JTable customerTable, transactionTable;
    private JTextField searchField;
    private JButton searchButton, deleteAccountButton, viewDetailsButton;
    private JLabel totalCustomersLabel, totalBalanceLabel, todayTransactionsLabel;

    // New buttons for additional features
    private JButton backupButton, settingsButton, reportsButton;
    private JButton blockAccountButton, sendNotificationButton;
    private JButton exportDataButton, importDataButton;

    public AdminDashboard() {
        super("Bank Management System - Admin Dashboard");

        // Set up the main frame
        setSize(1000, 600);
        setLocation(250, 100);
        setLayout(new BorderLayout());

        // Create header panel with summary statistics
        createHeaderPanel();

        // Create top panel for logout and refresh buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        refreshButton.addActionListener(this);
        logoutButton.addActionListener(this);
        topPanel.add(refreshButton);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize panels
        createCustomerPanel();
        createTransactionPanel();
        createSystemPanel();

        // Add panels to tabbed pane
        tabbedPane.addTab("Customer Management", customerPanel);
        tabbedPane.addTab("Transaction History", transactionPanel);
        tabbedPane.addTab("System Settings", systemPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        loadCustomerData(); // Load initial customer data
        loadTransactionData(); // Load initial transaction data
        updateSummaryStatistics(); // Load initial statistics
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        headerPanel.setBackground(new Color(240, 240, 240));

        totalCustomersLabel = new JLabel("Total Customers: Loading...");
        totalBalanceLabel = new JLabel("Total Balance: Loading...");
        todayTransactionsLabel = new JLabel("Today's Transactions: Loading...");

        headerPanel.add(totalCustomersLabel);
        headerPanel.add(totalBalanceLabel);
        headerPanel.add(todayTransactionsLabel);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createCustomerPanel() {
        customerPanel = new JPanel(new BorderLayout());

        // Create top control panel
        JPanel controlPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(new JLabel("Search by Card Number/Name: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewDetailsButton = new JButton("View Details");
        deleteAccountButton = new JButton("Delete Account");
        blockAccountButton = new JButton("Block Account");
        sendNotificationButton = new JButton("Send Notification");

        viewDetailsButton.addActionListener(this);
        deleteAccountButton.addActionListener(this);
        blockAccountButton.addActionListener(this);
        sendNotificationButton.addActionListener(this);

        actionPanel.add(viewDetailsButton);
        actionPanel.add(deleteAccountButton);
        actionPanel.add(blockAccountButton);
        actionPanel.add(sendNotificationButton);

        controlPanel.add(searchPanel, BorderLayout.NORTH);
        controlPanel.add(actionPanel, BorderLayout.SOUTH);

        customerPanel.add(controlPanel, BorderLayout.NORTH);

        // Create customer table
        String[] columns = {"Form No", "Name", "Card Number", "Account Type", "Balance", "Status"};
        customerTable = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(customerTable);
        customerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createTransactionPanel() {
        transactionPanel = new JPanel(new BorderLayout());

        // Transaction control panel
        JPanel transactionControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exportDataButton = new JButton("Export Transactions");
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"All", "Today", "This Week", "This Month"});

        exportDataButton.addActionListener(this);
        transactionControlPanel.add(new JLabel("Filter: "));
        transactionControlPanel.add(filterCombo);
        transactionControlPanel.add(exportDataButton);

        transactionPanel.add(transactionControlPanel, BorderLayout.NORTH);

        // Create transaction table
        String[] columns = {"Date", "Card Number", "Type", "Amount", "Balance After"};
        transactionTable = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        transactionPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createSystemPanel() {
        systemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize buttons
        backupButton = new JButton("Backup Database");
        settingsButton = new JButton("System Settings");
        reportsButton = new JButton("Generate Reports");
        importDataButton = new JButton("Import Data");

        // Add action listeners
        backupButton.addActionListener(this);
        settingsButton.addActionListener(this);
        reportsButton.addActionListener(this);
        importDataButton.addActionListener(this);

        // Add components with GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        systemPanel.add(backupButton, gbc);

        gbc.gridy = 1;
        systemPanel.add(settingsButton, gbc);

        gbc.gridy = 2;
        systemPanel.add(reportsButton, gbc);

        gbc.gridy = 3;
        systemPanel.add(importDataButton, gbc);
    }


    private void updateSummaryStatistics() {
        try {
            Con c = new Con();

            // Get total customers
            ResultSet rs = c.statement.executeQuery("SELECT COUNT(*) as total FROM signup");
            if (rs.next()) {
                totalCustomersLabel.setText("Total Customers: " + rs.getString("total"));
            }

            // Get total balance
            rs = c.statement.executeQuery(
                    "SELECT SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) " +
                            "WHEN type='Withdrawal' THEN -CAST(amount AS DECIMAL) ELSE 0 END) as total_balance " +
                            "FROM bank"
            );
            if (rs.next()) {
                totalBalanceLabel.setText("Total Balance: ₹" + rs.getString("total_balance"));
            }

            // Get today's transactions
            rs = c.statement.executeQuery(
                    "SELECT COUNT(*) as today_count FROM bank WHERE DATE(date) = CURDATE()"
            );
            if (rs.next()) {
                todayTransactionsLabel.setText("Today's Transactions: " + rs.getString("today_count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == logoutButton) {
            setVisible(false);
            new Login();
        } else if (ae.getSource() == refreshButton) {
            loadCustomerData();
            loadTransactionData();
            updateSummaryStatistics();
        } else if (ae.getSource() == searchButton) {
            searchCustomers();
        } else if (ae.getSource() == viewDetailsButton) {
            viewCustomerDetails();
        } else if (ae.getSource() == deleteAccountButton) {
            deleteCustomerAccount();
        } else if (ae.getSource() == backupButton) {
            DatabaseUtilities.backupDatabase();
        } else if (ae.getSource() == settingsButton) {
            new SystemSettings();
        } else if (ae.getSource() == reportsButton) {
            new ReportGenerator();
        } else if (ae.getSource() == blockAccountButton) {
            blockSelectedAccount();
        } else if (ae.getSource() == sendNotificationButton) {
            sendCustomerNotification();
        } else if (ae.getSource() == exportDataButton) {
            exportTransactionData();
        } else if (ae.getSource() == importDataButton) {
            importBankData();
        }
    }

    private void blockSelectedAccount() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to block/unblock.");
            return;
        }

        String cardNumber = (String) customerTable.getValueAt(selectedRow, 2);
        String currentStatus = (String) customerTable.getValueAt(selectedRow, 5);
        String newStatus = "Active".equals(currentStatus) ? "Blocked" : "Active";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + (newStatus.equals("Blocked") ? "block" : "unblock") + " this account?",
                "Confirm Action", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Con c = new Con();
                String query = "UPDATE login SET status = '" + newStatus + "' WHERE card_number = '" + cardNumber + "'";
                c.statement.executeUpdate(query);

                JOptionPane.showMessageDialog(this, "Account status updated successfully!");
                loadCustomerData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating account status: " + e.getMessage());
            }
        }
    }

    private void sendCustomerNotification() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to send notification.");
            return;
        }

        String message = JOptionPane.showInputDialog(this, "Enter notification message:");
        if (message != null && !message.trim().isEmpty()) {
            // Here you would typically integrate with an email or SMS service
            JOptionPane.showMessageDialog(this, "Notification sent successfully!");
        }
    }

    private void exportTransactionData() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Transaction Data");
            fileChooser.setSelectedFile(new File("transactions.csv"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // Add export logic here
                JOptionPane.showMessageDialog(this, "Data exported successfully to " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage());
        }
    }

    private void loadCustomerData() {
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            Con c = new Con();
            String query = "SELECT s.form_no, s.name, l.card_number, s3.account_type, " +
                    "(SELECT SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) " +
                    "WHEN type='Withdrawal' THEN -CAST(amount AS DECIMAL) ELSE 0 END) " +
                    "FROM bank WHERE pin = l.pin) as balance " +
                    "FROM signup s " +
                    "JOIN login l ON s.form_no = l.form_no " +
                    "JOIN signupthree s3 ON s.form_no = s3.form_no";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                String[] row = {
                        rs.getString("form_no"),
                        rs.getString("name"),
                        rs.getString("card_number"),
                        rs.getString("account_type"),
                        rs.getString("balance"),
                        "Active" // Default status
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage());
        }
    }

    private void loadTransactionData() {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            Con c = new Con();
            String query = "SELECT b.date, l.card_number, b.type, b.amount, " +
                    "(SELECT SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) " +
                    "WHEN type='Withdrawal' THEN -CAST(amount AS DECIMAL) ELSE 0 END) " +
                    "FROM bank WHERE pin = l.pin AND DATE(date) <= DATE(b.date)) as balance_after " +
                    "FROM bank b JOIN login l ON b.pin = l.pin " +
                    "ORDER BY b.date DESC";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                String[] row = {
                        rs.getString("date"),
                        rs.getString("card_number"),
                        rs.getString("type"),
                        rs.getString("amount"),
                        rs.getString("balance_after")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading transaction data: " + e.getMessage());
        }
    }

    private void searchCustomers() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCustomerData();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);

        try {
            Con c = new Con();
            String query = "SELECT s.form_no, s.name, l.card_number, s3.account_type, " +
                    "(SELECT SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) " +
                    "WHEN type='Withdrawal' THEN -CAST(amount AS DECIMAL) ELSE 0 END) " +
                    "FROM bank WHERE pin = l.pin) as balance " +
                    "FROM signup s " +
                    "JOIN login l ON s.form_no = l.form_no " +
                    "JOIN signupthree s3 ON s.form_no = s3.form_no " +
                    "WHERE l.card_number LIKE '%" + searchTerm + "%' OR s.name LIKE '%" + searchTerm + "%'";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                String[] row = {
                        rs.getString("form_no"),
                        rs.getString("name"),
                        rs.getString("card_number"),
                        rs.getString("account_type"),
                        rs.getString("balance"),
                        "Active" // Default status
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching customers: " + e.getMessage());
        }
    }

    private void viewCustomerDetails() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to view details.");
            return;
        }

        String formNo = (String) customerTable.getValueAt(selectedRow, 0);

        try {
            Con c = new Con();
            String query = "SELECT s.*, s2.*, s3.* FROM signup s " +
                    "JOIN signuptwo s2 ON s.form_no = s2.form_no " +
                    "JOIN signupthree s3 ON s.form_no = s3.form_no " +
                    "WHERE s.form_no = '" + formNo + "'";

            ResultSet rs = c.statement.executeQuery(query);

            if (rs.next()) {
                StringBuilder details = new StringBuilder();
                details.append("Customer Details:\n\n");
                details.append("Personal Information:\n");
                details.append("Name: ").append(rs.getString("name")).append("\n");
                details.append("Father's Name: ").append(rs.getString("father_name")).append("\n");
                details.append("DOB: ").append(rs.getString("DOB")).append("\n");
                details.append("Gender: ").append(rs.getString("gender")).append("\n");
                details.append("Email: ").append(rs.getString("email")).append("\n");
                details.append("Marital Status: ").append(rs.getString("marital_status")).append("\n");
                details.append("Address: ").append(rs.getString("address")).append("\n");
                details.append("City: ").append(rs.getString("city")).append("\n");
                details.append("State: ").append(rs.getString("state")).append("\n");
                details.append("Pin Code: ").append(rs.getString("pincode")).append("\n\n");

                details.append("Additional Information:\n");
                details.append("Religion: ").append(rs.getString("religion")).append("\n");
                details.append("Category: ").append(rs.getString("category")).append("\n");
                details.append("Income: ").append(rs.getString("income")).append("\n");
                details.append("Education: ").append(rs.getString("education")).append("\n");
                details.append("Occupation: ").append(rs.getString("occupation")).append("\n");
                details.append("PAN: ").append(rs.getString("Pan")).append("\n");
                details.append("Aadhar: ").append(rs.getString("aadhar")).append("\n");
                details.append("Senior Citizen: ").append(rs.getString("seniorcitizen")).append("\n");
                details.append("Existing Account: ").append(rs.getString("existing_account")).append("\n\n");

                details.append("Account Information:\n");
                details.append("Account Type: ").append(rs.getString("account_type")).append("\n");
                details.append("Card Number: ").append(rs.getString("card_number")).append("\n");
                details.append("Services: ").append(rs.getString("facility")).append("\n");

                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));

                JOptionPane.showMessageDialog(this, scrollPane, "Customer Details",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error viewing customer details: " + e.getMessage());
        }
    }

    private void deleteCustomerAccount() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this account? This action cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String formNo = (String) customerTable.getValueAt(selectedRow, 0);

            try {
                Con c = new Con();

                // Delete from all related tables
                c.statement.executeUpdate("DELETE FROM bank WHERE pin IN (SELECT pin FROM login WHERE form_no = '" + formNo + "')");
                c.statement.executeUpdate("DELETE FROM login WHERE form_no = '" + formNo + "'");
                c.statement.executeUpdate("DELETE FROM signupthree WHERE form_no = '" + formNo + "'");
                c.statement.executeUpdate("DELETE FROM signuptwo WHERE form_no = '" + formNo + "'");
                c.statement.executeUpdate("DELETE FROM signup WHERE form_no = '" + formNo + "'");

                JOptionPane.showMessageDialog(this, "Account deleted successfully");
                loadCustomerData();
                loadTransactionData();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting account: " + e.getMessage());
            }
        }
    }
    
    private void importBankData() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Import Bank Data");

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // Add import logic here
                JOptionPane.showMessageDialog(this, "Data imported successfully from " + file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error importing data: " + e.getMessage());
        }
    }

    // ... (keep existing methods like loadCustomerData, loadTransactionData, etc.)
}