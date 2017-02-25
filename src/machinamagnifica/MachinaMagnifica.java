package machinamagnifica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MachinaMagnifica {
	private boolean DEBUG = false;
	private boolean VERBOSE = false;
	
	
	private final static int DEFAULT_NB_REGISTRE = 8;
	private final static int DEFAULT_MEMORY_SIZE = (int) Math.pow(2, 16);
	private final static int DEFAULT_LOAD_SIZE = 4; //J'avais pas d'autre nom en tête...

	private Registre[] registres;
	private Memory memoire;
	private PrintStream out;
	private InputStream in;

	private PrintStream logsOut;
	
	private FileInputStream inputFileReader;
	
	private int finger;
	
	private boolean isOn;

	public MachinaMagnifica(FileInputStream inputReader) {
		inputFileReader = inputReader;
		out = System.out;
		in = System.in;
		memoire = new Memory(DEFAULT_MEMORY_SIZE);
		registres = new Registre[DEFAULT_NB_REGISTRE];

		for (int i = 0; i < DEFAULT_NB_REGISTRE; i++) {
			registres[i] = new Registre();
		}

		isOn = true;
		DEBUG = false;
	}
	
	public MachinaMagnifica(FileInputStream inputReader, PrintStream logs) {
		this(inputReader);
		logsOut = logs;
		DEBUG = true;
	}
	
	public void loadProgramFromStream() {
		byte[] readerBuffer = new byte[DEFAULT_LOAD_SIZE];
		ArrayList<PlateauDeSable> programme = new ArrayList<>();
		PlateauDeSable crt;
		
		try {
			while (inputFileReader.read(readerBuffer) > 0) {
				crt = new PlateauDeSable();
				
				
				crt.setData(readerBuffer);
				programme.add(crt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		memoire.setProgramData(programme);
	}

	public void run() throws IOException {
		finger = 0;
		int op;
		PlateauDeSable crtInstruction;
		int[] reg;
		Registre a, b, c;
		int tour = 0;
		
		
		while (isOn) {
			crtInstruction = memoire.getData(0)[finger];
			op = crtInstruction.getOperator();
			reg = crtInstruction.getRegistres();
			a = registres[reg[0]];
			b = registres[reg[1]];
			c = registres[reg[2]];
			
			finger++;
			

			if (DEBUG) {
				logsOut.println("Tour -> " + tour);
				logsOut.println("Finger -> " + finger);
				logsOut.println("Plateau -> ");
				logsOut.println(crtInstruction + " : " + crtInstruction.toInt() + " : " + Integer.toHexString(crtInstruction.toInt()));
				logsOut.println("OP -> " + op);
				logsOut.println("Registres ->");
				logsOut.println(Arrays.toString(reg));
			}
			
			if (VERBOSE) {
				System.out.println(tour);
				System.out.println("OP -> " + op);
			}
			
			switch (op) {
			case 0:
				ifC(a, b, c);
				break;
			case 1:
				getOffset(a, b, c);
				break;
			case 2:
				arrayModif(a, b, c);
				break;
			case 3:
				add(a, b, c);
				break;
			case 4:
				mul(a, b, c);
				break;
			case 5:
				div(a, b, c);
				break;
			case 6:
				nand(a, b, c);
				break;
			case 7:
				stop();
				break;
			case 8:
				alloc(b, c);
				break;
			case 9:
				free(c);
				break;
			case 10:
				print(c);
				break;
			case 11:
				input(c);
				break;
			case 12:
				loadProgram(b, c);
				break;
			case 13:
				int value = crtInstruction.getSpecialValue();
				int spReg = crtInstruction.getSpecialRegistre();

				if (DEBUG)
					logsOut.println("Running special op 13 on registre " + spReg + " with value " + (char)value + "(" + value + ")");
				orthographe(crtInstruction);
				break;
			default:
				System.out.println("Error, unknown operator!");
				isOn = false;
				break;
			}
			

			if (DEBUG) {
				logsOut.println(regDump());
				logsOut.println();
			}
			
		}

	}


	/**Opérateurs**/

	// 0: Mouvement conditionnel
	public void ifC(Registre a, Registre b, Registre c) {
		if (!c.equalsZero()) 
			a.copyFrom(b);
		
	}

	// 1: Indice tableau
	public void getOffset(Registre a, Registre b, Registre c) {
		int offset = c.toInt(); 
		int address = b.toInt();
		a.setData(memoire.getDataOffset(address, offset).getData());
	}

	// 2: Modification tableau (=amendment)
	public void arrayModif(Registre a, Registre b, Registre c) {
		int adress = a.toInt();
		int offset = b.toInt();
		
		memoire.setDataOffset(adress, offset, c);
	}

	// 3: Addition
	public void add(Registre a, Registre b, Registre c) {
		a.setData(b.toLong() + c.toLong());
	}
	
	// 4: Multiplication
	public void mul(Registre a, Registre b, Registre c) {
		a.setData(c.toInt() * b.toInt());
	}

	// 5: Division
	public void div(Registre a, Registre b, Registre c) {
		a.setData(Integer.divideUnsigned(b.toInt(), c.toInt()));
	}

	// 6: Not-And
	public void nand(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataB = b.getData();
		boolean[] dataC = c.getData();

		for (int i = 0; i < Registre.DEFAULT_DATA_SIZE; i++) {
			if (!(dataB[i] && dataC[i]))
				dataA[i] = true;
			else
				dataA[i] = false;
		}

	}

	// 7: Stop
	public void stop() {
		isOn = false;
	}

	// 8: Allocation
	public void alloc(Registre b, Registre c) {
		int size = c.toInt();
		int address = memoire.getFreeAddress();

		if (DEBUG) {
			logsOut.println("Allocating " + size + " plateaux in " + address + ".");
		}
		
		memoire.alloc(address, size);
		b.setData(address);
	}

	// 9: Abandon XXX Test ok
	public void free(Registre c) {
		memoire.free(c.toInt());
	}

	// 10: Sortie XXX Test ok
	public void print(Registre c) {
		int toPrint = c.toInt();
		
		if (toPrint >= 0 && toPrint <= 255) {
			Character chara = new Character((char) toPrint);
			out.print(chara);
		} else {
			System.err.println("Can't print, unsupported value :(");
		}
	}

	// 11: Entrée
	public void input(Registre c) {
		try {
			InputStreamReader reader = new InputStreamReader(in);
			char input = 0;
			do {
				input = (char)reader.read();
			} while (input != '\n');

			c.setDataToOne();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 12: Chargement de programme
	public void loadProgram(Registre b, Registre c) {
		if (DEBUG) {
			logsOut.println("Loading " + b.toInt());
			logsOut.println("which contains " + memoire.getData(b.toInt()).length + " plateaux");
		}
		
		PlateauDeSable[] cpy = memoire.cpy(b.toInt());
		memoire.setData(0, cpy);
		finger = c.toInt();
		
		if (VERBOSE) {
			System.out.println("finger->" + finger);
		}
	}
	
	// 13(S): Orthographe
	public void orthographe(PlateauDeSable p) {
		Registre a  = registres[p.getSpecialRegistre()];
		a.setData(p.getSpecialValue());
	}
	
	
	
	public String regDump() {
		String result = "";
		
		result += "-----------------\n";
		
		for (int i = 0; i < registres.length; i++) {
			result += "#" + i + "->" + registres[i].toSexyString() + "\n";
		}
		
		result += "-----------------";
		
		return result;
	}
	
	
}
