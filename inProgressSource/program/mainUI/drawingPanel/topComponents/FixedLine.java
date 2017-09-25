package program.mainUI.drawingPanel.topComponents;

import java.awt.Point;

//the lines that are fixed in shape (opposite to ExtensibleLine) but can be moved around
public class FixedLine extends TopLine{
	
	@Override
	public void changeByMouse(Point mouse) {
		int x_move=mouse.x-mouseLocation.x;
		int y_move=mouse.y-mouseLocation.y;
		point1=new Point(point1.x+x_move,point1.y+y_move);
		point2=new Point(point2.x+x_move,point2.y+y_move);
		mouseLocation=mouse;		
	}
	
	public FixedLine(Point point1, Point point2, Point mouseLocation){
		super(point1,point2,mouseLocation);
	}



}
