import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    private JLabel idLabel;
    private JTextField usernameField;
    private JTextField passwordfield;
    private JLabel usernameLabel;

    private JButton loginButton;
    private JButton signupButton;


    public Main() {
        setTitle("Insert Data");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);


        idLabel = new JLabel("USERNAME:");
        c.gridx = 0;
        c.gridy = 0;
        add(idLabel, c);

        usernameField = new JTextField(10);
        c.gridx = 1;
        c.gridy = 0;
        add(usernameField, c);

        usernameLabel = new JLabel("Password:");
        c.gridx = 0;
        c.gridy = 1;
        add(usernameLabel, c);

        passwordfield = new JTextField(10);
        c.gridx = 1;
        c.gridy = 1;
        add(passwordfield, c);


        loginButton = new JButton("Login");
        c.gridx = 1;
        c.gridy = 2;
        add(loginButton, c);

        signupButton = new JButton("Signup");
        c.gridx = 2;
        c.gridy = 2;
        add(signupButton, c);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordfield == null && usernameField == null) {
                    JOptionPane.showMessageDialog(null, "Please enter username and password");
                } else {
                    checkData();
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignupPage signupPage = new SignupPage();
                signupPage.setVisible(true);
                dispose();


            }
        });


    }

    private void checkData() {
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
                    // Table does not exist
                    JOptionPane.showMessageDialog(this, "Database error: 'signup' table does not exist.");
                    return;
                }

                // Query the database for the username and password
                String username = usernameField.getText();
                String password = passwordfield.getText();
                String sql = "SELECT * FROM signup WHERE username=? AND password=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet result = preparedStatement.executeQuery();

                // If a match is found, show success message. Otherwise, show error message.
                if (result.next()) {
                    HomeScreen homeScreen = new HomeScreen();
                    homeScreen.setVisible(true);
                    dispose();
//                    JOptionPane.showMessageDialog(this, "Login successful");

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password");
                }

                // Close the statements and connection
                preparedStatement.close();
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
                Main form = new Main();
                form.setVisible(true);
            }
        });
    }
}
