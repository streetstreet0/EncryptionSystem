package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class KeyStorer {

	public KeyStorer() {
		// TODO Auto-generated constructor stub
	}
	
	public void secretKeyStream() {
		try {
			FileInputStream secretKeyStream = new FileInputStream(new File("icon.png"));
			ArrayList<Byte> secretKey = new ArrayList<Byte>();
			ArrayList<Byte> decryptedKey = new ArrayList<Byte>();
			while (secretKeyStream.available() > 0) {
				byte keyByte = (byte)secretKeyStream.read() ;
				secretKey.add(keyByte);
				decryptedKey.add((byte)(keyByte ^ keyByte));
				System.out.println(secretKey.get(secretKey.size()-1));
				System.out.println(decryptedKey.get(decryptedKey.size()-1));
			}
			secretKeyStream.close();
		}
		catch (IOException exception) {
			System.out.println("cannot find file");
		}
	}

}
