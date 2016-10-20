import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class Plain extends JLayeredPane {
	int checkGEorNot=1;
	int locationx;
	int locationy;
	int locationw;
	int locationh;
	//record whether the plain is enabled
	int enabled=1;
	MouseAdapter sgMouseAdapter ;
	MouseAdapter dialogueMouseAdapter;
	MouseAdapter drMouseAdapter ;
	MouseAdapter nodesMouseAdapter;
	//int to record whether popup menu shows up in
	Preference pp=new Preference();
	private int rightclick=0;
	private NodeLabel dragLabel = null;
	JLabel labeler = null;
	private int dragLabelWidthDiv2;
	private int dragLabelHeightDiv2;
	private JPanel clickedPanel = null;
	private MouseAdapter topListener;
	// the point recording panel's location regarding to whole back
	private Point location = null;
	// the point recording part-rel location regard to the mouse
	private Point relLocation = null;
	// the point recording components location regard to the upleft corner of
	// the back inside that scrollpane
	private Point realInLocation = null;
	// the point to record the position of the mouse relative to the whole
	// JLayeredPane
	Point now = null;

	// the splitPane containing dialogue and trash
	JSplitPane trashDialog;
	// the splitPane containing drawroom and sg
	JSplitPane right;
	// the splitPane containing drawroom, sg, and nodes
	final JSplitPane total;
	// the splitPane containing total and dialogue and trash
	final JSplitPane upDown;

	Plain plain = this;
	/// attributes

	Top top;
	Grammar2 grammar;

	// major components

	// the jpanel that hold all other components
	JPanel back;
	// the dialogue window that informs the user the grammar error and other
	// things
	JTextArea dialogue;
	JScrollPane dialogueP;
	// the area labeled as trash, actually unnecessary
	JPanel trash;
	JScrollPane trashP;
	// the rule grammar panel
	GrammarPanel3 nodes;
	// drawing panel
	DrawPanel drawroom;
	// single nodes grammar panel
	GrammarPanel2 sg;
	// the info textField at the bottom to guide the user to use the program
	JLabel info = new JLabel();

	// other attributs
	// Tree tree=new Tree();
	// the set storing nodes added on the drawroom
	Vector<NodeLabel> list;
	// the set storing lines
	Vector<Line> linelist;

	// when mouseMoved, this will be updated for the delete function to delete
	// chosen label
	NodeLabel dlabel;
	// when mouseMoved, this will be updated for the delete function to delete
	// chosen lines
	Vector<Line> dline;
	// used in right-clicking to delete lines, because when mouse moved, dline
	// component will be changed,
	// so when popup menu appears, first store whatever is inside dline into
	// deleteL
	Vector<Line> deleteL;
	// for clicking two nodes to pair them up
	Vector<NodeLabel> doublenode = new Vector<NodeLabel>();
	// the set for dragging to draw lines to store the two points of the line
	final Vector<Point> store;
	// modified by bracketForm, height is the height of the whole tree
	// and width is the width of the whole tree
	int height = 0;
	int width = 0;
	// storing the error message of completeTree
	Vector<String> error = new Vector<String>();

	// the index of how wide the tree goes, this index is used in bracketForm()
	// to
	// determine how wide the tree is.
	double widthIndex = pp.getDouble("WidthIndex");
	// the index of how tall the tree goes, this index is used in bracketForm()
	// to
	// determine how tall the tree is.
	double heightIndex = pp.getDouble("HeightIndex");
	// used for dragging to draw lines
	Point pointEnd = null;
	MyMouseAdapter myMouseAdapter;

	// the int storing the mouse status
	int clicked = 0;
	// clicked=1-->after pressed on the movable nodes from either of the grammar
	// panel --press
	// clicked=2-->dragging the nodes from the grammar panel --drag
	// clicked=3-->after pressed on the moveable nodes from drawroom --press
	// clicked=4-->after dragging the nodes got from drawroom --drag
	// clicked=10-->while drawing a line by dragging --drag
	// clicked=11-->after first clicked on the grammar panel and then pressed
	// somewhere again--press

	/// functions

	// private functions

	private static void createAndShowUI() {
		System.out.println("pl"+1);
		JFrame frame = new JFrame("DragLabelOnLayeredPane");
		Grammar2 grammar = new Grammar2();
		Plain layers = new Plain(grammar);
		frame.setPreferredSize(new Dimension(1200, 900));
		LMenuBar menuB = new LMenuBar();
		frame.add(menuB, BorderLayout.NORTH);
		frame.add(layers, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	// public functions

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		System.out.println("pl"+2);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void main(String[] args) {
		System.out.println("pl"+3);
		setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 24));
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {

				createAndShowUI();
			}
		});
	}

	
	public void changeGrammar(Grammar2 g){
		System.out.println("pl"+4);
		grammar=g;
		
	}
	// the whole tree information can be copied after calling this function and
	// copyLinelist(Vector<NodeLaebel>)
	// nodelabel cannot be copied individually but only can be copied as a whole
	// unit
	// because of the relation between different nodes
	// this step only copy the list of nodes and their relation with each other
	// without building up the linelist
	// need to pass the vector of nodeLabel to be copied
	public Vector<NodeLabel> copylist(Vector<NodeLabel> list) {
		System.out.println("pl"+5);
		Vector<NodeLabel> r = new Vector<NodeLabel>();
		// first get a whole copy list with the same index
		for (NodeLabel x : list) {
			NodeLabel now = new NodeLabel(x.label.getText(),plain);
			now.location = new Point(x.location.x, x.location.y);
			now.setLocation(now.location);
			now.color = x.color;
			now.type = x.type;
			now.gerror = x.gerror;
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
	
	public void updateGrammar(){
		System.out.println("pl"+6);
		nodes.updateGrammar();
		nodes.revalidate();
		sg.updateGrammar();
		sg.revalidate();
		int nodesDividerLocation = nodes.back.getPreferredSize().width;
		total.setDividerLocation(nodesDividerLocation);
		right.revalidate();
		total.revalidate();
	}

	// pass in the already copyied vector of NodeLabel-copyNode
	// the vector of NodeLabel to be copied
	// the vector of Line to be copied
	// update the parentlines, childrenlines for those copied Nodelabel
	public Vector<Line> copyLinelist(Vector<NodeLabel> copyNode, Vector<NodeLabel> list, Vector<Line> linelist) {
		System.out.println("pl"+7);
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

	// will be called in TreeMode -clean button
	// also called in input function
	public void clean() {
		System.out.println("pl"+8);
		linelist = new Vector<Line>();
		list = new Vector<NodeLabel>();
		drawroom.back.removeAll();
		dialogue.setText("");
		repaint();

	}

	// input an int representing scrollPane like nodes, drawroom, or sg
	// and get the location of them relative to the whole Plain
	// i=1; returning the location of nodes
	// i=2; returning the location of drawroom
	// i=3; returning the location of sg
	// i=4; returning the location of dialogue
	// i=5; returning the location of trash
	public Point getLocation(int i) {
		System.out.println("pl"+9);
		Point p1 = upDown.getLocation();
		Point p2 = total.getLocation();
		Point p3 = right.getLocation();
		Point p4 = sg.getLocation();
		Point p5 = trashP.getLocation();
		Point r = null;


		if (i == 1) {
			r = new Point(p1.x + p2.x, p1.y + p2.y);

		} else if (i == 2) {
			r = new Point(p1.x + p2.x + p3.x, p1.y + p2.y + p3.y);
		} else if (i == 3) {
			r = new Point(p1.x + p2.x + p3.x + p4.x, p1.y + p2.y + p3.y + p4.y);
		} else if (i == 4) {
			r = new Point(p1.x, p1.y);
		} else if (i == 5) {
			r = new Point(p1.x + p5.x, p1.y + p5.y);
		}

		return r;
	}

	public JPanel getPanel(Point p) {
		System.out.println("pl"+10);
		JPanel r = null;

		JSplitPane mediate = null;
		try {
			mediate = (JSplitPane) back.getComponentAt(p);
		} catch (Exception e) {
			return r;
		}
		;

		int i = 0;
		while (i == 0) {
			Point mediateLocation = mediate.getLocation();
			p = new Point(p.x - mediateLocation.x, p.y - mediateLocation.y);
			try {
				JSplitPane mediate2 = (JSplitPane) mediate.getComponentAt(p);
				mediate = mediate2;

			} catch (Exception e) {
				JPanel rr = null;
				try {
					JScrollPane getReturn = (JScrollPane) mediate.getComponentAt(p);
					p = new Point(p.x - getReturn.getLocation().x, p.y - getReturn.getLocation().y);
					rr = (JPanel) getReturn.getViewport().getView();
					return rr;
					// return getReturn;
				} catch (Exception e2) {
					return null;
				}

			};
		}
		return r;
	}

	// make a copy of tree passed in to show in the plain, so whatever is
	// modified on the plain
	// is not saved immediately

	public void showTree(TreeMode.Tree tree) {
		System.out.println("pl"+11);
		dialogue.setText("");
		list = copylist(tree.list);
		linelist = copyLinelist(list, tree.list, tree.linelist);
		drawroom.back.removeAll();
		for (NodeLabel x : list) {
			// this step is important, sometimes when things are not showing up,
			// check the size of the thing
			x.setSize(x.getPreferredSize());
			x.setColor();
			drawroom.back.add(x);
			drawroom.revalidate();
			drawroom.repaint();
		}
		repaint();
	}

	// will be called in TreeMode-start button
	public void input(String s) {
		System.out.println("pl"+12);

		clean();
		Rectangle bounds = drawroom.getViewport().getViewRect();
		Point zeroP= drawroom.getViewport().getViewPosition();
		

		int heightdraw = (int) (bounds.height * 0.8)+zeroP.y;
		int i = 40+zeroP.x;

		for (String y : s.split(" ")) {
			//the same code is copied into setLocation in SentenceSetStorage, please 
			//remember to modify both of them
			NodeLabel add = new NodeLabel(y,plain);
			i = (int) (i + widthIndex * add.label.getPreferredSize().getHeight());
			add.setVisible(true);
			add.setLocation(i, heightdraw);
			add.setSize(add.getPreferredSize());
			add.location = add.getLocation();
			add.type = 1;
			i = (int) (i + add.getPreferredSize().getWidth());
			list.addElement(add);
			drawroom.back.add(add);
		}
		drawroom.revalidate();
		drawroom.repaint();

	}

	// call when there is a moving dragLabel
	// change the color when nodes moved
	public void orange(NodeLabel dragLabel) {
		System.out.println("pl"+13);
		Vector<NodeLabel> heads = new Vector<NodeLabel>();

		// reset all the nodes and get a list of head nodes
		for (NodeLabel x : list) {

			x.setColor();
			x.setPlain();

			if (x.parents.size() == 0) {
				heads.add(x);
			}
		}

		if (dragLabel.type == 0 && dragLabel.children.size() == 0) {
			// position of dragLabel's center point relative to drawroom
			Point draglu = dragLabel.location;
			Point center = new Point(draglu.x + dragLabel.getWidth() / 2, draglu.y + dragLabel.getHeight() / 2);
			

			int check = 0;
			// test for straight lines
			for (NodeLabel nl : list) {
				if (nl.parents.size() == 0) {
					int x1 = nl.getLocation().x + nl.label.getLocation().x;
					int x2 = x1 + nl.label.getWidth();
					int y1 = nl.getLocation().y - nl.getHeight();
					int y2 = nl.getLocation().y;

					if (center.x < x2 && center.x > x1 && center.y < y2 && center.y > y1) {
						nl.label.setForeground(Color.decode("0x994C00"));
						dragLabel.label.setForeground(Color.decode("0x994C00"));
						Font font = nl.label.getFont();
						Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
						nl.label.setFont(boldFont);
						dragLabel.label.setFont(boldFont);
						check = 1;
						break;

					}
				}
			}

			Collections.sort(heads);
			// test for two children
			NodeLabel checker = null;
			if (check == 0) {
				for (NodeLabel xi : heads) {
					Point center2 = new Point(xi.getLocation().x + xi.getWidth() / 2,
							xi.getLocation().y + xi.getHeight() / 2);
					if (center2.y > center.y + xi.getHeight() / 2 && center2.y < center.y + xi.getHeight() / 2 * 3) {
						if (checker == null) {
							checker = xi;
						} else {
							if (Math.abs(center2.x - center.x) < Math
									.abs(checker.getLocation().x + checker.getWidth() / 2 - center.x)) {
								checker = xi;
							}
						}
					}

				}
				if (checker != null) {
					NodeLabel checker2 = null;
					int checkercenter = checker.getLocation().x + checker.getWidth() / 2;
					if (checkercenter < center.x) {
						int i = heads.indexOf(checker);
						if (i > 0) {
							int centerr = heads.elementAt(i - 1).getLocation().x
									+ heads.elementAt(i - 1).getWidth() / 2;
							if (centerr > center.x)
								checker2 = heads.elementAt(i - 1);
						}
					} else {

						int i = heads.indexOf(checker);
						if (i < heads.size() - 1) {
							int centerr = heads.elementAt(i + 1).getLocation().x
									+ heads.elementAt(i + 1).getWidth() / 2;
							if (centerr < center.x) {
								checker2 = heads.elementAt(i + 1);
							}
						}

					}
					if (checker2 != null && checker.type == 0 && checker2.type == 0
							&& checker2.getLocation().y > center.y) {
						check = 1;

						checker.label.setForeground(Color.decode("0x994C00"));
						checker2.label.setForeground(Color.decode("0x994C00"));
						dragLabel.label.setForeground(Color.decode("0x994C00"));
						Font font = checker.label.getFont();
						Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
						checker.label.setFont(boldFont);
						checker2.label.setFont(boldFont);
						dragLabel.label.setFont(boldFont);

					}
				}
			}

			// check for 3 children
			if (check == 0) {
				checker = null;
				for (NodeLabel xi : heads) {
					int x1 = xi.getLocation().x + xi.label.getLocation().x;
					int x2 = x1 + xi.label.getWidth();
					int y1 = xi.getLocation().y - xi.getHeight();
					int y2 = y1 - xi.getHeight();
					if (center.x < x2 && center.x > x1) {
						checker = xi;
					}
				}

				if (checker != null) {

					NodeLabel checker2 = null;
					NodeLabel checker3 = null;

					for (int i = heads.size() - 1; i >= 0; i--) {
						if (heads.elementAt(i).getLocation().x > checker.getLocation().x) {
							checker3 = heads.elementAt(i);
							if (i < heads.size() - 2) {
								checker2 = heads.elementAt(i + 2);
							}
							break;
						}
					}

					if (checker2 != null && checker3 != null && checker.type == 0 && checker2.type == 0
							&& checker3.type == 0 && checker2.getLocation().y > center.y
							&& checker3.getLocation().y > center.y) {

						int ymin = checker.getLocation().y;
						if (ymin > checker2.getLocation().y) {
							ymin = checker2.getLocation().y;
						}
						if (ymin > checker3.getLocation().y) {
							ymin = checker3.getLocation().y;
						}
						if (center.y > ymin - 2 * checker.getHeight()) {
							check = 1;

							checker.label.setForeground(Color.decode("0x994C00"));
							checker2.label.setForeground(Color.decode("0x994C00"));
							checker3.label.setForeground(Color.decode("0x994C00"));
							dragLabel.label.setForeground(Color.decode("0x994C00"));
							Font font = checker.label.getFont();
							Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
							checker.label.setFont(boldFont);
							checker2.label.setFont(boldFont);
							checker3.label.setFont(boldFont);
							dragLabel.label.setFont(boldFont);

						}
					}
				}

			}
		}
	}

	// used in mousereleased to add lines
	// dragLabel is already drawroom zuobiao
	private void orangeLines(NodeLabel dragLabel) {
		System.out.println("pl"+14);
		Vector<NodeLabel> heads = new Vector<NodeLabel>();
		for (NodeLabel x : list) {
			Font font = x.label.getFont();
			Font boldFont = new Font(font.getFontName(), Font.PLAIN, font.getSize());
			x.label.setFont(boldFont);
			if (x.color == 4) {
				x.label.setForeground(Color.red);
			} else {
				x.label.setForeground(Color.black);
			}
			if (x.parents.size() == 0) {
				heads.add(x);
			}
		}

		if (dragLabel.type == 0 && dragLabel.children.size() == 0) {
			// position of dragLabel's center point relative to drawroom

			Point center = new Point(dragLabel.location.x + dragLabel.getWidth() / 2,
					dragLabel.location.y + dragLabel.getHeight() / 2);

			int check = 0;
			// test for straight lines
			for (NodeLabel nl : list) {
				if (nl.parents.size() == 0) {
					int x1 = nl.getLocation().x + nl.label.getLocation().x;
					int x2 = x1 + nl.label.getWidth();
					int y1 = nl.getLocation().y - nl.getHeight();
					int y2 = nl.getLocation().y;

					if (center.x < x2 && center.x > x1 && center.y < y2 && center.y > y1) {

						addlines(nl, dragLabel);
						adjustOneTree(dragLabel);
						check = 1;
						break;

					}
				}
			}

			Collections.sort(heads);
			// test for two children
			NodeLabel checker = null;
			if (check == 0) {
				for (NodeLabel xi : heads) {
					Point center2 = new Point(xi.getLocation().x + xi.getWidth() / 2,
							xi.getLocation().y + xi.getHeight() / 2);
					if (center2.y > center.y + xi.getHeight() / 2 && center2.y < center.y + xi.getHeight() / 2 * 3) {
						if (checker == null) {
							checker = xi;
						} else {
							if (Math.abs(center2.x - center.x) < Math
									.abs(checker.getLocation().x + checker.getWidth() / 2 - center.x)) {
								checker = xi;
							}
						}
					}

				}
				if (checker != null) {
					NodeLabel checker2 = null;
					int checkercenter = checker.getLocation().x + checker.getWidth() / 2;
					if (checkercenter < center.x) {
						int i = heads.indexOf(checker);
						if (i > 0) {
							int centerr = heads.elementAt(i - 1).getLocation().x
									+ heads.elementAt(i - 1).getWidth() / 2;
							if (centerr > center.x)
								checker2 = heads.elementAt(i - 1);
						}
					} else {

						int i = heads.indexOf(checker);
						if (i < heads.size() - 1) {
							int centerr = heads.elementAt(i + 1).getLocation().x
									+ heads.elementAt(i + 1).getWidth() / 2;
							if (centerr < center.x) {
								checker2 = heads.elementAt(i + 1);
							}
						}

					}
					if (checker2 != null && checker.type == 0 && checker2.type == 0
							&& checker2.getLocation().y > center.y) {
						check = 1;

						addlines(dragLabel, checker);
						addlines(dragLabel, checker2);
						adjustOneTree(dragLabel);

					}
				}
			}

			// check for 3 children
			if (check == 0) {
				checker = null;
				for (NodeLabel xi : heads) {
					int x1 = xi.getLocation().x + xi.label.getLocation().x;
					int x2 = x1 + xi.label.getWidth();
					int y1 = xi.getLocation().y - xi.getHeight();
					int y2 = y1 - xi.getHeight();
					if (center.x < x2 && center.x > x1) {
						checker = xi;
					}
				}

				if (checker != null) {

					NodeLabel checker2 = null;
					NodeLabel checker3 = null;

					for (int i = heads.size() - 1; i >= 0; i--) {
						if (heads.elementAt(i).getLocation().x > checker.getLocation().x) {
							checker3 = heads.elementAt(i);
							if (i < heads.size() - 2) {
								checker2 = heads.elementAt(i + 2);
							}
							break;
						}
					}

					if (checker2 != null && checker3 != null && checker.type == 0 && checker2.type == 0
							&& checker3.type == 0 && checker2.getLocation().y > center.y
							&& checker3.getLocation().y > center.y) {

						int ymin = checker.getLocation().y;
						if (ymin > checker2.getLocation().y) {
							ymin = checker2.getLocation().y;
						}
						if (ymin > checker3.getLocation().y) {
							ymin = checker3.getLocation().y;
						}
						if (center.y > ymin - 2 * checker.getHeight()) {
							check = 1;

							addlines(dragLabel, checker);
							addlines(dragLabel, checker2);
							addlines(dragLabel, checker3);
							adjustOneTree(dragLabel);

						}
					}
				}

			}
		}

	}

	// helper function in draggingNodes
	// if a dragLabel is dragged into a line while moving or dragging
	// called when the dragLabel is still on the dragLayer
	// change the line into color6--pink
	// so the coordinate is relative to JLayeredPane
	private void findaddinline(NodeLabel dragLabel) {
		System.out.println("pl"+15);
		// line variable to record the line crossed if there is any line crossed
		Line cl = null;
		// first determine whether a line crosses the dragLabel
		for (Line i : linelist) {
			if (dragLabel.crosslingLine(i)) {
				cl = i;
				// this solves the puzzle if there are multiple lines being
				// crossed
				// only one line will be recorded
				break;
			}
		}

		if (cl != null) {
			cl.color = 6;
			top.repaint();
		}

	}

	// helper function of addinline
	// the function returns the head of a tree when get the input of
	// one node in that tree
	// if on the path of finding parent, there is one node having more than one
	// parent
	// the function returns null
	private NodeLabel findHead(NodeLabel x) {
		System.out.println("pl"+16);
		Stack<NodeLabel> stack = new Stack<NodeLabel>();
		stack.add(x);

		// the NodeLabel to return
		NodeLabel r = null;
		while (stack.size() > 0) {
			NodeLabel now = stack.pop();
			if (now.parents.size() == 0) {
				r = now;
			} else if (now.parents.size() > 1) {
				return null;
			} else {
				stack.add(now.parents.elementAt(0));
			}
		}
		return r;
	}

	// helper function in releaseDR
	// if a dragLabel is dragged into a line
	// called when the dragLabel is added on to drawroom
	// so the coordinate is relative to drawroom
	private void addinline(NodeLabel dragLabel) {
		System.out.println("pl"+17);
		// line variable to record the line crossed if there is any line crossed
		Line cl = null;
		// first determine whether a line crosses the dragLabel
		for (Line i : linelist) {
			if (dragLabel.crosslingLine(i)) {
				cl = i;
				// this solves the puzzle if there are multiple lines being
				// crossed
				// only one line will be recorded
				break;
			}
		}

		// if there is actually line crossed
		if (cl != null) {
			if (cl.parent != null && cl.children != null) {
				removelines(cl.parent, cl.children);
				addlines(dragLabel, cl.parent);
				addlines(dragLabel, cl.children);
				adjustOneTree(findHead(cl.parent));
			}
		}

	}

	public void removeNode(NodeLabel dlabel) {
		System.out.println("pl"+18);

		drawroom.remove(dlabel);
		Vector<NodeLabel> others = new Vector<NodeLabel>();

		for (NodeLabel x : dlabel.children) {
			others.add(x);
		}
		for (NodeLabel x : dlabel.parents) {
			others.add(x);
		}
		for (NodeLabel x : others) {
			removelines(dlabel, x);
		}
		list.remove(dlabel);
		Vector<Line> ll = new Vector<Line>();
		for (Line x : linelist) {
			if (x.parent == dlabel || x.children == dlabel) {
				ll.add(x);

			}
		}

		for (Line xy : ll) {

			linelist.remove(xy);
		}

		repaint();

	}

	static boolean inRange(Line line, Point p) {
		System.out.println("pl"+19);
		int mx = line.start.x;
		int my = line.start.y;
		int nx = line.end.x;
		int ny = line.end.y;
		int px = p.x;
		int py = p.y;
		double length = Math.sqrt((mx - nx) * (mx - nx) + (my - ny) * (my - ny));
		double rangeLength = length / 5;

		if (mx == nx) {
			if (px - mx > -5 && px - mx < 5) {
				if (my > ny) {
					if (py <= my - rangeLength && py >= ny + rangeLength) {
						return true;
					} else {
						if (py <= ny - rangeLength && py >= my + rangeLength) {
							return true;
						}
					}
				}
			}
		} else if (my == ny) {

			if (mx > nx) {
				if (px <= mx - rangeLength && px >= nx + rangeLength) {
					return true;
				} else {
					if (px <= nx - rangeLength && px >= mx + rangeLength) {
						return true;
					}
				}
			}

		} else {
			double t = (double) (my - ny) / (mx - nx);
			double ct = (double) (my - t * mx);
			double tangent = (double) (-1) / (my - ny) * (mx - nx);
			double c = (double) (py - tangent * px);
			double x = (double) (c - ct) / (t - tangent);
			double y = (double) (x * tangent + c);
			if (((x - px) * (x - px) + (y - py) * (y - py) < 25)) {
				double xx = (double) (rangeLength / Math.sqrt(1 + t * t));
				if (mx < nx) {
					if (x < nx - xx && x > mx + xx) {
						return true;
					}
				} else {
					if (x > nx + xx && x < mx - xx) {
						return true;
					}
				}
			}

		}

		return false;
	}

	// adjust a tree from the head node passed to all the way down, but do
	// nothing
	// to the nodes higher than the head node
	public void adjustOneTree(NodeLabel head) {
		System.out.println("pl"+20);
		Stack<NodeLabel> process1 = new Stack<NodeLabel>();

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
				NodeLabel now = process1.pop();

				for (NodeLabel c : now.children) {
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

			Stack<NodeLabel> queue = new Stack<NodeLabel>();

			while (process1.size() > 0) {

				// using first in first out strategy
				NodeLabel now = process1.firstElement();
				process1.remove(0);
				queue.add(now);
				if (now.children.size() == 0 && y == 0) {
					y = now.location.y;
				}

				// now reverse the level value from down to top
				// word nodes will be leveled as 1 always
				if (now.type == 1) {
					now.level = 1;
				} else {
					now.level = total - now.level;
				}
				for (NodeLabel x : now.children) {
					process1.add(x);
				}
			}

			// reverse order of BFS to do x-y cor setting for all nodes in the
			// tree
			while (queue.size() > 0) {
				NodeLabel now = queue.pop();
				if (now.level > 1) {
					now.location.y = (int) (y - (now.level - 1) * now.getHeight()
							- (now.level - 1) * heightIndex * now.label.getHeight());
				}
				if (now.parents.size() > 0 && now != head) {
					NodeLabel nowparent = now.parents.elementAt(0);
					Collections.sort(nowparent.children);
					int x2 = nowparent.children.firstElement().location.x
							+ nowparent.children.firstElement().getWidth();
					int x1 = nowparent.children.lastElement().location.x;
					nowparent.location.x = (x2 + x1) / 2 - nowparent.getWidth() / 2;

					for (NodeLabel cw : nowparent.children) {
						try {
							queue.remove(cw);
							if (cw.level > 1) {
								cw.location.y = (int) (y - (cw.level - 1) * cw.getHeight()
										- (cw.level - 1) * heightIndex * cw.label.getHeight());
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
			Stack<NodeLabel> terminals = new Stack<NodeLabel>();
			// the set storing all nodes in the subtree
			Stack<NodeLabel> tree = new Stack<NodeLabel>();
			process1.add(head);

			// get the value for set tree
			while (process1.size() > 0) {
				NodeLabel now = process1.pop();
				tree.add(now);
				for (NodeLabel ch : now.children) {
					process1.add(ch);
				}
			}

			// set y-cor for ever node in the subtree
			for (NodeLabel wh : tree) {
				if (wh.level > 1) {
					wh.location.y = (int) (head.location.y
							+ (wh.level - 1) * (wh.getHeight() + wh.label.getHeight() * heightIndex));
				}
				if (wh.children.size() == 0) {
					terminals.add(wh);
				}

			}

			// set x-cor for terminals
			Collections.sort(terminals);
			// the fixed x point
			int x = terminals.lastElement().location.x;
			int xnow = x;
			for (int i = terminals.size() - 1; i >= 0; i--) {
				terminals.elementAt(i).location.x = xnow;
				xnow += terminals.elementAt(i).getWidth() + terminals.elementAt(i).label.getHeight() * widthIndex;
			}

			while (terminals.size() > 0) {
				NodeLabel now = terminals.pop();
				if (now.parents.size() > 0 && now != head) {
					NodeLabel nowparent = now.parents.firstElement();
					Collections.sort(nowparent.children);
					int x1 = nowparent.children.lastElement().location.x;
					int x2 = nowparent.children.firstElement().location.x
							+ nowparent.children.firstElement().getWidth();
					int center = (x1 + x2) / 2;
					nowparent.location.x = center - nowparent.getWidth() / 2;
					terminals.add(nowparent);
					for (NodeLabel jh : nowparent.children) {
						try {
							terminals.remove(jh);
						} catch (Exception df) {
							// comment here to denote i did nothing here in
							// Powell today hahha
							// please do not do that again
						}
					}
				}
			}

		}
		// if a tree contains words terminals and non words terminals
		else {
			Stack<NodeLabel> terminals = new Stack<NodeLabel>();
			Stack<NodeLabel> tree = new Stack<NodeLabel>();
			process1.add(head);

			while (process1.size() > 0) {
				NodeLabel now = process1.pop();
				tree.add(now);
				for (NodeLabel ch : now.children) {
					process1.add(ch);
				}
			}
			System.out.println("tree size"+tree.size());
			for (NodeLabel wh : tree) {

				if (wh.children.size() == 0) {
					terminals.add(wh);

				}

			}
			Collections.sort(terminals);
			System.out.println("terminal size"+terminals.size());
			// the fixed x point
			int x = terminals.lastElement().location.x;
			int xnow = x;
			for (int i = terminals.size() - 1; i >= 0; i--) {
				terminals.elementAt(i).location.x = xnow;
				xnow += terminals.elementAt(i).getWidth() + terminals.elementAt(i).label.getHeight() * widthIndex;
			}

			while (terminals.size() > 0) {
				NodeLabel now = terminals.pop();
				if (now.parents.size() > 0 && now != head) {
					NodeLabel nowparent = now.parents.firstElement();
					Collections.sort(nowparent.children);
					int x1 = nowparent.children.lastElement().location.x;
					int x2 = nowparent.children.firstElement().location.x
							+ nowparent.children.firstElement().getWidth();
					int center = (x1 + x2) / 2;
					nowparent.location.x = center - nowparent.getWidth() / 2;
					terminals.add(nowparent);
					for (NodeLabel jh : nowparent.children) {
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
				NodeLabel now = process1.pop();
				if (now.type == 1) {
					yu = now.location.y;
					// break;
				}
				for (NodeLabel ccc : now.children) {
					process1.add(ccc);
				}
			}

			process1.add(head);
			while (process1.size() > 0) {
				NodeLabel now = process1.pop();
				if (now.type == 1) {
					now.level = 1;

				} else {
					now.level = total - now.level;

					now.location.y = (int) (yu
							- (now.level - 1) * (now.getHeight() + now.label.getHeight() * heightIndex));
				}
				for (NodeLabel xc : now.children) {
					process1.add(xc);
				}
			}

		}

		// in MouseReleased, this called before dragLabel is added into list
		// so for the head, do it sepereately
		head.setLocation(head.location);

		for (NodeLabel xu : list) {

			xu.setLocation(xu.location);
		}

		for (Line i : linelist) {
			i.update();
		}
		drawroom.revalidate();
		drawroom.repaint();
	}

	// will be called in TreeMode-sl button
	public void adjust() {
		System.out.println("pl"+21);
		Vector<NodeLabel> here = new Vector<NodeLabel>();
		Vector<NodeLabel> words=new Vector<NodeLabel>();
		for (NodeLabel x : list) {
			if (x.parents.size() == 0) {
				here.add(x);
			}
			if(x.type==1){
				words.add(x);
			}
		}

		
		//first adjust basic nodes' position
		Collections.sort(words);
		Collections.reverse(words);
		
		Rectangle bounds = drawroom.getViewport().getViewRect();
		Point zeroP= drawroom.getViewport().getViewPosition();
		int heightdraw = (int) (bounds.height * 0.8)+zeroP.y;
		int i = 40+zeroP.x;

		for (NodeLabel y : words) {
			//the same code is copied into setLocation in SentenceSetStorage, please 
			//remember to modify both of them
			
			
			y.setLocation(i,heightdraw);
			y.location = y.getLocation();
			i = (int) (i + widthIndex * y.label.getPreferredSize().getHeight());
			i = (int) (i + y.getPreferredSize().getWidth());
			
			
		}
		
		
		for (NodeLabel y : here) {
			adjustOneTree(y);
			// repaint();
		}
		for (Line xy : linelist)
			xy.update();

		repaint();

	}

	public void topDown() {
		adjust();
		System.out.println("pl"+22);
		for (NodeLabel x : list) {
			if(x.type==1){
			x.location.y = (int) (x.parents.elementAt(0).getLocation().y+x.getHeight()+heightIndex * x.label.getHeight());;;
			x.setLocation(x.location);}		
		}
		for (Line y : linelist) {
			y.start = new Point(y.children.location.x + y.children.getWidth() / 2,
					y.children.location.y + y.children.label.getLocation().y);
			y.end = new Point(y.parent.location.x + y.parent.getWidth() / 2,
					y.parent.location.y + y.parent.label.getHeight() + y.parent.label.getLocation().y);
		}
		
	}

	public void bottomUp() {
		for(NodeLabel x:list){
			if(x.type==1){
				Rectangle bounds = drawroom.getViewport().getViewRect();
				Point zeroP= drawroom.getViewport().getViewPosition();
				

				int heightdraw = (int) (bounds.height * 0.8)+zeroP.y;
				x.location.y=heightdraw;
				x.setLocation(x.location);
			}
		}
		adjust();
		

	}

	// used in MouseReleased when dragging line to delete
	// used in popup menu for lines when deleting the lines
	// used in key board deleting line function
	public void removeline(Line x) {
		System.out.println("pl"+24);
		// if both ends have nodes attached to
		if (x.parent != null && x.children != null) {
			removelines(x.parent, x.children);
		}

		// if at least one end is not attached to anything
		else {
			linelist.remove(x);
			if (x.parent != null) {
				x.parent.childrenlines.remove(x);
			} else if (x.children != null) {
				x.children.parentlines.remove(x);
			}
		}

	}

	// used in MouseReleased
	// this delete all lines associated with one node even those lines might not
	// have both ends
	public void removelines(NodeLabel dragLabel) {
		System.out.println("pl"+25);
		// remove all parent lines of dragLabel with parents
		Vector<NodeLabel> pp = new Vector<NodeLabel>();
		for (NodeLabel xx : dragLabel.parents) {
			pp.add(xx);
		}
		for (NodeLabel x : pp) {
			removelines(dragLabel, x);
		}

		// remove all children lines of dragLabel with children
		Vector<NodeLabel> cc = new Vector<NodeLabel>();
		for (NodeLabel xx : dragLabel.children) {
			cc.add(xx);
		}
		for (NodeLabel x : cc) {
			removelines(dragLabel, x);
		}

		// those lines without double nodes are still there
		// the following for loops delete those lines
		for (Line i : dragLabel.childrenlines) {
			linelist.remove(i);
		}

		for (Line i : dragLabel.parentlines) {
			linelist.remove(i);
		}
	}

	// helper function of updatePC
	// helper function of removelines(NodeLabel dragLabel)
	// regardless of parent child relation, able to remove lines
	// and the relation between two nodes
	public void removelines(NodeLabel x, NodeLabel y) {
		System.out.println("pl"+26);
		// build a independent copy of x's parents lines and y'parents lines
		Vector<Line> xparentss = new Vector<Line>();
		Vector<Line> yparentss = new Vector<Line>();

		for (Line i : x.parentlines) {
			xparentss.add(i);
		}
		for (Line i : y.parentlines) {
			yparentss.add(i);
		}

		// int to store whether to check for the other possibility
		int anothercheck = 0;

		// poss1: if x is the child of y
		for (Line xy : xparentss) {
			if (xy.parent == y) {
				x.parentlines.remove(xy);
				y.childrenlines.remove(xy);
				linelist.remove(xy);
				y.children.remove(x);
				x.parents.remove(y);
				anothercheck = 1;

			}

		}

		// poss2: if y is the child of x
		if (anothercheck == 0) {
			for (Line xy : yparentss) {
				if (xy.parent == x) {
					y.parentlines.remove(xy);
					x.childrenlines.remove(xy);
					linelist.remove(xy);
					x.children.remove(y);
					y.parents.remove(x);
					break;
				}
			}
		}

	}

	// helper function of updatePC
	public void addlines(NodeLabel x, NodeLabel y) {
		System.out.println("pl"+26);
		int widthy = y.getWidth() / 2;
		int heighty = y.getHeight();
		int widthx = x.getWidth() / 2;
		int heightx = x.getHeight();
		if (x.location.getY() > y.location.getY()) {
			Line newline = new Line(plain);
			newline.start = new Point(x.location.x + widthx, x.location.y + x.label.getLocation().y);
			newline.end = new Point(y.location.x + widthy, y.location.y + heighty - y.label.getLocation().y);
			newline.parent = y;
			newline.children = x;
			System.out.println("parent is"+newline.parent.label.getText());
			System.out.println("child is"+newline.children.label.getText());
			linelist.add(newline);
			x.parentlines.add(newline);
			y.childrenlines.add(newline);
			x.parents.add(y);
			y.children.add(x);

		} else {
			Line newline = new Line(plain);
			newline.end = new Point(x.location.x + widthx, x.location.y + heightx - x.label.getLocation().y);
			newline.start = new Point(y.location.x + widthy, y.location.y + y.label.getLocation().y);

			newline.parent = x;
			newline.children = y;
			System.out.println("parent is"+newline.parent.label.getText());
			System.out.println("child is"+newline.children.label.getText());
			linelist.add(newline);
			y.parentlines.add(newline);
			x.childrenlines.add(newline);
			y.parents.add(x);
			x.children.add(y);
		}

	}

	
	
	
	// used in MouseDragged to update the children-parent relation for dragLabel
	public void updatePC(NodeLabel dragLabel) {
		System.out.println("pl"+27);
		// new Vector to store the children lines of dragLabel
		Vector<Line> dclines = new Vector<Line>();

		// new Vector to store the parent lines of dragLabel
		Vector<Line> dplines = new Vector<Line>();

		for (Line i : dragLabel.parentlines) {
			dplines.add(i);
		}

		for (Line i : dragLabel.childrenlines) {
			dclines.add(i);
		}

		for (Line xy : dclines) {

			if (xy.children != null) {
				System.out.println("childen no null");
				removelines(dragLabel, xy.children);
				addlines(dragLabel, xy.children);
			} else {
				System.out.println("childen null");
				xy.end = new Point(dragLabel.location.x + dragLabel.getWidth() / 2,
						dragLabel.location.y + dragLabel.label.getLocation().y + dragLabel.label.getHeight());
			}
		}

		for (Line xy : dplines) {
			if (xy.parent != null) {
				System.out.println("parent no null");
				removelines(dragLabel, xy.parent);
				addlines(dragLabel, xy.parent);
			} else {
				System.out.println("parent null");
				xy.start = new Point(dragLabel.location.x + dragLabel.getWidth() / 2,
						dragLabel.location.y + dragLabel.label.getLocation().y);
			}
		}
	}

	public void setdt() {
		System.out.println("pl"+28);
		info.setText("Welcome to UCLA-Parse and Practice Program");
	}

	/**
	 * Layout each of the components in this JLayeredPane so that they all fill
	 * the entire extents of the layered pane -- from (0,0) to (getWidth(),
	 * getHeight())
	 */
	@Override
	public void doLayout() {
		System.out.println("pl"+29);
		super.doLayout();

		synchronized (getTreeLock()) {
			int w = getWidth();
			int h = getHeight();
			for (Component c : getComponents()) {
				if (getLayer(c) == JLayeredPane.DEFAULT_LAYER || getLayer(c) == JLayeredPane.PALETTE_LAYER) {
					c.setBounds(0, 0, w, h);
				}

			}
		}
		dialogueP.getViewport().setViewPosition(new Point(0,0));
	}

	public void disableDrawing() {

	}
	
	// helper function in MouseReleased
			// check Grammar Error
			public void checkGE() {
				if(checkGEorNot==1){
				System.out.println("checkGE");
				//whether there is at least one wrong node
				String check=null;
				for (final NodeLabel ji : list) {
					// clear grammar error for all nodes
					ji.gerror = "";
					Collections.sort(ji.children);
					Collections.reverse(ji.children);
					String e = grammar.obeyRules(ji,ji.children);
					if (e != null) {
						check=e;
						System.out.println("grammar incorrect");
						ji.color = 4;
						ji.label.setForeground(Color.red);
//						back.revalidate();
//						back.repaint();
						ji.gerror = e;
					} else {
						System.out.println("grammar correct");
						ji.color = 0;
						ji.label.setForeground(Color.black);
						
				
//						SwingUtilities.invokeLater(new Runnable()
//						{
//						    public void run()
//						    {
//						    	System.out.println("i am here runner");
//						    	ji.label.revalidate();
//						    	ji.label.repaint();
//						    	ji.revalidate();
//						    	ji.repaint();
//						    	back.updateUI();
//						    	back.revalidate();
//						        back.repaint();
//						    }
//						});
						
						System.out.println("should be black");
						
						
//						back.revalidate();
//						back.repaint();
//						back.remove(ji);
//						back.add(ji);

					}
				}
				
				if(check==null){
					dialogue.setText("");
				}else{
					dialogue.setText(check);
					dialogueP.getViewport().setViewPosition(new Point(0,0));
					System.out.println("set");
				}
			}else{
				for(NodeLabel ji:list){
					ji.color = 0;
					ji.label.setForeground(Color.black);
				}
			}
			}

	// constructor
	public Plain(Grammar2 g) {
		// initialize all components
		System.out.println("pl"+30);
		// attributes
		top = new Top();
		grammar = g;

		// basic components
		back = new JPanel();
		dialogue = new JTextArea();
		trash = new JPanel();
		nodes = new GrammarPanel3(grammar);
		drawroom = new DrawPanel();
		sg = new GrammarPanel2(grammar);
		info = new JLabel();

		// other attributes
		now = null;
		dlabel = null;
		dline = new Vector<Line>();
		list = new Vector<NodeLabel>();
		linelist = new Vector<Line>();
		deleteL = new Vector<Line>();
		store = new Vector<Point>();

		super.add(back, JLayeredPane.DEFAULT_LAYER);
		super.add(top, JLayeredPane.PALETTE_LAYER);

		// setting up the mouseAdapter
		myMouseAdapter = new MyMouseAdapter();
		super.addMouseListener(myMouseAdapter);
		super.addMouseMotionListener(myMouseAdapter);

		// top setting, otherwise, it will cover the whole back
		top.repaint();
		top.setOpaque(false);
		topListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getPoint());
			}
		};
		
//		top.addMouseListener(topListener);
//		disable();
//		enable();
		//top.setBackground(new Color(213, 134, 145, 123));
		//back.setBackground(new Color(0,0,0,64));
		//top.setOpaque(true);
		//top.removeMouseListener(topListener);

		
		// top.addMouseListener(topListener);

		// set up the basic components
		back.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		back.setBackground(Color.LIGHT_GRAY);

		// 1:0,0-1,0==dialogue informing the user grammar error etc
		dialogue.setEditable(false);
		dialogue.setBackground(Color.decode("0xA0A0A0"));
		dialogue.setLineWrap(true);
		dialogue.setWrapStyleWord(true);
		dialogueP = new JScrollPane(dialogue);

		JLabel message = new JLabel("Trash");
		trashP = new JScrollPane(trash);
		trash.add(message);

		// the splitPane containing dialogue and trash
		trashDialog = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dialogueP, trashP);
		// the splitPane containing drawroom and sg
		right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, drawroom, sg);
		// the splitPane containing drawroom, sg, and nodes
		total = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nodes, right);
		// the splitPane containing total and dialogue and trash
		upDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT, trashDialog, total);

		right.setBorder(null);
		right.setResizeWeight(1);
		upDown.setBorder(null);
		upDown.setResizeWeight(0.1);
		total.setBorder(null);
		trashDialog.setBorder(null);
		trashDialog.setResizeWeight(0.7);

		dialogueP.setBorder(new LineBorder(Color.black, 1));
		trashP.setBorder(new LineBorder(Color.black, 1));
		nodes.setBorder(new LineBorder(Color.black, 1));
		drawroom.setBorder(new LineBorder(Color.black, 1));
		sg.setBorder(new LineBorder(Color.black, 1));

		sgMouseAdapter = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(3);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - sg.back.getLocation().x,
						relLocation.y - sg.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_PRESSED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mousePressed(me);
			}

			public void mouseDragged(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(3);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - sg.back.getLocation().x,
						relLocation.y - sg.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseDragged(me);
			}

			public void mouseReleased(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(3);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - sg.back.getLocation().x,
						relLocation.y - sg.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseReleased(me);
			}

			public void mouseMoved(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(3);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - sg.back.getLocation().x,
						relLocation.y - sg.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseMoved(me);
			}
		};
		sg.addMouseListener(sgMouseAdapter);
		sg.addMouseMotionListener(sgMouseAdapter);

		 drMouseAdapter = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				//e.
				relLocation = e.getPoint();
				location = getLocation(2);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
						relLocation.y - drawroom.back.getLocation().y);
				if(e.getButton() == MouseEvent.BUTTON3){
					rightclick=1;
					
				}
				
				MouseEvent me = new MouseEvent(plain, e.getButton(), 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mousePressed(me);
			}

			public void mouseDragged(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(2);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
						relLocation.y - drawroom.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseDragged(me);
			}

			public void mouseReleased(MouseEvent e) {
				rightclick=0;
				relLocation = e.getPoint();
				location = getLocation(2);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
						relLocation.y - drawroom.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseReleased(me);

			}

			public void mouseMoved(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(2);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
						relLocation.y - drawroom.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseMoved(me);
			}
		};
		drawroom.addMouseListener(drMouseAdapter);
		drawroom.addMouseMotionListener(drMouseAdapter);

		nodesMouseAdapter = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(1);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - nodes.back.getLocation().x,
						relLocation.y - nodes.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_PRESSED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mousePressed(me);

			}

			public void mouseDragged(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(1);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - nodes.back.getLocation().x,
						relLocation.y - nodes.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseDragged(me);
			}

			public void mouseReleased(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(1);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - nodes.back.getLocation().x,
						relLocation.y - nodes.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0, now.x, now.y, 0, false);
			    myMouseAdapter.mouseReleased(me);
			}

			public void mouseMoved(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(1);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				realInLocation = new Point(relLocation.x - nodes.back.getLocation().x,
						relLocation.y - nodes.back.getLocation().y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseMoved(me);
			}
		};
		nodes.addMouseListener(nodesMouseAdapter);
		nodes.addMouseMotionListener(nodesMouseAdapter);
		MouseAdapter trashPMouseAdapter = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				relLocation = e.getPoint();
				location = getLocation(5);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseMoved(me);

			}

			public void mouseDragged(MouseEvent e) {

				relLocation = e.getPoint();
				location = getLocation(5);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseDragged(me);
			}
			
			public void mouseReleased(MouseEvent e){
				relLocation = e.getPoint();
				location = getLocation(5);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseReleased(me);
			}
		};
		trashP.addMouseMotionListener(trashPMouseAdapter);
		trashP.addMouseListener(trashPMouseAdapter);

		dialogueMouseAdapter = new MouseAdapter() {

			public void mouseMoved(MouseEvent e) {
			System.out.println("dialogue moved");
				relLocation = e.getPoint();
				location = getLocation(4);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseMoved(me);
			}

			public void mouseDragged(MouseEvent e) {
				System.out.println("dialogue dragged");
				relLocation = e.getPoint();
				location = getLocation(4);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseDragged(me);
			}
		
			
			public void mouseReleased(MouseEvent e){
				System.out.println("dialogue released");
				relLocation = e.getPoint();
				location = getLocation(4);
				now = new Point(location.x + relLocation.x, location.y + relLocation.y);
				MouseEvent me = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0, now.x, now.y, 0, false);
				myMouseAdapter.mouseReleased(me);
			}
		};
		
		dialogue.addMouseMotionListener(dialogueMouseAdapter);
		dialogue.addMouseListener(dialogueMouseAdapter);

		int nodesDividerLocation = nodes.back.getPreferredSize().width;
		total.setDividerLocation(nodesDividerLocation);
		sg.setMinimumSize(new Dimension(sg.getWidth(), nodes.labelHeight * 2));
		trashP.setMinimumSize(new Dimension(trash.getPreferredSize().width, nodes.labelHeight * 2));

		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 10);
		back.add(upDown, c);

		// 6:0,2-2,2==info, information to guide the user to use the program
		// info.setEditable(false);

		info.setBackground(Color.lightGray);
		info.setText("Welcome to UCLA-Parse and Practice Program");

		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 10, 10);
		back.add(info, c);

	}

	class Top extends JPanel {

		// helper function of paint for drawroom
		// update two points
		// will be called when the size of store is two
		public void updateTP() {
			System.out.println("pl"+31);
			// create a newline based on the two points provided
			Line newline = new Line(plain);
			if (store.elementAt(0).y > store.elementAt(1).y) {
				newline.start = store.elementAt(0);
				newline.end = store.elementAt(1);
			} else {
				newline.start = store.elementAt(1);
				newline.end = store.elementAt(0);
			}

			// check for potential parent or child nearby and attach them to the
			// line
			newline.checkfornodes();

			// to check whether the line with the same parent and child is
			// existing
			// both parent and child cannot be null and must be the same
			// because with lines having null end being the same, they are not
			// actually the same
			Line existed = null;
			for (Line ll : linelist) {
				if (ll.parent == newline.parent && ll.children == newline.children && ll.parent != null
						&& ll.children != null) {
					existed = ll;
					break;
				}
			}

			// if not existing, add that line
			if (existed == null) {
				linelist.addElement(newline);
			}
			// if existing, delete the same line
			else {
				removelines(existed.parent, existed.children);
			}

			// clear all the storage variable
			pointEnd = null;
			store.removeElementAt(1);
			store.removeElementAt(0);

		}

		// helper function of paint for drawroom
		// update for double nodes-clicking and twinkling option
		// will be called when the size of doublenode is two
		public void updateDN() {
			System.out.println("pl"+32);
			// int to store whether to delete a line
			int delete = 0;
			for (NodeLabel xy : doublenode.elementAt(0).children) {
				if (xy == doublenode.elementAt(1)) {
					delete = 1;
					break;
				}
			}
			if (delete == 0) {
				for (NodeLabel xy : doublenode.elementAt(0).parents) {
					if (xy == doublenode.elementAt(1)) {
						delete = 1;
						break;
					}
				}
			}

			// line already existed, so delete the line
			if (delete == 1) {
				removelines(doublenode.elementAt(0), doublenode.elementAt(1));
			}
			// otherwise add a new line
			else {
				addlines(doublenode.elementAt(0), doublenode.elementAt(1));
			}
//			System.out.println("color0"+doublenode.elementAt(0).color);
//			System.out.println("color1"+doublenode.elementAt(1).color);
			doublenode.elementAt(0).stop();
			doublenode.elementAt(1).stop();
//			SwingUtilities.invokeLater(new Runnable()
//			{
//			    public void run()
//			    {
//			    	System.out.println("i am here runner");
//			    	checkGE();
////			    	System.out.println("color0"+doublenode.elementAt(0).color);
////			    	System.out.println("color1"+doublenode.elementAt(1).color);
//			    }
//			});

			//// TO MAKE BOTH OF THEM TWINKLE ONE MORE TIME AND STOP
			doublenode.removeAllElements();

		}

		public void paint(Graphics g) {
			System.out.println("top paint");
			final Graphics2D g2 = (Graphics2D) g;
			super.paint(g);
			

			// store's size being two mean dragging to create lines
			// based on y-cor comparison, put value into the start and end of
			// the newline
			// line-start==y-cor larger, child
			// line-end==y-cor smaller, parent

			if (store.size() == 2) {
				// update two points
				updateTP();
				Top.this.repaint();
				checkGE();
				//back.repaint();
			}

			if (doublenode.size() == 2) {
				// update double node for clicking and twinkling
				updateDN();
				Top.this.repaint();
				checkGE();
				//back.repaint();
			}
			g2.setStroke(new BasicStroke(1));
			Point adjust=plain.drawroom.back.getLocation();
			if (store.size() == 1) {
				g2.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				
				try {
					g.drawLine(store.elementAt(0).x +plain.getLocation(2).x+adjust.x,
							store.elementAt(0).y + plain.getLocation(2).y+adjust.y, pointEnd.x + plain.getLocation(2).x+adjust.x,
							pointEnd.y + plain.getLocation(2).y+adjust.y);
				}

				catch (NullPointerException e) {
				}
				;
			}
			
			if(dline.size()>0){
				for (Line xy : dline) {
					
						g2.setStroke(new BasicStroke(2));
						g.setColor(Color.blue);
					
					g.drawLine(xy.start.x+plain.getLocation(2).x+adjust.x, xy.start.y+plain.getLocation(2).y +adjust.y,
							xy.end.x+plain.getLocation(2).x+adjust.x , xy.end.y +plain.getLocation(2).y+adjust.y);
				}
			}
			
			

			//drawroom.repaint();
		}
	}

	class DrawPanel extends JScrollPane {

		JPopupMenu menu = new JPopupMenu("Popup");
		JMenuItem delete = new JMenuItem("delete");
		Back back = new Back();

		public DrawPanel() {
			
			System.out.println("pl"+34);
			back.setPreferredSize(new Dimension(3000, 3000));
//			back.setBackground(Color.decode("0xFFCCE5"));
	back.setBackground(Color.white);
			back.setLayout(null);
			super.setViewportView(back);
			menu.add(delete);
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// text.requestFocusInWindow();
					// TODO Auto-generated method stub

					for (Line xx : deleteL) {
						removeline(xx);
					}

					repaint();

				}

			});
		}
		
	
		
		

		
	}

	class Back extends JPanel{
		public void paint(Graphics g){
			System.out.println("back paint");
			final Graphics2D g2 = (Graphics2D) g;
			super.paint(g);
		
			for (Line xy : linelist) {
				// black plain form
				if (xy.color == 0) {
					g2.setStroke(new BasicStroke(1));
					g.setColor(Color.black);
				}
				// orange bold ready form
				else if (xy.color == 3) {
					g2.setStroke(new BasicStroke(3));
					g.setColor(Color.decode("0x994C00"));
				} else if (xy.color == 6) {
					g2.setStroke(new BasicStroke(3));
					g.setColor(Color.decode("0xFF66FF"));
				}
				// blue bold chosen form
				else {
					g2.setStroke(new BasicStroke(2));
					g.setColor(Color.blue);
				}
			g.drawLine(xy.start.x, xy.start.y ,
						xy.end.x , xy.end.y );
			}
		}
	}

	class MyMouseAdapter extends MouseAdapter {

		// used on MouseMoved and MouseDragged when moving nodes
		// x is mouse x-cor relative to the whole JLayeredPane
		// y is mouse y-cor relative to the whole JLayeredPane
		// when dragLabel is already added to dragLayer
		// dl is the dragLabel
		void draggingNodes(int x, int y, NodeLabel dl) {
			System.out.println("pl"+36);
			location=getLocation(2);
			relLocation=new Point(x-location.x,y-location.y);
			realInLocation=new Point(relLocation.x-drawroom.back.getLocation().x,relLocation.y-drawroom.back.getLocation().y);
			// set the position of dragLabel on dragLayer and on drawroom
			dl.setLocation(x - dragLabelWidthDiv2, y - dragLabelHeightDiv2);
			dl.location = new Point(realInLocation.x - dragLabelWidthDiv2, realInLocation.y - dragLabelHeightDiv2);
			// set relative nodes to orange
			orange(dl);

			// set relative lines to orange
			linesReady(dl);

			// find addin line
			findaddinline(dl);

		}

		// helper function for draggingNodes to check whether some lines are
		// ready to be paired up with the dragLabel and change those lines to
		// orange
		// change those lines not in range into normal color

		// not totally independent with pairupwithLines, they have the same
		// first part
		// if modify the first part, don't forget to modify for pairupwithLines
		// as well
		void linesReady(NodeLabel dl) {
			System.out.println("pl"+37);
			// first part, same

			for (Line hihi : linelist) {
				hihi.color = 0;
			}

			// to store those lines whose parent position is ready for the
			// dragLabel
			Vector<Line> parentss = new Vector<Line>();
			// to store those lines whose children position is ready for the
			// dragLabel
			Vector<Line> childrens = new Vector<Line>();

			for (Line xixi : linelist) {
				if (xixi.parent == null && xixi.children != dl) {
					if (xixi.end.x <= dl.location.x + dl.getWidth() && xixi.end.x >= dl.location.x
							&& xixi.end.y <= dl.location.y + dl.getHeight() && xixi.end.y >= dl.location.y) {
						parentss.add(xixi);
					}

				}
				if (xixi.children == null && xixi.parent != dl) {
					if (xixi.start.x <= dl.location.x + dl.getWidth() && xixi.start.x >= dl.location.x
							&& xixi.start.y <= dl.location.y + dl.getHeight() && xixi.start.y >= dl.location.y) {
						childrens.add(xixi);
					}
				}
			}

			// second part, different
			for (Line hihi : parentss) {
				hihi.color = 3;
				// color=3, 994C00 orange for ready lines to be paired up
			}

			for (Line hihi : childrens) {
				hihi.color = 3;
				// color=3, 994C00 orange for ready lines to be paired up
			}

		}

		// called in MouseRealeased

		// not totally independent with linesReady, they have the same first
		// part
		// if modify the first part, don't forget to modify for linesReady as
		// well
		void pairupwithLines(NodeLabel dl) {
			System.out.println("pl"+38);
			// first part
			for (Line hihi : linelist) {
				hihi.color = 0;
			}

			// to store those lines whose parent position is ready for the
			// dragLabel
			Vector<Line> parentss = new Vector<Line>();
			// to store those lines whose children position is ready for the
			// dragLabel
			Vector<Line> childrens = new Vector<Line>();

			for (Line xixi : linelist) {
				if (xixi.parent == null && xixi.children != dl) {
					if (xixi.end.x <= dl.location.x + dl.getWidth() && xixi.end.x >= dl.location.x
							&& xixi.end.y <= dl.location.y + dl.getHeight() && xixi.end.y >= dl.location.y) {
						parentss.add(xixi);
					}

				}
				if (xixi.children == null && xixi.parent != dl) {
					if (xixi.start.x <= dl.location.x + dl.getWidth() && xixi.start.x >= dl.location.x
							&& xixi.start.y <= dl.location.y + dl.getHeight() && xixi.start.y >= dl.location.y) {
						childrens.add(xixi);
					}
				}
			}

			// second part
			// different part with linesReady

			// because lines settled down, change the color and font back to
			// default setting
			for (Line hihi : parentss) {
				hihi.color = 0;
			}

			for (Line hihi : childrens) {
				hihi.color = 0;
			}

			// actually pair those up

			for (Line hihi : parentss) {
				hihi.end = new Point(dragLabel.location.x + dragLabel.getWidth() / 2,
						dragLabel.location.y + dragLabel.label.getLocation().y + dragLabel.label.getHeight());
				hihi.parent = dragLabel;
				dragLabel.childrenlines.add(hihi);
				if (hihi.children != null) {
					dragLabel.children.add(hihi.children);
				}
			}

			for (Line hihi : childrens) {
				hihi.start = new Point(dragLabel.location.x + dragLabel.getWidth() / 2,
						dragLabel.location.y + dragLabel.label.getLocation().y);
				hihi.children = dragLabel;
				dragLabel.parentlines.add(hihi);
				if (hihi.parent != null) {
					dragLabel.parents.add(hihi.parent);
				}
			}

		}

		// called in MousePressed, if the clicked panel is the grammar panel
		// except the situation of adding nodes by clicking
		// relied on point now, make sure already update point now before using
		// it
		void pressOnGP() {
			System.out.println("pl"+39);
			// Point location = clickedPanel.getLocation();
			// int xi = now.x - location.x;
			// int yi = now.y - location.y;
			// if (clickedPanel == nodes) {
			// labeler = nodes.getTheLabel(xi, yi);
			// } else {
			// labeler = sg.getTheLabel(xi, yi);
			// }

			// if successfully get a movable node
			if (labeler != null) {
				// set the border of nodes on grammar panel to be lowered
				labeler.setBorder(new BevelBorder(BevelBorder.LOWERED));

				// create a new NodeLabel using the text from labeler
				dragLabel = new NodeLabel(labeler.getText(), plain);
				dragLabel.setSize(dragLabel.getPreferredSize());
				// having the half width and half height ready for later moving
				// or dragging
				dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
				dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

				// adding the new NodeLabel to dragLayer
				int x = now.x - dragLabelWidthDiv2;
				int y = now.y - dragLabelHeightDiv2;
				dragLabel.setLocation(x, y);
				location=getLocation(2);
				relLocation=new Point(now.x-location.x,now.y-location.y);
				realInLocation=new Point(relLocation.x-drawroom.back.getLocation().x,relLocation.y-drawroom.back.getLocation().y);
				dragLabel.location=new Point(realInLocation.x-dragLabelWidthDiv2,realInLocation.y-dragLabelHeightDiv2);
				add(dragLabel, JLayeredPane.DRAG_LAYER);

				// indicate the status of pressing on the grammar panel for once
				clicked = 1;

			} else {
				return;
			}

		}

		// helper function for mousePressed, if the clicked panel is the
		// drawroom
		void pressOnDR(MouseEvent me) {
			System.out.println("pl"+40);
			try {
				// whether able to get a dragLabel on the location from drawroom
				dragLabel = (NodeLabel) clickedPanel.getComponentAt(realInLocation.x, realInLocation.y);
				int xnow = realInLocation.x - dragLabel.getLocation().x;
				int ynow = realInLocation.y - dragLabel.getLocation().y;
				// whether able to get the labler from the dragLabel
				labeler = (JLabel) dragLabel.getComponentAt(xnow, ynow);
			} catch (Exception haha) {
			}

			// if no labeler get, ready for lines drawing or line moving
			if (labeler == null) {
				System.out.println("labeler is null");
				int tostart = 0;
				// check whether to move lines
				for (Line xy : linelist) {
					//// inRange function needs to be adjusted for long lines
					if (inRange(xy, realInLocation)) {
						double x1 = xy.start.getX();
						double y1 = xy.start.getY();
						double x2 = xy.end.getX();
						double y2 = xy.end.getY();
						xy.color = 1;
						// make sure not to draw lines if supposed to move lines
						tostart = 1;
						xy.x1 = x1 - realInLocation.x;
						xy.y1 = y1 - realInLocation.y;
						xy.x2 = x2 - realInLocation.x;
						xy.y2 = y2 - realInLocation.y;
						// add the elements to move lines set, later can be
						// merged into dline
						dline.addElement(xy);
					}
				}

				// if not moving any lines, add the first position for drawing a
				// line
				if (tostart == 0) {
					System.out.println("elsewhere");
					store.add(realInLocation);
				}

				// if the lines is chosen, to start==1, the popup menu thing
				// the popup menu for the node is written in the NodeLabel class
				else if (rightclick==1) {
					System.out.println("I am here");
					drawroom.menu.show(me.getComponent(), me.getX(), me.getY());
					for (Line i : dline) {
						deleteL.add(i);
					}
				}
			} else { // indicate the status of choosing a node from drawroom
				clicked = 3;
			}

		}

		@Override
		public void mousePressed(MouseEvent me) {
			System.out.println("pl"+41);
			now = me.getPoint();

			// special status of adding nodes by clicking
			if (clicked == 1) {
				clicked = 11;
			} else {
				clickedPanel = getPanel(now);

				// if clicked on grammar panel
				if (clickedPanel == nodes.back) {
					location = getLocation(1);
					relLocation = new Point(now.x - location.x, now.y - location.y);
					realInLocation = new Point(relLocation.x - nodes.back.getLocation().x,
							relLocation.y - nodes.back.getLocation().y);
					labeler = nodes.getTheLabel(realInLocation.x, realInLocation.y);
					pressOnGP();

				} else if (clickedPanel == sg.back) {
					location = getLocation(3);
					relLocation = new Point(now.x - location.x, now.y - location.y);
					realInLocation = new Point(relLocation.x - sg.back.getLocation().x,
							relLocation.y - sg.back.getLocation().y);
					labeler = sg.getTheLabel(realInLocation.x, realInLocation.y);
					pressOnGP();
				}

				// if clicked on drawroom
				else if (clickedPanel == (JPanel)drawroom.back) {
					location = getLocation(2);
					relLocation = new Point(now.x - location.x, now.y - location.y);
					realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
							relLocation.y - drawroom.back.getLocation().y);
					pressOnDR(me);
				}else{
				}

			}

		}

		// called in MouseMoved
		// when mouse moved in the drawroom, find ready lines or nodes
		// thus get ready for deleting nodes or lines using keyboard option
		// thus get ready for dragging lines
		// relied on point now, so before calling the function, make sure now is
		// updated
		void MovingMouse() {
			System.out.println("pl"+42);
			location = getLocation(2);
			relLocation = new Point(now.x - location.x, now.y - location.y);
			realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
					relLocation.y - drawroom.back.getLocation().y);
			// clean out dialogue
			//dialogue.setText("");
			// first clear out the previous ready lines and nodes record
			dlabel = null;
			dline = new Vector<Line>();

			// int to store whether to look for ready nodes
			// if tostart becomes 1, that means there are lines ready
			// then no need to look for ready nodes
			int tostart = 0;

			for (Line xy : linelist) { // change every line to default color and
										// font first
				xy.color = 0;

				if (inRange(xy, realInLocation)) {
					// mark lines as blue to indicate close enough as chosen
					xy.color = 1;

					// get ready for dragging the chosen line
					double x1 = xy.start.getX();
					double y1 = xy.start.getY();
					double x2 = xy.end.getX();
					double y2 = xy.end.getY();
					xy.x1 = x1 - realInLocation.x;
					xy.y1 = y1 - realInLocation.y;
					xy.x2 = x2 - realInLocation.x;
					xy.y2 = y2 - realInLocation.y;

					// no need to look for ready nodes
					tostart = 1;

					// add a ready line to the set
					dline.addElement(xy);

				}
				top.repaint();
			}

			// set every node to default color and font
			for (NodeLabel x : list) {
				x.setPlain();
				x.setColor();
			}

			// if there is need to explore ready nodes
			if (tostart == 0) { // a new variable to store the chosen node with
								// the smallest distance
								// calculated by function
								// NodeLabel.distanceToLabel(Point)
				NodeLabel colored = null;
				for (NodeLabel x : list) {
					if (x.distanceToLabel(realInLocation) != 0) {
						if (colored == null) {
							colored = x;
						} else if (x.distanceToLabel(realInLocation) < colored.distanceToLabel(realInLocation)) {
							colored = x;
						}

					}
				}
			

				// if there is a chosen node
				if (colored != null) { // set Bold and Blue for one time
										// color property is not changed
					colored.setBold();
					colored.label.setForeground(Color.blue);
					if(!colored.gerror.equals(dialogue.getText())){
						dialogueP.getViewport().setViewPosition(new Point(0,0));
						
					dialogue.setText(colored.gerror);
					System.out.println("set");
					
					System.out.println("here");
					}

					// set the outsidely-known chosen node to the one we just
					// chose out
					dlabel = colored;

					// set the info text
					info.setText("Node " + colored.label.getText() + " is ready to draw a line");
				} else { // set the info text to default text
					setdt();
				}
			}

		}

		@Override
		public void mouseMoved(MouseEvent me) {
			System.out.println("pl"+43);
			now = me.getPoint();
			if (clicked == 1) {
				draggingNodes(now.x, now.y, dragLabel);

			} else {
				clickedPanel = getPanel(now);
				

				if (clickedPanel == (JPanel)drawroom.back) {
					MovingMouse();
				} else {
					if(clickedPanel==null){
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			System.out.println("pl"+44);
			NodeLabel colored = null;
			now = me.getPoint();
			location = getLocation(2);
			relLocation = new Point(now.x - location.x, now.y - location.y);
			realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
					relLocation.y - drawroom.back.getLocation().y);


			// if on the pressed time, there is no labeler got
			// either in the moving line mode, or in the drawing line mode
			if (labeler == null) {

				// in the moving line mode
				if (dline.size() > 0) {
					for (Line xy : dline) {
						xy.start.setLocation(xy.x1 + realInLocation.x, xy.y1 + realInLocation.y);
						xy.end.setLocation(xy.x2 + realInLocation.x, xy.y2 + realInLocation.y);
					}
				}

				// in the drawing line mode
				else {
					pointEnd = realInLocation;

					// indicate the status of drawing lines by dragging
					clicked = 10;
				}

			}

			// indicate the status of adding nodes by dragging
			else if (clicked == 1) {
				clicked = 2;
			}

			// indicate the status of starting to dragging nodes got from the
			// drawroom
			// and add the nodes to dragPanel
			// did not do that because when pressing a node on drawroom, it's
			// possible we want
			// to choose that node to twinkle or right click that node without
			// adding it on to
			// the dragLayer
			else if (clicked == 3) {
				clicked = 4;
				int x = now.x - dragLabelWidthDiv2;
				int y = now.y - dragLabelHeightDiv2;
				dragLabel.setLocation(x, y);
				drawroom.remove(dragLabel);

				// if on dragLayer, then not in the list
				list.remove(dragLabel);
				add(dragLabel, JLayeredPane.DRAG_LAYER);
			}

			// the normal form of dragging, to notice ready lines and nodes
			else {
				// set the new location and properties for the dragLabel
				// mark ready nodes and ready lines to orange
				draggingNodes(now.x, now.y, dragLabel);

				// update the parent-children relation for dragLabel
				// this is not needed in MouseMoved because MouseMoved is only
				// used in adding nodes
				updatePC(dragLabel);
			}

			// it's necessary here;
			repaint();
		}

		// helper function in MouseReleased
		// should call before point now is updated
		public void releaseDR(MouseEvent e) {
			System.out.println("pl"+45);
			now=e.getPoint();
			location = getLocation(2);
			relLocation = new Point(now.x - location.x, now.y - location.y);
			realInLocation = new Point(relLocation.x - drawroom.back.getLocation().x,
					relLocation.y - drawroom.back.getLocation().y);

			// when pressed did not get any labler
			// either in line drawing mode
			// or line moving mode
			if (labeler == null) {
				// in line moving mode
				if (dline.size() > 0) {
					for (Line xy : dline) {
						xy.start.setLocation(xy.x1 + realInLocation.x, xy.y1 + realInLocation.y);
						xy.end.setLocation(xy.x2 + realInLocation.x, xy.y2 + realInLocation.y);
						//// something to be done here, the relationship should
						//// be updated of the parent and child of that line
					}
					dline.removeAllElements();
				}
				// in line drawing mode
				else if (store.size() == 1) {
					// add another point for store to form a line
					// or delete all the points in store if the two points are
					// too close to each other
					Point start = store.elementAt(0);
					if ((start.x - realInLocation.x) * (start.x - realInLocation.x)
							+ (start.y - realInLocation.y) * (start.y - realInLocation.y) > 100) {
						store.addElement(realInLocation);
					} else {
						store.removeAllElements();
					}

				}

				// when release does not have any function in drawroom
				// and one node is already chosen,
				// randomly clicking somewhere else on the drawroom will
				// unchoose the chosen one
				else if (doublenode.size() == 1) {
					doublenode.elementAt(0).stop();
					doublenode.removeAllElements();
				}

			}
			// clicked=2, adding nodes by dragging
			// clicked=11, adding nodes by clicking
			// clicked=4, moving nodes on drawroom
			else if (clicked == 2 || clicked == 11 || clicked == 4) {

				// the following 4 parts must be in this order, 2 and 3 have to
				// be in between 1 and 4
				// 1 needs to be before 2 and 3, 4 needs to be after because of
				// the setting of the function
				// orangeLines

				// 1.settle down the dragLabel
				dragLabel.setLocation(realInLocation.x - dragLabelWidthDiv2, realInLocation.y - dragLabelHeightDiv2);
				dragLabel.location = dragLabel.getLocation();
				remove(dragLabel);
				drawroom.back.add(dragLabel);
				

				// the following two parts must be before the third part
				// 2.drawlines for ready nodes
				orangeLines(dragLabel);

				// 3.pair up with ready lines
				pairupwithLines(dragLabel);

				// 4.add the dragLabel to list
				list.add(dragLabel);

				// if the dragLabel is added into a line
				addinline(dragLabel);

				// restore the border for adding nodes on grammar panel once
				// the adding process has ended
				if (clicked != 4) {
					labeler.setBorder(new BevelBorder(BevelBorder.RAISED));
				}

			}

			// if just pressed on one node in the drawroom without dragging it
			else if (clicked == 3) {
				// press the same node twice to make it stop twinkling
				if (doublenode.size() == 1 && doublenode.elementAt(0) == dragLabel) {

					doublenode.removeAllElements();
					dragLabel.stop();
				}
				// press the second node after there is a node already twinkling
				else {
					//// to look at the color issue
					//// also for the little drag bug
					dragLabel.browntwinkle();
					doublenode.add(dragLabel);
				}
				drawroom.repaint();
			}

			// clear the dragLabel and the labler variable
			// because cannot include the situation of clicked==1
			dragLabel = null;
			labeler = null;

		}

		// helper function in MouseReleased
		public void releaseEL() {
			System.out.println("pl"+46);
			if (labeler == null) {
				if (dline.size() > 0) {

					for (Line xx : dline) {
						removeline(xx);
					}
					dline.removeAllElements();
					//repaint();

				}

				else if (store.size() == 1) {
					store.removeAllElements();
					info.setText("line cannot be added because it goes beyond the range of the drawing panel");
					repaint();
				}

				return;
			}

			else if (clicked == 2 || clicked == 11 || clicked == 4) {
				// remove all lines associated with dragLabel
				removelines(dragLabel);

				// remove dragLabel from dragLayer, while nodes are on
				// dragLayer, they are not
				// in list, so no need to delete from list
				remove(dragLabel);
				list.remove(dragLabel);
				
				

				// if at the end of adding nodes, set grammar panel node border
				// to default
				if (clicked == 2 || clicked == 11) {
					labeler.setBorder(new BevelBorder(BevelBorder.RAISED));
				}
				dragLabel=null;
				labeler=null;
			}

			// only if clicked==1, those storage variables don't have to be
			// cleaned
			else if (clicked != 1) {
				dragLabel = null;
				labeler = null;
			}

		}

		

		@Override
		public void mouseReleased(MouseEvent me) {
			System.out.println("pl"+48);
			// if not in the dragging to draw lines status, remove everything in
			// store
			// because there is situation in pressing random-non special area,
			// store will be added of one point
			// if released right after pressing without dragging
			// we want to delete that added position
			if (clicked != 10 && store.size() > 0) {
				store.removeElementAt(store.size() - 1);
			}

			try {
				clickedPanel = getPanel(now);
			} catch (ClassCastException ec) {
				// for those that cannot be casted to JPanel
				// if dragging to add lines, without release, clickedPanel will
				// still
				// be drawroom
				clickedPanel = null;
			}

			// when released in drawroom
			if (clickedPanel == (JPanel)drawroom.back) {
				releaseDR(me);
			}

			// when relased position is not in drawroom
			else {
				// release elsewhere
				releaseEL();
			}

			// the following code check for each node whether they obey the
			// rules or not
			checkGE();
			//very necessary
			repaint();
		}

	}

	public void clean2() {
		System.out.println("clean2");
		linelist = new Vector<Line>();
		Vector<NodeLabel> deleteList=new Vector<NodeLabel>();
		for(NodeLabel x:list){
			if(x.type!=1){
				deleteList.add(x);
			}else{
				x.parentlines=new Vector<Line>();
				x.parents=new Vector<NodeLabel>();
				x.children=new Vector<NodeLabel>();
				x.childrenlines=new Vector<Line>();
			}
		}
		for(NodeLabel x:deleteList){
			drawroom.back.remove(x);
			list.remove(x);
		}
		dialogue.setText("");
		repaint();
	}

	
	public void instructionEdit(){
		dialogue.setBorder(new LineBorder(Color.black, 3));
		sg.removeMouseListener(sgMouseAdapter) ;
		drawroom.removeMouseListener(drMouseAdapter);
		nodes.removeMouseListener(nodesMouseAdapter);
		dialogue.removeMouseListener(dialogueMouseAdapter);
		dialogue.setEditable(true);
		//top.addMouseListener(topListener);
		
	}
	public void sentenceEdit(){
		dialogue.setBorder(null);
		dialogue.setEditable(false);
		sg.removeMouseListener(sgMouseAdapter) ;
		drawroom.removeMouseListener(drMouseAdapter);
		nodes.removeMouseListener(nodesMouseAdapter);
		sg.addMouseListener(sgMouseAdapter) ;
		drawroom.addMouseListener(drMouseAdapter);
		nodes.addMouseListener(nodesMouseAdapter);
		dialogue.removeMouseListener(dialogueMouseAdapter);
		dialogue.addMouseListener(dialogueMouseAdapter);
		
	}
	
	public void folderEdit(){
		dialogue.setBorder(null);
		dialogue.setEditable(false);
		sg.removeMouseListener(sgMouseAdapter) ;
		drawroom.removeMouseListener(drMouseAdapter);
		nodes.removeMouseListener(nodesMouseAdapter);
		dialogue.removeMouseListener(dialogueMouseAdapter);
	}
	
	public void instruction() {
		// TODO Auto-generated method stub
		sg.removeMouseListener(sgMouseAdapter) ;
		drawroom.removeMouseListener(drMouseAdapter);
		nodes.removeMouseListener(nodesMouseAdapter);
		dialogue.removeMouseListener(dialogueMouseAdapter);
	}

	public void sentence() {
		// TODO Auto-generated method stub
		sg.removeMouseListener(sgMouseAdapter) ;
		drawroom.removeMouseListener(drMouseAdapter);
		nodes.removeMouseListener(nodesMouseAdapter);
		sg.addMouseListener(sgMouseAdapter) ;
		drawroom.addMouseListener(drMouseAdapter);
		nodes.addMouseListener(nodesMouseAdapter);
		dialogue.removeMouseListener(dialogueMouseAdapter);
		dialogue.addMouseListener(dialogueMouseAdapter);
	}
	
	public void getlocationcor(){
		int xmin=0;
		int ymin=0;
		int xmax=0;
		int ymax=0;
		for(NodeLabel x:list){
			int nxmin=x.getLocation().x;
			int nxmax=x.getLocation().x+x.getWidth();
			int nymin=x.getLocation().y;
			int nymax=x.getLocation().y+x.getHeight();
			if(xmin==0){
				xmin=nxmin;
			}else if(nxmin<xmin){
				xmin=nxmin;
			}
			
			if(ymin==0){
				ymin=nymin;
			}else if(nymin<ymin){
				ymin=nymin;
			}
			
			if(xmax==0){
				xmax=nxmax;
			}else if(nxmax>xmax){
				xmax=nxmax;
			}
			
			if(ymax==0){
				ymax=nymax;
			}else if(nymax>ymax){
				ymax=nymax;
			}
		}
		
		locationx=xmin;
		locationy=ymin;
		locationw=xmax-xmin;
		locationh=ymax-ymin;
	}

	public boolean completeTree() {
		// TODO Auto-generated method stub
		int right=0;
		for(NodeLabel x:list){
			if(x.parents.size()==0){
				right++;
			}
		}
		
		if(right==1){
			return true;
		}
		
		return false;
	}

	public void turnoffRC() {
		// TODO Auto-generated method stub
		checkGEorNot=0;
		checkGE();
	}

	public void turnonRC() {
		// TODO Auto-generated method stub
		checkGEorNot=1;
		checkGE();
	}
	
}
