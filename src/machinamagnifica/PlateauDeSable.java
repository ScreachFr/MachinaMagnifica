package machinamagnifica;

import java.util.Arrays;


public class PlateauDeSable {
	public final static int DEFAULT_DATA_SIZE = 32;

	private boolean [] data;

	public PlateauDeSable() {
		data = new boolean [DEFAULT_DATA_SIZE];
		Arrays.fill(data, false);
	}


	public boolean[] getData() {
		return data;
	}

	public void setData(boolean[] data) {
		for (int i = 0; i < DEFAULT_DATA_SIZE && i < data.length; i++) {
			this.data[i] = data[i];
		}
	}

	public void setData(String newData) throws InvalidDataException {
		for(int i = 0; i < newData.length() && i < DEFAULT_DATA_SIZE; i++) {
			if (newData.charAt(i) == '1')
				data[i] = true;
			else if (newData.charAt(i) == '0')
				data[i] = false;
			else
				throw new InvalidDataException();
		}
	}

	public boolean equalsZero() {
		for (boolean b : data) 
			if (b)
				return false;

		return true;
	}

	public void copyFrom(Registre r) {
		boolean[] rData = r.getData();
		for (int i = 0; i < DEFAULT_DATA_SIZE; i++) {
			data[i] = rData[i];
		}
	}

	public int toInt() {
		return getDataOffset(0);
	}

	public void setData(int d) {
		Arrays.fill(data, false);

		for (int i = DEFAULT_DATA_SIZE-1; i >= 0; i--) {
			data[i] = (d & (1 << i)) != 0;
		}
	}

	public void setData(Byte[] d) {
		String result = "";

		for (Byte b : d) {
			result += byteToString(b);
		}

		try {
			setData(result);
		} catch (InvalidDataException e) {
			//N'arrive jamais
		}
	}

	public void setDataOffset(int offset, boolean[] d) {
		for (int i = offset, j = 0; i < data.length; i++, j++) {
			data[i] = d[j];
		}
	}

	public int getDataOffset(int offset) {
		int result = 0;

		for (int i = offset, j = 0; i < data.length; i++, j++) {
			if (data[i])
				result += Math.pow(2, j);
		}

		return result;
	}

	public void setDataToOne() {
		Arrays.fill(data, true);
	}

	public void setDataToZero() {
		Arrays.fill(data, false);
	}

	public int getOperator() {
		return getDataOffset(DEFAULT_DATA_SIZE-4);
	}

	private static String byteToString(Byte b) {
		String result = "";

		result += Integer.toString(b & 0xFF, 2);


		int toAdd = 7 - result.length();

		for (int i = 0; i < toAdd; i++) {
			result = '0' + result;
		}
		

		return '0' + result;
	}

	public int[] getRegistres() {
		//TODO
		return new int[]{0, 1, 2};
	}
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < data.length; i++) {

		}
		for (boolean d : data) {
			if (d)
				result += "1";
			else
				result += "0";
		}

		return result;
	}

	public static void main(String[] args) {
		PlateauDeSable p = new PlateauDeSable();

		byte zero = 0;
		byte un = 0b0000001;
		byte soizanteQuatre = 0b1000000;
		System.out.println("un : " + byteToString(un));
		System.out.println("127 : " + byteToString(soizanteQuatre));
		
		Byte[] tab = {soizanteQuatre, soizanteQuatre, un, un};

		p.setData(tab);
		System.out.println(p.toInt());
		System.out.println(p);
		System.out.println(p.toString().length());
	}
}
