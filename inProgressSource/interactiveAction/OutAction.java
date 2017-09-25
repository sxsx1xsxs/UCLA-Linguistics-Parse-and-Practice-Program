package interactiveAction;

import java.awt.Component;
import java.awt.Point;
import java.util.Vector;

import program.mainUI.drawingPanel.topComponents.TopMovable;

public class OutAction extends Action {

	public OutAction(Vector<TopMovable> objects, Interactive sender, Interactive receiver) throws Exception {
		super(objects, sender, receiver);
		
		Point senderLocation=sender.getLocationOnScreen();
		Point receiverLocation=receiver.getLocationOnScreen();
		for(TopMovable object:objects){
		Point inLocation=new Point(object.getLocation().x+senderLocation.x-receiverLocation.x,object.getLocation().y+senderLocation.y-receiverLocation.y);	
		object.adjustLocation(inLocation);
		}
		InAction inAction=new InAction(objects, sender, receiver);
	}

}
