package program.mainUI.drawingPanel.topComponents;

import java.awt.Point;

//super class of ExtensibleLine and FixedLine
abstract public class TopLine implements TopMovable{
	
	public Point point1;
	public Point point2;
	//this mouseLocation records the mouse position when the line was created
	public Point mouseLocation;
	
	public TopLine(Point point1, Point point2, Point mouseLocation){
		this.point1=point1;
		this.point2=point2;
		this.mouseLocation=mouseLocation;
	}
	
	@Override
	public void adjustLocation(Point location) {
		int x_adjust=location.x-mouseLocation.x;
		int y_adjust=location.y-mouseLocation.y;
		point1=new Point(point1.x+x_adjust, point1.y+y_adjust);
		point2=new Point(point2.x+x_adjust, point2.y+y_adjust);
		mouseLocation=location;	
	}
	
	@Override
	public Point getLocation() {
		return mouseLocation;
	}
	
	@Override
	public void setLocation(Point p) {
		mouseLocation=p;		
	}
}
