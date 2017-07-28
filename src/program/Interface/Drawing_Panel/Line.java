package program.Interface.Drawing_Panel;

import java.awt.Point;


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
	Plain plain;

	public Line(Plain p){
		attachTo(p);
	}
	
	public Line(){
		
	}
	
	
	public void attachTo(Plain p){
		plain=p;
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

}
