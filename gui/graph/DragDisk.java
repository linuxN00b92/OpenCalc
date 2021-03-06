package gui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DragDisk extends GraphComponent {
	
	private int pixelRadius = 40;
	private Color color;
	private boolean showingDisk;

	public DragDisk(Graph g, Color c) {
		super(g);
		color = c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		if (showingDisk){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
			g2d.fillOval(graph.X_SIZE/2 - pixelRadius, graph.Y_SIZE/2 - pixelRadius, pixelRadius * 2, pixelRadius * 2);
			g2d.setColor(Color.BLACK);
			g2d.drawString("Click to", graph.X_SIZE/2 - 
					(g2d.getFontMetrics().stringWidth("Click to")/2), 
					graph.Y_SIZE/2 - g2d.getFontMetrics().getHeight() + 7);
			g2d.drawString("Drag", graph.X_SIZE/2 - (g2d.getFontMetrics().stringWidth("Drag")/2)
					, graph.Y_SIZE/2 + (g2d.getFontMetrics().getHeight() - 7) * 2);
		}
		else{
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 25));
			g2d.fillOval(graph.X_SIZE/2 - pixelRadius, graph.Y_SIZE/2 - pixelRadius, pixelRadius * 2, pixelRadius * 2);
		}
	}
	
	public int getPixelRadius(){
		return pixelRadius;
	}
	
	public boolean isShowingDisk(){
		return showingDisk;
	}
	
	public void setShowingDisk(boolean b){
		showingDisk = b;
	}

}
