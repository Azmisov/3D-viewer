/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simple3dviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 * Renders a 3D object
 * @author isaac
 */
public class Renderer extends JPanel{	
	private static final BasicStroke line;
	private static final HashMap<RenderingHints.Key, Object> antialiasing;
	private static final int pixelsPerUnit;
	private Object3D obj;
	//Initialize rendering settings
	static{
		pixelsPerUnit = 100;
		line = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		antialiasing = new HashMap(){{
			put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}};
	}

	public void viewObject(Object3D obj){
		this.obj = obj;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//Window dimensions
		int winx = getWidth()/2, winy = getHeight()/2;
		
		//Enable antialiasing
		g2.setRenderingHints(antialiasing);
		
		//Draw all points
		g2.setStroke(line);
		//First, perform depth sort on edges
		obj.depthSort();
		//Loop through all edges and draw lines
		int e0x, e0y, e1x, e1y;		
		for (Point3D[] edge: obj.edges){
			//Map the object coordinates to screen coordinates
			e0x = (int) ((edge[0].projection.x * pixelsPerUnit) + winx);
			e0y = (int) ((edge[0].projection.y * pixelsPerUnit) + winy);
			e1x = (int) ((edge[1].projection.x * pixelsPerUnit) + winx);
			e1y = (int) ((edge[1].projection.y * pixelsPerUnit) + winy);
			
			//Unfortunately, Java2D can't do gradient lines, so we'll
			//fake it by modifying the full line's brightness
			double max_dist = 1.5;
			float brightness = (float) (((edge[0].z + edge[1].z)/2.0 + max_dist)/(max_dist*2.0));
			g2.setColor(Color.getHSBColor(.8f, 1f, brightness));
			g2.drawLine(e0x, e0y, e1x, e1y);
		}
	}
}
