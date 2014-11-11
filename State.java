import java.io.File;
import java.io.FileWriter;

public class State {
	char[][] state;
	File edFile;
	FileWriter edWriter;


	public State(String fileName, boolean isEncrypt){
		this.state = new char[4][4];

		try {
			if (isEncrypt) {
				this.edFile = new File(fileName + ".enc");
			} else {
				this.edFile = new File(fileName + ".dec");
			}
			String name = this.edFile.getName();

			if (this.edFile.exists()) {
				this.edFile.delete();
				this.edFile.createNewFile();
				this.edFile = new File(name);
				// TODO: may need to create another file object
			}
		} catch (Exception e) {
			System.out.println("This is getting pretty annoying.");
		}

		try {
			this.edWriter = new FileWriter(this.edFile, true);
		} catch (Exception e) {
			System.out.println("Could not make and write to a new file for some reason.");
		}
	}

	public void writeToFile(){
		for(int row = 0; row < 4; row++) {
	      	for(int col = 0; col < 4; col++) {
	         	
	         	try {
	         		// System.out.println(String.format("%h", this.state[row][col]));
	         		String hex = String.format("%h", this.state[row][col]);
	         		if (hex.length() < 2) {
	         			hex = "0" + hex;
	         		}
	   				this.edWriter.write(hex);
	   				System.out.printf(hex + " ");
	   				this.edWriter.flush();
	   			} catch (Exception e) {
	   				System.out.println("I seriously don't remember java being this annoying.");
	   			}	
	      	}

	      	System.out.println();
	   	}
	   	System.out.println();
	   	try {
	   		this.edWriter.write('\n');
	   	} catch (Exception e) {
	   		System.out.println("I seriously don't remember java being this annoying.");
	   	}
	}

	// always overrides the current state. 
	// will return null if any non-hex values are present. 
	// pads lines too small and will read only a max of 32 characters
	public void readLine(String line) {
		if (line.length() < 32) {
			while (line.length() < 32) {
				line += "0";
			}
		}

		int begin = 0;
		int end = 2;

		
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				// int val = 0;
				String hexVal = line.substring(begin, end);
				hexVal = "0x" + hexVal;
				char val; 
				try {
					val = (char) Integer.decode(hexVal).intValue();
				} catch (Exception e) {
					// System.out.println("Error: Key has non hex characters.");
					this.state = null;
					return;
				}

				this.state[row][col] = val;
				begin += 2;
				end += 2;
			}
		}
	}

	public boolean isValid() {
		if (this.state == null) {
			return false;
		}
		return true;
	}

	public char[][] getState() {
		return this.state;
	}
}
