package interactiveAction;

import java.awt.Component;
import java.awt.Point;
import java.util.Vector;

import program.mainUI.drawingPanel.topComponents.TopMovable;

public class InAction extends Action {

	public InAction(Vector<TopMovable> object, Interactive sender, Interactive receiver) throws Exception {
		super(object, sender, receiver);
		receiver.receiveAction(this);
	}

}
