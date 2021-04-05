/**
 * 
 */
package com.winnovature.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseManager {

	private static DataSource datasource;

	static {

		try {
			Context ctx = new InitialContext();
			// datasource = (DataSource) ctx.lookup("java:/comp/env/jdbc/WinDB");
			datasource = (DataSource) ctx.lookup("java:/comp/env/jdbc/WinDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static Connection getConnection() { Connection conn = null; try {
	 * Class.forName("com.mysql.jdbc.Driver"); String connectionUrl =
	 * "jdbc:mysql://localhost:3307/issuertollpay"; // data1";//netcdata1 // String
	 * // connectionUrl = "jdbc:mysql://192.168.1.99:3306/acqhost";
	 * 
	 * String dbUser = "root"; String dbPwd = "root"; conn =
	 * DriverManager.getConnection(connectionUrl, dbUser, dbPwd); } catch (Exception
	 * e) { e.printStackTrace(); } return conn; }
	 */

	public static Connection getConnection() throws SQLException {

		return (Connection) datasource.getConnection();
	}

	public static Connection getAutoCommitConnection() throws SQLException {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public static void closeConnection(Connection connection) {

		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean commitConnection(Connection connection) {

		try {
			if (connection != null) {
				connection.commit();
				connection.close();
				connection = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public static boolean rollbackConnection(Connection connection) {

		try {
			if (connection != null) {
				connection.rollback();
				connection.close();
				connection = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public static void closeResultSet(ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void closePreparedStatement(PreparedStatement ps) {

		try {
			if (ps != null) {
				ps.close();
				ps = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeStatement(Statement st) {

		try {
			if (st != null) {
				st.close();
				st = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeCallableStatement(CallableStatement cs) {

		try {

			if (cs != null) {
				cs.close();
				cs = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
