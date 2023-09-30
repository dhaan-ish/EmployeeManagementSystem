import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/ajp"; // replace 'your_database_name' with the actual database name
        String username = "dhaanish";
        String password = "password";

        // JDBC variables for opening, closing, and managing connection
        Connection connection = null;

        try {
            // Register the JDBC driver (if you are using JDBC 4.0 and later, you don't need to do this)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(url, username, password);

            // Create a Statement object for sending SQL statements to the database
            Statement statement = connection.createStatement();

            // SQL query to create a table named 'employee'
            String createTableQuery = "CREATE TABLE employee ("
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

            // Execute the query to create the 'employee' table
            statement.executeUpdate(createTableQuery);

            System.out.println("Table 'employee' created.");

        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block to close resources
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
