package program.mainUI.drawingPanel.grammarPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import program.grammar.Grammar;


public class SingleColumnPanel extends GrammarPanel {
	private Grammar grammar;
	
	//--------------------------------------------------------------------------------------------------
	
	//constructor
	public SingleColumnPanel(Grammar x) {
		grammar = x;
		this.setLayout(new GridBagLayout());
		this.addingNodes();
	}

	//--------------------------------------------------------------------------------------------------
	
	//@constructor helper
	private void addingNodes() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 0, 0, 0);
		Vector<String> set=new Vector<String>();
		for(String x:grammar.set){
			set.add(x);
		}
		Collections.sort(set);
		for(String x:set){
			LLabel now = new LLabel(x,grammar);
			this.add(now, c);
			c.gridy++;
		}
	}

	//--------------------------------------------------------------------------------------------------
	
	@Override
	public JLabel getTheLabel(int x, int y) {
		LLabel label = null;
		try {
			label = (LLabel) this.getComponentAt(x, y);
		} catch (Exception e) {}
		
		JLabel rLabel=null;
		if(label!=null){
			rLabel=new JLabel(label.getText());
			rLabel.setSize(rLabel.getPreferredSize());
		}
		return rLabel;
	}
	
	@Override
	public void updateGrammar(){
		this.removeAll();
		addingNodes();
	}
	
	//--------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		JFrame frame = new JFrame("Single Column Grammar Panel");
		Grammar grammar=new Grammar();
		JPanel back = new SingleColumnPanel(grammar);

		frame.add(back);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
	
	}

}
