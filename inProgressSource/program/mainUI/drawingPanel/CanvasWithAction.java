package program.mainUI.drawingPanel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interactiveAction.InAction;
import interactiveAction.Interactive;
import interactiveAction.OutAction;
import program.mainUI.drawingPanel.topComponents.ExtensibleLine;
import program.mainUI.drawingPanel.topComponents.FixedLine;
import program.mainUI.drawingPanel.topComponents.TopLabel;
import program.mainUI.drawingPanel.topComponents.TopLine;
import program.mainUI.drawingPanel.topComponents.TopMovable;

public class CanvasWithAction extends CanvasWithGrammar implements Interactive{

	transient CWAMouseAdapter mouseAdapter;

	//will be modified in InteractiveCanvas to connect them up
	transient InteractiveCanvas interactiveContainer;
	//CanvasWithAction does not receive actions directly from Top but through InteractiveCanvas,
	//but it can send actions to Top and mimic topMouseAdapter
	transient Top top;

	//--------------------------------------------------------------------------------------------------	

	//the mouseAdapter is not added to CanvasWithAction, it only mimics what will happen if some MouseEvent happens;
	//it doesn't make sense when it's not used together with InteractiveCanvas
	class CWAMouseAdapter extends MouseAdapter{


		//helper
		//setter: [mousePressed] user:first [mouseDragged] ender:[mouseReleased]
		//record the point to start the line when drawing a line
		private Point lineStartPoint=null;

		//helper
		//setter: [tellTheSituation] for [mousePressed] user:[tellTheSituation] for [mouseDragged] ender:[mouseReleased]
		//record the CanvasNode that is ready to draw a line
		//need to keep track of it because in the process of dragging, 
		//it needs to have the ready state (the color is different from other normal CanvasNode)
		private CanvasNode lineStartLabel=null;

		//user:InteractiveCanvas.ICMouseAdapter
		public void doMouseEvent(MouseEvent e){
			if(e.getID()==MouseEvent.MOUSE_DRAGGED){
				mouseDragged(e);
			}else if(e.getID()==MouseEvent.MOUSE_RELEASED){
				mouseReleased(e);
			}else if(e.getID()==MouseEvent.MOUSE_MOVED){
				mouseMoved(e);
			}else if(e.getID()==MouseEvent.MOUSE_PRESSED){
				mousePressed(e);
			}
		}


		//control the behavior of things already on the Canvas, like coloring and special action
		//helper used in [mouseMoved], [MousePressed], [mouseDragged] and [mouseReleased]
		//basic idea: go through all the lines and nodes on the Canvas and set them to desired color and motion
		//modify environment fields so that others can know about the situation
		//environment: 
		//<<(invisibleLabel) modified when [mousePressed] on some label ==> if not null, can be the first step for dragging label or clicking label to draw line
		//setter: [mousePressed]==>[tellTheSituation] user:[mouseDragged] [mouseReleased] ender:[mouseReleased] 
		//if pressed on a node, [mousePressed]==>[tellTheSituation] will initialize it,
		//if later dragged, [mouseDragged] will make it invisible by removeing it from canvas, but leave itself unchanged
		//so that if when released, the whole procedure is invalid, it will be restored
		//[mouseReleased] will restore it if applicable and then end it (make it null)
		//>>(invisibleLabel)
		private CanvasNode labelChosen;

		private CanvasLine lineChosen;



		//minor difference when applied in those two functions, so there is anther int argument passed in to indicate where it will be used
		//type 0==> when used in [mouseMoved]
		//type 1==> when used in [mousePressed]
		//type 2==> when used in [mouseDragged] for drawing a line
		//based on the location, tell the right situation
		//return -1 ==> nothing special is happening
		//return 0 ==> ready to choose a CanvasNode
		//return 1 ==> ready to draw a line for a CanvasNode
		private void tellTheSituation(MouseEvent e, int type){

			normalize_canvas();
			//declare local environment that suggests the position of the mouse
			CanvasNode tempChosenNode=null;
			CanvasNode tempReadyNode=null;
			//can either be tempChosenNode or tempReadyNode
			CanvasNode relatedNode=null;
			CanvasLine relatedLine=null;

			//<<first tell the position
			//either inside a Node, or near a node to draw the line, or...		
			//modify the local environment according, 
			//(in a order to improve efficiency because positions are mutually exclusive:
			//if you are inside a node, you can not be in the range of starting a line for a node)
			
			//check whether a line can be chosen
			relatedLine=CanvasWithAction.this.chosenLine(e.getPoint());
			//check whether a node can be chosen
			if(relatedLine==null){
			tempChosenNode=CanvasWithAction.this.chosenLabelAtPosition(e.getPoint());
			//check whether a node is ready to draw a line
			if(tempChosenNode==null){
				tempReadyNode=CanvasWithAction.this.closestLabelInRange(e.getPoint());
				relatedNode=tempReadyNode;
			}
			else{
				relatedNode=tempChosenNode;
			}
			}
			//>>

			//do actions according to the current process

			//if is in the process of drawing a line, 
			if(lineStartLabel!=null){
				//keep the starting node in the right color mode
				lineStartLabel.readyToDrawLine();
				//make the second node in range to the readyToDrawLine mode
				//the node can either be tempChosenNode or tempReadyNode
				if(relatedNode!=null){
					relatedNode.readyToDrawLine();		
				}
			}else if(relatedLine!=null){
				relatedLine.chosen();
				if(e.getButton() == MouseEvent.BUTTON3){
					relatedLine.showRightClickMenu(e.getPoint());
				}
				if(type==1){
					lineChosen=relatedLine;
				}
			}else if(tempChosenNode!=null){
				//first set x to be in chosen mode
				tempChosenNode.chosen();
				if(e.getButton() == MouseEvent.BUTTON3){
					tempChosenNode.showRightClickMenu(e.getPoint());
				}
				//if [tellTheSituation is used in [mousePressed], should set the labelChosen to x
				if(type==1){
					labelChosen=tempChosenNode;
				}
			}else if(tempReadyNode!=null){
				//set it to correct format
				tempReadyNode.readyToDrawLine();
				//will only modify lineStartLabel if [tellTheSituation] is used in [mousePressed]
				if(type==1){
					lineStartLabel=tempReadyNode;
				}				
			}
		}		

		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println("CWA: Pressed");		
			tellTheSituation(e,1);
			//if mouse is pressed near a label to draw a line
			if(lineStartLabel!=null){
				lineStartPoint=e.getPoint();
			}
		}


		private CanvasNode draggedLabel=null;
		private ExtensibleLine newline=null;
		private FixedLine draggedLine=null;

		@Override
		public void mouseDragged(MouseEvent e) {
//			System.out.println("CWA: Dragged");
			Vector<TopMovable>outSet=new Vector<TopMovable>();
			//dragging a label
			if(labelChosen!=null){
				//first [mouseDragged] action in a sequence
				//only dragging terminal and non-terminals but not words in the sentence
				if(draggedLabel==null&&labelChosen.type!=1){
					//to indicate that first [mouseDragged] function already occurred
					draggedLabel=labelChosen;					
					//<<create a new TopLabel and try to send it to Top
					TopLabel movingLabel=new TopLabel(draggedLabel.getText());
					movingLabel.setSize(movingLabel.getPreferredSize());
					Point outLocation=new Point(e.getX()-movingLabel.getSize().width/2,e.getY()-movingLabel.getSize().height/2);
					movingLabel.setLocation(outLocation);
					outSet.add(movingLabel);

					//calculate how much the label has moved, adjust the end of the line accordingly
					int x_adjust=movingLabel.getLocation().x-draggedLabel.getLocation().x;
					int y_adjust=movingLabel.getLocation().y-draggedLabel.getLocation().y;

					for (CanvasLine cline:labelChosen.connectedLines){
						Point fixedEnd;
						Point movingEnd;
						if(cline.parent==labelChosen){
							fixedEnd=new Point(cline.start);
							movingEnd=new Point(cline.end.x+x_adjust,cline.end.y+y_adjust);
						}else{
							fixedEnd=new Point(cline.end);
							movingEnd=new Point(cline.start.x+x_adjust,cline.start.y+y_adjust);
						}
						ExtensibleLine newline=new ExtensibleLine(fixedEnd, movingEnd,e.getPoint());
						outSet.add(newline);
					}

					//make current invisibleLabel off the Canvas					
					draggedLabel.removeFromCanvas();					
				}
				//subsequent [mouseDragged] action
				else{					
					//move the label, leave it to Top
					mimicTopMouseAdapter(e);
				}
			}
			//drawing a line
			else if(lineStartLabel!=null){
				tellTheSituation(e,2);
				//first [mouseDragged] action in a sequence
				if(newline==null){
					//to indicate that first [mouseDragged] function already occurred
					newline=new ExtensibleLine(lineStartPoint,e.getPoint(),e.getPoint());
					newline.setLocation(e.getPoint());
					outSet.add(newline);
				}
				//subsequent [mouseDragged] action
				else{					
					//move the label, leave it to Top
					mimicTopMouseAdapter(e);
				}
			}else if(lineChosen!=null){
				//first [mouseDragged] action in a sequence
				if(draggedLine==null){
					//to indicate that first [mouseDragged] function already occurred
					draggedLine=new FixedLine(lineChosen.start,lineChosen.end,e.getPoint());
					draggedLine.setLocation(e.getPoint());
					outSet.add(draggedLine);
					lineChosen.removeFromCanvas();
				}
				//subsequent [mouseDragged] action
				else{
					//move the label, leave it to Top
					mimicTopMouseAdapter(e);
				}

			}

			if(outSet.size()>0){
				try {
					OutAction sendLabelOut=new OutAction(outSet, CanvasWithAction.this, top);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}


		@Override
		public void mouseReleased(MouseEvent e) {
//			System.out.println("CWA: released");

			mimicTopMouseAdapter(e);

			//set all the environment variables to the initial value
			draggedLabel=null;
			newline=null;
			labelChosen=null;
			lineStartPoint=null;
			lineStartLabel=null;
		}

		//helper for [mouseDragged] and [mouseReleased] when top has the changing item,
		//leave everything to Top
		private void mimicTopMouseAdapter(MouseEvent e) {
			//CanvasWithAction's position on the screen
			Point cwa_pos=CanvasWithAction.this.getLocationOnScreen();
			//top's position on the screen
			Point top_pos=top.getLocationOnScreen();
			//mouseEvent on top created
			MouseEvent e_top = new MouseEvent(top, e.getID(), 0, 0, 
					e.getX()+cwa_pos.x-top_pos.x,e.getY()+cwa_pos.y-top_pos.y, 0, false);
			//mimic that event happens on top
			top.mouseAdapter.doMouseEvent(e_top);			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			System.out.println("CWA: Moved");
			tellTheSituation(e,0);
		}

	}

	//--------------------------------------------------------------------------------------------------	

	//set every node and line on the canvas to default form
	private void normalize_canvas() {
		// set every node to default color and font
		for (CanvasNode x : this.nodes) {
			x.setToDefault();
		}
		for (CanvasLine xy : this.lines) { 
			xy.setToDefault();
		}

	}

	//--------------------------------------------------------------------------------------------------	


	public CanvasWithAction(InteractiveCanvas container){
		this.interactiveContainer=container;
		this.top=container.top;
		mouseAdapter=new CWAMouseAdapter();
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.setPreferredSize(new Dimension(2000,2000));
	}

	@Override
	public void receiveAction(InAction action) {
		for(TopMovable x:action.getObject()){
			if(x instanceof TopLabel){
				JLabel label=(TopLabel) x;
				try {
					CanvasNode node=new CanvasNode(this, label.getText(), label.getLocation());
					CanvasWithAction.this.addNode(node);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(x instanceof TopLine){
				TopLine topline=(TopLine) x;
				try {
					CanvasLine newline=new CanvasLine(topline.point1, topline.point2);
					CanvasWithAction.this.addLine(newline);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//used InteractiveCanvas.cleanThingsToRestore
	//clean the things to restore 
	//things to restore is used for[if some invalid change of CanvasWithAction happens, CanvasWithAction is able to restore things] 
	public void cleanThingsToResore() {
		mouseAdapter.draggedLabel=null;		
	}

	public void inputSentence(String s, Rectangle bounds) {
		clean();
		Point zeroP= bounds.getLocation();
		
		int height = (int) (heightIndex * this.unit);
		int heightdraw = (int) (bounds.height -height-this.unit)+zeroP.y;
		int i = 40+zeroP.x;

		for (String y : s.split(" ")) {
			i = (int) (i + widthIndex * this.unit);
			CanvasNode add;
			try {
				add = new CanvasNode(this,y,new Point(i, heightdraw));
				add.type = 1;
				i = (int) (i + add.getPreferredSize().getWidth());
				this.addNode(add);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.repaint();
		
	}

	public void straightenLine() {
		Vector<CanvasNode> heads = new Vector<CanvasNode>();
		Vector<CanvasNode> words=new Vector<CanvasNode>();
		for (CanvasNode x : nodes) {
			if (x.parent == null) {
				heads.add(x);
			}
			if(x.type==1){
				words.add(x);
			}
		}
		for (CanvasNode y : heads) {
			adjustOneTree(y);
		}

		repaint();
		
	}
	
	private void adjustOneTree(CanvasNode head){
		Stack<CanvasNode> process1 = new Stack<CanvasNode>();

		// recording the max level down of non-words terminal nodes
		int maxnonwords = 0;
		// recording the max level down of words nodes
		int maxwords = 0;

		head.level = 1;

		// first get the value of maxnonwords and maxwords by 2 steps
		// also set the level value of each node from top down
		// later will be reversed to the final value
		// 1.if the head node has no children
		if (head.children.size() == 0) {
			if (head.type == 1) {
				maxwords = 1;
			} else {
				maxnonwords = 1;
			}
		}
		// 2.if the head node has children
		else {
			process1.add(head);
			while (process1.size() > 0) {
				CanvasNode now = process1.pop();

				for (CanvasNode c : now.children) {
					c.level = now.level + 1;
					process1.add(c);
					if (c.children.size() == 0 && c.type != 1) {
						if (c.level > maxnonwords) {
							maxnonwords = c.level;
						}
					}
					if (c.type == 1) {
						if (c.children.size() != 0) {
							// should be error, but ignore for now
						} else {
							if (c.level > maxwords) {
								maxwords = c.level;
							}
						}
					}
				}

			}

		}

		// assume words terminal are all on the same line and in the middle of
		// the screen horizontally

		// if a tree only contains words terminal
		if (maxwords > 0 && maxnonwords == 0) {

			int total = maxwords + 1;
			process1.add(head);

			// record the y-cor for the terminal word node
			int y = 0;

			Stack<CanvasNode> queue = new Stack<CanvasNode>();

			while (process1.size() > 0) {

				// using first in first out strategy
				CanvasNode now = process1.firstElement();
				process1.remove(0);
				queue.add(now);
				if (now.children.size() == 0 && y == 0) {
					y = now.getLocation().y;
				}

				// now reverse the level value from down to top
				// word nodes will be leveled as 1 always
				if (now.type == 1) {
					now.level = 1;
				} else {
					now.level = total - now.level;
				}
				for (CanvasNode x : now.children) {
					process1.add(x);
				}
			}

			// reverse order of BFS to do x-y cor setting for all nodes in the
			// tree
			while (queue.size() > 0) {
				CanvasNode now = queue.pop();
				if (now.level > 1) {
					now.setLocation(new Point(now.getLocation().x,(int) (y - (now.level - 1) * now.getHeight()
							- (now.level - 1) * heightIndex * now.getHeight())));
				}
				if (now.parent !=null && now != head) {
					CanvasNode nowparent = now.parent;
					Collections.sort(nowparent.children);
					int x2 = nowparent.children.firstElement().getLocation().x
							+ nowparent.children.firstElement().getWidth();
					int x1 = nowparent.children.lastElement().getLocation().x;
					nowparent.setLocation(new Point((x2 + x1) / 2 - nowparent.getWidth() / 2,nowparent.getLocation().y));

					for (CanvasNode cw : nowparent.children) {
						try {
							queue.remove(cw);
							if (cw.level > 1) {
								cw.setLocation(new Point(
										cw.getLocation().x,
										(int) (y - (cw.level - 1) * cw.getHeight()- (cw.level - 1) * heightIndex * cw.getHeight()))
										);
							}
						} catch (Exception ww) {

						}
					}
				}
			}
		}
		// if a tree only contains non words terminal
		// fixed position: y of head and levels down, x of the leftmost terminal
		// nodes
		else if (maxnonwords > 0 && maxwords == 0) {
			// the set storing all the terminal nodes in the subtree
			Stack<CanvasNode> terminals = new Stack<CanvasNode>();
			// the set storing all nodes in the subtree
			Stack<CanvasNode> tree = new Stack<CanvasNode>();
			process1.add(head);

			// get the value for set tree
			while (process1.size() > 0) {
				CanvasNode now = process1.pop();
				tree.add(now);
				for (CanvasNode ch : now.children) {
					process1.add(ch);
				}
			}

			// set y-cor for ever node in the subtree
			for (CanvasNode wh : tree) {
				if (wh.level > 1) {
					wh.setLocation(new Point(wh.getLocation().x,(int) (head.getLocation().y
							+ (wh.level - 1) * (wh.getHeight() + wh.getHeight() * heightIndex))));
				}
				if (wh.children.size() == 0) {
					terminals.add(wh);
				}

			}

			// set x-cor for terminals
			Collections.sort(terminals);
			// the fixed x point
			int x = terminals.lastElement().getLocation().x;
			int xnow = x;
			for (int i = terminals.size() - 1; i >= 0; i--) {
				terminals.elementAt(i).getLocation().x = xnow;
				xnow += terminals.elementAt(i).getWidth() + terminals.elementAt(i).getHeight() * widthIndex;
			}

			while (terminals.size() > 0) {
				CanvasNode now = terminals.pop();
				if (now.parent!=null && now != head) {
					CanvasNode nowparent = now.parent;
					Collections.sort(nowparent.children);
					int x1 = nowparent.children.lastElement().getLocation().x;
					int x2 = nowparent.children.firstElement().getLocation().x
							+ nowparent.children.firstElement().getWidth();
					int center = (x1 + x2) / 2;
					nowparent.setLocation(new Point(center - nowparent.getWidth() / 2,nowparent.getLocation().y));
					terminals.add(nowparent);
					for (CanvasNode jh : nowparent.children) {
						try {
							terminals.remove(jh);
						} catch (Exception df) {
						}
					}
				}
			}

		}
		// if a tree contains words terminals and non words terminals
		else {
			Stack<CanvasNode> terminals = new Stack<CanvasNode>();
			Stack<CanvasNode> tree = new Stack<CanvasNode>();
			process1.add(head);

			while (process1.size() > 0) {
				CanvasNode now = process1.pop();
				tree.add(now);
				for (CanvasNode ch : now.children) {
					process1.add(ch);
				}
			}
			for (CanvasNode wh : tree) {

				if (wh.children.size() == 0) {
					terminals.add(wh);

				}

			}
			Collections.sort(terminals);
			// the fixed x point
			int x = terminals.lastElement().getLocation().x;
			int xnow = x;
			for (int i = terminals.size() - 1; i >= 0; i--) {
				terminals.elementAt(i).getLocation().x = xnow;
				xnow += terminals.elementAt(i).getWidth() + terminals.elementAt(i).getHeight() * widthIndex;
			}

			while (terminals.size() > 0) {
				CanvasNode now = terminals.pop();
				if (now.parent!=null && now != head) {
					CanvasNode nowparent = now.parent;
					Collections.sort(nowparent.children);
					int x1 = nowparent.children.lastElement().getLocation().x;
					int x2 = nowparent.children.firstElement().getLocation().x
							+ nowparent.children.firstElement().getWidth();
					int center = (x1 + x2) / 2;
					nowparent.setLocation(new Point(center - nowparent.getWidth() / 2,nowparent.getLocation().y));
					terminals.add(nowparent);
					for (CanvasNode jh : nowparent.children) {
						try {
							terminals.remove(jh);
						} catch (Exception df) {

						}
					}
				}
			}

			// set all y cor for nodes in the single tree
			int total = 0;
			if (maxwords >= maxnonwords + 2 || maxnonwords == 0) {
				total = maxwords + 1;
			} else {
				total = maxnonwords + 3;
			}
			int yu = 0;
			process1.add(head);
			while (process1.size() > 0) {
				CanvasNode now = process1.pop();
				if (now.type == 1) {
					yu = now.getLocation().y;
					// break;
				}
				for (CanvasNode ccc : now.children) {
					process1.add(ccc);
				}
			}

			process1.add(head);
			while (process1.size() > 0) {
				CanvasNode now = process1.pop();
				if (now.type == 1) {
					now.level = 1;

				} else {
					now.level = total - now.level;
					now.setLocation(new Point(now.getLocation().x,(int) (yu
							- (now.level - 1) * (now.getHeight() + now.getHeight() * heightIndex))));
				}
				for (CanvasNode xc : now.children) {
					process1.add(xc);
				}
			}

		}

		// in MouseReleased, this called before dragLabel is added into list
		// so for the head, do it sepereately
//		head.setLocation(head.location);

		for (CanvasLine i : lines) {
			i.updateByEnds();
		}

		this.repaint();
	
	}


}
