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
		int result = 0;
		for (int i = 0; i < d.length; i++) {
			result += d[i] & 0xFF;
			if (i < d.length-1)
				result = result << 8;
		}
		
		setData(result);
	}

	public void setDataOffset(int offset, boolean[] d) {
		for (int i = offset, j = 0; i < data.length; i++, j++) {
			data[i] = d[j];
		}
	}

	public int getDataOffset(int offset) {
		return getDataOffset(offset, DEFAULT_DATA_SIZE);
	}

	//bits = how many bits ?
	public int getDataOffset(int offset, int bits) {
		int result = 0;

		for (int i = offset, j = 0; i < data.length && j < bits; i++, j++) {
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


	public int[] getRegistres() {
		int a = getDataOffset(0, 3);
		int b = getDataOffset(3, 3);
		int c = getDataOffset(6, 3);
		return new int[]{a, b, c};
	}
	
	public int getSpecialRegistre() {
		return getDataOffset(DEFAULT_DATA_SIZE-7, 3);
	}
	
	public int getSpecialValue() {
		return getDataOffset(0, 25);
	}
	
	public PlateauDeSable cpy() {
		PlateauDeSable result = new PlateauDeSable();
		result.data = Arrays.copyOf(data, data.length);
		
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		int i = 0;
		for (boolean d : data) {
			i++;
			if (d)
				result += "1";
			else
				result += "0";
			if (i%8 == 0)
				result += " ";
		}

		return result;
	}
	

	public static void main(String[] args) {
		PlateauDeSable p = new PlateauDeSable();
		
		try {
			p.setData("11100110000000000000000001001000");
			System.out.println(p);
			System.out.println(p.getOperator());
			System.out.println(p.getSpecialRegistre());
			System.out.println(p.getSpecialValue());
			System.out.println(Arrays.toString(p.getRegistres()));
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
