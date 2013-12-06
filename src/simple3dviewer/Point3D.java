package simple3dviewer;

import java.awt.geom.Point2D;

/**
 * Holds information for a 3D vertex
 * @author isaac, jared, mark, dallen
 */
public class Point3D {
	public static enum ProjectionMode{
		PERSPECTIVE("Perspective"), ORTHOGRAPHIC("Orthographic");
		public String name;
		ProjectionMode(String name){
			this.name = name;
		}
		@Override
		public String toString(){
			return name;
		}
	};
	public static ProjectionMode mode = ProjectionMode.PERSPECTIVE;
	public double x, y, z;
	public Point2D.Double projection;
	
	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		project();
	}
	
	public double[][] matrixMultiplication(double[][]transMatrix, double [][]pointInfo)
        {
            double numbers[][] = new double[transMatrix.length][pointInfo[0].length];
            double sum;
            for (int i = 0; i<pointInfo[0].length; i++)
            {
                for (int row =0; row<transMatrix.length; row++)
                {
                    sum=0;
                    for (int col =0; col<transMatrix[0].length; col++)
                    {
                        sum+=transMatrix[row][col]*pointInfo[col][i];
                    }
                    numbers[row][i]=sum;
                }
            }
            
            return numbers;
        }
	//TRANSFORMATIONS
	/**
	 * Rotate points in 3D space; all angles are in radians
	 * @param thetaX x-axis rotation
	 * @param thetaY y-axis rotation
	 * @param thetaZ z-axis rotation
	 */
	public void rotate(double thetaX, double thetaY, double thetaZ){
	//I Believe this Method is Finished Now, Jared.	
            //TODO, perform rotation transformation  
		//This gets called by Object3D.rotate, so if you want
		//you can change how this method works. Maybe you want to
		//pass in a transformation matrix here, and generate the matrix
		//once in Object3D
            //I'm 90% sure that this works, even if it isn't the most efficient -Jared
            //double x = Math.toRadians(45);
            double numbers[][];//Used to represent the rotated point
            double point [][]= new double [2][1];
            double transformMatrix[][] = new double[2][2];
            //transformMatrix[][] = {{Math.cos(thetaX),-Math.sin(thetaX)},{Math.sin(thetaX),Math.cos(thetaX)}};
            //Rotates around the X axis.
            
            transformMatrix[0][0]=Math.cos(thetaX);
            transformMatrix[0][1]=-Math.sin(thetaX);
            transformMatrix[1][0]=Math.sin(thetaX);
            transformMatrix[1][1]=Math.cos(thetaX);
            point[0][0] = y;
            point[1][0] = z;
            numbers=matrixMultiplication(transformMatrix, point);
            this.y = numbers[0][0];
            this.z = numbers[1][0];
            
            //Rotates around the Y axis.
            transformMatrix[0][0]=Math.cos(thetaY);
            transformMatrix[0][1]=-Math.sin(thetaY);
            transformMatrix[1][0]=Math.sin(thetaY);
            transformMatrix[1][1]=Math.cos(thetaY);
            point[0][0] = x;
            point[1][0] = z;
            numbers=matrixMultiplication(transformMatrix, point);
            this.x = numbers[0][0];
            this.z = numbers[1][0];
            
            //Rotates around the Z axis.
            transformMatrix[0][0]=Math.cos(thetaZ);
            transformMatrix[0][1]=-Math.sin(thetaZ);
            transformMatrix[1][0]=Math.sin(thetaZ);
            transformMatrix[1][1]=Math.cos(thetaZ);
            point[0][0] = x;
            point[1][0] = y;
            numbers=matrixMultiplication(transformMatrix, point);
            this.x = numbers[0][0];
            this.y = numbers[1][0];
            

            
            project();
	}
	public void scale(double scale)
        {
		//TODO, perform scale transformation
		//You can also modify Object3D.scale, since that
		//is what calls this method
            double transMatrix[][]={{scale,0,0},{0,scale,0},{0,0,scale}};
            double XYZ [][]={{x},{y},{z}};
            XYZ = matrixMultiplication(transMatrix,XYZ);
            x = XYZ[0][0];
            y= XYZ[1][0];
            z=XYZ[2][0];
		project();
	}
        public void shear(double factorX, double factorY, double factorZ)
        {
            double transMatrix[][]={{1,factorY,factorZ},{factorX,1,factorZ},{factorX,factorY,1}};
            double XYZ [][]={{x},{y},{z}};
            XYZ = matrixMultiplication(transMatrix,XYZ);
            x = XYZ[0][0];
            y= XYZ[1][0];
            z=XYZ[2][0];
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
		projection = new Point2D.Double(x,y);
	}
	private void projectPerspective()
        {
		//TODO, dummy code
            double distance =8;
           
		projection = new Point2D.Double(x/(distance-z)*distance,y/(distance-z)*distance);
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
