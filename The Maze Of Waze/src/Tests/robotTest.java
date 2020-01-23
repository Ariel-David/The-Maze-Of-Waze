package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elements.robot;
import utils.Point3D;

class robotTest {

	@Test
	void testGetSrc() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setSrc(1);
		r2.setSrc(2);
		r3.setSrc(3);
		assertEquals(3,r3.getSrc());
		assertEquals(2,r2.getSrc());
		assertEquals(1,r1.getSrc());
	}

	@Test
	void testSetSrc() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setSrc(1);
		r2.setSrc(2);
		r3.setSrc(3);
		assertEquals(3,r3.getSrc());
		assertEquals(2,r2.getSrc());
		assertEquals(1,r1.getSrc());
	}

	@Test
	void testGetPos() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		Point3D p1 = new Point3D(1,2,0);
		Point3D p2 = new Point3D(2,4,0);
		Point3D p3 = new Point3D(5,6,0);
		r1.setPos(p1);
		r2.setPos(p2);
		r3.setPos(p3);
		assertEquals(p3,r3.getPos());
		assertEquals(p2,r2.getPos());
		assertEquals(p1,r1.getPos());
	}

	@Test
	void testSetPos() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		Point3D p1 = new Point3D(1,2,0);
		Point3D p2 = new Point3D(2,4,0);
		Point3D p3 = new Point3D(5,6,0);
		r1.setPos(p1);
		r2.setPos(p2);
		r3.setPos(p3);
		assertEquals(p3,r3.getPos());
		assertEquals(p2,r2.getPos());
		assertEquals(p1,r1.getPos());
	}

	@Test
	void testGetId() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setId(1);
		r2.setId(2);
		r3.setId(3);
		assertEquals(3,r3.getId());
		assertEquals(2,r2.getId());
		assertEquals(1,r1.getId());
	}

	@Test
	void testSetId() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setId(1);
		r2.setId(2);
		r3.setId(3);
		assertEquals(3,r3.getId());
		assertEquals(2,r2.getId());
		assertEquals(1,r1.getId());
	}

	@Test
	void testGetValue() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setValue(1.0);
		r2.setValue(2.0);
		r3.setValue(3.0);
		assertEquals(3.0,r3.getValue(),0.0001);
		assertEquals(2.0,r2.getValue(), 0.0001);
		assertEquals(1.0,r1.getValue(), 0.0001);
	}

	@Test
	void testSetValue() {
		robot r1 = new robot();
		robot r2 = new robot();
		robot r3 = new robot();
		r1.setValue(1.0);
		r2.setValue(2.0);
		r3.setValue(3.0);
		assertEquals(3.0,r3.getValue(),0.0001);
		assertEquals(2.0,r2.getValue(), 0.0001);
		assertEquals(1.0,r1.getValue(), 0.0001);
	}

}
