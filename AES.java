import java.io.File;
import java.util.Scanner;

public class AES{
	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("Not enough arguments. Form should be: java AES e/d keyFile plainTextFile");
			return;

		} else if (args.length > 3) {
			System.out.println("Too many arguments. Form should be: java AES e/d keyFile plainTextFile");
			return;
		}

		String argument = args[0];
		String keyFileName = args[1];
		String plainTextName = args[2];

		File keyFile = new File(keyFileName);
		if (!keyFile.exists()) {
			System.out.println("The given key file does not exist in this directory.");
			return;
		}

		if (keyFile.length() < 32) {
			System.out.println("Key size too small. Needs to be 32 hex characters  (128 bits) in hex format.");
			return;
		}

		KeyFunctions key = new KeyFunctions(keyFile);

		if (key.isInvalid()) {
			System.out.println("Error: Key has non hex characters.");
			return;
		}
		

		File plainTextFile = new File(plainTextName);
		if (!plainTextFile.exists()) {
			System.out.println("The given plain text file does not exist in this directory.");
			return;
		}

		key.initializeKey();

		// key.printKey();
		
		Scanner readPT;
		try {
			readPT = new Scanner(plainTextFile);
		} catch (Exception e) {
			System.out.println("The given plain text file does not exist in this directory.");
			return;
		}

		long startTime = System.nanoTime();
		int howManyLinesEncrypted = 0;

		if (argument.equalsIgnoreCase("-e")) {
			State currentState = new State(plainTextName, true);

			while (readPT.hasNextLine()) {
				// reads in the line, checks if it is valid, and stores into the state
				currentState.readLine(readPT.nextLine());

				if (currentState.isValid()) {
					howManyLinesEncrypted++;
					char[][] stateArray = currentState.getState();
					key.addRoundKey(stateArray, 0);

					for (int round = 1; round < 10; round++) {

						//sub in values of the array
						for (int i = 0; i < 4; i++) {
							SBox.subBytes(stateArray, i);
						}
						shiftRows(stateArray);
						// mixed columns
						for (int col = 0; col < 4; col++) {
							MixedColumns.mixColumn2(stateArray, col);
						}
						// add round key
						key.addRoundKey(stateArray, round);
						
					}

					// sub bytes
					for (int i = 0; i < 4; i++) {
						SBox.subBytes(stateArray, i);
					}
					// shift rows
					shiftRows(stateArray);

					// add round key
					key.addRoundKey(stateArray, 10);
					currentState.writeToFile();

				}// end if. Do nothing is this conditional is not met
			} 

		} else if (argument.equalsIgnoreCase("-d")) {
			State currentState = new State(plainTextName, false);
			while (readPT.hasNextLine()) {
				currentState.readLine(readPT.nextLine());

				if(currentState.isValid()) {
					howManyLinesEncrypted++;

					char[][] stateArray = currentState.getState();

					key.addRoundKey(stateArray, 10);
					invShiftRows(stateArray);
					for (int i = 0; i < 4; i++) {
						SBox.invSubBytes(stateArray, i);
					}

					for (int round = 9; round > 0; round--) {
						key.addRoundKey(stateArray, round);
						for (int col = 0; col < 4; col++) {
							MixedColumns.invMixColumn2(stateArray, col);
						}
						invShiftRows(stateArray);
						for (int i = 0; i < 4; i++) {
							SBox.invSubBytes(stateArray, i);
						}
					}

					key.addRoundKey(stateArray, 0);
					currentState.writeToFile();
				}
			}

		} else {
			System.out.println("Ivalid flag or not flag not in proper position.");
		}

		long endTime = System.nanoTime();
		double time = (endTime - startTime) / 1000000000.0;
		double megaBytes = (howManyLinesEncrypted * 16) / 1000000.0;
		double throughput = megaBytes / time;
		System.out.printf("\nThroughput: %.3f MB/s\n", throughput);

	}

	private static void shiftRows(char[][] stateArray) {

		// shift row 1
		char val1 = stateArray[1][0];
		char val2 = stateArray[1][1];
		char val3 = stateArray[1][2];
		char val4 = stateArray[1][3];

		stateArray[1][0] = val2;
		stateArray[1][1] = val3;
		stateArray[1][2] = val4;
		stateArray[1][3] = val1;

		// shift row 2
		val1 = stateArray[2][0];
		val2 = stateArray[2][1];
		val3 = stateArray[2][2];
		val4 = stateArray[2][3];

		stateArray[2][0] = val3;
		stateArray[2][1] = val4;
		stateArray[2][2] = val1;
		stateArray[2][3] = val2;

		// shift row 3
		val1 = stateArray[3][0];
		val2 = stateArray[3][1];
		val3 = stateArray[3][2];
		val4 = stateArray[3][3];

		stateArray[3][0] = val4;
		stateArray[3][1] = val1;
		stateArray[3][2] = val2;
		stateArray[3][3] = val3;
	}

	private static void invShiftRows(char[][] stateArray) {
		// shift row 1
		char val1 = stateArray[1][0];
		char val2 = stateArray[1][1];
		char val3 = stateArray[1][2];
		char val4 = stateArray[1][3];

		stateArray[1][0] = val4;
		stateArray[1][1] = val1;
		stateArray[1][2] = val2;
		stateArray[1][3] = val3;

		// shift row 2
		val1 = stateArray[2][0];
		val2 = stateArray[2][1];
		val3 = stateArray[2][2];
		val4 = stateArray[2][3];

		stateArray[2][0] = val3;
		stateArray[2][1] = val4;
		stateArray[2][2] = val1;
		stateArray[2][3] = val2;

		// shift row 3
		val1 = stateArray[3][0];
		val2 = stateArray[3][1];
		val3 = stateArray[3][2];
		val4 = stateArray[3][3];

		stateArray[3][0] = val2;
		stateArray[3][1] = val3;
		stateArray[3][2] = val4;
		stateArray[3][3] = val1;
	}
}