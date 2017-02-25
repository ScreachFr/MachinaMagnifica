package machinamagnifica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Application {

	private final static String HELP = "Usage : <cmd> [OPTION]... [FICHIER]\n" +
			"Exemple d'utilisation : <cmd> -l logs.txt sandmark.umz\n" +
			"-l <fichier> : active les logs. Attention le fichier peut être volumineux!\n" +
			"-h : affiche ce que vous êtes en train de lire.\n\n"
			+ "Note : Le nom du fichier à interpreter doit se trouver à la fin.";


	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Erreur, pas assez d'arguments.");
			System.out.println(HELP);
			return;
		}

		String logPath = null;
		File file;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-h":
				System.out.println(HELP);
				return;
			case "-l":
				if (args.length < 3) {
					System.out.println("Erreur pas assez d'arguments.");
					System.out.println(HELP);
					return;
				}
					
				logPath = args[i+1];
				break;
			}
		}


		try {
			file = new File(args[args.length-1]);
			FileInputStream inputStream;
			
			
			if (!file.exists()) {
				System.out.println(file.getName() + " n'existe pas.");
				return;
			}
			
			inputStream = new FileInputStream(file);


			MachinaMagnifica mm;
			PrintStream logs = null;
			
			
			if (logPath != null) {
				logs = new PrintStream(new File(logPath));
				mm = new MachinaMagnifica(inputStream, logs);
			} else {
				mm = new MachinaMagnifica(inputStream); 
			}

			mm.loadProgramFromStream();
			mm.run();
			
			inputStream.close();
			
			if (logPath != null)
				logs.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}




	}
}
