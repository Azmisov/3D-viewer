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
		line = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
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
		Graphics2D g2 = (Graphics2D) g;
		
		//Enable antialiasing
		g2.setRenderingHints(antialiasing);
		
		//Draw all points
		g2.setStroke(line);
		//First, perform depth sort on edges
		obj.depthSort();
		//Loop through all edges and draw lines
		for (Point3D[] edge: obj.edges){
			//Unfortunately, Java2D can't do gradient lines, so we'll
			//fake it by modifying the full line's brightness
			float brightness = (float) (1 - (edge[0].z + edge[1].z)/2.0);
			g2.setColor(new Color(Color.HSBtoRGB(8f, 1f, brightness)));
			g2.drawLine(
				(int) edge[0].projection.x,
				(int) edge[0].projection.y,
				(int) edge[1].projection.x,
				(int) edge[1].projection.y
			);
		}
	}
}
