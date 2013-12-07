package simple3dviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import simple3dviewer.Object3D.Primitives;
import simple3dviewer.Point3D.ProjectionMode;

/**
 * 3D object viewer
 * @author isaac
 */
public class Simple3DViewer extends JFrame{
	private static final int winX = 590, winY = 500;
	private static final double transformInc = .01;
	private static final double shearInc = .1;
	private static final Renderer viewer = new Renderer();
	private static enum TransformMode{
		Rotate, Scale, Shear, Perspective
	}
	private static TransformMode mode = TransformMode.Rotate;
	private static Primitives objMode = Primitives.Cube;
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
		setSize(new Dimension(winX, winY));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		//Controls
		JPanel btns = new JPanel();
		btns.setLayout(new FlowLayout(FlowLayout.CENTER, 13, 5));
		
		//Projection Mode
		final JComboBox<Point3D.ProjectionMode> projectMode = new JComboBox(){{
			for (ProjectionMode m: ProjectionMode.values())
				addItem(m);
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
		
		//Transform Mode
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
					case Shear:
						obj.shear(t1, t2, 0);
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
				obj.scale(1-notches/10.0);
				viewer.repaint();
			}

		});
		
		//Object Mode
		final JComboBox<Point3D.ProjectionMode> objectMode = new JComboBox(){{
			for (Primitives m: Primitives.values())
				addItem(m);
			setSelectedItem(objMode);
		}};
		objectMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				objMode = (Primitives) objectMode.getSelectedItem();
				reset();
			}
		});
		
		//Reset button
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		//Layout components in grid
		JPanel box = new JPanel();
		box.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 3;		c.ipady = 3;
		c.gridwidth = 1;	c.gridheight = 1;
		c.gridx = 0;		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		box.add(new JLabel("Projection:"), c);
		c.insets.top = 0;
		c.gridy++;
		box.add(projectMode, c);
		c.insets.top = 20;
		c.gridy++;
		box.add(new JLabel("Transformation:"), c);
		c.insets.top = 0;
		c.gridy++;
		box.add(transformMode, c);
		c.insets.top = 20;
		c.gridy++;
		box.add(new JLabel("Object:"), c);
		c.insets.top = 0;
		c.gridy++;
		box.add(objectMode, c);
		c.insets.top = 20;
		c.gridy++;
		box.add(btnReset, c);
		
		btns.add(box);
		add(btns, BorderLayout.WEST);
		add(viewer, BorderLayout.CENTER);
	}
	
	private void reset(){
		Point3D.cameraDistance = 8;
		obj = objMode.create(1);
		viewer.viewObject(obj);
		viewer.repaint();
		//Start auto-rotater
		rotate = new AutoRotater(obj);
		(new Thread(rotate)).start();
	}
	
	private class AutoRotater implements Runnable{
		public boolean enabled = true;
		public Object3D obj;
		private double period = 0;
		
		public AutoRotater(Object3D obj){
			this.obj = obj;
		}
		@Override
		public void run(){
			while (enabled){
				period += .05;
				double top = Math.sin(period)*.03;
				obj.rotate(top, .08, 0);
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
