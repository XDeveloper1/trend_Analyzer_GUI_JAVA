import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignupPage extends JFrame {
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton signupButton;
    private JButton Loginbutton;

    public SignupPage() {
        setTitle("Sign Up");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2));

        usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel);

        usernameField = new JTextField(10);
        formPanel.add(usernameField);

        passwordLabel = new JLabel("Password:");
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField(10);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        signupButton = new JButton("Sign Up");
        buttonPanel.add(signupButton);
        Loginbutton = new JButton("Login");
        buttonPanel.add(Loginbutton);

        Loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main main = new Main();
                main.setVisible(true);
                dispose();

            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Perform the database insert operation here
                insertData(username, password);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void insertData(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/test";
        String dbUsername = "root";
        String dbPassword = "";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            if (connection != null) {
                Statement statement = connection.createStatement();

                // Check if the table exists
                ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'signup'");
                if (!resultSet.next()) {
                    // Table does not exist, create it
                    statement.executeUpdate("CREATE TABLE signup (username VARCHAR(50), password VARCHAR(50))");
                }

                // Insert the data
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO signup (username, password) VALUES (?, ?)");
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Sign up successful");
                } else {
                    JOptionPane.showMessageDialog(this, "Sign up failed");
                }

                // Close the statements and connection
                insertStatement.close();
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SignupPage form = new SignupPage();
                form.setVisible(true);
            }
        });
    }
}
