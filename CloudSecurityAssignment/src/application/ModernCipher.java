package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ModernCipher {
	private static final String masterAlgorithm = "DES";
	private static final String masterKey = "asdfasdf";
	private String key;
	private String algorithm;
	
	public ModernCipher(String algorithm, String key) {
		this.key = key;
		this.algorithm = algorithm;
	}
	
	public SecretKey randomKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		return keyGen.generateKey();
	}
	
	public String secretKeyToString(SecretKey secretKey) {
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}
	
	public String encryptText(String text) throws Throwable {
		return encryptDecryptText(Cipher.ENCRYPT_MODE, text);
	}
	
	public String decryptText(String text) throws Throwable {
		return encryptDecryptText(Cipher.DECRYPT_MODE, text);
	}

	public void encryptFile(String inputFileName, String outputFileName) {
		try {
			FileInputStream inputFileStream = new FileInputStream(inputFileName);
			FileOutputStream outputFileStream = new FileOutputStream(outputFileName);
			encryptDecryptFile(Cipher.ENCRYPT_MODE, inputFileStream, outputFileStream);
		}
		catch (Throwable exception) {
			exception.printStackTrace();
		}
	}
	
	public void decryptFile(String inputFileName, String outputFileName) {
		try {
			FileInputStream inputFileStream = new FileInputStream(inputFileName);
			FileOutputStream outputFileStream = new FileOutputStream(outputFileName);
			encryptDecryptFile(Cipher.DECRYPT_MODE, inputFileStream, outputFileStream);
		}
		catch (Throwable exception) {
			exception.printStackTrace();
		}
	}
	
	private void encryptDecryptFile(int mode, FileInputStream input, FileOutputStream output) throws Throwable {
//		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
//		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
//		SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
		SecretKey secretKey = new SecretKeySpec(key.getBytes(),algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		
		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			CipherInputStream cipherInput = new CipherInputStream(input, cipher);
			copyStream(cipherInput, output);
		}
		else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher);
			copyStream(input, cipherOutput);
		}
	}
	
	private String encryptDecryptText(int mode, String text) throws Throwable {
		SecretKey secretKey = new SecretKeySpec(key.getBytes(),algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		
		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(encryptedBytes);
		}
		else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(text));
	        return new String(decryptedBytes, StandardCharsets.UTF_8);
		}
		throw new Throwable("not encrypting or decrypting?");
	}
	
	private String masterEncryptDecryptText(int mode, String text) throws Throwable {
		SecretKey secretKey = new SecretKeySpec(masterKey.getBytes(), masterAlgorithm);
		Cipher cipher = Cipher.getInstance(masterAlgorithm);
		
		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(encryptedBytes);
		}
		else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(text));
	        return new String(decryptedBytes, StandardCharsets.UTF_8);
		}
		throw new Throwable("not encrypting or decrypting?");
	}
	
	private void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes = input.read(bytes);
		while (numBytes != -1) {
			output.write(bytes, 0, numBytes);
			numBytes = input.read(bytes);
		}
		output.flush();
		output.close();
		input.close();
	}
	
	public static void saveKeyToFile(String algorithm, String key, String fileName) throws IOException {
		File tempKeyFile = new File("keys/" + fileName + "_temp");
		tempKeyFile.createNewFile();
		File keyFile = new File("keys/" + fileName);
		keyFile.createNewFile();
		PrintStream keyPrinter = new PrintStream(tempKeyFile);
		keyPrinter.println(key);
		keyPrinter.println(algorithm);
		keyPrinter.close();
		
		ModernCipher cipher = new ModernCipher(masterAlgorithm, masterKey);
		cipher.encryptFile("keys/" + fileName + "_temp", "keys/" + fileName);
		tempKeyFile.delete();
	}
	
	public static Key loadKeyFromFile(String fileName) throws IOException {
		ModernCipher cipher = new ModernCipher(masterAlgorithm, masterKey);
		cipher.decryptFile("keys/" + fileName, "keys/" + fileName + "_temp");
		File rawKeyFile = new File("keys/" + fileName + "_temp");
		Scanner scanner = new Scanner (rawKeyFile);
		String keyString = scanner.nextLine();
		String algorithm = scanner.nextLine();
		scanner.close();
		rawKeyFile.delete();
		return new Key(keyString, algorithm);
	}
	
	public Key encryptKey() throws Throwable {
		String encryptedKey = masterEncryptDecryptText(Cipher.ENCRYPT_MODE, key);
		String encryptedAlgorithm = masterEncryptDecryptText(Cipher.ENCRYPT_MODE, algorithm);
		return new Key(encryptedKey, encryptedAlgorithm);
	}
	
	public String masterEncryptString(String text) throws Throwable {
		return masterEncryptDecryptText(Cipher.ENCRYPT_MODE, text);
	}
	
	public String masterDecryptString(String text) throws Throwable {
		return masterEncryptDecryptText(Cipher.DECRYPT_MODE, text);
	}
}
