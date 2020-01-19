package elements;

import utils.Point3D;

public class fruit {
	public double value;
	public int type;
	public Point3D pos;

	public fruit() {
		this.value = 0;
		this.type = 0;
		this.pos = null;
	}
	public fruit(int type, double value, Point3D pos) {
		this.type = type;
		this.value = value;
		this.pos = pos;
	}
	public fruit(fruit f) {
		this.type = f.type;
		this.value = f.value;
		this.pos = f.pos;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}



}
