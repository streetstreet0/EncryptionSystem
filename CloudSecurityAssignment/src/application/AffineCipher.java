package application;

public class AffineCipher {
	private int multKey;
	private int addKey;
	private int inverseMultKey;

	public AffineCipher(int multKey, int addKey) {
		this.addKey = addKey;
		this.multKey = multKey;
		this.inverseMultKey = calculateInverseMultKey();
	}
	
	private int calculateInverseMultKey() {
		for (int i=1; i<26; i++) {
			if ((i * multKey) % 26 == 1) {
				return i;
			}
		}
		return 0;
	}

	private char intToLetter(int letterNum) throws Exception {
		if (letterNum < 0 || letterNum > 25) {
			System.out.println(letterNum);
			throw new Exception("Not a capital letter, do not decrypt");
		}
		char letter = (char)('A' + letterNum);
//		System.out.println(letterNum + " to " + letter);
		return letter;
	}
	
	private int letterToInt(char letter) throws Exception {
		int charNum = ((int)letter) - ((int)'A');
		if (charNum < 0 || charNum > 25) {
//			System.out.println(letter + " unchanged (" + charNum + ")");
			System.out.println(letter);
			throw new Exception("Not a capital letter, do not encrypt");
		}
//		System.out.println(letter + " to " + charNum);
		return charNum;
	}
	
	private char decryptCharacter(char letter) {
		try {
			int letterNum = letterToInt(letter);
			letterNum = (((letterNum - addKey) * inverseMultKey)) % 26;
			while (letterNum < 0) {
				letterNum += 26;
			}
			return intToLetter(letterNum);
		}
		catch (Exception exception) {
			return letter;
		}
	}
	
	public String encryptString(String line) {
		char[] originalCharacters = line.toUpperCase().toCharArray();
		String encryptedLine = "";
		for (char character : originalCharacters) {
			encryptedLine += encryptCharacter(character);
		}
		return encryptedLine;
	}
	
	public String decryptString(String line) {
		char[] originalCharacters = line.toUpperCase().toCharArray();
		String encryptedLine = "";
		for (char character : originalCharacters) {
			encryptedLine += decryptCharacter(character);
		}
		return encryptedLine;
	}
	
	private char encryptCharacter(char letter) {
		try {
			int letterNum = letterToInt(letter);
			letterNum = ((multKey * letterNum) + addKey) % 26;
			return intToLetter(letterNum);
		}
		catch (Exception exception) {
			return letter;
		}
	}
}
