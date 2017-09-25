package program.mainUI.drawingPanel.topComponents;

import java.awt.Point;


//the lines that has one fixed end and one other end that changes according to the mouseLocation
public class ExtensibleLine extends TopLine{

	//Point1 from TopLine is the fixed point
	//Point2 from TopLine is the moving point

	@Override
	public void changeByMouse(Point mouse) {
		int x_move=mouse.x-mouseLocation.x;
		int y_move=mouse.y-mouseLocation.y;
		point2=new Point(point2.x+x_move,point2.y+y_move);
		mouseLocation=mouse;		
	}
	
	public ExtensibleLine(Point fixedEnd, Point movingEnd, Point mouseLocation){
		super(fixedEnd, movingEnd,mouseLocation);
	}

}
