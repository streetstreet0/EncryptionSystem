package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseConnector {
	
	public static boolean validUser(String username, String password) throws Exception {
		try {
			// register the jdbc driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// open a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/encryptUsers", "root", "");
			
			// execute a query
			String sql = "SELECT * FROM `Users` WHERE `username`=? AND `password`=?;";
			PreparedStatement selectStatement = connection.prepareStatement(sql);
			selectStatement.setString(1, username);
			selectStatement.setString(2, hashPassword(password));
			ResultSet results = selectStatement.executeQuery();
			
			boolean validUser = results.next();
			// close external resourses
			results.close();
			selectStatement.close();
			connection.close();
			
			return validUser;
		}
		catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}
		throw new Exception("could not connect to database");
	}
	
	public static void createNewUser(String username, String password) {
		try {
			// register the jdbc driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// open a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/encryptUsers", "root", "");
			
			// execute a query
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO `Users` (`username`, `password`) VALUES (?, ?)";
			PreparedStatement Insertstatement = connection.prepareStatement(sql);
			Insertstatement.setString(1, username);
			Insertstatement.setString(2, hashPassword(password));
			Insertstatement.executeUpdate();
			
			// close external resourses
			statement.close();
			connection.close();
		}
		catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		}
	}
	
	private static String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest MD5algorithm = MessageDigest.getInstance("MD5");
		MD5algorithm.update(password.getBytes());
		byte[] hashedBytes = MD5algorithm.digest();
		
		StringBuilder stringBuilder = new StringBuilder();
		for (byte hashedByte : hashedBytes) {
			stringBuilder.append(hashedByte);
		}
		
		return stringBuilder.toString();
	}
	
	public static void saveKeyToDatabase(String algorithm, String key, String username) throws SQLException, ClassNotFoundException, Throwable {
		ModernCipher cipher = new ModernCipher(algorithm, key);
		Key encryptedKeyAlgorithm = cipher.encryptKey();
		String encryptedKey = encryptedKeyAlgorithm.getKey();
		String encryptedAlgorithm = encryptedKeyAlgorithm.getAlgorithm();
		
		// register the jdbc driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		// open a connection
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/encryptUsers", "root", "");
		
		// execute a query
		String sql = "SELECT * FROM `cipherKeys` WHERE `username`=? AND `cipherKey`=? AND algorithm=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, username);
		selectStatement.setString(2, encryptedKey);
		selectStatement.setString(3, encryptedAlgorithm);
		ResultSet results = selectStatement.executeQuery();
		
		if (!results.next()) {
			sql = "INSERT INTO `cipherKeys` (`username`, `cipherKey`, `algorithm`) VALUES (?, ?, ?);";
			PreparedStatement insertStatement = connection.prepareStatement(sql);
			insertStatement.setString(1, username);
			insertStatement.setString(2, encryptedKey);
			insertStatement.setString(3, encryptedAlgorithm);
			insertStatement.executeUpdate();
			
			insertStatement.close();
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
	}
	
	public static ArrayList<String> loadKeysFromDatabase(String algorithm, String username) throws SQLException, ClassNotFoundException, Throwable {
		ArrayList<String> keys = new ArrayList<String>();
		ModernCipher cipher = new ModernCipher(algorithm, "");
		Key encryptedKeyAlgorithm = cipher.encryptKey();
		String encryptedAlgorithm = encryptedKeyAlgorithm.getAlgorithm();
		
		// register the jdbc driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		// open a connection
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/encryptUsers", "root", "");
		
		// execute a query
		String sql = "SELECT * FROM `cipherKeys` WHERE `username`=? AND algorithm=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, username);
		selectStatement.setString(2, encryptedAlgorithm);
		ResultSet results = selectStatement.executeQuery();
		
		while (results.next()) {
			String encryptedKey = results.getString("cipherKey");
			keys.add(cipher.decryptKey(encryptedKey));
		}
		
		if (keys.size() == 0) {
			throw new Throwable("No keys");
		}
		return keys;
	}
}
