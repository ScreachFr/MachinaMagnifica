package machinamagnifica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class MachinaMagnifica {
	private final static boolean DEBUG = false;
	
	private final static int DEFAULT_NB_REGISTRE = 8;
	private final static int DEFAULT_MEMORY_SIZE = 256;
	private final static int DEFAULT_LOAD_SIZE = 4; //J'avais pas d'autre nom en tête...

	private Registre[] registres;
	private Memory memoire;
	private PrintStream out;
	private InputStream in;

	private FileInputStream inputFileReader;
	private byte[] readerBuffer;
	
	private boolean isOn;

	public MachinaMagnifica() {
		out = System.out;
		in = System.in;
		memoire = new Memory(DEFAULT_MEMORY_SIZE);
		registres = new Registre[DEFAULT_NB_REGISTRE];

		for (int i = 0; i < DEFAULT_NB_REGISTRE; i++) {
			registres[i] = new Registre();
		}

		isOn = true;
	}

	public void run(FileInputStream inputReader) throws IOException {
		inputFileReader = inputReader;
		readerBuffer = new byte[DEFAULT_LOAD_SIZE];
		
		int op;
		PlateauDeSable crtInstruction;
		int[] reg;
		Registre a, b, c;
		while (isOn) {
			crtInstruction = loadNextPlateauDeSableFromInput();
			op = crtInstruction.getOperator();
			reg = crtInstruction.getRegistres();
			a = registres[reg[0]];
			b = registres[reg[1]];
			c = registres[reg[2]];

			if (DEBUG) {
				System.out.println("Plateau ->");
				System.out.println(crtInstruction + " : " + crtInstruction.toInt() + " : " + Integer.toHexString(crtInstruction.toInt()));
				System.out.println("OP -> " + op);
				System.out.println("Registres ->");
				System.out.println(Arrays.toString(reg));
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
				loadProgram(c);
				break;
			case 13:
				orthographe(registres[crtInstruction.getSpecialRegistre()], crtInstruction.getSpecialValue());
				break;
			default:
				System.out.println("Error, unknown operator!");
				isOn = false;
				break;
			}

		}

		try {
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private PlateauDeSable loadNextPlateauDeSableFromInput() throws IOException {
		PlateauDeSable result = new PlateauDeSable();

		inputFileReader.read(readerBuffer);
		
		if (DEBUG) {
			System.out.println("Loading " + Integer.toHexString(readerBuffer[0]) + " " + Integer.toHexString(readerBuffer[1]) + ".");
		}
			
		
		result.setData(readerBuffer);

		return result;
	}

	/**Opérateurs**/

	// 0: Mouvement conditionnel
	private void ifC(Registre a, Registre b, Registre c) {
		if (!c.equalsZero())
			a.copyFrom(b);
	}

	// 1: Indice tableau
	private void getOffset(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataB = b.getData();

		for (int i = c.toInt(), j = 0; i < dataA.length; i++, j++)
			dataB[j] = dataA[i];
	}

	// 2: Modification tableau
	private void arrayModif(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataC = c.getData();

		for (int i = b.toInt(), j = 0; i < dataA.length; i++, j++)
			dataA[j] = dataC[i];
	}

	// 3: Addition
	private void add(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataB = b.getData();
		boolean[] dataC = c.getData();

		boolean va, vb;
		boolean retenue = false;
		for (int i = 0; i < PlateauDeSable.DEFAULT_DATA_SIZE; i++) {
			va = dataA[i];
			vb = dataB[i];

			if(retenue) {
				if(va && vb){
					dataC[i] = true;
				} else if (!va && !vb){
					dataC[i] = true;
					retenue = false;
				} else {
					dataC[i] = false;
				}
			} else {
				if(va && vb){
					dataC[i] = false;
					retenue = true;
				} else if(!va && !vb){
					dataC[i] = false;
				} else{
					dataC[i] = true;
				}

			}

		}

	}

	// 4: Multiplication
	private void mul(Registre a, Registre b, Registre c) {
		c.setData(a.toInt() * b.toInt());
	}

	// 5: Division
	private void div(Registre a, Registre b, Registre c) {
		c.setData(a.toInt() / b.toInt());
	}

	// 6: Not-And
	private void nand(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataB = b.getData();
		boolean[] dataC = c.getData();

		for (int i = 0; i < Registre.DEFAULT_DATA_SIZE; i++) {
			if (dataA[i] != dataB[i])
				dataC[i] = true;
			else
				dataC[i] = false;
		}

	}

	// 7: Stop
	private void stop() {
		isOn = false;
	}

	// 8: Allocation
	private void alloc(Registre b, Registre c) {
		int size = c.toInt();
		int address = memoire.getFreeAddress();

		memoire.alloc(address, size);

		b.setData(address);
	}

	// 9: Abandon
	private void free(Registre c) {
		memoire.free(c.toInt());
	}

	// 10: Sortie
	private void print(Registre c) {
		int toPrint = c.toInt();

		if (toPrint >= 0 && toPrint <= 255)
			out.print((char)toPrint);
	}

	// 11: Entrée
	private void input(Registre c) {
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
	private void loadProgram(Registre c) {
		memoire.setData(0, memoire.cpy(c.toInt()));
	}
	
	// 13(S): Orthographe
	private void orthographe(Registre a, int value) {
		a.setData(value);
	}
	
	public static void main(String[] args) {
		//10111111 11111111 00000000 00010000 : 134283261 : 800fffd
		//00001000 00000000 00000000 11010000 : 134217936 : 80000d0

		int i = 0x080000D0;
		
		System.out.println(Integer.toBinaryString(i) + " : " + i + " : " + Integer.toHexString(i));
	}
}
