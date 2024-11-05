package bms;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Random;

public class Signup extends JFrame implements ActionListener {

    // UI components
    private JRadioButton r1, r2, m1, m2, m3;
    private JButton next;
    private JTextField textName, textFname, textEmail, textAdd, textCity, textState, textPin;
    private JDateChooser dateChooser;

    // Generate unique form number
    private Random ran = new Random();
    private long first4 = (ran.nextLong() % 9000L) + 1000L;
    private String formNo = " " + Math.abs(first4);

    // Constructor to set up UI
    public Signup() {
        super("APPLICATION FORM");

        // Bank icon
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bank.png"));
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(25, 10, 100, 100);
        add(image);

        // Form labels and fields
        setupFormLabelsAndFields();

        // Set window properties
        getContentPane().setBackground(new Color(222, 255, 228));
        setLayout(null);
        setSize(850, 800);
        setLocation(360, 40);
        setVisible(true);
    }

    // Set up the form fields and labels
    private void setupFormLabelsAndFields() {
        JLabel label1 = new JLabel("APPLICATION FORM NO. " + formNo);
        label1.setFont(new Font("Raleway", Font.BOLD, 38));
        label1.setBounds(160, 20, 600, 40);
        add(label1);

        JLabel label2 = new JLabel("Page 1");
        label2.setFont(new Font("Raleway", Font.BOLD, 22));
        label2.setBounds(330, 70, 600, 30);
        add(label2);

        JLabel label3 = new JLabel("Personal Details");
        label3.setFont(new Font("Raleway", Font.BOLD, 22));
        label3.setBounds(290, 90, 600, 30);
        add(label3);

        setupTextFieldsAndRadios();
    }

    // Set up text fields and radio buttons
    private void setupTextFieldsAndRadios() {
        addLabel("Name :", 100, 190);
        textName = addTextField(300, 190);

        addLabel("Father's Name :", 100, 240);
        textFname = addTextField(300, 240);

        addLabel("Gender", 100, 290);
        r1 = addRadioButton("Male", 300, 290);
        r2 = addRadioButton("Female", 450, 290);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(r1);
        genderGroup.add(r2);

        addLabel("Date of Birth", 100, 340);
        dateChooser = new JDateChooser();
        dateChooser.setForeground(new Color(105, 105, 105));
        dateChooser.setBounds(300, 340, 400, 30);
        dateChooser.setMaxSelectableDate(new Date());  // Prevent future dates
        add(dateChooser);

        addLabel("Email Address :", 100, 390);
        textEmail = addTextField(300, 390);

        addLabel("Marital Status :", 100, 440);
        m1 = addRadioButton("Married", 300, 440);
        m2 = addRadioButton("Unmarried", 450, 440);
        m3 = addRadioButton("Other", 635, 440);
        ButtonGroup maritalGroup = new ButtonGroup();
        maritalGroup.add(m1);
        maritalGroup.add(m2);
        maritalGroup.add(m3);

        addAddressFields();

        next = new JButton("Next");
        next.setFont(new Font("Raleway", Font.BOLD, 14));
        next.setBackground(Color.white);
        next.setForeground(Color.black);
        next.setBounds(620, 710, 80, 30);
        next.addActionListener(this);
        add(next);
    }

    // Helper methods for adding UI components
    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Raleway", Font.BOLD, 20));
        label.setBounds(x, y, 200, 30);
        add(label);
    }

    private JTextField addTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Raleway", Font.BOLD, 14));
        textField.setBounds(x, y, 400, 30);
        add(textField);
        return textField;
    }

    private JRadioButton addRadioButton(String text, int x, int y) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        radioButton.setBackground(new Color(222, 255, 228));
        radioButton.setBounds(x, y, 100, 30);
        add(radioButton);
        return radioButton;
    }

    private void addAddressFields() {
        addLabel("Address :", 100, 490);
        textAdd = addTextField(300, 490);

        addLabel("City :", 100, 540);
        textCity = addTextField(300, 540);

        addLabel("Pin Code :", 100, 590);
        textPin = addTextField(300, 590);
        // Pin code should only allow numbers
        textPin.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore non-numeric input
                }
            }
        });

        addLabel("State :", 100, 640);
        textState = addTextField(300, 640);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = textName.getText();
        String fname = textFname.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String gender = r1.isSelected() ? "Male" : r2.isSelected() ? "Female" : null;
        String email = textEmail.getText();
        String marital = m1.isSelected() ? "Married" : m2.isSelected() ? "Unmarried" : m3.isSelected() ? "Other" : null;
        String address = textAdd.getText();
        String city = textCity.getText();
        String pincode = textPin.getText();
        String state = textState.getText();

        // Input validation and database insertion
        try {
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Fill all the fields");
            } else {
                Con c = new Con();
                String query = "INSERT INTO signup VALUES('" + formNo + "', '" + name + "', '" + fname + "', '" + dob + "', '" + gender + "', '" + email + "', '" + marital + "', '" + address + "', '" + city + "', '" + pincode + "', '" + state + "')";
                c.statement.executeUpdate(query);
                new Signup2(formNo);
                setVisible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
