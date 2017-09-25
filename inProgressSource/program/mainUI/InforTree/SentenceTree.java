package program.mainUI.InforTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import program.grammar.Grammar;
import program.mainUI.Interface;
import program.mainUI.InforTree.IndexNode.IndexNode;
import program.mainUI.Interface.TeacherPanel;
import program.mainUI.Interface.Tree;
import program.mainUI.drawingPanel.Plain;



public class SentenceTree extends JPanel {


	// the panel on the top holding all the buttons
	JPanel buttons = new JPanel();
	// new category
	JButton newCategory = new JButton("New Category");
	Interface treeMode;
	// sentence Pane
	JScrollPane scrollPane;
	public STree sentence;

	// the currrently chosen node in sentence
	public DefaultMutableTreeNode presentNode;
	// the boolean tells the program whether there is a previous copy or cut
	// behavior
	// pasteStatus=0; no copy action before
	// pasteStatus=1; copied instruction before
	boolean pasteStatus = false;

	// the following three hold components in Interface in order to connect
	// actions
	public Interface.TeacherPanel teacherPanel;
	public Plain treeModePlain;

	// the same status as in Interface, either "Add" or "Review"
	// will be changed in Interface "review" and "add" label (as button)
	public String status = "Add";
	// the variable to store the copied node
	// if copy is called, then this is a new node
	// if cut is called, then this is the existing node
	IndexNode copyNode;
	// popup menu items
	JPopupMenu popup = new JPopupMenu();
	// new category menu item
	JMenuItem nc = new JMenuItem("New Category");
	// delete menu item
	JMenuItem delete = new JMenuItem("Delete");

	// when in review mode
	// will be called when clicked on certain nodes
	// or somenode is selected
	public void selectNode() {

		teacherPanel.clean.doClick();
		treeModePlain.clearUndo();

		if (presentNode != sentence.root) {

			IndexNode present = (IndexNode) presentNode;
			// Edit Mode
			if (treeMode.status.equals("edit")) {
				System.out.println("edit mode");
				if (present.type.equals("sentence")) {
					System.out.println("sentence");
					treeMode.forSentenceEdit();
					Rectangle bounds = treeModePlain.drawroom.getViewport().getViewRect();
					present.outputTree(teacherPanel.sentence, teacherPanel.meaning, treeModePlain, bounds);
				} else if (present.type.equals("instruction")) {
					System.out.println("instruction: " + present.instruction);
					treeMode.forInstructionEdit();
					treeModePlain.dialogue.setText(present.instruction);
				} else if (present.type.equals("folder")) {
					System.out.println("folder");
					treeMode.forFolder();
				}
			}
			// Practice Mode
			else {

				if (present.type.equals("sentence")) {
					treeMode.forSentence();
					Rectangle bounds = treeModePlain.drawroom.getViewport().getViewRect();
					present.outputTree(teacherPanel.sentence, teacherPanel.meaning, treeModePlain, bounds);
				} else if (present.type.equals("instruction")) {
					System.out.println("instruction: " + present.instruction);
					treeMode.forInstruction();
					treeModePlain.dialogue.setText(present.instruction);
				} else if (present.type.equals("folder")) {
					System.out.println("folder");
					treeMode.forFolder();
				}

			}
		}
		// Root Selected
		else {
			System.out.println("RootSelected");
			treeMode.forFolder();
		}
	}

	public void changeSTree(STree tree) {
		System.out.println("st" + 2);
		sentence.changeRoot(tree.root);
		// because by default the root is selected
		presentNode = sentence.root;
		scrollPane.setViewportView(sentence);
	}

	public SentenceTree(Interface treeModeInput) {
		System.out.println("SentenceTree Constructed");
		treeMode = treeModeInput;
		treeModePlain = treeMode.drawingPanel;
		teacherPanel = treeMode.teacherPanel;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//adding the buttons panel
		buttons.add(newCategory);
		newCategory.setVisible(false);

		newCategory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addCategory();
			}

		});

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		add(buttons, c);

		// adding the scrollpane containing the tree
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Sentences");
		sentence = new STree(root, "default");
		sentence.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("MouseClicked in sentenceTree");
				TreePath path=sentence.getClosestPathForLocation(e.getX(), e.getY());
				System.out.println(path);
			//	new STree().setsele
//				TreePath path = getPathForRow(row);
				sentence.setSelectionPath(path);
//				setSelectionRow(row);
//				System.out.println(path);
//				System.out.println("row" + row + "selected");

				try {
					treeMode.saveTree();
					((DefaultTreeModel) sentence.getModel()).reload(presentNode);
				} catch (Exception pp) {
					System.out.println("Cannot SaveTree");
				}

				try {
					presentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
					selectNode();
				} catch (Exception ell) {
					System.out.println("when clicked sentenceTree, cannot set presentNode");
				}

				if (SwingUtilities.isRightMouseButton(e)) {

					if (presentNode.equals(sentence.root)) {
						new PopUp("root").show(e.getComponent(), e.getX(), e.getY());
					} else {
						IndexNode now = (IndexNode) presentNode;
						if (now.type.equals("instruction")) {
							new PopUp("instruction").show(e.getComponent(), e.getX(), e.getY());
						} else if (now.type.equals("folder")) {
							new PopUp("folder").show(e.getComponent(), e.getX(), e.getY());
						} else if (now.type.equals("sentence")) {
							new PopUp("sentence").show(e.getComponent(), e.getX(), e.getY());
						}
					}

				}
			}
		});

		// by default expand all the children sentence have
		for (int i = 0; i < sentence.getRowCount(); i++) {
			sentence.expandRow(i);
		}
		scrollPane = new JScrollPane(sentence);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(scrollPane, c);

		// setting up popup menu
		popup.add(nc);
		popup.add(delete);

	}

	/* 
	 * helper function for copyWholeNode
	 * This function copies the single node without copying the node's children
	 * the index is not copied and needs to be updated later after the new tree is built
	 */
	private IndexNode copyNode(IndexNode original) {

		System.out.println("st" + 4);
		String name = original.name;
		String type = original.type;

		IndexNode r = new IndexNode("", name, type);
		r.instruction = original.instruction;
		r.tree = original.tree.copyTree();

		return r;
	}

	// this function copyies the node and the node's children--
	// basically the whole substree has a new copy
	public IndexNode copyWholeNode(IndexNode original) {
		System.out.println("st" + 5);
		IndexNode r = copyNode(original);
		Stack<IndexNode> process = new Stack<IndexNode>();
		for (int i = 0; i < original.getChildCount(); i++) {
			IndexNode now = (IndexNode) original.getChildAt(i);
			IndexNode childAsNow = copyNode(now);
			// when adding, always add as a pair, first the original and then
			// the copy of the original
			process.add(now);
			process.add(childAsNow);
			r.add(childAsNow);
		}
		while (process.size() > 0) {
			IndexNode nowcopy = process.pop();
			IndexNode now = process.pop();
			for (int i = 0; i < now.getChildCount(); i++) {
				IndexNode child = (IndexNode) now.getChildAt(i);
				IndexNode childcopy = copyNode(child);
				process.add(child);
				process.add(childcopy);
				nowcopy.add(childcopy);
			}

		}

		return r;
	}

	/*
	 *  the function showing the dialogue window asking the user to input name and instruction
	 *  the function returns null if the user hit cancel
	 *  the function returns the vector of size 2 if the user hit ok
	 */
	private static Vector<String> addCategoryWindow() {
		System.out.println("st" + 6);
		JTextField name = new JTextField("");
		JTextArea instruction = new JTextArea("");
		instruction.setLineWrap(true);
		instruction.setWrapStyleWord(true);
		// instruction pane
		JScrollPane ip = new JScrollPane(instruction);
		ip.setPreferredSize(new Dimension(300, 200));
		JPanel panel = new JPanel(new GridBagLayout());

		Vector<String> r = new Vector<String>();

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel("Please enter the name of the category"), c);
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(name, c);
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel("Please enter information or instruction of this category"), c);
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.BOTH;
		panel.add(ip, c);

		int result = JOptionPane.showConfirmDialog(null, panel, "New Category", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			r.add(name.getText());
			r.add(instruction.getText());
		} else {
			return null;

		}

		return r;
	}

	public void addCategory() {
		System.out.println("st" + 7);
		// TODO Auto-generated method stub
		Vector<String> info = addCategoryWindow();
		// if not cancelled window
		if (info != null) {
			String name = info.elementAt(0);
			String instruction = info.elementAt(1);

			// path will never be null, because root is default chosen
			IndexNode node = new IndexNode("", name, "folder");
			IndexNode in = new IndexNode("", "instruction", "instruction");
			in.instruction = instruction;
			node.add(in);

			// if root is selected
			if (presentNode.equals(sentence.root)) {
				// if there is instruction under root, insert the node after the
				// instruction
				if (presentNode.getChildCount() > 0
						&& ((IndexNode) presentNode.getChildAt(0)).type.equals("instruction")) {
					presentNode.insert(node, 1);
				}
				// if there is no instruction under root, insert the node in the
				// beginning of root's children
				// kind of useless, because there will always be an instruction
				else {
					presentNode.insert(node, 0);
				}
				// this is necessary, otherwise the tree node will not show up
				((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(presentNode);

			} else {
				// in normal cases, always add the new category after the
				// selected item
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
				int index = parent.getIndex(presentNode);
				parent.insert(node, index + 1);
				((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(presentNode.getParent());
			}
			sentence.resetIndex();
			sentence.expandPath(new TreePath(node.getPath()));
			sentence.setSelectionPath(new TreePath(node.getPath()));
			presentNode = node;
			selectNode();
		}

	}

	/*
	 *  the function showing the dialogue window asking the user to input name for renaming
	 *  the function returns null if the user hit cancel
	 *  the function returns the a string if the user hit ok
	 *  an empty string if the user does not give any input
	 */
	private static String renameWindow() {
		System.out.println("st" + 8);
		JTextField name = new JTextField("");
		JPanel panel = new JPanel(new GridBagLayout());
		String r;

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel("Please enter a new name"), c);
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(name, c);

		int result = JOptionPane.showConfirmDialog(null, panel, "Rename", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			r = name.getText();
		} else {
			return null;

		}

		return r;
	}

	private static void errorWindow() {

		System.out.println("st" + 9);
		JPanel panel = new JPanel(new GridBagLayout());
		String r;

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		panel.add(new JLabel("Instruction cannot be replaced by non-instruction object."), c);

		JOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.ERROR_MESSAGE);

	}



	public void addSentence(Interface.Tree tree, DefaultMutableTreeNode node) {
		System.out.println("st" + 14);
		IndexNode addedSentence = new IndexNode(tree);
		if (node == sentence.root) {
			node.add(addedSentence);
			((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(node);
			sentence.resetIndex();
			sentence.expandPath(new TreePath(node.getPath()));
		} else {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			int index = parent.getIndex(node);
			parent.insert(addedSentence, index + 1);
			((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);
			sentence.resetIndex();
			sentence.expandPath(new TreePath(parent.getPath()));
		}
		sentence.setSelectionPath(new TreePath(addedSentence.getPath()));
		presentNode = addedSentence;
		((DefaultTreeModel) sentence.getModel()).reload(addedSentence);

	}

	public void changeSentence(Interface.Tree tree, DefaultMutableTreeNode node) {
		System.out.println("st" + 15);
		IndexNode changedSentence = new IndexNode(tree);
		// if the type of node is root or folder, the saveTree button will be
		// disabled and this function
		// will never be called
		IndexNode now = (IndexNode) node;
		if (now.type.equals("instruction")) {
			now.instruction = treeModePlain.dialogue.getText();
		} else if (now.type.equals("sentence")) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			int index = parent.getIndex(node);
			parent.remove(node);
			parent.insert(changedSentence, index);
			((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);
			sentence.resetIndex();
			// sentence.expandPath(new TreePath(parent.getPath()));
			sentence.setSelectionPath(new TreePath(changedSentence.getPath()));
			presentNode = changedSentence;
			((DefaultTreeModel) sentence.getModel()).reload(changedSentence);
		}

	}

	public class STree extends JTree {
		public DefaultMutableTreeNode root;
		public IndexNode instructionNode;
		
		
		public void teacherupdate(){
			Stack<IndexNode> process=new Stack<IndexNode>();
			for(int i=0;i<root.getChildCount();i++){
				
				IndexNode now=(IndexNode) root.getChildAt(i);
				process.add(now);
				
			}
			
			while(process.size()>0){
				IndexNode now=process.pop();
				if(now.type.equals("sentence")){
					now.tree.changeStructureToTree(now.structure);
				}else if(now.type.equals("folder")){
				  for(int i=0;i<now.getChildCount();i++){
					  IndexNode toadd=(IndexNode) now.getChildAt(i);
					  process.add(toadd);
				  }
				}
			}
		}
		
		public void studentupdate(){
			Stack<IndexNode> process=new Stack<IndexNode>();
			for(int i=0;i<root.getChildCount();i++){
				
				IndexNode now=(IndexNode) root.getChildAt(i);
				process.add(now);
				
			}
			
			while(process.size()>0){
				IndexNode now=process.pop();
				if(now.type.equals("sentence")){
					if(now.response.equals("none")||now.response.equals("error")||now.response.equals("")){
						System.out.println("second case");
						System.out.println(now.structure);
						now.tree.changeStructureToTree2(now.structure);}
					else{
						System.out.println("first case");
						System.out.println(now.response);
					now.tree.changeStructureToTree(now.response);
						
					}
				}else if(now.type.equals("folder")){
				  for(int i=0;i<now.getChildCount();i++){
					  IndexNode toadd=(IndexNode) now.getChildAt(i);
					  process.add(toadd);
				  }
				}
			}
		}

		// input mode=="default"
		// input mode=="empty"--will create a stree with no children attached to
		// the root
		public STree(DefaultMutableTreeNode rootInput, String mode) {

			// create the root node
			super(rootInput);
			System.out.println("st" + 16);
			root = rootInput;

			//setPreferredSize(new Dimension(400, 400));
			if (mode.equals("default")) {
				System.out.println("default STree is create");
				// create the child nodes
				IndexNode instruction0 = new IndexNode("0.", "instruction", "instruction");
//				instruction0.instruction = "--To change the instruction on your problem set: "
//						+ "type the new instruction in this box."
//						+newline
//						+"--To create new sentences: click [new].";
				instruction0.instruction="Please import a sentence set by clicking ->File->Open";
				// add the child nodes to the root node
				root.add(instruction0);
				instructionNode = instruction0;
				super.setSelectionPath(new TreePath(instructionNode.getPath()));
				presentNode = instructionNode;
				System.out.println("presentNode"+presentNode);
				treeMode.forInstructionEdit();
				treeModePlain.dialogue.setText(instructionNode.instruction);
			}

			else {
				// default select root
				super.setSelectionPath(new TreePath(instructionNode.getPath()));
				presentNode = root;
				treeMode.forFolder();
			}
			
			
//			DefaultTreeCellRenderer renderer =
//	                (DefaultTreeCellRenderer)STree.this.getCellRenderer();
//			//renderer.se
//	        renderer.setTextSelectionColor(Color.white);
//	        renderer.setBackgroundSelectionColor(Color.blue);
//	        renderer.setBorderSelectionColor(Color.black);
			
			STree.this.setCellRenderer(new MyTreeCellRenderer());
			

		}
		
		
		public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

		    @Override
		    public Component getTreeCellRendererComponent(JTree tree, Object value,
		            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
		        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

		        // Assuming you have a tree of Strings
//		        String node = (String) ((DefaultMutableTreeNode) value).getUserObject();
		       

		        // If the node is a leaf and ends with "xxx"
		        if (leaf) {
		        	try{
		        	 IndexNode node=(IndexNode)value;
		        	 if(node.correct==1){
		            // Paint the node in blue
		        	setOpaque(true);
		            setBackground(Color.decode("0x99FF99"));}
		        	 else if(node.correct==-1){
		        		 System.out.println("this is the incorrect node");
		        		 setOpaque(true);
				         setBackground(Color.decode("0xFFCCCC"));
		        	 }
		        	 else{
		        		 setOpaque(false);
		        		 }
		        	 }
		        	catch(Exception ee){
		        		ee.printStackTrace();
		        	}
		        }
		        
		        else{
		        	setOpaque(false);
		        }

		        return this;
		    }
		}

		// helper function of resetIndex()
		// set the children of a node into the right index
		public void resetIndex(IndexNode node) {
			for (int i = 0; i < node.getChildCount(); i++) {
				IndexNode child = (IndexNode) node.getChildAt(i);
				child.changeIndex(node.index + i + ".");
				((DefaultTreeModel) getModel()).nodeStructureChanged(child);
			}
		}

		public void changeRoot(DefaultMutableTreeNode node) {
			System.out.println("st" + 19);
			root = node;
			DefaultMutableTreeNode root = node;
			DefaultTreeModel model = new DefaultTreeModel(root);
			this.setModel(null);
			this.setModel(model);
		}

		public void resetIndex() {
			System.out.println("st" + 20);
			Stack<IndexNode> process = new Stack<IndexNode>();

			// set the nodes in the first layer and add them into the stack
			for (int i = 0; i < root.getChildCount(); i++) {
				IndexNode node = (IndexNode) root.getChildAt(i);
				node.changeIndex(i + ".");
				process.add(node);
				((DefaultTreeModel) getModel()).reload(node);
			}

			while (process.size() > 0) {
				IndexNode now = process.pop();
				for (int i = 0; i < now.getChildCount(); i++) {
					IndexNode node = (IndexNode) now.getChildAt(i);
					node.changeIndex(now.index + i + ".");
					((DefaultTreeModel) getModel()).reload(node);
					process.add(node);
				}
			}

		}

		public void updateATreenNode(IndexNode now, Interface.Tree tree) {
			// TODO Auto-generated method stub
			now.updateTree(tree);
			((DefaultTreeModel) getModel()).reload(now);
		}
		
		public void setSelectedNode(DefaultMutableTreeNode x){
			super.setSelectionPath(new TreePath(x.getPath()));
		}

	}

	

	class PopUp extends JPopupMenu {
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem rename = new JMenuItem("Rename");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem cut = new JMenuItem("Cut");
		JMenuItem paste = new JMenuItem("Paste After");
		JMenuItem newCategoryAfter = new JMenuItem("Insert New Category After");
		JMenuItem pasteReplace = new JMenuItem("Paste To Replace");

		public PopUp(String type) {
			System.out.println("st" + 27);
			// according to the type given, add corresponding JMenuItem to menu
			if (type.equals("root")) {
				add(rename);
			}
			if (type.equals("folder")) {
				add(delete);
				add(rename);
				add(copy);
				add(cut);
				add(paste);
				add(pasteReplace);
				add(newCategoryAfter);
			} else if (type.equals("instruction")) {
				add(copy);
				add(paste);
				add(pasteReplace);
				add(newCategoryAfter);
			} else if (type.equals("sentence")) {
				add(delete);
				add(copy);
				add(cut);
				add(paste);
				add(pasteReplace);
				add(newCategoryAfter);
			}

			// the way the following actionlistener is designed is closely
			// related to
			// the corresponding JMenuItem setup
			// because the actionlistener only consider the situation that those
			// items will appear
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 28);
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
					// next is holding the node that after the deletion, where
					// to select nodes

					// same piece of code as in cut actionlistener,
					// if modified, modify together
					DefaultMutableTreeNode next = presentNode.getNextSibling();
					if (next == null) {
						next = presentNode.getPreviousSibling();
					}
					if (next == null) {
						next = parent;
					}
					parent.remove(presentNode);
					((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);

					sentence.resetIndex();
					sentence.expandPath(new TreePath(next.getPath()));
					sentence.setSelectionPath(new TreePath(next.getPath()));
					presentNode = next;

				}

			});

			rename.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 29);
					String name = renameWindow();
					if (name != null) {
						if (presentNode == sentence.root) {
							presentNode.setUserObject(name);
						} else {
							IndexNode now = (IndexNode) presentNode;
							now.setUserObject(now.index + " " + name);
						}
						((DefaultTreeModel) sentence.getModel()).reload(presentNode);
						sentence.expandPath(new TreePath(presentNode.getPath()));
					}
				}

			});

			copy.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 30);
					// TODO Auto-generated method stub
					copyNode = copyWholeNode((IndexNode) presentNode);
					pasteStatus = true;

				}
			});

			paste.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 31);
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
					int index = parent.getIndex(presentNode);
					parent.insert(copyNode, index + 1);
					((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);

					sentence.resetIndex();
					sentence.expandPath(new TreePath(copyNode.getPath()));
					sentence.setSelectionPath(new TreePath(copyNode.getPath()));
					presentNode = copyNode;
					pasteStatus = false;

				}
			});
			// enable according to pasteStatus
			if (pasteStatus == false) {
				paste.setEnabled(false);
			}

			pasteReplace.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 32);
					IndexNode present = (IndexNode) presentNode;
					// if it is the instruction to be replaced,
					// the copier must be another instruction
					if (present.type.equals("instruction")) {
						if (copyNode.type != "instruction") {
							// instruction cannot be replaced by non-instruction
							errorWindow();
						} else {
							DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
							int index = parent.getIndex(presentNode);
							parent.insert(copyNode, index);
							parent.remove(presentNode);
							/// wait to be confirmed, actually did the job
							((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);

							sentence.resetIndex();
							sentence.expandPath(new TreePath(copyNode.getPath()));
							sentence.setSelectionPath(new TreePath(copyNode.getPath()));
							presentNode = copyNode;
						}
					} else {
						DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
						int index = parent.getIndex(presentNode);
						parent.insert(copyNode, index);
						parent.remove(presentNode);
						/// wait to be confirmed, actually did the job
						((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);

						sentence.resetIndex();
						sentence.expandPath(new TreePath(copyNode.getPath()));
						sentence.setSelectionPath(new TreePath(copyNode.getPath()));
						presentNode = copyNode;
					}
					pasteStatus = false;
				}
			});
			// enable according to pasteStatus
			if (pasteStatus == false) {
				pasteReplace.setEnabled(false);
			}

			cut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("st" + 33);
					copyNode = (IndexNode) presentNode;
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) presentNode.getParent();
					// same piece of code as in delete actionlistener,
					// if modified, modify together
					DefaultMutableTreeNode next = presentNode.getNextSibling();
					if (next == null) {
						next = presentNode.getPreviousSibling();
					}
					if (next == null) {
						next = parent;
					}
					parent.remove(presentNode);
					((DefaultTreeModel) sentence.getModel()).nodeStructureChanged(parent);

					sentence.resetIndex();
					sentence.expandPath(new TreePath(parent.getPath()));
					sentence.setSelectionPath(new TreePath(next.getPath()));
					presentNode = next;
					pasteStatus = true;
					paste.setEnabled(true);

				}
			});

			newCategoryAfter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addCategory();
					System.out.println("st" + 34);

				}
			});

		}

	}

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		Grammar grammar = new Grammar();
		Plain layers = new Plain(grammar);
		Interface main_interface = new Interface(layers);
		SentenceTree st=new SentenceTree(main_interface);
		frame.add(st);
		frame.setVisible(true);
		frame.pack();

	}

	
	public void updateATreeNode(IndexNode now, Interface.Tree tree) {

		sentence.updateATreenNode(now, tree);
	}

	
	
	
	
	
}
