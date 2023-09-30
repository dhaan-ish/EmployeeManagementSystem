import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ajp"; 
        String username = "dhaanish";
        String password = "password";

       
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();

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

            statement.executeUpdate(createTableQuery);

            System.out.println("Table 'employee' created.");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
