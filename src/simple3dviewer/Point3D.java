package simple3dviewer;

import java.awt.geom.Point2D;

/**
 * Holds information for a 3D vertex
 * @author isaac, jared, mark, dallen
 */
public class Point3D {
	private static enum ProjectionMode{
		PERSPECTIVE, ORTHOGRAPHIC
	};
	private static ProjectionMode mode;
	public double x, y, z;
	public Point2D.Double projection;
	
	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//TRANSFORMATIONS
	/**
	 * Rotate points in 3D space; all angles are in radians
	 * @param thetaX x-axis rotation
	 * @param thetaY y-axis rotation
	 * @param thetaZ z-axis rotation
	 */
	public void rotate(double thetaX, double thetaY, double thetaZ){
		//TODO, perform rotation transformation
		//This gets called by Object3D.rotate, so if you want
		//you can change how this method works. Maybe you want to
		//pass in a transformation matrix here, and generate the matrix
		//once in Object3D
		project();
	}
	public void scale(double scale){
		//TODO, perform scale transformation
		//You can also modify Object3D.scale, since that
		//is what calls this method
		project();
	}
	
	//PROJECTIONS
	private void project(){
		//Project onto the Z-Y plane
		//you may want to just do both types of projections
		//in the same method, if they're similar enough...
		switch (mode){
			case ORTHOGRAPHIC: projectOrthographic(); break;
			case PERSPECTIVE: projectPerspective(); break;
		}
	}
	private void projectOrthographic(){
		//TODO, dummy code
		projection = new Point2D.Double(0,0);
	}
	private void projectPerspective(){
		//TODO, dummy code
		projection = new Point2D.Double(0,0);
	}
	
	//OVERRIDES
	@Override
	public boolean equals(Object obj){
		if (obj == null || getClass() != obj.getClass())
			return false;
		final Point3D other = (Point3D) obj;
		return x == other.x && y == other.y && z == other.z;		
	}
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 67 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		hash = 67 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
		return hash;
	}
}
