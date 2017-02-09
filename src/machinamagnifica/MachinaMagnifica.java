package machinamagnifica;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MachinaMagnifica {
	private final static int DEFAULT_NB_REGISTRE = 8;
	private final static int DEFAULT_MEMORY_SIZE = 256;
	private final static int DEFAULT_LOAD_SIZE = 4; //J'avais pas d'autre nom en tête...

	private Registre[] registres;
	private Memory memoire;
	private PrintStream out;
	private InputStream in;

	private Scanner inputFile;

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


	public Registre[] getRegistres() {
		return registres;
	}

	public void run(Scanner inpuScanner) {
		inputFile = inpuScanner;
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

			default:
				System.out.println("Error, unknown operator!");
				isOn = false;
				break;
			}

		}

		inputFile.close();
	}


	public PlateauDeSable loadNextPlateauDeSableFromInput() {
		PlateauDeSable result = new PlateauDeSable();
		Byte[] rawData = new Byte[DEFAULT_LOAD_SIZE];

		for (int i = 0; i < DEFAULT_LOAD_SIZE; i++) {
			rawData[i] = inputFile.nextByte();
		}

		result.setData(rawData);

		return result;
	}

	/**Opérateurs**/

	// 0: Mouvement conditionnel
	public void ifC(Registre a, Registre b, Registre c) {
		if (!c.equalsZero())
			a.copyFrom(b);
	}

	// 1: Indice tableau
	public void getOffset(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataB = b.getData();

		for (int i = c.toInt(), j = 0; i < dataA.length; i++, j++)
			dataB[j] = dataA[i];
	}

	// 2: Modification tableau
	public void arrayModif(Registre a, Registre b, Registre c) {
		boolean[] dataA = a.getData();
		boolean[] dataC = c.getData();

		for (int i = b.toInt(), j = 0; i < dataA.length; i++, j++)
			dataA[j] = dataC[i];
	}

	// 3: Addition
	public void add(Registre a, Registre b, Registre c) {
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
	public void mul(Registre a, Registre b, Registre c) {
		c.setData(a.toInt() * b.toInt());
	}

	// 5: Division
	public void div(Registre a, Registre b, Registre c) {
		c.setData(a.toInt() / b.toInt());
	}

	// 6: Not-And
	public void nand(Registre a, Registre b, Registre c) {
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
	public void stop() {
		isOn = false;
	}

	// 8: Allocation
	public void alloc(Registre b, Registre c) {
		int size = c.toInt();
		int address = memoire.getFreeAddress();

		memoire.alloc(address, size);

		b.setData(address);
	}

	// 9: Abandon
	public void free(Registre c) {
		memoire.free(c.toInt());
	}

	// 10: Sortie
	public void print(Registre c) {
		int toPrint = c.toInt();

		if (toPrint >= 0 && toPrint <= 255)
			out.print((char)toPrint);
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



	public static void main(String[] args) {
		Registre test = new Registre();
		MachinaMagnifica m = new MachinaMagnifica();

		m.input(test);
		System.out.println(test);
	}

	private enum NomRegistre {
		A("A", 0), B("B", 1), C("C", 2), D("D", 3);

		private int i;
		private String nom;

		private NomRegistre(String nom, int i) {
			this.i = i;
			this.nom = nom;
		}

		public int getI() {
			return i;
		}

		public String getNom() {
			return nom;
		}

		public boolean equals(String s) {
			return nom.equals(s);
		}
	}
}
