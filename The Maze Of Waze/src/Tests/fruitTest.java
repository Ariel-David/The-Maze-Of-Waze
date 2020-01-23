package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elements.fruit;
import utils.Point3D;

class fruitTest {

	@Test
	void testGetValue() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		f1.setValue(1.0);
		f2.setValue(2.0);
		f3.setValue(3.0);
		assertEquals(3.0,f3.getValue(),0.0001);
		assertEquals(2.0,f2.getValue(), 0.0001);
		assertEquals(1.0,f1.getValue(), 0.0001);
	}

	@Test
	void testSetValue() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		f1.setValue(1.0);
		f2.setValue(2.0);
		f3.setValue(3.0);
		assertEquals(3.0,f3.getValue(),0.0001);
		assertEquals(2.0,f2.getValue(), 0.0001);
		assertEquals(1.0,f1.getValue(), 0.0001);
	}

	@Test
	void testGetType() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		f1.setType(1);
		f2.setType(-1);
		f3.setType(1);
		assertEquals(1,f1.getType());
		assertEquals(-1,f2.getType());
		assertEquals(1,f3.getType());
	}

	@Test
	void testSetType() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		f1.setType(1);
		f2.setType(-1);
		f3.setType(1);
		assertEquals(1,f1.getType());
		assertEquals(-1,f2.getType());
		assertEquals(1,f3.getType());
	}

	@Test
	void testGetPos() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		Point3D p1 = new Point3D(1,2,0);
		Point3D p2 = new Point3D(2,4,0);
		Point3D p3 = new Point3D(5,6,0);
		f1.setPos(p1);
		f2.setPos(p2);
		f3.setPos(p3);
		assertEquals(p3,f3.getPos());
		assertEquals(p2,f2.getPos());
		assertEquals(p1,f1.getPos());
	}

	@Test
	void testSetPos() {
		fruit f1 = new fruit(0, 0, null);
		fruit f2 = new fruit(0, 0, null);
		fruit f3 = new fruit(0, 0, null);
		Point3D p1 = new Point3D(1,2,0);
		Point3D p2 = new Point3D(2,4,0);
		Point3D p3 = new Point3D(5,6,0);
		f1.setPos(p1);
		f2.setPos(p2);
		f3.setPos(p3);
		assertEquals(p3,f3.getPos());
		assertEquals(p2,f2.getPos());
		assertEquals(p1,f1.getPos());
	}

}
