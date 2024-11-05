package bms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.io.FileWriter;
import java.io.IOException;

public class mini extends JFrame implements ActionListener {
    String pin;
    JButton exitButton;
    JButton saveTextButton;

    mini(String pin) {
        this.pin = pin;
        getContentPane().setBackground(new Color(255, 204, 204));
        setSize(400, 600);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new GridBagLayout()); // Use GridBagLayout for centering

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Space between components

        // Title Label
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>First National Bank Statement</div></html>");
        titleLabel.setFont(new Font("System", Font.BOLD, 20));
        gbc.gridx = 0; // Column
        gbc.gridy = 0; // Row
        gbc.anchor = GridBagConstraints.CENTER; // Center
        add(titleLabel, gbc);

        // Card Number Label
        JLabel cardLabel = new JLabel();
        gbc.gridy = 1; // Next row
        gbc.anchor = GridBagConstraints.CENTER; // Center
        add(cardLabel, gbc);

        // Transactions Label
        JLabel transactionsLabel = new JLabel();
        transactionsLabel.setVerticalAlignment(JLabel.TOP); // Align text to top
        gbc.gridy = 2; // Next row
        gbc.anchor = GridBagConstraints.CENTER; // Center
        add(transactionsLabel, gbc);

        // Balance Label
        JLabel balanceLabel = new JLabel();
        gbc.gridy = 3; // Next row
        gbc.anchor = GridBagConstraints.CENTER; // Center
        add(balanceLabel, gbc);

        try {
            Con c = new Con();
            ResultSet resultSet = c.statement.executeQuery("SELECT * FROM login WHERE pin = '" + pin + "'");
            if (resultSet.next()) {
                cardLabel.setText("<html><div style='text-align: center;'>Card Number: "
                        + resultSet.getString("card_number").substring(0, 4)
                        + "XXXXXXXX" + resultSet.getString("card_number").substring(12)
                        + "</div></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int balance = 0;
            Con c = new Con();
            ResultSet resultSet = c.statement.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");
            StringBuilder transactions = new StringBuilder("<html><div style='text-align: center;'>");

            while (resultSet.next()) {
                transactions.append(resultSet.getString("date"))
                        .append(" ")
                        .append(resultSet.getString("type"))
                        .append(" ")
                        .append(resultSet.getString("amount"))
                        .append("<br>"); // Use <br> for line breaks
                if (resultSet.getString("type").equals("Deposit")) {
                    balance += Integer.parseInt(resultSet.getString("amount"));
                } else {
                    balance -= Integer.parseInt(resultSet.getString("amount"));
                }
            }
            transactions.append("</div></html>");
            transactionsLabel.setText(transactions.toString());
            balanceLabel.setText("<html><div style='text-align: center;'>Your Total Balance is Rs " + balance + "</div></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Filler to push components to the top
        gbc.weighty = 1; // Allow vertical growth
        gbc.gridy = 4; // Next row for filler
        add(new JLabel(), gbc); // Empty label as a spacer

        // Exit Button
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        exitButton.setBackground(Color.white);
        exitButton.setForeground(Color.black);
        gbc.weighty = 0; // Reset weight
        gbc.gridy = 5; // Set button to the next row (very bottom)
        gbc.anchor = GridBagConstraints.CENTER; // Align to center
        add(exitButton, gbc);

        // Save Text File Button
        saveTextButton = new JButton("Save Text");
        saveTextButton.addActionListener(e -> saveText(transactionsLabel.getText(), balanceLabel.getText()));
        saveTextButton.setBackground(Color.white);
        saveTextButton.setForeground(Color.black);
        gbc.gridx = 0; // Keep it in the same column
        gbc.gridy = 6; // Next row
        gbc.anchor = GridBagConstraints.CENTER; // Align to center
        add(saveTextButton, gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    private void saveText(String transactions, String balanceInfo) {
        try {
            // Prompt user for file save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".txt";
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write("First National Bank Statement\n");
                    writer.write(transactions.replaceAll("<html>|</html>", "").replaceAll("&nbsp;", " ") + "\n");
                    writer.write(balanceInfo.replaceAll("<html>|</html>", "").replaceAll("&nbsp;", " "));
                    JOptionPane.showMessageDialog(this, "Text file saved successfully!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new mini("");
    }
}
