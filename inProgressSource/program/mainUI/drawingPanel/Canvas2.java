package program.mainUI.drawingPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canvas2 extends JPanel implements Serializable{
	//this is equal to labelHeight, it serves as the length unit of the whole canvas
	protected int unit;
	//the rectangle indicates the bound of all nodes and lines
	protected Rectangle bound;
	protected Vector<CanvasNode> nodes=new Vector<CanvasNode>();
	transient protected Vector<CanvasLine> lines=new Vector<CanvasLine>();
	//two double indicating the horizontal and vertical distances between nodes
	protected double heightIndex=1.5;
	protected double widthIndex=1;
	
	//--------------------------------------------------------------------------------------------------	

	//?? don't know whether necessary or not
	public Canvas2(){
		this.setLayout(null);
		JLabel x=new JLabel("unit");
		unit=x.getPreferredSize().height;
	}

	public Canvas2(String serialized_string){
		//needs to include the resize the whole picture part
	}
	
	public void updateLines(){
		
	}

	//--------------------------------------------------------------------------------------------------

	//only when there are CanvasNodes in the appropriate range of both ends of the CanvasLine
	//and those two CanvasNodes are able to accept the new relationship represented by this line,
	//the line can be added
	//while judging for its possibility, the canvasLine's parent and child field will be modified and the 
	//two points will be adjusted according to both ends
	private boolean ableToAddLine(CanvasLine canvasLine) {
		CanvasNode childLabel=relatedLabelAtPosition(canvasLine.start);
		CanvasNode parentLabel=relatedLabelAtPosition(canvasLine.end);
		if(childLabel!=null&&parentLabel!=null&&childLabel!=parentLabel){
			canvasLine.child=childLabel;
			canvasLine.parent=parentLabel;			
			if(childLabel.parent==null){
				canvasLine.start=new Point (childLabel.getLocation().x+childLabel.getSize().width/2,childLabel.getLocation().y);
				canvasLine.end=new Point(parentLabel.getLocation().x+parentLabel.getSize().width/2,parentLabel.getLocation().y+parentLabel.getSize().height);
				return true;
			}
		}
		return false;
	}

	//??return type probably can be changed
	private String serialize(){
		return null;
	}

	//used in [addNode]
	//for now as long as the node is not overlapping with other nodes, it is ok
	private boolean nodeAddable(CanvasNode x) {
		for(CanvasNode now:nodes){
			if(now.overlappedWith(x)){
				return false;
			}
		}
		return true;
	}

	//--------------------------------------------------------------------------------------------------

	protected void addLine(CanvasLine canvasLine) {
		//if able to add this line
		if(ableToAddLine(canvasLine)){
			canvasLine.canvas=Canvas2.this;
			lines.add(canvasLine);
			//update the childNode of the line
			canvasLine.child.parent=canvasLine.parent;
			canvasLine.child.connectedLines.add(canvasLine);
			//update the parentNode of the line
			canvasLine.parent.children.add(canvasLine.child);
			canvasLine.parent.connectedLines.add(canvasLine);			
		}
		//if there is conflict to add this line
		else{

		}
	}

	//used @addNode(JLabel, Point) in CanvasWithAction 
	protected void addNode(CanvasNode x) {
		if(nodeAddable(x)){
			this.add(x);
			this.nodes.addElement(x);
		}
	}

	//--------------------------------------------------------------------------------------------------


	@Override
	public void paint(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		for(CanvasLine x:lines){
			try {
				g2.setStroke(new BasicStroke(x.width));
				g.setColor(Color.decode(x.color));
				g.drawLine(x.start.x, x.start.y, x.end.x, x.end.y);
			}
			catch (NullPointerException e) {
			}
		}



	}


	//call [chosenLabelAtPosition] and [closestLabelInRange] to return the one related 
	//CanvasNode at Point p
	public CanvasNode relatedLabelAtPosition(Point p){
		CanvasNode returnNode=chosenLabelAtPosition(p);
		if(returnNode!=null){
			return returnNode;
		}else{
			returnNode=closestLabelInRange(p);			
		}		
		return returnNode;		
	}


	//return null if Point p is inside no CanvasNode
	//otherwise return the CanvasNode that contains Point p
	public CanvasNode chosenLabelAtPosition(Point p){
		for(CanvasNode node:nodes){
			if(node.distanceToLabel(p)==-1){
				//because Canvas is set for no overlapping nodes,
				//as soon as you find a node, you return the node
				return node;
			}
		}
		return null;

	}


	//return null if Point p is not in CanvasNode's drawing-line range
	//otherwise return the best candidate with the smallest distance
	public CanvasNode closestLabelInRange(Point p){
		CanvasNode candidateNode=null;
		for(CanvasNode node:nodes){
			if(node.distanceToLabel(p)>0){
				if(candidateNode==null){
					candidateNode=node;
				}else{
					if(node.distanceToLabel(p)<candidateNode.distanceToLabel(p)){
						candidateNode=node;
					}
				}
			}
		}		
		return candidateNode;

	}


	//return the CanvasLine in range
	public CanvasLine chosenLine(Point point) {
		for(CanvasLine x:lines){
			if(x.inRange(point)){
				return x;
			}
		}
		return null;
	}


	public void clean() {
		nodes=new Vector<CanvasNode>();
		lines=new Vector<CanvasLine>();
		for (Component x: this.getComponents()){
			this.remove(x);
		}
		this.repaint();
	}


}
