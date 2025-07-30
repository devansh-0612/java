import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentApp extends JFrame {
    // Database URL, username, and password
    static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root"; // your MySQL username
    static final String PASS = "";     // your MySQL password

    // UI components
    JTextField nameField, ageField;
    JTextArea displayArea;

    public StudentApp() {
        // UI Setup
        setTitle("Student Management");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View All");

        inputPanel.add(addButton);
        inputPanel.add(viewButton);

        add(inputPanel, BorderLayout.NORTH);

        // Text area for display
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
    }

    private void addStudent() {
        String name = nameField.getText();
        int age;

        try {
            age = Integer.parseInt(ageField.getText());

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();

            conn.close();
            displayArea.setText("Student added successfully!\n");

        } catch (Exception ex) {
            displayArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewStudents() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                        .append(", Name: ").append(rs.getString("name"))
                        .append(", Age: ").append(rs.getInt("age")).append("\n");
            }

            displayArea.setText(sb.toString());
            conn.close();
        } catch (Exception ex) {
            displayArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Load JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found.");
            return;
        }

        SwingUtilities.invokeLater(() -> new StudentApp().setVisible(true));
    }
}
