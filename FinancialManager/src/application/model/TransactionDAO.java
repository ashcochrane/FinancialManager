package application.model;

import java.sql.SQLException;

import application.util.DBUtil;

public class TransactionDAO {

	/*
	 * Deletes an entire bank statement out of the database
	 *
	 * @param stmtNumber is the bank statement to delete.
	 */
	public static void deleteStmt(int stmtNumber) throws SQLException, ClassNotFoundException {
		String sql = "DELETE FROM transactions\n" + "WHERE stmt = " + stmtNumber + ";";
		System.out.println(sql);

		// Execute dbUtil ExecuteUpdate method
		try {
			DBUtil.dbExecuteUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error executing statement to insert transactions into table");
			throw e;
		}
	}
	/*
	 * Clears the entire transaction Database.
	 *
	 */
	public static void clearTable() throws SQLException, ClassNotFoundException {
		String sql = "DELETE FROM transactions;";
		// Execute dbUtil ExecuteUpdate method
		try {
			DBUtil.dbExecuteUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error executing statement to insert transactions into table");
			throw e;
		}
	}

	/*
	 * Inserts a specfic transaction into the database.
	 * 
	 * @param stmtNumber is the statement number of the uploaded bank statement.
	 * @param date is the date of the given transaction.
	 * @param amount is the amount of the given transaction.
	 */
	public static void insertTrans(int stmtNumber, String date, String amount) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO transactions\n" + "(stmt, date, amount)\n" + "VALUES\n" + "('" + stmtNumber + "', '"
				+ date + "', '" + Float.parseFloat(amount) + "')";

		// Execute dbUtil ExecuteUpdate method
		try {
			DBUtil.dbExecuteUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error executing statement to insert transactions into table");
			throw e;
		}
	}
	
	public static void extractTrans() {
		
	}

}
