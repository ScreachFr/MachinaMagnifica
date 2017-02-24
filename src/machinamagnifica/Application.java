package machinamagnifica;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

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
			
			PrintStream logs = new PrintStream(new File("log.txt"));
//			FileInputStream fr = new FileInputStream(codex);
			FileInputStream fr = new FileInputStream(sandMark);
			
			MachinaMagnifica mm = new MachinaMagnifica(fr, logs);
			mm.loadProgramFromStream();
			mm.run();
			
			logs.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
