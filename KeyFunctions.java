import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.lang.Integer;


// TODO: NEED TO TEST BEFORE WE MOVE ON.
public class KeyFunctions {
	// for this program, we are assuming the key is a 4x4 array
	private char[][] key;
	// private int round;

	public KeyFunctions (File keyFile) {
		try {
			this.key = readFile(keyFile);
			// this.round = 1;

		} catch (Exception e) {
			System.out.println("Error in reading the file. File may not exist or filepath may be wrong.");
			this.key = null;
		}
	}


	public void initializeKey() {
		// char[][] newKey = new char[4][4];
		int startOfMatrix = 4;
		for (int round = 1; round < 11; round++) {
			// this is the RotWord step
			this.key[0][startOfMatrix] = this.key[1][startOfMatrix - 1];
			this.key[1][startOfMatrix] = this.key[2][startOfMatrix - 1];
			this.key[2][startOfMatrix] = this.key[3][startOfMatrix - 1];
			this.key[3][startOfMatrix] = this.key[0][startOfMatrix - 1];

			SBox.subBytes(this.key, startOfMatrix);	

			// System.out.println("After subs.");
			// for (int row = 0; row < 4; row++) {
			// 	for (int col = 0; col < 4; col++) {
			// 		System.out.printf("%h ", newKey[row][col]);
			// 	}
			// 	System.out.println();
			// }
			// System.out.println();

			// this is the Rcon addition step
			char rConVal = Rcon.getRconVal(round);
			this.key[0][startOfMatrix] = (char) (this.key[0][startOfMatrix] ^ rConVal);	

			// the name is misleading but all we're doing is adding the old and new matrices
			addNewMatrix(startOfMatrix); 

			startOfMatrix += 4;
			// this.key = newKey;

			// update to next round
			// round++;
		}
	}

	private void addNewMatrix(int startOfMatrix) {

		// // this is for the first column
		for (int row = 0; row < 4; row++) {
			this.key[row][startOfMatrix] = (char) (this.key[row][startOfMatrix] ^ this.key[row][startOfMatrix - 4]);
		}

		for (int col = startOfMatrix + 1; col < startOfMatrix + 4; col++) {
			for (int row = 0; row < 4; row++) {
				this.key[row][col] = (char) (this.key[row][col - 4] ^ this.key[row][col - 1]);
			}
		}
	}


	public void addRoundKey(char[][] matrix, int round) {
		int startOfKey = 4 * round;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				matrix[row][col] = (char) ((matrix[row][col]) ^ (this.key[row][startOfKey]));
				startOfKey++;
			}
			startOfKey = 4 * round;
		}
	}

	private char[][] readFile (File keyFile) throws Exception {

		char[][] newKey = new char[4][4 * 11];

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
			System.out.println("Error: Key is too small.");
			return null;
		}
		int begin = 0;
		int end = 2;

		
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				// int val = 0;
				String hexVal = hexLine.substring(begin, end);
				hexVal = "0x" + hexVal;
				char val; 
				try {
					val = (char) Integer.decode(hexVal).intValue();
				} catch (Exception e) {
					System.out.println("Error: Key has non hex characters.");
					return null;
				}

				newKey[row][col] = val;
				begin += 2;
				end += 2;
			}
		}

		return newKey;
	}

	public void printKey () {
		int start = 0;
		int end = 4;
		for (int i = 0; i < 11; i++) {
			for(int row = 0; row < 4; row++) {
	      		for(int col = start; col < start + 4; col++) {
	         		System.out.printf("%h ", this.key[row][col]);
	      		}

	      		System.out.println();
	   		}
	   		start += 4;
	   		System.out.println("\n");
	   	}
	   	
	}



	// TODO: need to test this
	public boolean isInvalid() {
		if (this.key == null) {
			return true;
		}

		return false;
	}
}