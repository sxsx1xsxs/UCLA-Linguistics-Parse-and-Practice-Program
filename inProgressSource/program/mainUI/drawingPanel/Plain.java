package program.mainUI.drawingPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import program.mainUI.Interface;
import program.Preference;
import program.grammar.Grammar;
import program.grammar.HeadRule;

public class Plain extends JLayeredPane {
	//The border width of the whole plain
	final private int MARGIN_WIDTH=10;

	//the top layer covering the whole Plain
	private Top top;
	// the bottom jpanel that hold all other components 
	private JPanel back;
	// the dialogue window that informs the user the grammar error and other things
	public DialogueP dialogueP;
	// the area labeled as trash, actually unnecessary
	private TrashP trashP;
	//the drawing area
	//Top needs to talk to it
	public InteractiveCanvas drawingArea;
	// the rule grammar panel
	private InteractiveGrammarPanel nodes;
	// single nodes grammar panel
	private InteractiveGrammarPanel sg;
	// the info textField at the bottom to guide the user to use the program
	private JLabel info = new JLabel();


	//JSplitPane parts
	// the splitPane containing dialogue and trash
	private JSplitPane trashDialog;
	// the splitPane containing drawroom and sg
	private JSplitPane right;
	// the splitPane containing right nodes
	private JSplitPane total;
	// the splitPane containing total and trashDialog
	private JSplitPane upDown;

	//--------------------------------------------------------------------------------------------------

	//constructor
	public Plain(Grammar g) throws Exception {
		// initialize all components
		top = new Top();
		top.plain=this;
		back = new JPanel();
		dialogueP = new DialogueP();
		trashP = new TrashP();
		Grammar grammar=new Grammar();
		nodes = new InteractiveGrammarPanel(grammar,top,"RuleFormPanel");
		drawingArea = new InteractiveCanvas(top);
		sg = new InteractiveGrammarPanel(grammar,top,"SingleColumnPanel");
		info = new JLabel();

		this.add(back, JLayeredPane.DEFAULT_LAYER);
		this.add(top, JLayeredPane.PALETTE_LAYER);
		this.setUpUI();

	}

	//--------------------------------------------------------------------------------------------------

	//@constructor helper: set up layout on back with nested JSplitPanes
	private void setUpUI() {

		back.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		back.setBackground(Color.LIGHT_GRAY);

		// the splitPane containing dialogue and trash
		trashDialog = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dialogueP, trashP);
		// the splitPane containing drawroom and sg
		right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, drawingArea, sg);
		// the splitPane containing drawroom, sg, and nodes
		total = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nodes, right);
		// the splitPane containing total and dialogue and trash
		upDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT, trashDialog, total);

		right.setBorder(null);
		right.setResizeWeight(1);
		upDown.setBorder(null);
		upDown.setResizeWeight(0.1);
		total.setBorder(null);
		total.setResizeWeight(0);
		trashDialog.setBorder(null);
		trashDialog.setResizeWeight(0.7);

		dialogueP.setBorder(new LineBorder(Color.black, 1));
		trashP.setBorder(new LineBorder(Color.black, 1));
		nodes.setBorder(new LineBorder(Color.black, 1));
		drawingArea.setBorder(new LineBorder(Color.black, 1));
		sg.setBorder(new LineBorder(Color.black, 1));

		sg.setMinimumSize(new Dimension(0, 0));
		trashP.setMinimumSize(new Dimension(0, 0));
		trashDialog.setMinimumSize(new Dimension(0,0));
		dialogueP.setMinimumSize(new Dimension(0,0));
		nodes.setMinimumSize(new Dimension(0,0));
		total.setMinimumSize(new Dimension(0,0));

		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH/2, MARGIN_WIDTH);
		back.add(upDown, c);

		info.setBackground(Color.lightGray);
		info.setText("Welcome to UCLA-Parse and Practice Program");

		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(MARGIN_WIDTH/2, MARGIN_WIDTH, MARGIN_WIDTH, MARGIN_WIDTH);
		back.add(info, c);

	}

	//@main helper to set the font of the whole display
	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	//--------------------------------------------------------------------------------------------------

	//this is a public function that needs to be called after frame is set up
	public void setDividersLocation() {
		int nodesPreferWidth=nodes.getPreferredSize().width;
		int sgPreferWidth=sg.getPreferredSize().width;
		int backWidth=back.getSize().width;		
		int dividerWidth=total.getDividerSize();
		//if both sg and nodes are set to preferredsize,
		//then drawingAreaWidth is got by substracting two MARGIN_WIDTH, 2 dividerWidth, sg and nodes preferred width
		int leftWidth=backWidth-2*dividerWidth-2*MARGIN_WIDTH;
		int drawingAreaWidth=leftWidth-nodesPreferWidth-sgPreferWidth;
		//if the drawingArea is smaller than either grammarPanels, then set them as proportional
		if(drawingAreaWidth<nodesPreferWidth||drawingAreaWidth<sgPreferWidth){
			//the drawingArea holds 1/2 of the total leftWidth
			//the left part is divided proportionally between the two grammarPanels
			int nodesW=leftWidth/2*nodesPreferWidth/(nodesPreferWidth+sgPreferWidth);
			int sgW=leftWidth/2*sgPreferWidth/(nodesPreferWidth+sgPreferWidth);
			total.setDividerLocation(nodesW);
			right.setDividerLocation(leftWidth-nodesW-sgW);
		}
		//if there is enough space, then set both divider to the location that both sg and nodes have preferred width
		else{
			total.setDividerLocation(nodesPreferWidth);
			right.setDividerLocation(leftWidth-nodesPreferWidth-sgPreferWidth);
		}

	}

	//this will be called in Top to get the JScrollPane on specific point relative to Plain
	public JScrollPane getPanel(Point p) {

		JSplitPane mediate = null;	
		try {
			mediate = (JSplitPane) back.getComponentAt(p);
		} catch (Exception e) {
			//the mouse is not in the range of upDown
			return null;
		};

		//mediate is first set to be upDown
		while(true){
			try {
				Point mediateLocation = mediate.getLocation();
				p = new Point(p.x - mediateLocation.x, p.y - mediateLocation.y);
				mediate = (JSplitPane) mediate.getComponentAt(p);
			} catch (Exception e) {
				//no imbedded JSplitPane can be got
				JScrollPane getReturn = null;
				try {
					//try to get a JScrollPane in the same location
					getReturn = (JScrollPane) mediate.getComponentAt(p);
					return getReturn;
				} catch (Exception e2) {
					//no JScrollPane can be got
					return null;
				}
			};
		}

	}

	//--------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws Exception {
		setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 24));

		JFrame frame = new JFrame("Plain");
		Grammar grammar = new Grammar();
		Plain layers = new Plain(grammar);
		frame.setPreferredSize(new Dimension(1200, 900));
		frame.add(layers, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		layers.setDividersLocation();

	}


	// will be called in Interface -clean button
	// also called in input function
	public void clean() {
		drawingArea.clean();
		dialogueP.dialogue.setText("");
		repaint();
	}


	/**
	 * Layout each of the components in this JLayeredPane so that they all fill
	 * the entire extents of the layered pane -- from (0,0) to (getWidth(),
	 * getHeight())
	 * @throws Exception 
	 */
	@Override
	public void doLayout() {
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



}
