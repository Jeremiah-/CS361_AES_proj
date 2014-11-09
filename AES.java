import java.io.File;

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
		String plainTextFile = args[2];

		File keyFile = new File(keyFileName);
		if (!keyFile.exists()) {
			System.out.println("The given key file does not exist in this directory.");
			return;
		}

		// TODO: is this what they want? Make sure if they want output.
		if (keyFile.length() < 32) {
			System.out.println("Key size too small. Needs to be 32 hex characters  (128 bits) in hex format.");
			return;
		}


		KeyFunctions key = new KeyFunctions(keyFile);

		if (key.isInvalid()) {
			System.out.println("Error: Key has non hex characters.");
			return;
		}

		
		
		// initialize the round key
	}
}