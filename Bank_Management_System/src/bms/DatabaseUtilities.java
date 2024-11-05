package bms;

import javax.swing.*;
        import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseUtilities {

    public static void backupDatabase() {
        try {
            // Get current date for backup file name
            String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String backupPath = "backup/bankms_" + date + ".sql";

            // Ensure backup directory exists
            new File("backup").mkdirs();

            // Create ProcessBuilder for mysqldump
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mysqldump",
                    "--user=root",
                    "--password=",  // Add your MySQL password here if any
                    "bankms"
            );

            // Redirect output to backup file
            processBuilder.redirectOutput(new File(backupPath));

            // Start the process
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                JOptionPane.showMessageDialog(null,
                        "Database backup created successfully!\nLocation: " + backupPath);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error creating backup. Exit code: " + exitCode,
                        "Backup Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error: " + e.getMessage(),
                    "Backup Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}