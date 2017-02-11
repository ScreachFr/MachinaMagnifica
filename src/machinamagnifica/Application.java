package machinamagnifica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Application {
	public static void main(String[] args) {
		File codex = new File("res/codex.umz");
		File sandMark = new File("res/sandmark.umz");
		if (!codex.exists()) {
			System.out.println("Failed to load codex.");
			return;
		} else {
			System.out.println("Codex found.");
		}
		
		try {
			
			
//			FileInputStream fr = new FileInputStream(codex);
			FileInputStream fr = new FileInputStream(sandMark);
			
			MachinaMagnifica mm = new MachinaMagnifica();

			mm.run(fr);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
