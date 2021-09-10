package program.mainUI.drawingPanel;

import java.awt.*;


public class Line {

	public Point start = new Point(0, 0);// the position of children head
	public Point end = new Point(0, 0);// the position of parent bottom
	int color = 0;
	// color=0, black normal form without bold
	// color=3, 994C00 orange bold for ready lines to be paired up
	// color=6, FF66FF pink bold for addInDragLabel line
	double x1 = 0;// touch point distance to start in x
	double y1 = 0;
	double x2 = 0;
	double y2 = 0;
	public NodeLabel parent = null;
	public NodeLabel children = null;
	//for those functions that interacts with Plain
	protected Plain plain;

	public Line(Plain p){
		attachTo(p);
	}
	
	public Line(){
		
	}

	public int maxX(){
		return Math.max(start.x,end.x);
	}
	public int minX(){
		return Math.min(start.x,end.x);
	}
	public int maxY(){
		return Math.max(start.y,end.y);
	}
	public int minY(){
		return Math.min(start.y,end.y);
	}

	public void attachTo(Plain p){
		plain=p;
	}


	public void paintSelf(Graphics2D g){
		// black plain form
		if (color == 0) {
			g.setStroke(new BasicStroke(1));
			g.setColor(Color.black);
		}
		// orange bold ready form
		else if (color == 3) {
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.decode("0x994C00"));
		} else if (color == 6) {
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.decode("0xFF66FF"));
		}
		// blue bold chosen form
		else {
			g.setStroke(new BasicStroke(2));
			g.setColor(Color.blue);
		}
		g.drawLine(start.x, start.y ,
				end.x , end.y );
	}

	public void addparent(NodeLabel ha) {
		parent = ha;
		ha.childrenlines.addElement(Line.this);
		end = new Point(ha.location.x + ha.getWidth() / 2,
				ha.location.y + ha.label.getLocation().y + ha.label.getHeight());
		if (children != null) {
			children.parents.add(ha);
			ha.children.add(children);
		}
	}

	public void addchildren(NodeLabel ha) {
		children = ha;
		ha.parentlines.addElement(Line.this);
		start = new Point(ha.location.x + ha.getWidth() / 2, ha.location.y + ha.label.getLocation().y);
		if (parent != null) {
			parent.children.add(ha);
			ha.parents.add(parent);
		}
	}

	// if either parent or child moves, update the line
	public void update() {
		if (children != null)
			start = new Point(children.location.x + children.getWidth() / 2,
					children.location.y + children.label.getLocation().y);
		if (parent != null)
			end = new Point(parent.location.x + parent.getWidth() / 2,
					parent.location.y + parent.label.getLocation().y + parent.label.getHeight());
	}

	// when a line doesn't have parent or child, it pairs up the line with
	// nodes in range
	public void checkfornodes() {

		if (parent == null) {
			NodeLabel colored = null;

			for (NodeLabel x : plain.list) {
				if (x != children) {
					if (x.distanceToLabel(end) != 0) {
						if (colored == null) {
							colored = x;
						} else {
							if (x.distanceToLabel(end) < colored.distanceToLabel(end)) {
								colored = x;
							}
						}
					}

				}
			}
			if (colored != null) {
				addparent(colored);
			}
		}

		if (children == null) {
			NodeLabel colored = null;

			for (NodeLabel x : plain.list) {
				if (x != parent) {
					if (x.distanceToLabel(start) != 0) {
						if (colored == null) {
							colored = x;
						} else {
							if (x.distanceToLabel(start) < colored.distanceToLabel(start)) {
								colored = x;
							}
						}
					}

				}
			}
			if (colored != null) {
				addchildren(colored);
			}
		}
	}
	public Line makeCopy(Plain p){
		return new Line(p);
	}
	public boolean inRange(Point p) {
		int startX = start.x;
		int startY = start.y;
		int endX = end.x;
		int endY = end.y;
		return inRangeMath(startX,startY,endX,endY,p);
	}
	public boolean inRangeMath(int startX, int startY, int endX, int endY, Point p){
		// decides if a point p is in range of a line by projection
		int px = p.x;
		int py = p.y;
		double length = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
		// Create a projection of point p onto the line, centered on start
		// Perp is the perpindicular coordinate, par is the parallel point to the line projection
		// to do this we need to make a vector, we do so below.
		int vecX = endX - startX;
		int vecY = endY - startY;
		double par = ((double)(px - startX)*vecX + (py-startY)*vecY)/length;
		if(par > 0 && par < length){// if the points projection is on the line
			double pointLengthSquared = (px - startX) * (px - startX) + (py - startY) * (py - startY);
			// since we only care about the magnitude of the perpindicular line, we calculate the square by pythagorean theorem
			double perpSquare = pointLengthSquared - par*par;
			return perpSquare <= 25;
		}
		return false;
	}
}
