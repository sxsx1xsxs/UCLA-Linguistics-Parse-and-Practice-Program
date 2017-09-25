package interactiveAction;

import java.awt.Component;
import java.awt.Point;
import java.util.Vector;

import program.mainUI.drawingPanel.topComponents.TopMovable;


abstract public class Action {
	
	private Vector<TopMovable> object;
	private Interactive sender;
	private Interactive receiver;

	public Action(Vector<TopMovable> object, Interactive sender, Interactive receiver) throws Exception{
		if(object==null||sender==null||receiver==null){
			throw new Exception("Class Action cannot be constructed using null arguments");
		}
		this.object=object;
		this.sender=sender;
		this.receiver=receiver;

	}
	
	public Vector<TopMovable> getObject(){
		return this.object;
	}
	
	public Interactive getSender(){
		return this.sender;
	}
	
	public Interactive getReceiver(){
		return this.receiver;
	}
	
	
	public static void main(String args[]) throws Exception{

	}
}
