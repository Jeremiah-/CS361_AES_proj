import java.io.File;

public class KeyFunctions {
	// for this program, we are assuming the key is a 4x4 array
	private int[][] key;

	public KeyFunctions (File keyFile) {
		
		this.key = readFile(keyFile);
	}

	private int[][] readFile (File keyFile) {
		int[][] newKey = new int[4][4];

		FileInputStream fileScanner = new FileInputStream(keyFile);

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (fileScanner.available < 1) {
					break;
				}
				char val = (char) fileScanner.read();

				if (val >= '0' && val <= '9') {

				} else if (val >= 'A' && val <= 'F') {

				} else if (val >= 'a' && val <= 'f') {

				} else {
					// TODO: what should happen here?

				}
			}
		}

		return newKey;
	}
}