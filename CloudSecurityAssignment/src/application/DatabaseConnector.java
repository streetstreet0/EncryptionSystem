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
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;

//import com.google.api.core.ApiFuture;
//import com.google.api.gax.paging.Page;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.DocumentReference;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.WriteResult;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.FirestoreClient;
//import com.google.common.collect.Lists;


public class DatabaseConnector {
	private static final String jsonPath = "firebase.json";
	private static final String firebaseId = "cloudsecurityassignment";
	
//	private static Firestore connectToFirebase() throws IOException {
//		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath)).createScoped(Lists.newArrayList("https:/www.googleapis.com/auth/cloud-platform"));
//		FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).setProjectId(firebaseId).build();
//		
//		return FirestoreClient.getFirestore();
//	}
//	
//	public static void testFirebase() {
//		try {
//			Firestore database = connectToFirebase();
//			
//			DocumentReference documentRef = database.collection("testing").document("testDoc");
//			
//			HashMap<String, Object> data = new HashMap<String, Object>();
//			data.put("test", "success?");
//			
////			ApiFuture<WriteResult> result = documentRef.set(data);
////			result.get();
//			documentRef.set(data).get();
//			System.out.println("saved successfully");
//			
////			System.out.println("Update time: " + result.get().getUpdateTime());
//		}
//		catch (Exception exception) {
//			exception.printStackTrace();
//		}
//	}
	
	private static Connection connectToDatabase() throws ClassNotFoundException, SQLException {
		// register the jdbc driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		// open a connection
		return DriverManager.getConnection("jdbc:mysql://127.0.0.1/encryptUsers", "root", "");
	}
	
	public static boolean validUser(String username, String password) throws Exception {
		try {
			Connection connection = connectToDatabase();
			
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
			Connection connection = connectToDatabase();
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
	
	public static void saveBackgroundColourToDatabase(String username, Color colour) throws SQLException, ClassNotFoundException, Throwable {
		String colourString = colour.getRed() + ":" + colour.getGreen() + ":" + colour.getBlue() + ":" + colour.getOpacity();
		saveBackgroundToDatabase(username, colourString);
	}
	
	public static void saveBackgroundImageToDatabase(String username) throws SQLException, ClassNotFoundException, Throwable {
		saveBackgroundToDatabase(username, "wooper.png");
	}
	
	private static void saveBackgroundToDatabase(String username, String backgroundString) throws SQLException, ClassNotFoundException, Throwable {
		ModernCipher cipher = new ModernCipher(null, null);
		String encryptedUser = hashPassword(username);
		String encryptedBackgroundString = cipher.masterEncryptString(backgroundString);
		
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `settings` WHERE `username`=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		ResultSet results = selectStatement.executeQuery();
		
		
		if (!results.next()) {
			sql = "INSERT INTO `settings` (`background`, `username`) VALUES (?, ?);";
		}
		else {
			sql = "UPDATE `settings` SET `background`=? WHERE `username`=?;";
		}
		results.close();
		selectStatement.close();
		PreparedStatement updateStatement = connection.prepareStatement(sql);
		updateStatement.setString(1, encryptedBackgroundString);
		updateStatement.setString(2, encryptedUser);
		updateStatement.executeUpdate();	
		
		
		// close external resourses
		updateStatement.close();
		connection.close();
	}
	
	public static Background loadBackgroundFromDatabase(String username) throws SQLException, ClassNotFoundException, Throwable {
		Background background = null;
		ModernCipher cipher = new ModernCipher("", "");
		String encryptedUser = hashPassword(username);
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `settings` WHERE `username`=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		ResultSet results = selectStatement.executeQuery();
		
		if (results.next()) {
			String encryptedBackgroundString = results.getString("background");
			String backgroundString = (cipher.masterDecryptString(encryptedBackgroundString));
			String[] backgroundStringSplit = backgroundString.split(":");
			if (backgroundStringSplit.length == 1) {
				background = new Background(new BackgroundImage(new Image(new FileInputStream("images/wooper.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null));
			}
			else {
				double red = Double.parseDouble(backgroundStringSplit[0]);
				double green = Double.parseDouble(backgroundStringSplit[1]);
				double blue = Double.parseDouble(backgroundStringSplit[2]);
				double opacity = Double.parseDouble(backgroundStringSplit[3]);
				Color colour = new Color(red, green, blue, opacity);
				background = new Background(new BackgroundFill(colour, null, null));
			}
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
		if (background == null) {
			throw new Throwable("No saved settings");
		}
		return background;
	}
	
	public static void saveKeyToDatabase(String algorithm, String key, String username) throws SQLException, ClassNotFoundException, Throwable {
		ModernCipher cipher = new ModernCipher(algorithm, key);
		Key encryptedKeyAlgorithm = cipher.encryptKey();
		String encryptedUser = hashPassword(username);
		String encryptedKey = encryptedKeyAlgorithm.getKey();
		String encryptedAlgorithm = encryptedKeyAlgorithm.getAlgorithm();
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `cipherKeys` WHERE `username`=? AND `cipherKey`=? AND algorithm=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		selectStatement.setString(2, encryptedKey);
		selectStatement.setString(3, encryptedAlgorithm);
		ResultSet results = selectStatement.executeQuery();
		
		if (!results.next()) {
			sql = "INSERT INTO `cipherKeys` (`username`, `cipherKey`, `algorithm`) VALUES (?, ?, ?);";
			PreparedStatement updateStatement = connection.prepareStatement(sql);
			updateStatement.setString(1, encryptedUser);
			updateStatement.setString(2, encryptedKey);
			updateStatement.setString(3, encryptedAlgorithm);
			updateStatement.executeUpdate();
			
			updateStatement.close();
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
	}
	
	public static ArrayList<String> loadKeysFromDatabase(String algorithm, String username) throws SQLException, ClassNotFoundException, Throwable {
		ArrayList<String> keys = new ArrayList<String>();
		ModernCipher cipher = new ModernCipher(algorithm, "");
		String encryptedUser = hashPassword(username);
		Key encryptedKeyAlgorithm = cipher.encryptKey();
		String encryptedAlgorithm = encryptedKeyAlgorithm.getAlgorithm();
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `cipherKeys` WHERE `username`=? AND algorithm=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		selectStatement.setString(2, encryptedAlgorithm);
		ResultSet results = selectStatement.executeQuery();
		
		while (results.next()) {
			String encryptedKey = results.getString("cipherKey");
			keys.add(cipher.masterDecryptString(encryptedKey));
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
		if (keys.size() == 0) {
			throw new Throwable("No keys");
		}
		return keys;
	}
	
	public static void saveMessageToDatabase(String algorithm, String key, String username, String encryptedMessage) throws SQLException, ClassNotFoundException, Throwable {
		ModernCipher cipher = new ModernCipher(algorithm, key);
		Key encryptedKeyAlgorithm = cipher.encryptKey();
		String encryptedUser = hashPassword(username);
		String encryptedKey = encryptedKeyAlgorithm.getKey();
		String encryptedAlgorithm = encryptedKeyAlgorithm.getAlgorithm();
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `messages` WHERE `username`=? AND `cipherKey`=? AND algorithm=? AND message=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		selectStatement.setString(2, encryptedKey);
		selectStatement.setString(3, encryptedAlgorithm);
		selectStatement.setString(4, encryptedMessage);
		ResultSet results = selectStatement.executeQuery();
		
		if (!results.next()) {
			sql = "INSERT INTO `messages` (`username`, `cipherKey`, `algorithm`, `message`) VALUES (?, ?, ?, ?);";
			PreparedStatement insertStatement = connection.prepareStatement(sql);
			insertStatement.setString(1, encryptedUser);
			insertStatement.setString(2, encryptedKey);
			insertStatement.setString(3, encryptedAlgorithm);
			insertStatement.setString(4, encryptedMessage);
			insertStatement.executeUpdate();
			
			insertStatement.close();
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
	}
	
	public static ArrayList<String> loadMessagesFromDatabase(String username) throws SQLException, ClassNotFoundException, Throwable {
		ArrayList<String> messages = new ArrayList<String>();
		String encryptedUser = hashPassword(username);
		
		Connection connection = connectToDatabase();
		
		// execute a query
		String sql = "SELECT * FROM `messages` WHERE `username`=?;";
		PreparedStatement selectStatement = connection.prepareStatement(sql);
		selectStatement.setString(1, encryptedUser);
		ResultSet results = selectStatement.executeQuery();
		
		while (results.next()) {
			ModernCipher cipher = new ModernCipher(null, null);
			String key = cipher.masterDecryptString(results.getString("cipherKey"));
			String algorithm = cipher.masterDecryptString(results.getString("algorithm"));
			cipher = new ModernCipher(algorithm, key);
			messages.add(cipher.decryptText(results.getString("message")));
		}
		
		// close external resourses
		results.close();
		selectStatement.close();
		connection.close();
		
		return messages;
	}
}
