import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SalesTrendViewer extends JFrame {

    private JTable salesTable;
    private JScrollPane scrollPane;
    private JButton backButton;

    private Connection conn;
    private Statement selectStatement;
    private PreparedStatement deleteStatement;

    public SalesTrendViewer() {
        super("Sales Trend Viewer");

        salesTable = new JTable();
        scrollPane = new JScrollPane(salesTable);
        backButton = new JButton("Back");

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(backButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
            selectStatement = conn.createStatement();
            deleteStatement = conn.prepareStatement("DELETE FROM trenddata WHERE product_name = ?");
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomeScreen();
            }
        });

        salesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = salesTable.rowAtPoint(evt.getPoint());
                String productName = salesTable.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + productName + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        deleteStatement.setString(1, productName);
                        int rowsDeleted = deleteStatement.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(null, productName + " deleted successfully.");
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(null, "No products found with name " + productName + ".");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadData() {
        try {
            String sql = "SELECT product_name, sales_year, quantity_sold FROM trenddata";
            ResultSet rs = selectStatement.executeQuery(sql);

            salesTable.setModel(buildTableModel(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // get column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // get data rows
        Object[][] data = new Object[100][columnCount];
        int rowCount = 0;
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                data[rowCount][i - 1] = rs.getObject(i);
            }
            rowCount++;
        }

        // create table model
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        return tableModel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SalesTrendViewer();
            }
        });
    }
}
