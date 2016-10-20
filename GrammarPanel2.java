import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class GrammarPanel2 extends JScrollPane {
	Grammar2 grammar;
	JPanel back=new JPanel();

	JLabel getTheLabel(int x, int y) {
		JLabel label = null;
		try {
			label = (JLabel) back.getComponentAt(x, y);
		} catch (Exception xer) {

		}

		// JPanel panel= (JPanel) super.getComponentAt(new Point(x,y));

		return label;

	}

	JPanel getThePanel(int x, int y) {

		JLabel label = null;

		JPanel panel = (JPanel) super.getComponentAt(new Point(x, y));

		while (label == null) {
			Point location = panel.getLocation();
			x = x - location.x;
			y = y - location.y;

			if (panel.getComponentAt(x, y) instanceof JLabel) {
				return panel;

			} else if (panel.getComponentAt(x, y) == null) {
				break;
			} else {
				panel = (JPanel) panel.getComponentAt(x, y);
			}
		}

		return null;

	}



	public GrammarPanel2(Grammar2 x) {
		super.setViewportView(back);
		grammar = x;
		back.setLayout(new GridBagLayout());
		addingNodes();
	}

	public void updateGrammar(){
		back.removeAll();
		addingNodes();
	}
	private void addingNodes() {
		// TODO Auto-generated method stub
		int yy = 0;
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 0, 0, 0);
		Vector<CompSet> p=new Vector<CompSet>();
		for (String medium : grammar.medium) {
			if(!grammar.setBasic.contains(medium)){
				CompSet jj=new CompSet(medium);
				p.add(jj);
			}
		}
		Collections.sort(p);
		for(CompSet o:p){
			
		
		LLabel now = new LLabel(o.t,grammar);
		back.add(now, c);
		c.gridy++;}
		for (String base : grammar.setBasic) {
			LLabel now = new LLabel(base,grammar);
			back.add(now, c);
			c.gridy++;
		}
		back.setSize(back.getPreferredSize());
		super.setSize(back.getSize());
		super.setPreferredSize(back.getSize());
	}

	static public int isOptional(String input) {
		int i = 0;// if not optional return 0;

		if (input.length() > 2 && input.charAt(0) == '(' && input.charAt(input.length() - 1) == ')') {
			i = 1;// start with ( and end with ), so correct optional format
		} else if (input.length() > 0 && input.charAt(0) == '(') {
			i = 2;// only has left parenthesis, so not in the right format
		} else if (input.length() > 0 && input.charAt(input.length() - 1) == ')') {
			i = 2;// only has right parenthesis, so not in the right format
		}

		return i;
	}

	// helper functions

	public static int isMultiple(String input) {
		int i = 0;// if not optional return 0;

		if (input.length() > 2 && input.charAt(0) == '{' && input.charAt(input.length() - 1) == '}') {
			i = 1;// start with { and end with }, so correct optional format
		}

		return i;
	}

	public static String noParenthesis(String x) {
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < x.length() - 1; i++) {
			sb.append(x.charAt(i));

		}
		String sr = sb.toString();
		return sr;

	}

	// call only when is multiple
	public static String nobrackets(String x) {
		StringBuffer sb = new StringBuffer();

		for (int i = 1; i < x.length() - 1; i++) {
			sb.append(x.charAt(i));

		}
		String sr = sb.toString();
		return sr;

	}



	private static void createAndShowUI() {
		JFrame frame = new JFrame("DragLabelOnLayeredPane");

		JPanel back = new JPanel();
		back.setBackground(Color.white);
		GridBagLayout lay = new GridBagLayout();
		back.setLayout(lay);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;

		
		final GrammarPanel2 pp = new GrammarPanel2(new Grammar2());
		c1.anchor = GridBagConstraints.WEST;
		back.add(pp, c1);

		

		frame.getContentPane().add(back);
		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void main(String[] args) {
		setUIFont(new javax.swing.plaf.FontUIResource("Serif", Font.PLAIN, 40));

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowUI();

			}
		});
	}

}
