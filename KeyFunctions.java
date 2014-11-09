import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.lang.Integer;

public class KeyFunctions {
	// for this program, we are assuming the key is a 4x4 array
	private int[][] key;

	public KeyFunctions (File keyFile) {
		try {
			this.key = readFile(keyFile);
		} catch (Exception e) {
			
		}
	}

	private int[][] readFile (File keyFile) throws Exception{

		// http://en.wikipedia.org/wiki/Rijndael_key_schedule#Constants
		// says the num of rows is 16 for 128 bit key
		int[][] newKey = new int[4][16];

		Scanner fileScanner;

		try {
			fileScanner = new Scanner(keyFile);
		} catch (Exception e) {
			// the file is checked before this point, so this should never execute.
			System.out.println("FileInputStream threw an error inside readFile().");
			return null;
		}

		String hexLine = fileScanner.nextLine();

		if (hexLine.length() < 32) {
			System.out.println("Error: Key is too small.")
			return null;
		}
		int begin = 0;
		int end = 2;

		// TODO: take the line, read two chars at a time (string of size 2)
		// use Integer.decode(string)
		// if it throws an error, it's a bad line since it's not hex
		outerLoop:
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				// int val = 0;
				String hexVal = hexLine.substring(begin, end);
				int val; 
				try {
					val = Integer.decode(hexVal);
				} catch (Exception e) {
					System.out.println("Error: Key has non hex characters.");
					return null;
				}

				// val = (firstHalf << 4) | secondHalf;
				this.key[row, col] = val;
				begin += 2;
				end += 2;
			}
		}

		return newKey;
	}

	// TODO: need to test this
	public boolean isInvalid() {
		if (this.key == null) {
			return true;
		}

		return false;
	}
}