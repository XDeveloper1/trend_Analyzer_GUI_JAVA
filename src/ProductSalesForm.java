import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductSalesForm extends JFrame implements ActionListener {

    private JLabel productNameLabel, salesYearLabel, quantitySoldLabel;
    private JTextField productNameField, salesYearField, quantitySoldField;
    private JButton saveButton, clearButton , back;

    private Connection conn;
    private PreparedStatement insertStatement;

    public ProductSalesForm() {
        super("Product Sales Form");

        productNameLabel = new JLabel("Enter Product Name:");
        productNameField = new JTextField(20);

        salesYearLabel = new JLabel("Enter Sales Year:");
        salesYearField = new JTextField(10);

        quantitySoldLabel = new JLabel("Enter Quantity Sold:");
        quantitySoldField = new JTextField(10);

        saveButton = new JButton("Save Data");
        clearButton = new JButton("Add More data");
        back = new JButton("Back");

        saveButton.addActionListener(this);
        clearButton.addActionListener(this);
        back.addActionListener(this);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 5, 5));
        formPanel.add(productNameLabel);
        formPanel.add(productNameField);
        formPanel.add(salesYearLabel);
        formPanel.add(salesYearField);
        formPanel.add(quantitySoldLabel);
        formPanel.add(quantitySoldField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(back);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(formPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
            createTableIfNotExists();
            insertStatement = conn.prepareStatement("INSERT INTO trenddata (product_name, sales_year, quantity_sold) VALUES (?, ?, ?)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            saveData();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource()==back) {
            HomeScreen homeScreen = new HomeScreen();
            homeScreen.setVisible(true);
            dispose();

        }
    }

    private void saveData() {
        try {
            insertStatement.setString(1, productNameField.getText());
            insertStatement.setInt(2, Integer.parseInt(salesYearField.getText()));
            insertStatement.setInt(3, Integer.parseInt(quantitySoldField.getText()));
            insertStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format: " + ex.getMessage());
        }
    }

    private void clearFields() {
        productNameField.setText("");
        salesYearField.setText("");
        quantitySoldField.setText("");
    }

    private void createTableIfNotExists() {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS trenddata " +
                    "(id INT(11) NOT NULL AUTO_INCREMENT, " +
                    " product_name VARCHAR(255) NOT NULL, " +
                    " sales_year INT(11) NOT NULL, " +
                    " quantity_sold INT(11) NOT NULL, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductSalesForm();
            }
        });
    }
}
