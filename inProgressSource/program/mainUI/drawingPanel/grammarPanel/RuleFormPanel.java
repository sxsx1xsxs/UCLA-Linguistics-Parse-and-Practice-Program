package program.mainUI.drawingPanel.grammarPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import program.grammar.Block;
import program.grammar.Grammar;
import program.grammar.Rule;



public class RuleFormPanel extends GrammarPanel{
	private Grammar grammar;

	//--------------------------------------------------------------------------------------------------
	
	//constructor
	public RuleFormPanel(Grammar g){
		grammar=g;
		this.setLayout(new GridBagLayout());
		this.addingNodes();
	}

	//--------------------------------------------------------------------------------------------------
	
	//@constructor helper
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
			head.setHorizontalAlignment(SwingConstants.CENTER);
			LLabel symbol=new LLabel("-->",grammar);
			this.add(head,c);
			c.gridx=1;
			this.add(symbol, c);
			c.gridx=2;
			c.fill=GridBagConstraints.NONE;
			c.anchor=GridBagConstraints.WEST;
			this.add(getRightPanel(rule), c);
			i++;
			}
		}
	}

	//@getRightPanel helper
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
	//@addindNodes() helper
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
	
	//--------------------------------------------------------------------------------------------------
	@Override
	public void updateGrammar(){
		this.removeAll();
		addingNodes();
		
	}

	//will return null if the label is a symbol label or nothing
	//return the NodeLabel with size set to PreferredSize if in range
	@Override
	public	JLabel getTheLabel(int x, int y){	
				LLabel label=null;
				JPanel panel= this;		
				while(label==null)
				{
					
					Point location=panel.getLocation();
					x=x-location.x;
					y=y-location.y;
					
					if(panel.getComponentAt(x, y) instanceof LLabel)
					{   
						
						LLabel now=(LLabel)(panel.getComponentAt(x, y));
					    if(now.type.equals("symbol"))
					    {
					    	//position (x,y) is non-movable label, return null
					    	return null;
					    }else
					    {
					    	//position (x,y) is a movable label, return the label
					    	JLabel rLabel=new JLabel(now.getText());
					    	rLabel.setSize(rLabel.getPreferredSize());
					    	return rLabel;
					    }
						
					}
					
					else{
						JPanel now_panel=(JPanel) panel.getComponentAt(x,y);
						if(now_panel==panel){
							//position (x,y) is nothing, return null
							return null;
						}else{
							panel=(JPanel) panel.getComponentAt(x, y);
						}
					}

				}
				
				return null;
				
			}
	
	
	public static void main(String args[]){
		Grammar g=new Grammar();
		RuleFormPanel j=new RuleFormPanel(g);
		
		JFrame frame=new JFrame("Grammar_Rule_Panel");
		frame.add(j);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	        
	}
}
