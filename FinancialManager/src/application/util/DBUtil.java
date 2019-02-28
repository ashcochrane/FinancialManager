package application.util;

import application.model.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.MonthDay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBUtil {
	
	private static final String URL = "jdbc:mysql://localhost:3306/financialManager";
    private static final String USER = "root";
    private static final String PASS = "";
	
	private static Connection conn = null;
	
	/*
	 * Connects to the SQL Workbench.
	 * 
	 */
	private static void dbConnect() throws SQLException, ClassNotFoundException {
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {
			System.out.println("Connection to database failed");
			e.printStackTrace();
			throw e;
		}
	}
	
	/*
	 * Disconnects from the SQL Workbench.
	 * 
	 */
	private static void dbDisconnect() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Creates an ArrayList of transactions that can then be used to plot the relevant information into graphs.
	 * 
	 * @return Returns an ArrayList of Transactions.
	 */
	public static ObservableList<Transaction> dbGetTransactions() throws SQLException, ClassNotFoundException {
		Statement stmt = null;
		ResultSet resultSet = null;
		//Initialize ArrayList to hold all transactions in table
		ObservableList<Transaction> transactions = FXCollections.observableArrayList();
		try {
			//Connect to database
			dbConnect();
			//Create Statement
			stmt = conn.createStatement();
			//Store all transactions from table
			resultSet = stmt.executeQuery("Select * from transactions");
		}catch (SQLException e) {
			System.out.println("Error in selecting transactions from table");
			throw e;
		}
		while (resultSet.next()) {
			int stmtNum = resultSet.getInt(1);
			String date = resultSet.getString(2);
			Double amount = resultSet.getDouble(3);
			//Create transactions with data pulled from table
			Transaction t = new Transaction(stmtNum, toMonthDay(date), amount);
			//Add transactions to ArrayList
			transactions.add(t);
		}
		return transactions;
	}
	
	/*
	 * @param A specific sql statement to execute.
	 * Executes an update to the database.
	 */
	public static void dbExecuteUpdate(String sqlstmt) throws SQLException, ClassNotFoundException {
		Statement stmt = null;
		try {
			//Connect to database
			dbConnect();
			//Create Statement
			stmt = conn.createStatement();
			//Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlstmt);
		} catch (SQLException e) {
			System.out.println("Problem occurred with execute statement...");
			throw e;
		} finally {
			if (stmt != null) {
				//Close statement
				stmt.close();
			}
			//Close connection
			dbDisconnect();
		}
	}
	
	//Need to be able to check if statement number has already been entered into the datebase, if it has dont add again.
	public static int dbCheckTable(int stmtNum) throws ClassNotFoundException {
		Statement stmt = null;
		ResultSet resultSet = null;
		String sqlstmt = "SELECT count(*) AS total FROM transactions\n " + "WHERE stmt = " + stmtNum;
		try {
			dbConnect();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sqlstmt);
			resultSet.next();
			return resultSet.getInt("total");
		} catch (SQLException e) {
			System.out.println("Error: Checking the table has not worked");
		}
		System.out.println("Success");
		return 0;
	}
	
	/*
	 * Used to convert the String date that is pulled from database into a MonthDay so we can then
	 * create a Transaction.
	 * 
	 * @param String s to convert into type MonthDay.
	 * @return Returns the converted Monthday.
	 * 
	 */
	public static MonthDay toMonthDay(String s) {
		s = s.toLowerCase();
		
		//Split the day and month up
		String x = s.substring(3);
		int day = Integer.parseInt(s.substring(0, 2));
		//Initialize month
		int month = 0;
		//Figure out the numeric representation of the month
		switch(x) {
		case "jan":
			month = 1;
			break;
		case "feb":
			month = 2;
			break;
		case "mar":
			month = 3;
			break;
		case "apr":
			month = 4;
			break;
		case "may":
			month = 5;
			break;
		case "jun":
			month = 6;
			break;
		case "jul":
			month = 7;
			break;
		case "aug":
			month = 8;
			break;
		case "sep":
			month = 9;
			break;
		case "oct":
			month = 10;
			break;
		case "nov":
			month = 11;
			break;
		case "dec":
			month = 12;
			break;
		}
		//Return MonthDay
		return MonthDay.of(month, day);
	}
	
	
     
}