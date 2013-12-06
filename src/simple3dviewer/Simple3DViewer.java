package simple3dviewer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * 3D object viewer
 * @author isaac
 */
public class Simple3DViewer extends JFrame{
	private static final int winSize = 450;
	private static final double rotateInc = .1;
	
	/**
	 * Initialize GUI components
	 */
	public Simple3DViewer(){
		setTitle("Simple 3D Viewer");
		setSize(new Dimension(winSize, winSize));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		final Renderer viewer = new Renderer();
		add(viewer);
		
		//Initialize starting object
		final Object3D cube = Object3D.createCube(1);
		viewer.viewObject(cube);
		
		//Rotation controls
		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()){
					case KeyEvent.VK_UP:
						cube.rotate(-rotateInc, 0, 0);
						break;
					case KeyEvent.VK_DOWN:
						cube.rotate(rotateInc, 0, 0);
						break;
					case KeyEvent.VK_LEFT:
						cube.rotate(0, -rotateInc, 0);
						break;
					case KeyEvent.VK_RIGHT:
						cube.rotate(0, rotateInc, 0);
						break;
                                        case KeyEvent.VK_1:
                                                cube.scale(1.2);
                                                break;
                                        case KeyEvent.VK_2:
                                                cube.scale(.8);
                                                break;
					default: return;
				}
				//Re-render the object
				viewer.repaint();
			}
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
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
