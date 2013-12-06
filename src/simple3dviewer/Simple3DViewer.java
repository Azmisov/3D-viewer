package simple3dviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
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
	private static final double transformInc = .01;
	private static final double shearInc = .1;
	private static final Renderer viewer = new Renderer();
	private static enum TransformMode{
		Rotate, Scale, ShearX, ShearY, ShearZ, Perspective
	}
	private static TransformMode mode = TransformMode.Rotate;
	private int mx, my;
	private Object3D obj;
	private AutoRotater rotate;
	
	/**
	 * Initialize GUI components
	 */
	public Simple3DViewer(){		
		//Initialize starting object
		reset();
		
		//Initialize GUI
		setTitle("Simple 3D Viewer");
		setSize(new Dimension(winSize, winSize));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//Projection Mode
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 5));
		btns.add(new JLabel("Project: "));
		final JComboBox<Point3D.ProjectionMode> projectMode = new JComboBox(){{
			addItem(Point3D.ProjectionMode.Orthographic);
			addItem(Point3D.ProjectionMode.Perspective);
			setSelectedItem(Point3D.mode);
		}};
		projectMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Point3D.mode = (Point3D.ProjectionMode) projectMode.getSelectedItem();
				obj.project();
				viewer.repaint();
			}
		});
		btns.add(projectMode);
		
		//Transform Mode
		btns.add(new JLabel("  Transform: "));
		final JComboBox<TransformMode> transformMode = new JComboBox(){{
			for (TransformMode m: TransformMode.values())
				addItem(m);
			setSelectedItem(mode);
		}};
		transformMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mode = (TransformMode) transformMode.getSelectedItem();
			}
		});
		viewer.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				mx = e.getX();
				my = e.getY();
				rotate.manualControl();
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		viewer.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e){
				double t1 = (my - e.getY())*transformInc,
						t2 = (mx - e.getX())*transformInc;
				switch (mode){
					case Rotate:
						obj.rotate(t1, t2, 0);
						break;
					case Scale:
						obj.scale(t1+1);
						break;
					case ShearX:
						obj.shear(t1, 0, 0);
						break;
					case ShearY:
						obj.shear(0, t1, 0);
						break;
					case ShearZ:
						obj.shear(0, 0, t1);
						break;
					case Perspective:
						Point3D.cameraDistance += t1*1.5;
						obj.project();
						break;
				}
				mx = e.getX();
				my = e.getY();
				viewer.repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		viewer.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
				obj.scale(-(1+notches/10.0));
				viewer.repaint();
			}

		});
		btns.add(transformMode);
		
		//Reset button
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		btns.add(btnReset);
		
		add(btns, BorderLayout.NORTH);
		add(viewer, BorderLayout.CENTER);
	}
	
	private void reset(){
		obj = Object3D.createCube(1);
		viewer.viewObject(obj);
		viewer.repaint();
		//Start auto-rotater
		rotate = new AutoRotater(obj);
		(new Thread(rotate)).start();
	}
	
	private class AutoRotater implements Runnable{
		public boolean enabled = true;
		public Object3D obj;
		
		public AutoRotater(Object3D obj){
			this.obj = obj;
		}
		@Override
		public void run(){
			while (enabled){
				obj.rotate(.03, .08, 0);
				viewer.repaint();
				try {
					Thread.sleep(80);
				} catch (InterruptedException ex) {
					System.err.println("Could not sleep in thread");
					enabled = false;
				}
			}
		}
		public void manualControl(){
			enabled = false;
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
