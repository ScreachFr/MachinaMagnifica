package machinamagnifica;

public class Memory {
	private PlateauDeSable[][] memory;
	
	public Memory(int size) {
		memory = new PlateauDeSable[size][0];
	}
	
	public int getFreeAddress() {
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == null || memory[i].length == 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	public PlateauDeSable[] alloc(int address, int size) {
		memory[address] = new PlateauDeSable[size];
		
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
	
	public PlateauDeSable[] cpy(int address) {
		PlateauDeSable[] result = new PlateauDeSable[memory[address].length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = memory[address][i].cpy();
		}
		
		return result;
	}
}