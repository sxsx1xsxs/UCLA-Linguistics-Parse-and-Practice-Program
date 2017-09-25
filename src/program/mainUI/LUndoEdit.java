package program.mainUI;

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;

import program.mainUI.drawingPanel.Line;
import program.mainUI.drawingPanel.NodeLabel;

public class LUndoEdit extends AbstractUndoableEdit {

	protected NodeLabel dl;
	protected Point beforeMove;
	protected Point endMove;
	protected NodeLabel head;
	protected NodeLabel end;
	protected Line line;
	
	public LUndoEdit(){
		
	}
	
	
	//for re/undo adding nodes
	public LUndoEdit(NodeLabel dragLabel){
		this.dl=dragLabel;
	}

	
	//for re/undo moving nodes
	public LUndoEdit(NodeLabel dragLabel,Point beforeMove){
		this.dl=dragLabel;
		this.beforeMove=beforeMove;
		this.endMove=dragLabel.location;
	}
	
	
	//for re/undo adding lines
	
	public LUndoEdit(NodeLabel head, NodeLabel end){
		this.head=head;
		this.end=end;
		
	}
	
    //add lines by dragging
	public LUndoEdit(Line line){
		this.line=line;
		
	}
	
	
	
	

}
