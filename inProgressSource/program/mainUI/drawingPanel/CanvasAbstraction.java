package program.mainUI.drawingPanel;

import java.util.Vector;


//this class represents the abstract relationship between nodes in a canvas
//this class will examine relationship, compare things, but never modify anything
public class CanvasAbstraction {
	Vector<CanvasNode> nodes;
	Vector<CanvasNode> heads;
	
	
	public CanvasAbstraction(Canvas2 canvas){
		nodes=canvas.nodes;
		
	}
	
}
