package gui.graph;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tree.EvalException;
import tree.ParseException;
import tree.ValueNotStoredException;
import tree.VarStorage;

import gui.MainApplet;
import gui.SubPanel;
import gui.TopLevelContainer;

public class GraphWindow extends SubPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainApplet mainApp;
	private int textHeight;
	private Graph largeGraph;
	private JPanel graph;
	private Selection selection;
	private BufferedImage buffer;
	private double mouseX,
	mouseY, mouseRefX, mouseRefY;
	boolean refPoint, dragSelection, justFinishedPic, isTimeToRedraw, movingSelectionEnd,
			draggingGraph, dragDiskShowing;
	private int heightInfoBar = 40;
	private Runnable current;
	private Object currObj;
	
	public GraphWindow(MainApplet mainApp, TopLevelContainer topLevelComp, int xSize, int ySize){
		super(topLevelComp);
		this.setPreferredSize(new Dimension(xSize, ySize));
		this.mainApp = mainApp;
		largeGraph = new Graph(this, mainApp);
		selection = new Selection(largeGraph, Color.orange);
		dragSelection = false;
		movingSelectionEnd = false;
		largeGraph.AddComponent(selection);
		buffer = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		repaint();
		
		graph = new JPanel(){
			
			public void paint(Graphics g) {
				largeGraph.repaint(g);
			}
		};
		
		graph.addMouseListener(new MouseListener(){
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				refPoint = true;
				mouseRefX = e.getX();
				mouseRefY = e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				refPoint = false;
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
				if (Math.sqrt(Math.pow(e.getX() - largeGraph.X_SIZE/2.0, 2) + 
						Math.pow(e.getY() - largeGraph.Y_SIZE/2.0, 2)) <= largeGraph.getDragDisk().getPixelRadius()){
					if (largeGraph.getDragDisk().isShowingDisk()){
						draggingGraph = true;
						dragSelection = false;
						mouseX = e.getX();
						mouseY = e.getY();
						return;
					}
				}
				if (selection.getStart() != selection.EMPTY){
					if (Math.abs(e.getX() - selection.gridXPtToScreen(selection.getEnd())) < 10){
						movingSelectionEnd = true;
						dragSelection = true;
						return;
					}
					else if (Math.abs(e.getX() - selection.gridXPtToScreen(selection.getStart())) < 10){
						if (selection.getEnd() == selection.EMPTY){
							movingSelectionEnd = true;
							dragSelection = true;
							return;
						}
						movingSelectionEnd = false;
						dragSelection = true;
						return;
					}
				}
				selection.setStart(e.getX() * largeGraph.X_PIXEL + largeGraph.X_MIN);
				
				//snap if close to a notch on the grid
				if (Math.abs((selection.getStart() - Math.round(selection.getStart() / 
						largeGraph.X_STEP) * largeGraph.X_STEP) / largeGraph.X_PIXEL) < 8){
					selection.setStart(Math.round(selection.getStart() / largeGraph.X_STEP) * largeGraph.X_STEP);
				}
				
				selection.setEnd(selection.EMPTY);
				movingSelectionEnd = true;
				dragSelection = true;
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				largeGraph.getDragDisk().setShowingDisk(false);
				repaint();
				dragSelection = false;
				draggingGraph = false;
			}
			
		});
		
		graph.addMouseMotionListener(new MouseMotionListener(){
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
				if (draggingGraph){
					largeGraph.shiftGraph(mouseX - e.getX(), e.getY() - mouseY);
					mouseX = e.getX();
					mouseY = e.getY();
				}
				if (dragSelection){
					if (movingSelectionEnd){
						if (e.getX() < selection.gridXPtToScreen(selection.getStart())){
							selection.setEnd(selection.getStart());
							selection.setStart(selection.ScreenXPtToGrid(e.getX()));
							movingSelectionEnd = !movingSelectionEnd;
							return;
						}
						
						selection.setEnd(selection.ScreenXPtToGrid(e.getX()));
						if (Math.abs((selection.getEnd() - Math.round(selection.getEnd() / 
								largeGraph.X_STEP) * largeGraph.X_STEP) / largeGraph.X_PIXEL) < 8){
							selection.setEnd(Math.round(selection.getEnd() / largeGraph.X_STEP) * largeGraph.X_STEP);
						}
					}
					else{
						
						if (e.getX() > selection.gridXPtToScreen(selection.getEnd())){
							selection.setStart(selection.getEnd());
							selection.setEnd(selection.ScreenXPtToGrid(e.getX()));
							movingSelectionEnd = !movingSelectionEnd;
							return;
						}
						
						selection.setStart(selection.ScreenXPtToGrid(e.getX()));
						if (Math.abs((selection.getStart() - Math.round(selection.getStart() / 
								largeGraph.X_STEP) * largeGraph.X_STEP) / largeGraph.X_PIXEL) < 8){
							selection.setStart(Math.round(selection.getStart() / largeGraph.X_STEP) * largeGraph.X_STEP);
						}
					}
				}
				
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				if (Math.sqrt(Math.pow(e.getX() - largeGraph.X_SIZE/2.0, 2) + 
						Math.pow(e.getY() - largeGraph.Y_SIZE/2.0, 2)) <= largeGraph.getDragDisk().getPixelRadius()){
					largeGraph.getDragDisk().setShowingDisk(true);
				}
				else{
					largeGraph.getDragDisk().setShowingDisk(false);
				}
				repaint();	
			}
			
		});
		
		this.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stu
				try {
					largeGraph.zoom(100 - e.getWheelRotation() * 5);
					Runnable timer = new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							repaint();
						}
						
					};
					javax.swing.SwingUtilities.invokeLater(timer);
				} catch (EvalException e1) {
					//TODO Auto-generated catch block
					//need to do something for errors
				}
			}
			
		});
		
		GridBagConstraints bCon = new GridBagConstraints();
		bCon.fill = GridBagConstraints.BOTH;
		bCon.weightx = 1;
		bCon.weighty = 1;
		bCon.gridheight = 7;
		bCon.gridwidth = 1;
		bCon.gridx = 0;
		bCon.gridy = 0;
		this.add(graph, bCon);
		this.setPreferredSize(new Dimension(xSize, ySize));
	}
	
//	public void repaintBuffer(){
//		buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//		Graphics g = buffer.getGraphics();
//		textHeight = g.getFontMetrics().getHeight();
//		g.setColor(Color.white);
//		g.fillRect(0, 0, getWidth(), getHeight());
//		g.drawImage(largeGraph.getGraphPic(), 0, 0, null);
//		g.dispose();
//		graph.repaint();
//	}
	
//	
//	public void repaintAxis(){
//		justFinishedPic = false;
//		repaint();
//	}
//	
//	public void tempZoom(double rate){
//		Graphics g = buffer.getGraphics();
//		int xSize = graph.getWidth();
//		int ySize = graph.getHeight();
//		if ( rate < 100){
//			BufferedImage tempImg = new BufferedImage(xSize - 2 * ((int) (xSize * ((100 - rate)/100))),
//					ySize - 2 * ((int) (ySize * ((100 - rate)/100))), BufferedImage.TYPE_4BYTE_ABGR);
//			Graphics tempG = tempImg.getGraphics();
//			tempG.drawImage(buffer, 0, 0, tempImg.getWidth(), tempImg.getHeight(), 0, 0, xSize, ySize, null);
//			g.setColor(Color.gray);
//			g.fillRect(0, 0, xSize, ySize);
//			g.drawImage(tempImg,
//					(int) (xSize * ((100 - rate)/100)), (int) (ySize * ((100 - rate)/100)), Color.gray, null);
//		}
//		else{
//			g.drawImage(buffer, 0, 0, xSize, ySize,
//					(int) (xSize * ((rate - 100)/100)), (int) (ySize * ((rate - 100)/100)), (int) (xSize - xSize * ((rate - 100)/100))
//					, (int) (ySize - ySize * ((rate - 100)/100)), Color.gray, null);
//		}
//		repaint();
//		try {
//			largeGraph.zoom(rate);
//		} catch (EvalException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	public int getGraphWidth(){
		if (this.getWidth() == 0){
			return 400;
		}
		return this.getWidth();
	}
	
	public int getGraphHeight(){
		if (this.getWidth() == 0){
			return 400;
		}
		return this.getHeight() - getInfoBarHeight();
	}
	
	public int getInfoBarHeight(){
		return 5 + 2 * textHeight;
	}

	public int getHeightInfoBar() {
		return heightInfoBar;
	}
	
}
