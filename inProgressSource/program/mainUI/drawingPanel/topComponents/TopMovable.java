package program.mainUI.drawingPanel.topComponents;

import java.awt.Point;


//this interface describes object's the capability of moving around on Top
//it also suggests that the object can be transferred from Interactive Platform to Top
public interface TopMovable {
	//set the field location in TopLabel and the field mouseLocation in TopLine
	public void setLocation(Point p);
	//returns the field location in TopLabel and the field mouseLocation in TopLine
	public Point getLocation();
	//used in InAction to adjust the location of the object from the point relative to sender to the point relative to the receiver
	public void adjustLocation(Point x);
	//change the object according to object type, relative to the mouse location
	//used in Top.[mouseDragged] and Top.[mouseMoved]
	public void changeByMouse(Point x);
	
}
