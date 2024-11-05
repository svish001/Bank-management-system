package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;  // Added import for JDateChooser

public class ReportGenerator extends JFrame implements ActionListener {

    private JComboBox<String> reportTypeCombo;
    private JButton generateButton, cancelButton;
    private JDateChooser fromDate, toDate;

    public ReportGenerator() {
        setTitle("Generate Reports");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Report Type:"), gbc);

        gbc.gridx = 1;
        reportTypeCombo = new JComboBox<>(new String[]{
                "Transaction Summary",
                "Customer List",
                "Account Balance Summary",
                "Daily Operations Report"
        });
        mainPanel.add(reportTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("From Date:"), gbc);

        gbc.gridx = 1;
        fromDate = new JDateChooser();
        mainPanel.add(fromDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("To Date:"), gbc);

        gbc.gridx = 1;
        toDate = new JDateChooser();
        mainPanel.add(toDate, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        generateButton = new JButton("Generate Report");
        cancelButton = new JButton("Cancel");

        generateButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);

        // Add panels to frame
        add(new JLabel("Generate Reports", SwingConstants.CENTER), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == generateButton) {
            generateReport();
        } else if (ae.getSource() == cancelButton) {
            dispose();
        }
    }

    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();

        try {
            Con c = new Con();

            switch (reportType) {
                case "Transaction Summary":
                    generateTransactionReport(c);
                    break;
                case "Customer List":
                    generateCustomerReport(c);
                    break;
                case "Account Balance Summary":
                    generateBalanceReport(c);
                    break;
                case "Daily Operations Report":
                    generateDailyReport(c);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }

    private void generateTransactionReport(Con c) throws Exception {
        // Create directory for reports if it doesn't exist
        new File("reports").mkdirs();

        // Generate filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "reports/transaction_report_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Transaction Summary Report");
            writer.println("Generated on: " + new Date());
            writer.println("----------------------------------------");

            String query = "SELECT b.date, l.card_number, b.type, b.amount " +
                    "FROM bank b JOIN login l ON b.pin = l.pin " +
                    "ORDER BY b.date DESC";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                writer.printf("Date: %s\n", rs.getString("date"));
                writer.printf("Card: %s\n", rs.getString("card_number"));
                writer.printf("Type: %s\n", rs.getString("type"));
                writer.printf("Amount: Rs. %s\n", rs.getString("amount"));
                writer.println("----------------------------------------");
            }

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nLocation: " + filename);
        }
    }

    private void generateCustomerReport(Con c) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "reports/customer_report_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Customer List Report");
            writer.println("Generated on: " + new Date());
            writer.println("----------------------------------------");

            String query = "SELECT s.name, s.email, s.city, s3.account_type, l.card_number " +
                    "FROM signup s " +
                    "JOIN signupthree s3 ON s.form_no = s3.form_no " +
                    "JOIN login l ON s.form_no = l.form_no";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                writer.printf("Name: %s\n", rs.getString("name"));
                writer.printf("Email: %s\n", rs.getString("email"));
                writer.printf("City: %s\n", rs.getString("city"));
                writer.printf("Account Type: %s\n", rs.getString("account_type"));
                writer.printf("Card Number: %s\n", rs.getString("card_number"));
                writer.println("----------------------------------------");
            }

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nLocation: " + filename);
        }
    }

    private void generateBalanceReport(Con c) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "reports/balance_report_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Account Balance Summary Report");
            writer.println("Generated on: " + new Date());
            writer.println("----------------------------------------");

            String query = "SELECT s.name, l.card_number, " +
                    "(SELECT SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) " +
                    "WHEN type='Withdrawal' THEN -CAST(amount AS DECIMAL) ELSE 0 END) " +
                    "FROM bank WHERE pin = l.pin) as balance " +
                    "FROM signup s " +
                    "JOIN login l ON s.form_no = l.form_no";

            ResultSet rs = c.statement.executeQuery(query);

            while (rs.next()) {
                writer.printf("Name: %s\n", rs.getString("name"));
                writer.printf("Card Number: %s\n", rs.getString("card_number"));
                writer.printf("Current Balance: Rs. %s\n", rs.getString("balance"));
                writer.println("----------------------------------------");
            }

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nLocation: " + filename);
        }
    }

    private void generateDailyReport(Con c) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "reports/daily_report_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Daily Operations Report");
            writer.println("Generated on: " + new Date());
            writer.println("----------------------------------------");

            // Total transactions
            String transQuery = "SELECT COUNT(*) as total, " +
                    "SUM(CASE WHEN type='Deposit' THEN 1 ELSE 0 END) as deposits, " +
                    "SUM(CASE WHEN type='Withdrawal' THEN 1 ELSE 0 END) as withdrawals " +
                    "FROM bank WHERE date = CURDATE()";

            ResultSet rs = c.statement.executeQuery(transQuery);

            if (rs.next()) {
                writer.println("DAILY TRANSACTION SUMMARY");
                writer.printf("Total Transactions: %s\n", rs.getString("total"));
                writer.printf("Total Deposits: %s\n", rs.getString("deposits"));
                writer.printf("Total Withdrawals: %s\n", rs.getString("withdrawals"));
                writer.println("----------------------------------------");
            }

            // Daily totals
            String totalsQuery = "SELECT " +
                    "SUM(CASE WHEN type='Deposit' THEN CAST(amount AS DECIMAL) ELSE 0 END) as total_deposits, " +
                    "SUM(CASE WHEN type='Withdrawal' THEN CAST(amount AS DECIMAL) ELSE 0 END) as total_withdrawals " +
                    "FROM bank WHERE date = CURDATE()";

            rs = c.statement.executeQuery(totalsQuery);

            if (rs.next()) {
                writer.println("DAILY AMOUNT SUMMARY");
                writer.printf("Total Deposit Amount: Rs. %s\n", rs.getString("total_deposits"));
                writer.printf("Total Withdrawal Amount: Rs. %s\n", rs.getString("total_withdrawals"));
                writer.println("----------------------------------------");
            }

            // List all transactions for today
            writer.println("TODAY'S TRANSACTIONS");
            String todayQuery = "SELECT b.date, l.card_number, b.type, b.amount " +
                    "FROM bank b JOIN login l ON b.pin = l.pin " +
                    "WHERE date = CURDATE() ORDER BY b.date DESC";

            rs = c.statement.executeQuery(todayQuery);

            while (rs.next()) {
                writer.printf("Time: %s\n", rs.getString("date"));
                writer.printf("Card: %s\n", rs.getString("card_number"));
                writer.printf("Type: %s\n", rs.getString("type"));
                writer.printf("Amount: Rs. %s\n", rs.getString("amount"));
                writer.println("----------------------------------------");
            }

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nLocation: " + filename);
        }
    }
}