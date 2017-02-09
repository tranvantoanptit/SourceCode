package local.toan.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnUtils {

	public static Connection getPostgreSQLConnection()
			throws ClassNotFoundException, SQLException{
			String hostName = "localhost";
			String dbName = "planresultprofit";
			String userName = "postgres";
			String passWord = "Ruangoc123";
			return getPostgreSQLConnection(hostName,dbName,userName,passWord);
	}
	
	public static Connection getPostgreSQLConnection(String hostName,String dbName,String userName,String passWord) 
			throws SQLException, ClassNotFoundException{
			Class.forName("org.postgresql.Driver");
			String connectionURL = "jdbc:postgresql://"+hostName+":5432/"+dbName;
			Connection conn = DriverManager.getConnection(connectionURL,userName,passWord);
			return conn;
		
	}
}
