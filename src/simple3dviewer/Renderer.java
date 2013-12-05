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
        public void drawGradientLine(int x, int y, double z, int x1, int y1, double z1, Graphics2D g, int num)
        {
            if (num<=0)
            {
                float brightness = (float) (((z + z1)/4.0 + 1.)/2.0);
                brightness=1-brightness;
                if (brightness<0)
                    brightness=0;
                if (brightness>1)
                    brightness=1;
                System.out.println("brightness = " + brightness);
                System.out.println("(z) = " + (z+z1));
                //brightness=1;
                //g.setColor(new Color(Color.HSBtoRGB(1f, 1f, brightness)));
                g.setColor(new Color((int)(255*brightness),0,0));
                g.drawLine(x, y, x1, y1);
               return; 
               
            }
            drawGradientLine(x,y,z,(x1+x)/2,(y1+y)/2, (z1+z)/2, g, num-1);
            drawGradientLine(x1,y1,z1,(x1+x)/2,(y1+y)/2, (z1+z)/2, g, num-1);    
            //drawGradientLine(e0x,e0y,edge[0].z,e1x,e1y,edge[1].z,g2,4);
        }
	
	@Override
	protected void paintComponent(Graphics g) {
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
                obj.rotate(Math.toRadians(10), Math.toRadians(10), Math.toRadians(10));
		for (Point3D[] edge: obj.edges){
			//Map the object coordinates to screen coordinates
			e0x = (int) ((edge[0].projection.x * pixelsPerUnit) + winx);
			e0y = (int) ((edge[0].projection.y * pixelsPerUnit) + winy);
			e1x = (int) ((edge[1].projection.x * pixelsPerUnit) + winx);
			e1y = (int) ((edge[1].projection.y * pixelsPerUnit) + winy);
			
			//Unfortunately, Java2D can't do gradient lines, so we'll
			//fake it by modifying the full line's brightness
			float brightness = (float) (((edge[0].z + edge[1].z)/2.0 + 1)/2.0);
			System.out.println("brightness = " + brightness);
			System.out.println("(edge[0].z+edge[1].z) = " + (edge[0].z+edge[1].z));
			g2.setColor(new Color(Color.HSBtoRGB(.1f, 1f, brightness)));
                      // drawGradientLine(e0x,e0y,edge[0].z,e1x,e1y,edge[1].z,g2,4);
			g2.drawLine(e0x, e0y, e1x, e1y);
		}
	}
}
