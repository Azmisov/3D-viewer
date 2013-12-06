package simple3dviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Holds all information for a 3D object
 * @author isaac
 */
public class Object3D {
	private boolean sorted = false;
	private final HashSet<Point3D> vertices;
	public final ArrayList<Point3D[]> edges;
	
	public Object3D(){
		vertices = new HashSet();
		edges = new ArrayList();
	}
	public void addEdge(Point3D p1, Point3D p2){
		vertices.add(p1);
		vertices.add(p2);
		edges.add(new Point3D[]{p1, p2});
		sorted = false;
	}
	/**
	 * Sort edges and vertices; furthest from the viewer
	 * will be at the start of the array
	 */
	public void depthSort(){
		if (!sorted){
			Collections.sort(edges, depthSorter);
			sorted = true;
		}
	}
	private final Comparator<Point3D[]> depthSorter = new Comparator<Point3D[]>(){
		@Override
		public int compare(Point3D[] e1, Point3D[] e2){
			double z1 = Math.max(e1[0].z, e1[1].z),
				   z2 = Math.max(e2[0].z, e2[1].z);
			return z1 < z2 ? -1 : 1;
		}
	};
	
	//TRANSFORMATIONS
	public void rotate(double thetaX, double thetaY, double thetaZ){
		for (Point3D vertex: vertices)
			vertex.rotate(thetaX, thetaY, thetaZ);
		sorted = false;
	}
	public void scale(double factor){
		for (Point3D vertex: vertices)
			vertex.scale(factor);
		sorted = false;
	}
	public void shear(double factorX, double factorY, double factorZ){
		for (Point3D vertex: vertices)
			vertex.shear(factorX, factorY, factorZ);
		sorted = false;
	}
    
	//PRIMITIVES
	public static Object3D createCube(final int scale){
		final Point3D
			//Top face
			t1 = new Point3D(1, 1, 1),
			t2 = new Point3D(1, -1, 1),
			t3 = new Point3D(-1, 1, 1),
			t4 = new Point3D(-1, -1, 1),
			//Bottom face
			b5 = new Point3D(1, 1, -1),
			b6 = new Point3D(1, -1, -1),
			b7 = new Point3D(-1, 1, -1),
			b8 = new Point3D(-1, -1, -1);
		return new Object3D(){{
			//Top face
			addEdge(t1, t2);
			addEdge(t1, t3);
			addEdge(t4, t2);
			addEdge(t4, t3);
			//Bottom face
			addEdge(b5, b6);
			addEdge(b5, b7);
			addEdge(b8, b6);
			addEdge(b8, b7);
			//Conjoining faces
			addEdge(t1, b5);
			addEdge(t2, b6);
			addEdge(t3, b7);
			addEdge(t4, b8);
			//Scale
			if (scale != 1)
				scale(scale);
		}};
	}
}
