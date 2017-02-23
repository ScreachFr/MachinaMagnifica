package machinamagnifica.test;

import static org.junit.Assert.*;
import machinamagnifica.MachinaMagnifica;
import machinamagnifica.Registre;

import org.junit.Test;


public class MachinaMagnificaTest {

	@Test
	public void zeroPlusZero() {
		MachinaMagnifica mm = new MachinaMagnifica(null);
		
		Registre a, b, c;
		
		a = new Registre();
		b = new Registre();
		c = new Registre();
		
		mm.add(a, b, c);
		
		assertTrue(c.toInt() == 0);
	}
	
	@Test
	public void unPlusUn() {
		MachinaMagnifica mm = new MachinaMagnifica(null);
		
		Registre a, b, c;
		
		a = new Registre();
		b = new Registre();
		c = new Registre();
		
		a.setData(1);
		b.setData(1);
		
		
		mm.add(a, b, c);
		
		assertTrue(c.toInt() == 2);
	}
	
	@Test
	public void mouvementCondFail() {
		MachinaMagnifica mm = new MachinaMagnifica(null);
		
		Registre a, b, c;
		
		a = new Registre();
		b = new Registre();
		c = new Registre();
		
		a.setData(1);
		
		
		mm.ifC(a, b, c);
		
		assertEquals(a.toInt(), 1);
		assertEquals(b.toInt(), 0);
		assertEquals(c.toInt(), 0);
	}
	
	@Test
	public void mouvementCond() {
		MachinaMagnifica mm = new MachinaMagnifica(null);
		
		Registre a, b, c;
		
		a = new Registre();
		b = new Registre();
		c = new Registre();
		
		a.setData(1);
		c.setData(1);
		
		mm.ifC(a, b, c);
		
		assertEquals(a.toInt(), 0);
		assertEquals(b.toInt(), 0);
		assertEquals(c.toInt(), 1);
	}
	
	@Test
	public void multi() {
		MachinaMagnifica mm = new MachinaMagnifica(null);
		
		Registre a, b, c;
		
		a = new Registre();
		b = new Registre();
		c = new Registre();
		
		a.setData(2);
		b.setData(2);
		
		mm.mul(a, b, c);
		
		assertEquals(a.toInt(), 2);
		assertEquals(b.toInt(), 2);
		assertEquals(c.toInt(), 4);
	}
	
	

}
