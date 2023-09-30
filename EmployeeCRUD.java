import java.sql.*;
import java.util.Scanner;

public class EmployeeCRUD {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/ajp";
    static final String USERNAME = "dhaanish";
    static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to the database.");

            // Create a sample employee table if not exists
            createEmployeeTable(connection);

            // Menu-driven CRUD operations
            while (true) {
                System.out.println("1. Add Employee");
                System.out.println("2. Read Employee");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");

                int choice = getUserChoice();

                switch (choice) {
                    case 1:
                        addEmployee(connection);
                        break;
                    case 2:
                        readEmployee(connection);
                        break;
                    case 3:
                        updateEmployee(connection);
                        break;
                    case 4:
                        deleteEmployee(connection);
                        break;
                    case 5:

                        System.out.println("Exiting the program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createEmployeeTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS employee ("
                + "emp_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "emp_first_name VARCHAR(255),"
                + "emp_last_name VARCHAR(255),"
                + "emp_number INT,"
                + "emp_email VARCHAR(255),"
                + "emp_gender VARCHAR(10),"
                + "emp_doj DATE,"
                + "emp_dob DATE,"
                + "emp_sal DECIMAL(10, 2),"
                + "emp_pos VARCHAR(255)"
                + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }

    private static int getUserChoice() {
        try {
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            return -1; // Return an invalid choice
        }
    }

    private static void addEmployee(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter employee first name: ");
        String firstName = scanner.nextLine();
    
        System.out.print("Enter employee last name: ");
        String lastName = scanner.nextLine();
    
        System.out.print("Enter employee number: ");
        int empNumber = scanner.nextInt();
    
        System.out.print("Enter employee email: ");
        String empEmail = scanner.next();
    
        System.out.print("Enter employee gender: ");
        String empGender = scanner.next();
    
        System.out.print("Enter employee date of joining (yyyy-mm-dd): ");
        String doj = scanner.next();
    
        System.out.print("Enter employee date of birth (yyyy-mm-dd): ");
        String dob = scanner.next();
    
        System.out.print("Enter employee salary: ");
        double empSal = scanner.nextDouble();
    
        System.out.print("Enter employee position: ");
        String empPos = scanner.next();
    
        String insertQuery = "INSERT INTO employee (emp_first_name, emp_last_name, emp_number, emp_email, emp_gender, emp_doj, emp_dob, emp_sal, emp_pos) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, empNumber);
            preparedStatement.setString(4, empEmail);
            preparedStatement.setString(5, empGender);
            preparedStatement.setDate(6, Date.valueOf(doj));
            preparedStatement.setDate(7, Date.valueOf(dob));
            preparedStatement.setDouble(8, empSal);
            preparedStatement.setString(9, empPos);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        }
    }
    
    private static void readEmployee(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
    
        String selectQuery = "SELECT * FROM employee WHERE emp_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, empId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                displayEmployeeDetails(resultSet);
            } else {
                System.out.println("Employee not found.");
            }
        }
    }
    
    private static void updateEmployee(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        // Check if the employee exists
        if (!employeeExists(connection, empId)) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Select the field to update:");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Employee Number");
        System.out.println("4. Email");
        System.out.println("5. Gender");
        System.out.println("6. Date of Joining");
        System.out.println("7. Date of Birth");
        System.out.println("8. Salary");
        System.out.println("9. Position");
        System.out.println("10. Exit");

        int choice = getUserChoice();

        if (choice == 10) {
            System.out.println("Exiting the update operation.");
            return;
        }

        // Get the column name based on the user's choice
        String columnName = getColumnName(choice);

        System.out.print("Enter new value for " + columnName + ": ");
        String newValue = scanner.next();

        String updateQuery = "UPDATE employee SET " + columnName + " = ? WHERE emp_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            if (isNumericColumn(columnName)) {
                preparedStatement.setInt(1, Integer.parseInt(newValue));
            } else {
                preparedStatement.setString(1, newValue);
            }

            preparedStatement.setInt(2, empId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("Failed to update employee details.");
            }
        }
        }

        private static boolean isNumericColumn(String columnName) {
        return columnName.equals("emp_number") || columnName.equals("emp_sal");
        }

        private static String getColumnName(int choice) {
        switch (choice) {
            case 1:
                return "emp_first_name";
            case 2:
                return "emp_last_name";
            case 3:
                return "emp_number";
            case 4:
                return "emp_email";
            case 5:
                return "emp_gender";
            case 6:
                return "emp_doj";
            case 7:
                return "emp_dob";
            case 8:
                return "emp_sal";
            case 9:
                return "emp_pos";
            default:
                return "";
        }
        }

    
    private static void deleteEmployee(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
    
        // Check if the employee exists
        if (!employeeExists(connection, empId)) {
            System.out.println("Employee not found.");
            return;
        }
    
        String deleteQuery = "DELETE FROM employee WHERE emp_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, empId);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Failed to delete employee.");
            }
        }
    }
    
    private static boolean employeeExists(Connection connection, int empId) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM employee WHERE emp_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
            preparedStatement.setInt(1, empId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        }
    }

    private static void displayEmployeeDetails(ResultSet resultSet) throws SQLException {
        System.out.println("Employee Details:");
        System.out.println("Employee ID: " + resultSet.getInt("emp_id"));
        System.out.println("First Name: " + resultSet.getString("emp_first_name"));
        System.out.println("Last Name: " + resultSet.getString("emp_last_name"));
        System.out.println("Employee Number: " + resultSet.getInt("emp_number"));
        System.out.println("Email: " + resultSet.getString("emp_email"));
        System.out.println("Gender: " + resultSet.getString("emp_gender"));
        System.out.println("Date of Joining: " + resultSet.getDate("emp_doj"));
        System.out.println("Date of Birth: " + resultSet.getDate("emp_dob"));
        System.out.println("Salary: " + resultSet.getDouble("emp_sal"));
        System.out.println("Position: " + resultSet.getString("emp_pos"));
    }
}
