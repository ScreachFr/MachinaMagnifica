package machinamagnifica.test;

import static org.junit.Assert.*;
import machinamagnifica.InvalidDataException;
import machinamagnifica.PlateauDeSable;

import org.junit.Test;

public class PlateauDeSableTest {
	private final static String ds1 = "11111111111111111111111111111111";
	
	
	@Test
	public void plateauVideOnStart() {
		PlateauDeSable pds = new PlateauDeSable();
		assertTrue(pds.equalsZero());
	}
	
	@Test
	public void plateauNonVide() {
		PlateauDeSable p1 = new PlateauDeSable();
		
		try {
			p1.setData(ds1);
			assertTrue(!p1.equalsZero());
		} catch (InvalidDataException e) {
			assertTrue(false);
		}
		
	}
	
	public void affectationInt() {
		PlateauDeSable p = new PlateauDeSable();
		int data = 1;
		
		p.setData(data);
		assertTrue(p.toInt() == data);
	}
	
	
	@Test
	public void addition() {
		PlateauDeSable p = new PlateauDeSable();
		int data = 1;
		
		p.setData(data);
	}
}
