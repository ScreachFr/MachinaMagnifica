package machinamagnifica;

import java.util.BitSet;

public class PlateauDeSable {
	public final static int DEFAULT_DATA_SIZE = 32;
	
	
	private char [] data;
	
	public PlateauDeSable() {
		data = new char [DEFAULT_DATA_SIZE];
	}
	
	
	public char[] getData() {
		return data;
	}
	
	public BitSet toIntBitSet() throws InvalidDataException {
		BitSet result = new BitSet(32);
		
		for (int i = 0; i < data.length; i++) {
			if (data[i] == '1')
				result.set(i);
			else if (data[i] != '0') {
				throw new InvalidDataException();
			}
		}
		
		return result;
	}
	
}
