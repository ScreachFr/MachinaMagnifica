package machinamagnifica.test;

import static org.junit.Assert.*;
import machinamagnifica.Memory;
import machinamagnifica.PlateauDeSable;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void allocLimit() {
		Memory m = new Memory(16);

		int addr;

		for (int i = 0; i < 16; i++) {
			addr = m.getFreeAddress();

			m.alloc(addr, 1);
		}

	}

	@Test
	public void allocLimitFail() {
		boolean failFlag = false;
		
		try {
			Memory m = new Memory(16);

			int addr;

			for (int i = 0; i < 17; i++) {
				addr = m.getFreeAddress();

				m.alloc(addr, 1);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			failFlag = true;
		}
		
		assertTrue(failFlag);

	}
	
	@Test
	public void free() {
		Memory m = new Memory(16);
		int addr = m.getFreeAddress();
		
		m.alloc(addr, 1);
		
		m.free(addr);
	}
	
	@Test
	public void storage() {
		Memory m = new Memory(16);
		PlateauDeSable[] ps = new PlateauDeSable[] {new PlateauDeSable()};
		
		int addr = m.getFreeAddress();
		
		ps[0].setData(42);
		
		m.alloc(addr, 1);
		
		m.setData(addr, ps);
		
		assertTrue(m.getData(addr)[0].toInt() == 42);
		
	}
}











