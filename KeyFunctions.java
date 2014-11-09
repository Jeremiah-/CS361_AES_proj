import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.lang.Integer;


// TODO: NEED TO TEST BEFORE WE MOVE ON.
public class KeyFunctions {
	// for this program, we are assuming the key is a 4x4 array
	private byte[][] key;
	private int round;

	public KeyFunctions (File keyFile) {
		try {
			this.key = readFile(keyFile);
			this.round = 1;

		} catch (Exception e) {
			System.out.println("Error in reading the file. File may not exist or filepath may be wrong.");
			this.key = null;
		}
	}


	public void newRoundKey(boolean isInitial) {
		byte[][] newKey = new byte[4][4];

		// copy over the old values of the old key
		for (int i = 0; i < 4; i++) {
			newKey[i] = this.key[i].clone();
		}

		// this is the RotWord step
		byte temp = newKey[4][4];
		newKey[4][4] = newKey[1][4];
		newKey[1][4] = temp;

		// this is the Rcon addition step
		byte rConVal = Rcon.getRconVal(round);
		newKey[1][4] = (byte) (newKey[1][4] ^ rConVal);

		// now begins the key transformation
		SBox.subBytes(newKey);
		addColumns(newKey);

		this.key = newKey;

		// update to next round
		this.round++;
	}

	private void addColumns(byte[][] nk) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				nk[row][col] = (byte) ((nk[row][col]) ^ (this.key[row][col]));
			}
		}
	}


	private byte[][] readFile (File keyFile) throws Exception {

		byte[][] newKey = new byte[4][4];

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

		
		outerLoop:
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				// int val = 0;
				String hexVal = hexLine.substring(begin, end);
				byte val; 
				try {
					val = (byte) Integer.decode(hexVal).intValue();
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

	// TODO: need to test this
	public boolean isInvalid() {
		if (this.key == null) {
			return true;
		}

		return false;
	}
}