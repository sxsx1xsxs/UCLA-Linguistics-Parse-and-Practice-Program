package program.mainUI.drawingPanel;

import java.awt.Point;

public class CanvasLine implements CanvasComponent{
	//the points are all relative to Canvas, which is a 2000*2000 JPanel with null layoutManager
	Point start;// the position of children head
	Point end;// the position of parent bottom
	
	//accessed in [CanvasNode.removeFromCanvas]
	CanvasNode child;
	CanvasNode parent;
	
	//default color black
	String color="0x000000";
	//default width
	int width=1;
	
	//will be modified when calling [Canvas2.addLine]
	transient public Canvas2 canvas;
	
	
	//--------------------------------------------------------------------------------------------------	
	
	//when constructing the CanvasLine, compare the two points and based the which is larger on y
	//put them respectively to point1 and point2
	//no horizontal line can be constructed on Canvas -- throws Exception
	public CanvasLine(Point point1, Point point2) throws Exception{
		if(point1.y==point2.y){
			throw new Exception("Horizontal CanvasLine cannot be constructed");
		}else if(point1.y<point2.y){
			this.start=point2;
			this.end=point1;
		}else{
			this.start=point1;
			this.end=point2;
		}		
	}



	@Override
	public void showRightClickMenu(Point location) {
		CanvasComponentRightClickMenu rightClickMenu=new CanvasComponentRightClickMenu(CanvasLine.this);
		rightClickMenu.showRightClickMenu(canvas, location);
	}


	@Override
	public void chosen() {
		color="#00a9e7";
		width=2;
		canvas.repaint();
	}


	@Override
	public void removeFromCanvas() {
		canvas.lines.remove(CanvasLine.this);		
		try{		
		CanvasLine.this.child.connectedLines.remove(CanvasLine.this);
		CanvasLine.this.child.parent=null;
		CanvasLine.this.parent.connectedLines.remove(CanvasLine.this);
		CanvasLine.this.parent.children.remove(CanvasLine.this.child);
		}
		catch (Exception e){}
		canvas.repaint();
	}
	
	//used in [chosenLine] in Canvas2
	public boolean inRange(Point p) {
		int mx = this.start.x;
		int my = this.start.y;
		int nx = this.end.x;
		int ny = this.end.y;
		int px = p.x;
		int py = p.y;
		double length = Math.sqrt((mx - nx) * (mx - nx) + (my - ny) * (my - ny));
		double rangeLength = length / 5;

		if (mx == nx) {
			if (px - mx > -5 && px - mx < 5) {
				if (my > ny) {
					if (py <= my - rangeLength && py >= ny + rangeLength) {
						return true;
					} else {
						if (py <= ny - rangeLength && py >= my + rangeLength) {
							return true;
						}
					}
				}
			}
		} else if (my == ny) {

			if (mx > nx) {
				if (px <= mx - rangeLength && px >= nx + rangeLength) {
					return true;
				} else {
					if (px <= nx - rangeLength && px >= mx + rangeLength) {
						return true;
					}
				}
			}

		} else {
			double t = (double) (my - ny) / (mx - nx);
			double ct = my - t * mx;
			double tangent = (double) (-1) / (my - ny) * (mx - nx);
			double c = py - tangent * px;
			double x = (c - ct) / (t - tangent);
			double y = x * tangent + c;
			if (((x - px) * (x - px) + (y - py) * (y - py) < 25)) {
				double xx = rangeLength / Math.sqrt(1 + t * t);
				if (mx < nx) {
					if (x < nx - xx && x > mx + xx) {
						return true;
					}
				} else {
					if (x > nx + xx && x < mx - xx) {
						return true;
					}
				}
			}

		}

		return false;
	}


	@Override
	public void setToDefault() {
		width=1;
		color="0x000000";
		canvas.repaint();
	}


	//when either child or parent changes position, this function allows the line to adjust corresponding end
	public void updateByEnds() {
			start = new Point(child.getLocation().x + child.getWidth() / 2,
					child.getLocation().y);
			end = new Point(parent.getLocation().x + parent.getWidth() / 2,
					parent.getLocation().y + parent.getHeight());		
	}
	
	
}
