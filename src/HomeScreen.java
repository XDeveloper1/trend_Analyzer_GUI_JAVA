import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JFrame implements ActionListener {

    private JButton insertButton;
    private JButton showButton;
    private JButton analyzeButton;
    private JButton graphButton;

    public HomeScreen() {
        // Set the title and size of the window
        setTitle("My Window");
        setSize(400, 300);

        // Create the buttons
        insertButton = new JButton("Add trend data");
        showButton = new JButton("Show trend Data");
        analyzeButton = new JButton("Analyze trend data");
        graphButton = new JButton("Exit");

        // Add action listeners to the buttons
        insertButton.addActionListener(this);
        showButton.addActionListener(this);
        analyzeButton.addActionListener(this);
        graphButton.addActionListener(this);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.add(insertButton);
        buttonPanel.add(showButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(graphButton);

        // Add the panel to the window
        add(buttonPanel);

        // Show the window
        setVisible(true);
    }

    // Handle button clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertButton) {
            ProductSalesForm productSalesForm = new ProductSalesForm();
            productSalesForm.setVisible(true);
            dispose();
//            System.out.println("Add trend data");
        } else if (e.getSource() == showButton) {
            SalesTrendViewer salesTrendViewer = new SalesTrendViewer();
            salesTrendViewer.setVisible(true);
            dispose();
//            System.out.println("Show trend Data");
        } else if (e.getSource() == analyzeButton) {
            SalesTrendAnalyzer salesTrendAnalyzer = new SalesTrendAnalyzer();
            salesTrendAnalyzer.setVisible(true);
            dispose();
//            System.out.println("Analyze trend data");
        } else if (e.getSource() == graphButton) {
            dispose();
//            System.out.println("Exit");
        }
    }

    // Main method to create and show the window
    public static void main(String[] args) {
        HomeScreen window = new HomeScreen();
    }
}
