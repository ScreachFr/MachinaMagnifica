package machinamagnifica.operateur;

import java.util.BitSet;

import machinamagnifica.InvalidDataException;
import machinamagnifica.PlateauDeSable;
import machinamagnifica.Registre;

public class Operateurs {


	public void add(Registre A, Registre B, Registre C) throws EvaluationException {
		try {
			BitSet a = A.toIntBitSet();
			BitSet b = B.toIntBitSet();
			BitSet c = C.toIntBitSet();
			boolean va, vb;
			boolean retenue = false;
			for (int i = 0; i < PlateauDeSable.DEFAULT_DATA_SIZE; i++) {
				va = a.get(i);
				vb = b.get(i);
				if(retenue) {
					if(va && vb){
						c.set(i);
					} else if (!va && !vb){
						c.set(i);
						retenue = false;
					} else {
						c.set(i, false);
					}
				} else {
					if(va && vb){
						c.set(i, false);
						retenue = true;
					} else if(!va && !vb){
						c.set(i, false);
					} else{
						c.set(i);
					}

				}
			}
			
			for (int i = 0; i < PlateauDeSable.DEFAULT_DATA_SIZE; i++) {
			}
			
		} catch (InvalidDataException e) {
			throw new EvaluationException();
		}
	}
	
	public static void main(String[] args) {
	}

}
