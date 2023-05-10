import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class test {

    private static Map<String, List<Integer>> salesData = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Please select an option:");
            System.out.println("1. Add trend data");
            System.out.println("2. Show trend data");
            System.out.println("3. Analyze trend data");
            System.out.println("0. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addTrendData(scanner);
                    break;
                case 2:
                    showTrendData(scanner);
                    break;
                case 3:
                    analyzeTrendData();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

    private static void addTrendData(Scanner scanner) {
        System.out.println("Adding trend data...");
        System.out.print("Enter product name: ");
        String productName = scanner.next();
        System.out.print("Enter sales year: ");
        int salesYear = scanner.nextInt();
        System.out.print("Enter quantity sold: ");
        int quantitySold = scanner.nextInt();
        List<Integer> sales = salesData.getOrDefault(productName, new ArrayList<>());
        while (sales.size() < salesYear) {
            sales.add(0);
        }
        sales.set(salesYear - 1, quantitySold);
        salesData.put(productName, sales);
        System.out.println("Data added successfully!");
    }

    private static void showTrendData(Scanner scanner) {
        System.out.println("Showing trend data...");
        System.out.print("Enter product name: ");
        String productName = scanner.next();
        System.out.print("Enter sales year: ");
        int salesYear = scanner.nextInt();
        List<Integer> sales = salesData.getOrDefault(productName, new ArrayList<>());
        if (salesYear > 0 && salesYear <= sales.size()) {
            int quantitySold = sales.get(salesYear - 1);
            System.out.println("Product: " + productName + ", Year: " + salesYear + ", Sales: " + quantitySold);
        } else {
            System.out.println("No data found for the specified year and product.");
        }
    }

    private static void analyzeTrendData() {
        System.out.println("Analyzing trend data...");
        for (String productName : salesData.keySet()) {
            List<Integer> sales = salesData.get(productName);
            int totalSales = 0;
            int highestSalesYear = 0;
            int highestSalesAmount = 0;
            int lowestSalesYear = 0;
            int lowestSalesAmount = Integer.MAX_VALUE;
            for (int i = 0; i < sales.size(); i++) {
                int yearSales = sales.get(i);
                if (yearSales > 0) {
                    totalSales += yearSales;
                    if (yearSales > highestSalesAmount) {
                        highestSalesYear = i + 1;
                        highestSalesAmount = yearSales;
                    }
                    if (yearSales < lowestSalesAmount) {
                        lowestSalesYear = i + 1;
                        lowestSalesAmount = yearSales;
                    }
                    System.out.println("Product: " + productName + ", Year: " + (i + 1) + ", Sales: " + yearSales);
                }
            }
            System.out.println("Total sales for " + productName + ": " + totalSales);
            System.out.println("Highest sales year for " + productName + ": " + highestSalesYear + ", Sales: " + highestSalesAmount);
            System.out.println("Lowest sales year for " + productName + ": " + lowestSalesYear + ", Sales: " + lowestSalesAmount);
            double averageSales = (double) totalSales / sales.size();
            System.out.println("Average sales for " + productName + ": " + averageSales);
        }
    }


}