package program.mainUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;

import program.Preference;
import program.grammar.Grammar;
import program.mainUI.InforTree.IndexNode.IndexNode;
import program.mainUI.drawingPanel.Plain;
import program.mainUI.inforTree.SentenceTree;



//import Interface.Tree;

//import Interface.Tree;

//this class creates a default free drawing mode JPanel
//with function it can change the panel to other modes that is TreeDrawing based
public class Interface extends JPanel {

	Preference pp=new Preference();
	// constants
	String newline = System.getProperty("line.separator");

	// the output to save to a file in teacher mode
	String output = "";
	// common component-drawing panel
	public Plain drawingPanel;
	//the dialogue window inside Plain
	private JTextArea dialogue;
	
	// String storing the status of the Mode
	// status=edit;
	// status=practice;
	public String status = "edit";

	// the upper layout of different modes
	JPanel freeDrawingPanel;
	public TeacherPanel teacherPanel;
	// the right panel holding upper level panel and a plain in teacherMode
	JPanel rightPanel;
	// the splitPane in teachermode
	JSplitPane splitPane;
	// the textField holding path in teacher mode in teacherPanel
	// JTextField path = new JTextField();

	// the left part showing sentences
	public SentenceTree sentenceTree;

	Tree tree = new Tree();

	
	// the constructor will build a default teacher mode JPanel
	public Interface(Plain inputdp) {
		drawingPanel = inputdp;
		dialogue=inputdp.dialogueP.dialogue;
		teacherPanel = new TeacherPanel();
				
		sentenceTree = new SentenceTree(this);
		sentenceTree.teacherPanel = teacherPanel;
		sentenceTree.treeModePlain = inputdp;
		
		// right panel holding the top part and the drawing panel
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		// set splitPane
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentenceTree, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		
		setLayout(new GridBagLayout());
	}

	public void clearPanels() {
		super.removeAll();
		rightPanel.removeAll();
		splitPane = null;
	}
	

	


	// public functions

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}


	public class Tree {
		public Vector<NodeLabel> list = new Vector<NodeLabel>();
		public Vector<Line> linelist = new Vector<Line>();
		public String sentence = "";
		public String meaning = "";
		Vector<String> structureError;
		//record the original structure


		// check whether able to write out a bracket form by checking whether
		// the
		// tree
		// is complete and no node has more than one parent

		// important:not checking whether two line segments intersect with each
		// other
		// causing not adjacent nodes having the same parent
		// assuming only the adjacent nodes having the same parent
		public boolean completeTree() {
			// first clear the previous errorMSG
			structureError = new Vector<String>();

			// storing the nodes with no parent, if there are more than one that
			// kind of nodes,
			// should return false
			Vector<NodeLabel> heads = new Vector<NodeLabel>();

			for (NodeLabel treenode : list) {
				// if any node has more than one parent
				if (treenode.parents.size() > 1) {
					String errorMSG = "Node " + treenode.label.getText() + " has " + treenode.parents.size()
							+ " parents:";
					for (NodeLabel parent : treenode.parents) {
						errorMSG += " " + parent.label.getText();
					}
					errorMSG += ".";
					structureError.add(errorMSG);
				}
				// recording nodes with no parent
				else if (treenode.parents.size() == 0) {
					heads.add(treenode);
				}

			}

			// check the number of heads
			if (heads.size() > 1) {
				String errorMSG = "There are more than one head nodes:";
				for (NodeLabel head : heads) {
					errorMSG += " " + head.label.getText();
				}
				errorMSG += ".";
				structureError.add(errorMSG);

			}
			
			
			//from here to //// check whether nothing is added except for the terminal words
			int numberofwords=0;
			for(String ww:sentence.split(" ")){
				if(!ww.equals("")){
					numberofwords++;
				}
			}
			
			if(numberofwords==list.size()&&heads.size()==numberofwords){
				structureError=new Vector<String>();
				return false;
			}
			//if this completeTree returns false, and the structureError's size is 0
			//then it means nothing is added except for the terminal words
			////

			if (structureError.size() > 0) {
				return false;
			}
			return true;

		}

		// returns the bracket form of a complete tree
		// returns "error" if it is not a complete tree

		public String bracketForm() {

			if (!completeTree()) {
				if(structureError.size()!=0)
				return "error";
				else					
				return "none";
			}
			Stack<NodeLabel> process1 = new Stack<NodeLabel>();
			Stack<NodeLabel> process2 = new Stack<NodeLabel>();
			NodeLabel root = new NodeLabel("", drawingPanel);
			for (NodeLabel x : list) {
				// make sure the order of childrens is consistent and right
				Collections.sort(x.children);
				Collections.reverse(x.children);
				if (x.parents.size() == 0) {
					root = x;
				}
			}

			process1.add(root);
			while (process1.size() > 0) {
				NodeLabel now = process1.firstElement();
				process1.remove(0);
				process2.add(now);

				for (NodeLabel x : now.children) {
					process1.add(x);
				}
			}

			// now process1 has BFS order, and we need to use it reversely
			// to make sure every node we call have their children all set up
			while (process2.size() > 0) {
				NodeLabel now = process2.pop();
				if (now.children.size() == 0) {
					now.bracketForm = " " + now.label.getText();
				} else {
					now.bracketForm = "[" + now.label.getText();
					for (NodeLabel x : now.children) {
						now.bracketForm += x.bracketForm;
					}
					now.bracketForm += "]";
				}
			}

			return root.bracketForm;
		}

		public Tree copyTree() {
			Tree r = new Tree();
			r.list = copylist();
			r.linelist = copyLinelist(r.list);
			r.meaning = meaning;
			r.sentence = sentence;
			return r;
		}
		


		// will return a vector of string containing "[","N apple","]" if passed
		// in [N apple].
		private Vector<String> getVector(String input) {
			System.out.println("this is input: " + input);
			Vector<String> r = new Vector<String>();

			StringBuffer buffer = new StringBuffer("");
			String now = "";
			for (int i = 0; i < input.length(); i++) {

				if (input.charAt(i) != '[' && input.charAt(i) != ']') {
					buffer.append(input.charAt(i));
				} else {
					now = buffer.toString();
					if (!now.equals("")) {
						r.add(now);
					}
					buffer = new StringBuffer("");
					if (input.charAt(i) == '[') {
						now = "[";
					} else if (input.charAt(i) == ']') {
						now = "]";
					}
					r.add(now);
					now = "";
				}

			}
			return r;
		}

		// passing in the returned string vector from Vector<String>
		// getVector(String input)
		// return the list of NodeLabel with type defined
		// with parent-child relation built
		// set the size of those NodeLabels to their preferredSize
		private Vector<NodeLabel> addInNodes(Vector<String> material) {
			System.out.println("doclick");
			teacherPanel.clean.doClick();
			Stack<NodeLabel> process = new Stack<NodeLabel>();

			// the return list of NodeLabel
			// with the same order that appears in the bracket form
			// the first nodeLabel is the root
			Vector<NodeLabel> r = new Vector<NodeLabel>();
			for (String x : material) {

				// if x is like "N apple"
				if (x.contains(" ")) {
					NodeLabel now = new NodeLabel(x.split(" ")[0], drawingPanel);
					now.setSize(now.getPreferredSize());
					NodeLabel nowchild = new NodeLabel(x.split(" ")[1], drawingPanel);
					nowchild.setSize(nowchild.getPreferredSize());
					nowchild.type = 1;
					System.out.println("type 1 is definded");
					now.children.add(nowchild);
					nowchild.parents.add(now);
					process.add(now);

					// updating the return list
					r.add(now);
					r.add(nowchild);

				}
				// if x is like "NP" or "["
				else if (x != "]") {
					NodeLabel now = new NodeLabel(x, drawingPanel);
					process.add(now);
					if (x != "[") {
						now.setSize(now.getPreferredSize());
						r.add(now);
					}
				}
				// if x is "]"
				else {
					Stack<NodeLabel> newmaterial = new Stack<NodeLabel>();
					while (!process.peek().label.getText().equals("[")) {
						newmaterial.add(process.pop());
					}
					// delete the "[" nodelabel
					process.pop();
					if (newmaterial.size() > 0) {
						NodeLabel parent = newmaterial.pop();
						// adding childen to parent node in left to right order
						// no need to reverse later
						while (newmaterial.size() > 0) {
							NodeLabel child = newmaterial.pop();
							parent.children.add(child);
							child.parents.add(parent);
						}
						process.add(parent);
					}
				}
			}

			return r;
		}

		// pass in the bounds of back of drawroom of plain
		public void setLocation(Rectangle bounds) {
			System.out.println("at least I am at setLocation");
			// set the location for base nodes
			Vector<NodeLabel> basement = new Vector<NodeLabel>();
			Vector<NodeLabel> head = new Vector<NodeLabel>();
			//String sentence="";
			if (list != null) {
				System.out.println("I am inside list thing");
				for (NodeLabel n : list) {
					System.out.println("1111");
					System.out.println("here is size");
					System.out.println(pp.getInt("TreeSize"));
					n.label.setFont(new Font(pp.getString("FontStyle"), Font.PLAIN, pp.getInt("TreeSize")));
					n.label.revalidate();
					n.revalidate();
					if (n.type == 1) {
						basement.add(n);
					}
					if (n.parents.size() == 0) {
						head.add(n);
					}
				}
			}
//			Point zeroP= drawingPanel.drawroom.getViewport().getViewPosition();
//			System.out.println("zeroP"+zeroP);
//			int heightdraw = (int) (bounds.height * 0.9)+zeroP.y;
//			int i = 40+zeroP.x;
			
			int heightdraw = (int) (bounds.height * 0.9) + bounds.y;
			int i = 40 + bounds.x;

			for (NodeLabel bb : basement) {
				i = (int) (i + drawingPanel.widthIndex * bb.label.getPreferredSize().getHeight());
				bb.setVisible(true);
				bb.setLocation(i, heightdraw);
				bb.setSize(bb.getPreferredSize());
				bb.location = bb.getLocation();
				i = (int) (i + bb.getPreferredSize().getWidth());
			}

			// use adjustonetree to set the location for the other nodes
			for (NodeLabel x : head) {
				drawingPanel.adjustOneTree(x);
			}

		}

		// call after all the nodes' location has been set
		public void addInLines() {
			// first, clear out the lines and then readd in
			linelist = new Vector<Line>();
			for (NodeLabel y : list) {
				y.childrenlines = new Vector<Line>();
				y.parentlines = new Vector<Line>();
			}

			for (NodeLabel x : list) {

				for (NodeLabel y : x.children) {
					Line line = new Line();
					line.children = y;
					System.out.println(line.children.label.getText());
					line.parent = x;
					line.end = new Point(x.location.x + x.getWidth() / 2,
							x.location.y + x.label.getLocation().y + x.label.getHeight());
					line.start = new Point(y.location.x + y.getWidth() / 2, y.location.y + y.label.getLocation().y);
					y.parentlines.add(line);
					x.childrenlines.add(line);
					linelist.add(line);
				}
			}
		}

		public void changeStructureToTree(String structure) {
			Vector<String> nodeString = getVector(structure);
			Vector<NodeLabel> nodes = addInNodes(nodeString);
			// setLocation(nodes);
			list = nodes;
			/// todo
			linelist = new Vector<Line>();

		}
		
		
		//student mode update
		public void changeStructureToTree2(String structure2) {
			// TODO Auto-generated method stub
			Vector<String> nodeString=getVector(structure2);
			Vector<NodeLabel> nodes=addInNodes(nodeString);
			Vector<NodeLabel> nodes2=new Vector<NodeLabel>();
			//setLocation(nodes);
			for(NodeLabel x:nodes){
				if(x.children.size()==0){
					nodes2.add(x);
					x.parents=new Vector<NodeLabel>();
				}
			}
			list=nodes2;
			///todo
			linelist=new Vector<Line>();
			
		}

		public void setLocation2(Rectangle bounds, Plain drawingPanel) {
			// TODO Auto-generated method stub
			// set the location for base nodes
			Vector<NodeLabel> basement = new Vector<NodeLabel>();
			Vector<NodeLabel> head = new Vector<NodeLabel>();
			for (NodeLabel n : list) {
				if (n.type == 1) {
					basement.add(n);
				}
				if (n.parents.size() == 0) {
					head.add(n);
				}
			}

			int heightdraw = (int) (bounds.height * 0.8) + bounds.y;
			int i = 40 + bounds.x;

			for (NodeLabel cc : basement) {
				NodeLabel bb = new NodeLabel(cc.label.getText(), drawingPanel);
				bb.type = 1;
				i = (int) (i + drawingPanel.widthIndex * bb.label.getPreferredSize().getHeight());
				bb.setVisible(true);
				bb.setLocation(i, heightdraw);
				bb.setSize(bb.getPreferredSize());
				bb.location = bb.getLocation();

				drawingPanel.drawroom.canvas.add(bb);
				drawingPanel.list.add(bb);
				i = (int) (i + bb.getPreferredSize().getWidth());
			}

		}

		// the whole tree information can be copied after calling this function and
		// copyLinelist(Vector<NodeLaebel>)
		// nodelabel cannot be copied individually but only can be copied as a whole
		// unit
		// because of the relation between different nodes
		// this step only copy the list of nodes and their relation with each other
		// without building up the linelist
		// need to pass the vector of nodeLabel to be copied
		public Vector<NodeLabel> copylist() {
			Vector<NodeLabel> r = new Vector<NodeLabel>();
			// first get a whole copy list with the same index
			for (NodeLabel x : list) {
				//??change NodeLabel constructor
				NodeLabel now = new NodeLabel(x.label.getText(),plain);
				now.location = new Point(x.location.x, x.location.y);
				now.setLocation(now.location);
				now.color = x.color;
				now.type = x.type;
				now.grammar_error = x.grammar_error;
				r.add(now);
			}
			for (int i = 0; i < list.size(); i++) {
				NodeLabel now = list.elementAt(i);
				NodeLabel copy = r.elementAt(i);
				// build up the relation between the node and its children
				for (NodeLabel x : now.children) {
					int index = list.indexOf(x);
					copy.children.add(r.elementAt(index));
				}
				// build up the relation between the node and its parent
				// although technically one node should not have more than one
				// parent in order to be legal
				for (NodeLabel y : now.parents) {
					int index = list.indexOf(y);
					copy.parents.add(r.elementAt(index));
				}
			}

			return r;

		}


		
		
		
		// pass in the already copyied vector of NodeLabel-copyNode
		// the vector of NodeLabel to be copied
		// the vector of Line to be copied
		// update the parentlines, childrenlines for those copied Nodelabel
		public Vector<Line> copyLinelist(Vector<NodeLabel> copyNode) {
			Vector<Line> r = new Vector<Line>();

			for (Line x : linelist) {
				Line copy = new Line(plain);
				// Point can be copied directly, because everytime the point change,
				// it is constructing a new point instead of modifying the point
				copy.start = x.start;
				copy.end = x.end;
				copy.color = x.color;
				NodeLabel parent = x.parent;
				if (parent != null) {
					int index = list.indexOf(parent);
					copy.parent = copyNode.elementAt(index);
				}
				NodeLabel children = x.children;
				if (children != null) {
					int index = list.indexOf(children);
					copy.children = copyNode.elementAt(index);
				}

				r.add(copy);
			}

			for (int i = 0; i < list.size(); i++) {
				NodeLabel ori = list.elementAt(i);
				NodeLabel copy = copyNode.elementAt(i);
				for (Line x : ori.parentlines) {
					int index = linelist.indexOf(x);
					copy.parentlines.add(r.elementAt(index));
				}
				for (Line x : ori.childrenlines) {
					int index = linelist.indexOf(x);
					copy.childrenlines.add(r.elementAt(index));
				}
			}
			return r;

		}


	}

	public Tree createNewTree(String sentence, String meaning, String structure) {
		Tree r = new Tree();
		r.sentence = sentence;
		r.meaning = meaning;
		r.changeStructureToTree(structure);

		return r;
	}



	private void editMode() {
		// TODO Auto-generated method stub

		clearPanels();
		GridBagConstraints c = new GridBagConstraints();
		teacherPanel.sentence.setEditable(true);
		teacherPanel.meaning.setEditable(true);
		// teacherPanel.add.setVisible(true);
		// teacherPanel.review.setVisible(true);
		// teacherPanel.save.setVisible(true);
		teacherPanel.sn.setVisible(true);
		// teacherPanel.saveTree.setVisible(true);
		teacherPanel.check.setVisible(false);
		teacherPanel.tlabel.setText("Teacher Mode");
		status = "view";

		// set splitPane
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentenceTree, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);

		BasicSplitPaneUI ui = (BasicSplitPaneUI) splitPane.getUI();
		JButton oneClick = (JButton) ui.getDivider().getComponent(0);
		// oneClick.doClick();
		// oneClick.setVisible(false);

		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		rightPanel.add(teacherPanel, c);
		// teacherPanel.add(oneClick);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		rightPanel.add(drawingPanel, c);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		add(splitPane, c);

	}

	public void saveTree() {
		System.out.println("saveTree");
		System.out.println("presentNode"+sentenceTree.presentNode);
		if (sentenceTree.presentNode != sentenceTree.sentence.root) {
			System.out.println("NonRoot");
			IndexNode now = (IndexNode) sentenceTree.presentNode;
			if (now.type.equals("sentence")) {
				System.out.println("sentence");
				tree.linelist = drawingPanel.linelist;
				tree.list = drawingPanel.list;
				tree.sentence = teacherPanel.sentence.getText();
				tree.meaning = teacherPanel.meaning.getText();
				sentenceTree.updateATreeNode(now, tree);
				now.updateTree(tree);
			} else if (now.type.equals("instruction")) {
				System.out.println("instruction");
				now.updateInstruction(drawingPanel.dialogue.getText());
			}
		}

	}

	public void forInstructionEdit() {
		teacherPanel.sentence.setEditable(false);
		teacherPanel.meaning.setEditable(false);
		drawingPanel.instructionEdit();
		
		teacherPanel.savechange.setVisible(false);
		teacherPanel.saveasnew.setVisible(false);
		teacherPanel.start.setVisible(false);
		teacherPanel.sl.setVisible(false);
		teacherPanel.clean.setVisible(true);

	}

	public void forSentenceEdit() {
		teacherPanel.sentence.setEditable(true);
		teacherPanel.meaning.setEditable(true);
		drawingPanel.sentenceEdit();

		teacherPanel.savechange.setVisible(true);
		teacherPanel.saveasnew.setVisible(true);
		teacherPanel.start.setVisible(true);
		teacherPanel.sl.setVisible(true);
		teacherPanel.clean.setVisible(true);

	}

	public void forFolder() {

		teacherPanel.sentence.setEditable(false);
		teacherPanel.meaning.setEditable(false);
		drawingPanel.folderEdit();

		teacherPanel.savechange.setVisible(false);
		teacherPanel.saveasnew.setVisible(false);
		teacherPanel.start.setVisible(false);
		teacherPanel.sl.setVisible(false);
		teacherPanel.clean.setVisible(false);
	}
	
	public Tree createNewTree2(String name, String meaning, String structure) {
		Tree r = new Tree();
		r.sentence = name;
		r.meaning = meaning;

		//r.changeStructureToTree2(structure);
		return r;
	}

	public void forSentence() {
		drawingPanel.sentence();
		teacherPanel.sl.setVisible(true);
		teacherPanel.clean2.setVisible(true);
		teacherPanel.check.setVisible(true);
		teacherPanel.next.setVisible(true);
	}
	public void forInstruction(){
		drawingPanel.instruction();
		teacherPanel.sl.setVisible(false);
		teacherPanel.clean2.setVisible(false);
		teacherPanel.check.setVisible(false);
		teacherPanel.next.setVisible(false);
	}

	

	public static void main(String[] args) {
		setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 22));
		JFrame frame = new JFrame("UCLA-PPP Parse and Practice Program");
		
		Grammar grammar = new Grammar();
		Plain layers = new Plain(grammar);
		Interface main_interface = new Interface(layers);
		frame.add(main_interface, BorderLayout.CENTER);
		
		//Interface mode choosing
		//main_interface.practiceMode();
		//main_interface.teacherMode();
		main_interface.freedrawingMode();
		
		//<<frame_setting 
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		frame.setPreferredSize(new Dimension(xSize - 300, ySize));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//>>frame_setting
	}

	
}
