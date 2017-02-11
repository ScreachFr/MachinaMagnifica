package machinamagnifica;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Application {
	public static void main(String[] args) {
		File codex = new File("res/codex.umz");
		
		if (!codex.exists()) {
			System.out.println("Failed to load codex.");
			return;
		} else {
			System.out.println("Codex found.");
		}
		
		try {
			FileReader fr = new FileReader(codex);
			MachinaMagnifica mm = new MachinaMagnifica();

			mm.run(fr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
