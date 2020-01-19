package elements;
import utils.Point3D;

public class robot {
	private int src;
	private Point3D pos;
	private int id;
	private int dest;
	private int speed;
	private double value;
	
	public robot(int id, int src, int dest, int speed, double value, Point3D pos) {
		this.id = id;
		this.src = src;
		this.value = value;
		this.pos = pos;
		this.dest = dest;
		this.speed = speed;
	}
	
	public robot(robot r) {
		this.id = r.id;
		this.src = r.src;
		this.value = r.value;
		this.pos = r.pos;
		this.speed = r.speed;
		this.dest = r.dest;
	}
	
	public robot(Point3D p) {
		this.pos = p;
		this.id = 0;
		this.src = 0;
		this.value = 0.0;
		this.dest = -1;
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
