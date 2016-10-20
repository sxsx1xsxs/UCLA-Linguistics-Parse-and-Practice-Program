import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Grammar2 implements PropertyChangeListener{
	//Constants	
	String newline=System.getProperty("line.separator");
	
	//the string containing the original txt or the modified format of the grammar
	String file = "";
	
	/*when importing grammar, 
	 * the nodes that only occurs on the right side will be treated as terminal nodes
	 * the nodes that once occur on the left side and does not stand alone in the rule will be 
	treated as non-terminal nodes
	 *if want to treat a node as terminal node when it once occurs on the left
	please in one line, make it stand alone
		for example, V-->V Conj V will not make V a non-terminal node if there is another line
		of rule containing "V" only*/
		
	//the set containing all nodes
	Set<String> set = new HashSet<String>();
	//the set of terminal nodes that can be connected to terminal words
	Set<String> setBasic = new HashSet<String>();
	//the set of non-terminal nodes that can only be connected to base nodes but not terminal words
	Set<String> medium = new HashSet<String>();
	//the set of grammar rules
	Vector<Rule> rules = new Vector<Rule>();
	//record the index of lines that are problematic, the detailed error message is inside line class
	Vector<Integer> wrongLine=new Vector<Integer>();
	Vector<Line> lines=new Vector<Line>();
	//record the rules with rules with the same head as a set, convenient for checking rules for every node
	Vector<HeadRule>headRules=new Vector<HeadRule>();
	
	

	public Grammar2(){
		String input;
		
		Preference pp=new Preference();
		input=pp.getString2("grammar");
		System.out.println(input);
//		input = "S-->NP (Aux) VP"+newline;
//		input += "NP-->({Art NP}) (AP)* N (PP)* (CP)"+newline;
//		input += "NP-->Pro"+newline;
//		input += "AP-->(Adv) A"+newline;
//		input += "VP-->V (NP) (NP) (PP)* (CP)"+newline;
//		input += "VP-->V AP (PP)*"+newline;
//		input += "PP-->P NP"+newline;
//		input += "CP-->(Comp) S"+newline;
//		input += "NP-->NP (Conj NP)*"+newline;
//		input += "VP-->VP (Conj VP)*"+newline;
//		input += "PP-->PP (Conj PP)*"+newline;
//		input += "S-->S (Conj S)*"+newline;
//		input += "CP-->CP (Conj CP)*"+newline;
//		input += "AP-->AP (Conj AP)*"+newline;
//		input += "V-->V (Conj V)*"+newline;
//		input += "V"+newline;		
		constructor(input);
		
	}
	
	
	public Grammar2(String input){
		constructor(input);
	}
	//create rules and adding elements to set, setBasic, medium for the right lines
	//modify [wrongLine] if there is a wrong format in some line
	
	
	private void constructor (String input){
		//the index of lines, note that it starts from 1
		file=input;
		int i=1;
		
		for(String line:input.split("\r\n|\r|\n")){
			Line nowline=new Line(line,i);
			lines.add(nowline);
			if(nowline.error==null){
				addingRule(nowline);
			}else{
				wrongLine.add(i);
				System.out.println("this is i"+i);
				System.out.println(wrongLine.size()+"wrongline added");
			}
			i++;
		}
		//now update setBasic, for those nodes not in medium but in set, they should be in setBasic
		//construct a rule containing only head for those nodes
		for(String x: set){
			if(!medium.contains(x)){
				setBasic.add(x);
				Rule newRule=new Rule(x);
				rules.add(newRule);
			}
		}
		
		//now construct headrule set
		int whether=0;
		for(Rule rule:rules){
			whether=0;
			HeadRule headrule=new HeadRule(rule);
			for(HeadRule x:headRules){
				if(headrule.head.equals(x.head)){
					x.addRule(rule);
					whether=1;
				}
			}
			
			if(whether==0){				
					headRules.add(headrule);
				
			}
		}
	}
	
	/*
	 * create a new rule and add it to [rules], add all nodes to set, to medium, setBasic gets some of its members
	 * needs to be updated after all the rules are added
	 */
	public void addingRule(Line line){
		Rule now=new Rule(line.units);
		if(now.error==null){
			rules.add(now);
			set.add(now.head.node);
			for(Block x:now.legalChildren){
				set.add(x.node);
			}
			//if there is only one element in that line
			if(now.blocks.size()==0){
				setBasic.add(now.head.node);
			}
			//if it's a regular rule
			else{
				medium.add(now.head.node);
			}
		}else{
			line.error=now.error;
			wrongLine.add(line.index);
		}
		
	}
	
	
	static class Line{
		//the index of the line
		int index;
		/*
		 * the units of the line, for example line AP-->(Adv) A would be [AP] [-->] [(] [Adv] [)] [A]
		 * would be null if error is not null
		 */
		Vector<Block> units;
		//the string storing the format error of that line
		String error;
		
		
		//x should be a string without line seperator
		public Line(String x, int indexInput)
		{
			units=getVector(x);
			index=indexInput;
			
		}
		
		/*
		 * return String[2]
		 * String[0]=the unit got, if it's words, it's regular Block type
		 * if it's symbol, it's unit type
		 * String[1]=the rest of the string
		 */
		private Object[] getUnit(String input){
			LegalText legal=new LegalText();
			Object[] r=new Object[2];
			
			for(String symbol:legal.set){
				
				Pattern p = Pattern.compile(legal.getRE(symbol));
				Matcher m = p.matcher(input);
				
				while(m.find()){
					//if successfully found
					//if it's symbol, build a Unit
					if(Arrays.asList(legal.setOfSymbols).contains(symbol)){
						r[0]=new Unit(symbol);
					}
					//if it's words, build a basic Block
					else{
						Block now=new Block(m.group(2));
						r[0]=now;
						
						}
					Pattern blank=Pattern.compile("^\\s*$");
					Matcher blankm=blank.matcher(m.group(3));
					if(blankm.find()){
						r[1]=null;
					}
					else{r[1]=m.group(3);}
					return r;
				}
			}
			
			
			//if none of the symbols inside legaltext can be extracted from the beginning of
			//input, then input is unrecognizable
			error="The beginning of the following string is unrecognizable : "+input;
			return null;
			
		}
		
		private Vector<Block> getVector(String input){
			Vector<Block> r=new Vector<Block>();
			Block unit=null;
			String rest=input;
			do{
				Object[] result=getUnit(rest);
				if(result!=null){
				unit=(Block) result[0];
				rest=(String) result[1];
				r.add(unit);}
				else{
					System.out.println(error);
					return null;
				}
				
			}while(rest!=null);
			
			return r;
		}
	
	
	
	}
	
	
	
//	public static void main(String args[]){
//
//		Grammar2 g=new Grammar2();
////		for(String x:g.set){
////			System.out.println("set "+x);
////		}
////		for(String x:g.setBasic){
////			System.out.println("setBasic"+x);
////		}
////		for(String x:g.medium){
////			System.out.println("medium "+x);
////		}
////		for(Rule x:g.rules){
////			System.out.println(x.head.node+"head");
////		}
////		
//		
//		for(Rule x:g.rules){
//			System.out.println(x.string);
//		}
//		Rule
//		rule=g.rules.elementAt(3);
////		System.out.println(rule.blocks.elementAt(0).blockList.elementAt(0).property);
//		
//		Vector<NodeLabel> right=new Vector<NodeLabel>();
////		NodeLabel np=new NodeLabel();
////		np.label.setText(text);
////		right.add("A");
////		right.add("AP");
////		right.add("AP");
////		right.add("AP");
////		right.add("AP");
////		right.add("AP");
////		right.add("N");
////		right.add("PP");
////		right.add("PP");
////		right.add("PP");
////		right.add("CP");
////		right.add("AP");
//		//System.out.println(rule.obeyRule(right));
//		Vector<Vector<String>> rig=new Vector<Vector<String>>();
//		//rig.add(right);
////		for(HeadRule x:g.headRules){
////			if(x.obeyRule("AP", right)){
////				System.out.println("hey");
////			}
//			//System.out.println(x.head);
//		//}
//		
//}

	//return null if obeying rules
	//return the error message if there is something wrong
	
	public String obeyRules(NodeLabel head, Vector<NodeLabel> jiji) {
		String headt=head.label.getText();
		//if it's words nodes
		if(head.type==1){
			if(jiji.size()>0){
				return "Warning: word nodes cannot have children;";
			}else{
				return null;
			}
		}
		//if it's not defined nodes
		else if(!set.contains(headt)){
			return "Warning: "+headt+" is not a defined node;";
		}
		//if it has no children
		else if(jiji.size()==0){
			return "Caution: "+ headt+" is incomplete;";
		}
		//if it cannot be terminal node
		if(!setBasic.contains(headt)){
			//if it's connected to any word nodes
			for(NodeLabel cc:jiji){
				if(cc.type==1){
					return "Warning: "+headt+" is not a terminal node, it cannot have a word node as its child;";
				}
			}
		}
		//if it cannot be intermediate node
		if(!medium.contains(headt)){
			if(jiji.size()>1||jiji.elementAt(0).type!=1){
				return "Warning: "+headt+" is a terminal node, it can only have one word node as its child;";
			}else{
					return null;			
			}
		}
		
		//now only the situation of medium nodes
		//either has illegal childen or cannot be formed by
		HeadRule now=null;
		for(HeadRule hr:headRules){
			if(hr.head.equals(headt)){
				now=hr;
			}
		}
		
			if(now.obeyRule(headt, jiji)){
				return null;
			}
			else{		
				return now.error;}
			
	}


	public void changeGrammar(String grammarInput) {
		set = new HashSet<String>();
		setBasic = new HashSet<String>();
		medium = new HashSet<String>();
		rules = new Vector<Rule>();
		wrongLine=new Vector<Integer>();
		lines=new Vector<Line>();
		constructor(grammarInput);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	


}