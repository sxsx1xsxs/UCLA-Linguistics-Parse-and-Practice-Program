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
import java.awt.event.ItemEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;

import program.Preference;
import program.grammar.Grammar;
import program.mainUI.drawingPanel.*;
import program.mainUI.inforTree.IndexNode;
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

	
	public void freedrawingMode(){
		clearPanels();
		forSentence();

		drawingPanel.dialogue.setEditable(false);
		teacherPanel.sentence.setEditable(true);
		teacherPanel.meaning.setVisible(false);
		
		teacherPanel.sn.setVisible(false);
		teacherPanel.savechange.setVisible(false);
		teacherPanel.saveasnew.setVisible(false);
		
		teacherPanel.check.setVisible(false);
		teacherPanel.next.setVisible(false);
		
		teacherPanel.tlabel.setText("Tree Drawing");
		teacherPanel.mlabel.setVisible(false);
		drawingPanel.dialogue.setText("");
		
		
		teacherPanel.start.setVisible(true);
		teacherPanel.sl.setVisible(true);
		teacherPanel.clean.setVisible(true);
		teacherPanel.clean2.setVisible(false);
		status = "practice";

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentenceTree, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		rightPanel.add(teacherPanel, c);

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
		add(rightPanel, c);

	
	}
	
	
	/*
	 * this mode's setting is closely related to practiceMode, when modify one
	 * of them, remember to modify the other!
	 * it's set up as instructionEdit view
	 */
	public void teacherMode() {
		clearPanels();
		drawingPanel.dialogue.setEditable(true);
		teacherPanel.sentence.setEditable(false);
		teacherPanel.meaning.setEditable(false);
		teacherPanel.meaning.setVisible(true);
		
		teacherPanel.mlabel.setVisible(true);
		sentenceTree.selectNode();
		
		
		teacherPanel.sn.setVisible(true);
		teacherPanel.savechange.setVisible(false);
		teacherPanel.saveasnew.setVisible(false);
		
		teacherPanel.check.setVisible(false);
		teacherPanel.next.setVisible(false);
		
		teacherPanel.tlabel.setText("Teacher Mode");
		
		teacherPanel.start.setVisible(false);
		teacherPanel.sl.setVisible(false);
		teacherPanel.clean.setVisible(true);
		teacherPanel.clean2.setVisible(false);
		
		status = "edit";

		// set splitPane
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentenceTree, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);


		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		rightPanel.add(teacherPanel, c);


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

	/*
	 * this mode's setting is closely related to teacherMode, when modify one
	 * of them, remember to modify the other!
	 * it's set up as instruction view
	 */
	public void practiceMode() {
		clearPanels();
		
		teacherPanel.mlabel.setVisible(true);
		sentenceTree.selectNode();

		drawingPanel.dialogue.setEditable(false);
		teacherPanel.sentence.setEditable(false);
		teacherPanel.meaning.setEditable(false);
		teacherPanel.meaning.setVisible(true);
		
		teacherPanel.sn.setVisible(false);
		teacherPanel.savechange.setVisible(false);
		teacherPanel.saveasnew.setVisible(false);
		
		teacherPanel.check.setVisible(false);
		teacherPanel.next.setVisible(false);
		
		teacherPanel.tlabel.setText("");
		
		teacherPanel.start.setVisible(false);
		teacherPanel.sl.setVisible(false);
		teacherPanel.clean.setVisible(false);
		teacherPanel.clean2.setVisible(false);
		status = "practice";

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sentenceTree, rightPanel);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		rightPanel.add(teacherPanel, c);

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

	private static void createAndShowUI() {
		

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

	
	
		

	// the right-top part for teacher mode
	public class TeacherPanel extends JPanel {
		// the left side buttons
		// for practice mode
		public JButton check = new JButton("Check Answer");
		JButton next = new JButton("Next");

		// for edit mode
		JButton sn = new JButton("New");
		JButton savechange = new JButton("Save Change");
		JButton saveasnew = new JButton("Save as New");

		/*
		 * the label denoting mode can be "Teacher Mode" or "Practice Mode"
		 */
		JLabel tlabel = new JLabel("Teacher Mode");
		// the right side
		// for edit mode
		JButton start = new JButton("Start");
		// straighten-lines button;
		JButton sl = new JButton("Straighten Lines");
		public JButton clean = new JButton("Clear");

		// for practice mode
		JButton clean2 = new JButton("Clear");

		// the lower components
		JLabel slabel = new JLabel("Sentence");
		public JTextField sentence = new JTextField();
		JLabel mlabel = new JLabel("Meaning");
		public JTextField meaning = new JTextField();

		public void setBasicLayout() {
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(sn, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(savechange, c);

			c.gridx = 2;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(saveasnew, c);

			c.gridx = 3;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(check,c);

			c.gridx = 4;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(next, c);

			c.gridx = 5;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.NONE;
			add(tlabel, c);

			c.gridx = 6;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			add(start, c);

			c.gridx = 7;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			add(sl, c);

			c.gridx = 8;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			add(clean, c);

			c.gridx = 9;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			add(clean2, c);

			// the panel holding 2 textfieldw
			JPanel enter = new JPanel();
			enter.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 10;
			c.weightx = 1;
			c.fill = GridBagConstraints.BOTH;
			add(enter, c);

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			enter.add(slabel, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.BOTH;
			enter.add(sentence, c);

			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			enter.add(mlabel, c);

			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.weightx = 1;
			c.fill = GridBagConstraints.BOTH;
			enter.add(meaning, c);

		}

		
		public void checkanswer(){
			Tree newtree=new Tree();
			newtree.list=drawingPanel.copylist(drawingPanel.list);
			String x=newtree.bracketForm();
			IndexNode nownode=(IndexNode)sentenceTree.presentNode;
			Tree newtree2=new Tree();
			
			String y=nownode.structure;
			if(x.equals(y)){
				drawingPanel.dialogue.setText("Correct!");
				nownode.correct=1;
			
			}else{
				int ee=0;
				for(NodeLabel xf:newtree.list){
					if(!xf.grammar_error.equals("")){
						ee=1;
				break;
					}
				}
				nownode.correct=-1;
			if(ee==1){
					drawingPanel.dialogue.setText("Incorrect! Not legal structure by the rules.");
				}else{
				drawingPanel.dialogue.setText("Incorrect!This is a legal structure, but it doesn't have a right meaning.");
			}}
		
		}
		
		public TeacherPanel() {
			setBasicLayout();
			drawingPanel.removeMouseListener(drawingPanel.myMouseAdapter);
			drawingPanel.removeMouseMotionListener(drawingPanel.myMouseAdapter);

			// will only be called in teachermode
			clean.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clean();
					sentence.setText("");
					meaning.setText("");
					tree = new Tree();
				}

			});

			clean2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clean2();
				}

			});

			sl.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.adjust();
				}

			});

			start.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String s = sentence.getText();
					drawingPanel.input(s);
					tree.sentence = s;
					saveTree();
				}

			});

			check.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					checkanswer();
					}

			});

			// save and new
			sn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					saveTree();
					tree = new Tree();
					//in the following function, presentNode is updated
					sentenceTree.addSentence(tree, sentenceTree.presentNode);
					sentenceTree.selectNode();
					repaint();
				}

			});

			next.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				int i=	sentenceTree.sentence.root.getIndex(sentenceTree.presentNode);
				if(i<sentenceTree.sentence.root.getChildCount()){
					DefaultMutableTreeNode now=(DefaultMutableTreeNode) sentenceTree.sentence.root.getChildAt(i+1);
//				sentenceTree.sentence.setSelectionPath(new TreePath(now));
				sentenceTree.sentence.setSelectedNode(now);
				sentenceTree.presentNode=now;
				sentenceTree.selectNode();
				}}
				
			});
			
			savechange.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					saveTree();	
					tree=new Tree();
					tree.sentence=sentence.getText();
					tree.meaning=meaning.getText();
					tree.list=drawingPanel.copylist(drawingPanel.list);
					tree.linelist=drawingPanel.copyLinelist(tree.list, drawingPanel.list, drawingPanel.linelist);
					drawingPanel.showTree(tree);
				}				
			});
			
			saveasnew.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					tree.list=drawingPanel.list;
					tree.linelist=drawingPanel.linelist;
					tree.sentence=sentence.getText();
					tree.meaning=meaning.getText();
					sentenceTree.addSentence(tree, sentenceTree.presentNode);
					sentenceTree.selectNode();
					repaint();
					
				}
				
			});

		}

	}

	public class Tree {
		public Vector<NodeLabel> list = new Vector<NodeLabel>();
		public Vector<Line> linelist = new Vector<Line>();
		public Vector<Line> arrowList = new Vector<>();
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
			HashMap<Arrow,Integer> arrowsToIDs = new HashMap<>();
			// Add in all arrows to the arrowmap
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
					now.bracketForm = " " + getBracketFormArrows(now,arrowsToIDs) + now.getLabelName();
				} else {
					now.bracketForm = "[" + getBracketFormArrows(now,arrowsToIDs) +  now.getLabelName();
					for (NodeLabel x : now.children) {
						now.bracketForm += x.bracketForm;
					}
					now.bracketForm += "]";
				}
			}

			return root.bracketForm;
		}

		public String getBracketFormArrows(NodeLabel node, HashMap<Arrow,Integer> map){
			if(node.parentArrows.isEmpty() && node.childrenArrows.isEmpty()){
				return "";
			}
			StringBuffer stringBuffer = new StringBuffer(20);
			for(Line arrow : node.childrenArrows){
				Arrow a = (Arrow)arrow;
				stringBuffer.append('>');
				Integer id;
				if(map.containsKey(a)){
					id = map.get(a);
				} else {
					id = map.size();
					map.put(a,id);
				}
				stringBuffer.append(id);
			}

			for(Line arrow : node.parentArrows){
				Arrow a = (Arrow)arrow;
				stringBuffer.append('<');
				Integer id;
				if(map.containsKey(a)){
					id = map.get(a);
				} else {
					id = map.size();
					map.put(a,id);
				}
				stringBuffer.append(id);
			}
			stringBuffer.append('|');
			return stringBuffer.toString();
		}

		public Tree copyTree() {
			Tree r = new Tree();
			r.list = drawingPanel.copylist(list);
			r.linelist = drawingPanel.copyLinelist(r.list, list, linelist);
			r.meaning = meaning;
			r.sentence = sentence;
			return r;
		}
		


		// will return a vector of string containing "[","N apple","]" if passed
		// in [N apple].
		private Vector<String> getVector(String input) {
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
			arrowList.clear();
			teacherPanel.clean.doClick();
			Stack<NodeLabel> process = new Stack<NodeLabel>();
			HashMap<String,Arrow> idsToArrows = new HashMap<>();
			// the return list of NodeLabel
			// with the same order that appears in the bracket form
			// the first nodeLabel is the root
			Vector<NodeLabel> r = new Vector<NodeLabel>();
			for (String x : material) {

				// if x is like "N apple"
				if (x.contains(" ")) {
					String[] split = x.split(" ");
					NodeLabel now = NodeLabelFactory.createWithReservedTextAndArrows(split[0], drawingPanel,idsToArrows,arrowList);
					now.setSize(now.getPreferredSize());
					NodeLabel nowchild = NodeLabelFactory.createWithReservedTextAndArrows(split[1],drawingPanel,idsToArrows,arrowList);
					nowchild.setSize(nowchild.getPreferredSize());
					nowchild.type = 1;
					now.children.add(nowchild);
					nowchild.parents.add(now);
					process.add(now);

					// updating the return list
					r.add(now);
					r.add(nowchild);

				}
				// if x is like "NP" or "["
				else if (x != "]") {
					NodeLabel now = NodeLabelFactory.createWithReservedTextAndArrows(x, drawingPanel,idsToArrows,arrowList);
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
			// set the location for base nodes
			Vector<NodeLabel> basement = new Vector<NodeLabel>();
			Vector<NodeLabel> head = new Vector<NodeLabel>();
			//String sentence="";
			if (list != null) {
				for (NodeLabel n : list) {
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
			linelist.addAll(arrowList);
			for (NodeLabel y : list) {
				y.childrenlines = new Vector<Line>();
				y.parentlines = new Vector<Line>();
			}

			for (NodeLabel x : list) {

				for (NodeLabel y : x.children) {
					Line line = new Line();
					line.children = y;
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
			list = nodes;
			/// todo
			linelist = new Vector<Line>();

		}
		
		
		//student mode update
		public void changeStructureToTree2(String structure2) {
			Vector<String> nodeString=getVector(structure2);
			Vector<NodeLabel> nodes=addInNodes(nodeString);
			Vector<NodeLabel> nodes2=new Vector<NodeLabel>();
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

	}

	public Tree createNewTree(String sentence, String meaning, String structure) {
		Tree r = new Tree();
		r.sentence = sentence;
		r.meaning = meaning;
		r.changeStructureToTree(structure);

		return r;
	}



	private void editMode() {
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
		if (sentenceTree.presentNode != sentenceTree.sentence.root) {
			IndexNode now = (IndexNode) sentenceTree.presentNode;
			if (now.type.equals("sentence")) {
				tree.linelist = drawingPanel.linelist;
				tree.list = drawingPanel.list;
				tree.sentence = teacherPanel.sentence.getText();
				tree.meaning = teacherPanel.meaning.getText();
				sentenceTree.updateATreeNode(now, tree);
				now.updateTree(tree);
			} else if (now.type.equals("instruction")) {
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
		//teacherPanel.sentence.setText("");
		
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
