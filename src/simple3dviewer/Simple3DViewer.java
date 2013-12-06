package simple3dviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 3D object viewer
 * @author isaac
 */
public class Simple3DViewer extends JFrame{
	private static final int winSize = 500;
	private static final double rotateInc = .01;
	private static final Renderer viewer = new Renderer();;
	private int mx, my;
	
	/**
	 * Initialize GUI components
	 */
	public Simple3DViewer(){		
		//Initialize starting object
		final Object3D cube = Object3D.createCube(1);
		viewer.viewObject(cube);
		
		final AutoRotater rotate = new AutoRotater(cube);
		(new Thread(rotate)).start();
		
		//Initialize GUI
		setTitle("Simple 3D Viewer");
		setSize(new Dimension(winSize, winSize));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 5));
		btns.add(new JLabel("Projection: "));
		final JComboBox<Point3D.ProjectionMode> projectMode = new JComboBox(){{
			addItem(Point3D.ProjectionMode.ORTHOGRAPHIC);
			addItem(Point3D.ProjectionMode.PERSPECTIVE);
			setSelectedItem(Point3D.mode);
		}};
		projectMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Point3D.mode = (Point3D.ProjectionMode) projectMode.getSelectedItem();
				viewer.repaint();
			}
		});
		btns.add(projectMode);
		btns.add(new JLabel("  Scale: "));
		JButton scaleUp = new JButton("+");
		scaleUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cube.scale(1.2);
				viewer.repaint();
			}
		});
		btns.add(scaleUp);
		JButton scaleDown = new JButton("-");
		scaleDown.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cube.scale(.8);
				viewer.repaint();
			}
		});
		btns.add(scaleDown);		
		
		add(btns, BorderLayout.NORTH);
		add(viewer, BorderLayout.CENTER);
		
		//Rotation controls
		viewer.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				mx = e.getX();
				my = e.getY();
				rotate.manualControl(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				rotate.manualControl(false);
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		viewer.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				cube.rotate((my - e.getY())*rotateInc, (mx - e.getX())*rotateInc, 0);
				mx = e.getX();
				my = e.getY();
				viewer.repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
	}
	
	private class AutoRotater implements Runnable{
		public boolean enabled = true;
		public Object3D obj;
		
		public AutoRotater(Object3D obj){
			this.obj = obj;
		}
		@Override
		public void run(){
			while (true){
				obj.rotate(.03, .08, 0);
				viewer.repaint();
				try {
					Thread.sleep(80);
				} catch (InterruptedException ex) {
					System.err.println("Could not sleep in thread");
					enabled = false;
				}
				synchronized (this){
					while (!enabled){
						try {
							this.wait();
						} catch (InterruptedException ex) {
							System.err.println("Cannot wait");
						}
					}
				}
			}
		}
		public void manualControl(boolean activate){
			synchronized (this){
				enabled = !activate;
				this.notify();
			}
		}
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
