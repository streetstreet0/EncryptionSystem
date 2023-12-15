package application;

public class Key {
	private String key;
	private String algorithm;
	
	public Key(String key, String algorithm) {
		this.key = key;
		this.algorithm = algorithm;
	}
	
	public String getKey() {
		return key;
	}
	public String getAlgorithm() {
		return algorithm;
	}
}
