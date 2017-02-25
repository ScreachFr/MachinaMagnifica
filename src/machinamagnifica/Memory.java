package machinamagnifica;

import java.util.ArrayList;

public class Memory {
	private PlateauDeSable[][] memory;
	
	public Memory(int size) {
		memory = new PlateauDeSable[size][0];
	}
	
	public int getFreeAddress() {
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == null || memory[i].length == 0) {
//				System.out.println("nextFree address : " + i);
				return i;
			}
		}
		
		
		return -1;
	}
	
	public PlateauDeSable[] alloc(int address, int size) {
		memory[address] = new PlateauDeSable[size];
		
//		System.out.println("new alloc size " + size);
		
		for	(int i = 0; i < size; i++)
			memory[address][i] = new PlateauDeSable();
		
		return memory[address];
	}
	
	public void free(int address) {
		memory[address] = null;
	}
	
	public PlateauDeSable[] getData(int address) {
		return memory[address];
	}
	
	public void setData(int address, PlateauDeSable[] data) {
		memory[address] = data;
	}
	
	public void setProgramData(ArrayList<PlateauDeSable> data) {
		memory[0] = new PlateauDeSable[data.size()];
		
		for (int i = 0; i < memory[0].length; i++) {
			memory[0][i] = data.get(i);
		}
	}
	
	public void setDataOffset(int address, int offset, PlateauDeSable data) {
		memory[address][offset].setData(data.getData());
	}
	
	public PlateauDeSable getDataOffset(int address, int offset) {
		return memory[address][offset];
	}
	
	public void appendData(int address, PlateauDeSable data) {
		PlateauDeSable[] old = memory[address];
		PlateauDeSable[] result = new PlateauDeSable[old.length + 1];
		
		for (int i = 0; i < old.length; i++) {
			result[i] = old[i];
		}
		
		result[result.length-1] = data;
		
		memory[address] = result;
	}
	
	public PlateauDeSable[] cpy(int address) {
		PlateauDeSable[] result = new PlateauDeSable[memory[address].length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = memory[address][i].cpy();
		}
		
		return result;
	}
	
}
