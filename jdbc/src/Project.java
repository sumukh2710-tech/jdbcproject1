import java.sql.*;
import java.util.Scanner;
public class Project {


    // 🔗 Database details
    private static final String URL = "jdbc:mysql://localhost:3306/company_db?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // 📌 Get DB Connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("❌ Database connection failed");
            e.printStackTrace();
            return null;
        }
    }

    // ➕ Add Employee
    public static void addEmployee(Scanner sc) {
        String sql = "INSERT INTO employees(name, email, department, salary) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            sc.nextLine(); // clear buffer

            System.out.print("Enter Name: ");
            ps.setString(1, sc.nextLine());

            System.out.print("Enter Email: ");
            ps.setString(2, sc.nextLine());

            System.out.print("Enter Department: ");
            ps.setString(3, sc.nextLine());

            System.out.print("Enter Salary: ");
            ps.setDouble(4, sc.nextDouble());

            ps.executeUpdate();
            System.out.println("✅ Employee Added Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 📋 View All Employees
    public static void viewEmployees() {
        String sql = "SELECT * FROM employees";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Name | Email | Department | Salary");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("department") + " | " +
                        rs.getDouble("salary")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✏️ Update Employee
    public static void updateEmployee(Scanner sc) {
        String sql = "UPDATE employees SET department=?, salary=? WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Department: ");
            ps.setString(1, sc.nextLine());

            System.out.print("Enter New Salary: ");
            ps.setDouble(2, sc.nextDouble());

            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Employee Updated" : "❌ Employee Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ❌ Delete Employee
    public static void deleteEmployee(Scanner sc) {
        String sql = "DELETE FROM employees WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Employee ID: ");
            ps.setInt(1, sc.nextInt());

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Employee Deleted" : "❌ Employee Not Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔍 Search Employee
    public static void searchEmployee(Scanner sc) {
        String sql = "SELECT * FROM employees WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Employee ID: ");
            ps.setInt(1, sc.nextInt());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nEmployee Found:");
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("department") + " | " +
                        rs.getDouble("salary")
                );
            } else {
                System.out.println("❌ Employee Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🖥️ Main Menu
    public static void main(String[] args) {
        try (Connection con = getConnection();
     Statement st = con.createStatement()) {

    st.execute("""
        CREATE TABLE IF NOT EXISTS employees (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(100) NOT NULL,
            email VARCHAR(100) UNIQUE,
            department VARCHAR(50),
            salary DOUBLE
        )
    """);
} catch (Exception e) {
    e.printStackTrace();
}

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Employee Management System ====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addEmployee(sc);
                case 2 -> viewEmployees();
                case 3 -> updateEmployee(sc);
                case 4 -> deleteEmployee(sc);
                case 5 -> searchEmployee(sc);
                case 6 -> {
                    System.out.println("👋 Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid Choice");
            }
        }
    }
}

    

