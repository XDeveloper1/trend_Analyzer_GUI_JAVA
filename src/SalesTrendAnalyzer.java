import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SalesTrendAnalyzer extends JFrame {

    private JTable analysisTable;
    private JScrollPane scrollPane;

    private Connection conn;
    private Statement selectStatement;

    public SalesTrendAnalyzer() {
        super("Sales Trend Analyzer");

        analysisTable = new JTable();
        scrollPane = new JScrollPane(analysisTable);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
            selectStatement = conn.createStatement();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            String sql = "SELECT product_name, SUM(quantity_sold) AS total_sales, " +
                    "MAX(quantity_sold) AS max_sales, " +
                    "MIN(quantity_sold) AS min_sales, " +
                    "AVG(quantity_sold) AS avg_sales " +
                    "FROM trenddata " +
                    "GROUP BY product_name";
            ResultSet rs = selectStatement.executeQuery(sql);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Product Name");
            tableModel.addColumn("Total Sales");
            tableModel.addColumn("Highest Sales");
            tableModel.addColumn("Lowest Sales");
            tableModel.addColumn("Average Sales");

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int totalSales = rs.getInt("total_sales");
                int highestSales = rs.getInt("max_sales");
                int lowestSales = rs.getInt("min_sales");
                double avgSales = rs.getDouble("avg_sales");

                // calculate percentage of total sales
                double highestPercentage = (double) highestSales / totalSales * 100;
                double lowestPercentage = (double) lowestSales / totalSales * 100;
                double avgPercentage = avgSales / totalSales * 100;

                tableModel.addRow(new Object[]{productName, totalSales, String.format("%.2f%%", highestPercentage), String.format("%.2f%%", lowestPercentage), String.format("%.2f%%", avgPercentage)});
            }

            analysisTable.setModel(tableModel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SalesTrendAnalyzer();
            }
        });
    }
}
