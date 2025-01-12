package alex.tfg.tfg;

import alex.tfg.tfg.models.ConnectDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class PswdMngrApplication {

	public static void main(String[] args) {
		SpringApplication.run(PswdMngrApplication.class, args);
		connectToDatabase();
	}

	private static void connectToDatabase(){

		ConnectDB db = new ConnectDB();
		Connection connection = db.getConnection();

		if (connection != null) {
			try {
				Statement statement = connection.createStatement();

				createDatabase(statement);
				insertData(statement);
				displayData(statement);
				updateData(statement);
				deleteData(statement);
				dropTable(statement);

				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				db.closeConnection();
			}
		} else {
			System.out.println("Connection failed.");
		}
	}

	private static void createDatabase(Statement statement) throws SQLException {

		String createTable="CREATE TABLE IF NOT EXISTS Users (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"username TEXT NOT NULL UNIQUE, " +
				"email TEXT NOT NULL UNIQUE, " +
				"password_hash TEXT NOT NULL, " +
				"role TEXT NOT NULL DEFAULT 'user'" +
				")";
		// Create table if not exists
		statement.executeUpdate(createTable);
	}
	private static void insertData(Statement statement) throws SQLException {
		statement.executeUpdate("INSERT INTO Users (username, email, password_hash, role) VALUES ('user', 'user@example.com', 'hashed_password1', 'user')");
		statement.executeUpdate("INSERT INTO Users (username, email, password_hash, role) VALUES ('admin', 'admin@example.com', 'hashed_password2', 'admin')");
		statement.executeUpdate("INSERT INTO Users (username, email, password_hash, role) VALUES ('prueba', 'prueba@example.com', 'hashed_password3', 'prueba')");
		System.out.println("Data Inserted");

	}
	private static void displayData(Statement statement) throws SQLException {
		ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
		System.out.println("ID\tUsername\tEmail\tRole");
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String username = resultSet.getString("username");
			String email = resultSet.getString("email");
			String role = resultSet.getString("role");
			System.out.println(id + "\t" + username + "\t" + email + "\t" + role);
		}
		resultSet.close();
	}
	private static void updateData(Statement statement) throws SQLException {
		statement.executeUpdate("UPDATE users SET username = 'prueba2' WHERE username = 'prueba'");
		System.out.println("Data Updated");
		displayData(statement);

	}
	private static void deleteData(Statement statement) throws SQLException {
		statement.executeUpdate("DELETE FROM users WHERE username='prueba2'");
		System.out.println("Data Deleted");
		displayData(statement);
	}
	private static void dropTable(Statement statement) throws SQLException {
		statement.executeUpdate("DROP TABLE IF EXISTS Users");
		System.out.println("Table deleted");
	}

}
