package program.mainUI.drawingPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;

import interactiveAction.InAction;
import interactiveAction.Interactive;
import interactiveAction.OutAction;
import program.mainUI.drawingPanel.topComponents.ExtensibleLine;
import program.mainUI.drawingPanel.topComponents.TopLabel;
import program.mainUI.drawingPanel.topComponents.TopLine;
import program.mainUI.drawingPanel.topComponents.TopMovable;

//for now it is assumed that Top is the top layer of Plain, this can later be extended or
//separated
public class Top extends JPanel implements Interactive{
	//the member function of it will be called in other components under Top
	//because other components needs to mimic that the mouseEvent is happening on Top
	TopMouseAdapter mouseAdapter;	
	//helper, storing whether there is a label ready to be moved in the [mouseMoved] or [mouseDragged]
	//setter: [receiveAction] user:[moveLabel] ender:[mouseReleased]
	private TopLabel label;
	//helper, storing whether there is a line being drawn in [mouseDragged]
	//setter: [receiveAction] user:[drawLine] ender:[mouseReleased]
	private Vector<TopLine> lines=new Vector<TopLine>();

	//action connector, will be accessed in Plain constructor to assign Plain.this to Top.this.plain 
	Plain plain;

	//--------------------------------------------------------------------------------------------------

	//the method this class will be called outside Top because other components under Top needs to mimic 
	//that the mouse action is happening on Top
	public class TopMouseAdapter extends MouseAdapter{
		public void doMouseEvent(MouseEvent e){
			if(e.getID()==MouseEvent.MOUSE_DRAGGED){
				mouseDragged(e);
			}else if(e.getID()==MouseEvent.MOUSE_RELEASED){
				mouseReleased(e);
			}
		}

		private void moveLabel(MouseEvent e){
			if(label!=null){
				int label_width=label.getSize().width;
				int label_height=label.getSize().height;
				label.setLocation(e.getX()-label_width/2, e.getY()-label_height/2);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println("Top: Pressed");
		}

		@Override
		public void mouseDragged(MouseEvent e) {
//			System.out.println("Top: Dragged");			
			moveLabel(e);
			drawLine(e);
		}

		private void drawLine(MouseEvent e) {
			if(lines.size()!=0){
				for(TopLine line:lines){
					line.changeByMouse(e.getPoint());
					Top.this.repaint();
				}
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
//			System.out.println("Top: Released");
			Vector <TopMovable>outSet=new Vector<TopMovable>();

			//since now assumes Top is the top layer of plain, so the position of e==the position of
			//mouse on plain

			//if there is an active label on Top
			if(label!=null){	
				Point release_pos=new Point(e.getPoint().x-label.getSize().width/2,e.getPoint().y-label.getSize().height/2);
				label.setLocation(release_pos);
				outSet.add(label);										
				Top.this.remove(label);
				Top.this.repaint();			
				label=null;
			}
			//if there is an active line on Top
			if(lines.size()!=0){				
				for(TopLine line:lines){
					line.setLocation(e.getPoint());
					outSet.add(line);			
				}
				Top.this.repaint();			
				lines=new Vector<TopLine>();				
			}

			if(outSet.size()>0){
				Interactive currentInteractivePanel=(Interactive) plain.getPanel(e.getPoint());
				//if the label is released inside Canvas
				if(currentInteractivePanel instanceof InteractiveCanvas){
					try {
						OutAction action=new OutAction(outSet,Top.this,currentInteractivePanel);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

			//delete temporary things on top and clean InteractiveCanvas's restoring container
			//make sure to restore things properly before this in receiving action in CanvasWithAction
			plain.drawingArea.cleanThingsToRestore();
			disableMouseAdapter();

		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			System.out.println("Top: Moved");			
			moveLabel(e);

		}

	}

	//--------------------------------------------------------------------------------------------------	

	//constructor
	public Top(){
		this.setLayout(null);
		this.setOpaque(false);
		mouseAdapter=new TopMouseAdapter();	
		//enableMouseListener();
	}

	//--------------------------------------------------------------------------------------------------

	//will be called in TopMouseAdapter.mouseReleased()
	//based on the assumption that at most one MouseAdapter named mouseAdapter is added to Top object
	private void disableMouseAdapter() {
		this.removeMouseListener(mouseAdapter);
		this.removeMouseMotionListener(mouseAdapter);

	}

	//--------------------------------------------------------------------------------------------------

	//when this function is called from outside, the other objects make sure there is a meaning action
	public void receiveAction(InAction action) {
		Vector<TopMovable>objects=action.getObject();
		for(TopMovable object:objects){
			if(object instanceof TopLabel){
				label=(TopLabel)object;
				label.setForeground(Color.decode("0x7B7C80"));
				this.add(label);
				this.repaint();

			}else if(object instanceof TopLine){
				lines.add((TopLine)object);
			}
		}

	}

	//will be called in ConnectedGrammarPanelScrollable's mouseReleased in the situation of
	//adding a NodeLabel to Top by clicking on the NodeLabel on the grammarPanel and then move the mouse
	public void enableMouseListener() {
		if(this.getMouseListeners().length==0){
			this.addMouseListener(mouseAdapter);
		}
		if(this.getMouseMotionListeners().length==0){
			this.addMouseMotionListener(mouseAdapter);
		}
	}

	@Override
	public void paint(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		g2.setStroke(new BasicStroke(2));
		g.setColor(Color.blue);
		for(TopLine line:lines){
		try {
			g.drawLine(line.point1.x, line.point1.y, line.point2.x, line.point2.y);
		}
		catch (NullPointerException e) {
		}

		}
	}

	//--------------------------------------------------------------------------------------------------

	public static void main(String args[]){
		//		Top top=new Top();
	}


}
