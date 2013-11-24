package simple3dviewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 3D object viewer
 * @author isaac
 */
public class Simple3DViewer extends JFrame{
	private static final int winSize = 450;
	
	/**
	 * Initialize GUI components
	 */
	public Simple3DViewer(){
		setTitle("Simple 3D Viewer");
		setSize(new Dimension(winSize, winSize));
		Renderer viewer = new Renderer();
		add(viewer);
		//Initialize starting object
		Object3D cube = Object3D.createCube(1);
		viewer.viewObject(cube);
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		//Start up the window
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Simple3DViewer().setVisible(true);
			}
		});
	}
}
