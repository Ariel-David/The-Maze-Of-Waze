package elements;
import utils.Point3D;

public class robot {
	public int src;
	public Point3D pos;
	public int id;
	public int dest;
	public int speed;
	public double value;
	
	public robot(int id, int src, int dest, int speed, double value, Point3D pos) {
		this.id = id;
		this.src = src;
		this.value = value;
		this.speed = speed;
		this.pos = pos;
	}
	
	public robot(robot r) {
		this.id = r.id;
		this.src = r.src;
		this.value = r.value;
		this.pos = r.pos;
		this.speed = r.speed;
	}
	
	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public Point3D getPos() {
		return pos;
	}

	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


	
}
