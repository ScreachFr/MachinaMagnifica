package machinamagnifica;

public class MachinaMagnifica {
	private final static int DEFAULT_NB_REGISTRE = 8;
	
	private Registre[] registres;
	
	
	public MachinaMagnifica() {
		registres = new Registre[DEFAULT_NB_REGISTRE];
		
		for (int i = 0; i < DEFAULT_NB_REGISTRE; i++) {
			registres[i] = new Registre();
		}
	}
	
	
	public Registre[] getRegistres() {
		return registres;
	}
	
	public void run(char[] code) {
		
	}
	
	public static void main(String[] args) {
		MachinaMagnifica mm = new MachinaMagnifica();
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
