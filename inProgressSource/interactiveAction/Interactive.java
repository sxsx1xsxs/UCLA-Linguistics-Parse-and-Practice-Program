package interactiveAction;

import java.awt.Point;

public interface Interactive {
	
	public void receiveAction(InAction action);	
	public Point getLocationOnScreen();
}
