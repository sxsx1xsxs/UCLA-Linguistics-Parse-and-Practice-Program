package program.mainUI.drawingPanel;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class CanvasNode extends JLabel implements CanvasComponent,Comparable<CanvasNode>{
	//	the following fields can be stored in parent class
	//	private String text;==>super.getText();
	//	private Point location;==>super.getlocation();
	//	private boolean visible=true;super.isVisible();

	CanvasNode parent=null;
	Vector<CanvasNode> children=new Vector<CanvasNode>();
	Vector<CanvasLine> connectedLines=new Vector<CanvasLine>();

	transient Canvas2 canvas;


	//used in [chosen] and [readyToDrawLine]
	private final String ACTION_COLOR="#0066cc";
	//type==1 : word in the sentence
	//type==0 : terminal or non-terminal (Default)
	public int type;
	//used in [adjustOneTree] in CanvasWithAction to record the bottom-up level in the tree the node belongs to
	transient public int level;

	//--------------------------------------------------------------------------------------------------

	//constructor
	//if text==null or text=="", CanvasNode cannot be created
	public CanvasNode(Canvas2 canvas, String text, Point point) throws Exception{
		super(text);
		if(text==null||text.equals("")){
			throw new Exception("CanvasNode with empty text cannot be created");
		}else if(point==null){
			throw new Exception("CanvasNode without a valid location cannot be created");
		}
		this.canvas=canvas;
		setToDefault();
		this.setLocation(point);
		this.setSize(this.getPreferredSize());
	}

	//--------------------------------------------------------------------------------------------------

	// returns -1 if inside the label
	// returns 0 if the label is outside the outer border
	// returns a integer number larger than 0 for the distance from the point to the label
	// passing in the point location relative to canvas
	// used in CanvasWithAction.CAWMouseAdapter
	public int distanceToLabel(Point p) {
		int height=this.getPreferredSize().height;
		int width=this.getPreferredSize().width;
		// the left corners x-cor for inner layer
		int xi1 = this.getLocation().x;
		// the right corners x-cor for inner layer
		int xi2 = this.getLocation().x+width;
		// the up corners y-cor for inner layer
		int yi1 = this.getLocation().y;
		// the lower corners y-cor for inner layer
		int yi2 = this.getLocation().y+height;

		// the left corners x-cor for outlayer
		int xo1 = xi1 - height;
		// the right corners x-cor for outlayer
		int xo2 = xi2 + height;
		// the up corners y-cor for outlayer
		int yo1 = yi1 - height;
		// the lower corners y-cor for outlayer
		int yo2 = yi2 + height;

		int x = p.x;
		int y = p.y;

		if (x <= xo2 && x >= xo1 && y >= yo1 && y <= yo2) {
			if (!(x <= xi2 && x >= xi1 && y >= yi1 && y <= yi2)) {
				if (x <= xi2 && x >= xi1) {
					if (y < yi1) {
						return yi1 - y;
					} else {
						return y - yi2;
					}
				} else if (y >= yi1 && y <= yi2) {
					if (x < xi1) {
						return xi1 - x;
					} else {
						return x - xi2;
					}
				} else if (x < xi1 && x >= xo1) {
					if (y < yi1) {
						if (yi1 - y > xi1 - x) {
							return yi1 - y;
						} else {
							return xi1 - x;
						}
					} else {
						if (y - yi2 > xi1 - x) {
							return y - yi2;
						} else {
							return xi1 - x;
						}
					}
				} else {
					if (y < yi1) {
						if (yi1 - y > x - xi2) {
							return yi1 - y;
						} else {
							return x - xi2;
						}
					} else {
						if (y - yi2 > x - xi2) {
							return y - yi2;
						} else {
							return x - xi2;
						}
					}
				}

			} else {
				return -1;
			}
		}

		return 0;

	}


	public void readyToDrawLine() {
		this.setForeground(Color.decode(ACTION_COLOR));		
	}

	//set the CanvasNode to default form
	@Override
	public void setToDefault() {
		this.setBorder(null);
		this.setForeground(Color.decode("#000000"));
		//because the JLabel is going to be added to a null-layout-JPanel,
		//so the size needs to be set
		this.setSize(this.getPreferredSize());
	}


	//used in [nodesAddable] in Canvas2
	public boolean overlappedWith(CanvasNode x) {
		//only when the two CanvasNodes are on the same canvas
		if(canvas==x.canvas){
			//store the four corner points of x into a vector
			Vector<Point> fourCorners=new Vector<Point>();
			fourCorners.add(x.getLocation());
			fourCorners.add(new Point(x.getLocation().x+x.getSize().width,x.getLocation().y));
			fourCorners.add(new Point(x.getLocation().x+x.getSize().width,x.getLocation().y+x.getSize().height));
			fourCorners.add(new Point(x.getLocation().x,x.getLocation().y+x.getSize().height));

			//check whether any of x's corners is within this CanvasNode's range
			for(Point now:fourCorners){
				if(now.x<=this.getLocation().x+this.getSize().width&&now.x>=this.getLocation().x
						&&now.y<=this.getLocation().y+this.getSize().height&&now.y>=this.getLocation().y){
					return true;
				}

			}
		}
		return false;
	}


	//--------------------------------------------------------------------------------------------------

	public static void main(String args[]) throws Exception{
		Canvas2 canvas=new Canvas2();
		CanvasNode node=new CanvasNode(canvas,"hi",new Point (20,20));
	}


	@Override
	// used in CanvasWithAction.CAWMouseAdapter
	// change the CanvasNode's appearance when chosen--similar to when enter the CanvasLabel
	// if it has mouseMotionListener
	public void chosen() {
		this.setBorder(new LineBorder(Color.BLUE));
		this.setForeground(Color.decode(ACTION_COLOR));
		this.setSize(this.getPreferredSize());
	}


	@Override
	public void showRightClickMenu(Point location) {
		//only show rightClickMenu to terminals and non-terminals
		if(type!=1){
		CanvasComponentRightClickMenu rightClickMenu=new CanvasComponentRightClickMenu(CanvasNode.this);
		rightClickMenu.showRightClickMenu(canvas, location);}
	}

	
	//user: CanvasWithAction when deleting a particular CanvasNode
	//will remove that CanvasNode from the Canvas, but change nothing inside the CanvasNode
	//will remove associated lines from the Canvas, but nothing inside those CanvasLines will be changed
	@Override
	public void removeFromCanvas() {
		//remove the node from the canvas
		canvas.nodes.remove(this);
		canvas.remove(this);
		//update the parent of its children
		for(CanvasNode x:this.children){
			x.parent=null;
		}
		//update the childrenlist of its parent
		if(this.parent!=null){
			this.parent.children.remove(this);
		}

		//update its connectedLines' the-other-end CanvasNode
		//different from directly calling x.removeFromCanvas
		for(CanvasLine x:this.connectedLines){
			canvas.lines.remove(x);
			if(x.parent==this){
				x.child.connectedLines.remove(x);
			}else{
				x.parent.connectedLines.remove(x);
			}
		}
		canvas.repaint();
	}

	@Override
	public int compareTo(CanvasNode o) {
		int midxo = o.getLocation().x + o.getWidth() / 2;
		int midx = getLocation().x + getWidth() / 2;

		// ascending order
		// return midx - midxo;

		// descending order
		return midxo - midx;
	}


}
