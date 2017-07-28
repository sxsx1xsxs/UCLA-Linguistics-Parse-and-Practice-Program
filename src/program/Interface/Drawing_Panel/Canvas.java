package program.Interface.Drawing_Panel;

import java.io.Serializable;
import java.util.Vector;

public class Canvas implements Serializable{
	private Vector<NodeLabel> nodes;
	private Vector<Line> lines;
	
	
	//?? don't know whether necessary or not
	public Canvas(){
		
	}
	
	public Canvas(String serialized_string){
		//needs to include the resize the whole picture part
	}
	
	//??return type probably can be changed
	private String serialize(){
		return null;
	}
	
	
	
}
