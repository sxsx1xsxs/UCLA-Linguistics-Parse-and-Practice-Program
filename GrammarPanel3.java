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


public class GrammarPanel3 extends JScrollPane{
	Grammar2 grammar;
	//used in plain because it's scrollPane
	JPanel back=new JPanel();
	//used in plain for the scrollPane's default min size
	int labelHeight=0;
	
	public GrammarPanel3(Grammar2 g){
		super.setViewportView(back);
		grammar=g;
		back.setLayout(new GridBagLayout());
		addingNodes();
		

	}
	
	private void addingNodes() {
		GridBagConstraints c=new GridBagConstraints();
		int i=0;
		for(Rule rule:grammar.rules){
			//only display the rules that has the right side
			//for example ; rule: V is not displayed
			if(rule.blocks.size()>0){
			c.gridx=0;
			c.gridy=i;
			c.insets=new Insets(0,5,0,0);
			c.fill=GridBagConstraints.HORIZONTAL;
			LLabel head=new LLabel(rule.head.node,grammar);
			if(labelHeight==0){
				labelHeight=head.getPreferredSize().height;
			}
			head.setHorizontalAlignment(SwingConstants.CENTER);
			LLabel symbol=new LLabel("-->",grammar);
			back.add(head,c);
			c.gridx=1;
			back.add(symbol, c);
			c.gridx=2;
			c.fill=GridBagConstraints.NONE;
			c.anchor=GridBagConstraints.WEST;
			back.add(getRightPanel(rule), c);
			i++;
			}
		}
	}

	public void updateGrammar(){
		back.removeAll();
		addingNodes();
		
	}

	//tool for building up panel
	private JPanel getBlockPanel(Block block){
		JPanel back=new JPanel();
		back.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		
		if(block.type.equals("basic")){
			LLabel now=new LLabel(block.node,grammar);
			back.add(now,c);
		}
		//if it's substitute block
		else if(block.property.equals("substitute")){
			int height=block.blockList.size();
			c.gridx=0;
			c.gridy=0;
			c.gridheight=height;
			c.fill=GridBagConstraints.BOTH;
			LLabel left=new LLabel("{",grammar);
			back.add(left, c);
			LLabel right=new LLabel("}",grammar);
			c.gridx=2;
			back.add(right, c);
			
			for(int i=0;i<block.blockList.size();i++){
				c.gridheight=1;
				c.gridx=1;
				c.gridy=i;
				back.add(getBlockPanel(block.blockList.elementAt(i)), c);
				
			}
		}
		//except for basic block and substitute block
		else{
			
			if(block.property.equals("optional")){
				c.gridx=0;
				c.gridy=0;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				LLabel left=new LLabel("(",grammar);
				back.add(left, c);
				LLabel right=new LLabel(")",grammar);
				c.gridx=block.blockList.size()+1;
				back.add(right, c);
				
			}else if(block.property.equals("together")){
				c.gridx=0;
				c.gridy=0;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				LLabel left=new LLabel("[",grammar);
				back.add(left, c);
				LLabel right=new LLabel("]",grammar);
				c.gridx=block.blockList.size()+1;
				back.add(right, c);
				
			}else if(block.property.equals("repetitive")){
				if(block.blockList.size()==1){
				c.gridx=0;
				c.gridy=0;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				LLabel left=new LLabel("",grammar);
				back.add(left, c);
				LLabel right=new LLabel("*",grammar);
				c.gridx=block.blockList.size()+1;
				back.add(right, c);
				}else{
					c.gridx=0;
					c.gridy=0;
					c.gridheight=1;
					c.fill=GridBagConstraints.BOTH;
					LLabel left=new LLabel("(",grammar);
					back.add(left, c);
					LLabel right=new LLabel(")*",grammar);
					c.gridx=block.blockList.size()+1;
					back.add(right, c);
				}
			}
			for(int i=0;i<block.blockList.size();i++){
				c.gridx=i+1;
				c.gridy=0;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				if(i>0){
					c.insets=new Insets(0,5,0,0);
				}
				back.add(getBlockPanel(block.blockList.elementAt(i)), c);				
			}
			
		}
		
		return back;
	}
	
	
	//returns the JPanel on the right side
	//used in GrammarPanel3 constructor
	private JPanel getRightPanel(Rule rule){
		JPanel back=new JPanel();
		GridBagConstraints c=new GridBagConstraints();
		
		for(int i=0;i<rule.blocks.size();i++){
			c.gridx=i;
			c.gridy=0;
			c.fill=GridBagConstraints.BOTH;
			if(i>0){
				c.insets=new Insets(0,5,0,0);
			}
			back.add(getBlockPanel(rule.blocks.elementAt(i)),c);
			
		}
		return back;
	}
	
		//will use in the Plain in mouseAdapter
		//will return null if the label is a symbol label
		public	LLabel getTheLabel(int x, int y)
			{	
				LLabel label=null;
				JPanel panel= back;		
				while(label==null)
				{
					Point location=panel.getLocation();
					x=x-location.x;
					y=y-location.y;
					
					if(panel.getComponentAt(x, y) instanceof LLabel)
					{   LLabel now=(LLabel)(panel.getComponentAt(x, y));
					    if(now.type.equals("symbol"))
					    {
					    	return null;
					    }else
					    {
					    	return now;
					    }
						
					}
					else if(panel.getComponentAt(x, y) ==null)
					{
						break;
					}
					else
					{
						panel=(JPanel) panel.getComponentAt(x, y);
					}
				}
				
				return label;
				
			}
	
	
	public static void main(String args[]){
		Grammar2 g=new Grammar2();
		JScrollPane j=new GrammarPanel3(g);
		
		JFrame frame=new JFrame("pAnel");
		frame.add(j);
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	}
}
