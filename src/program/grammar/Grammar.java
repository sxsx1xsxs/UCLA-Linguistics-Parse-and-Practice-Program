package program.grammar;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import program.Preference;




public class Grammar{
	private Preference preference=new Preference();
	
	//the string containing the original txt or the modified format of the grammar
	String grammar_text = "";
	
	/*when importing grammar, 
	 * the nodes that only occurs on the right side will be treated as terminal nodes
	 * the nodes that once occur on the left side and does not stand alone in the rule will be 
	treated as non-terminal nodes
	 *if want to treat a node as terminal node when it once occurs on the left
	please in one line, make it stand alone
		for example, V-->V Conj V will not make V a non-terminal node if there is another line
		of rule containing "V" only*/
		
	//the set containing all nodes
	 public Set<String> set = new HashSet<String>();
	//the set of terminal nodes that can be connected to terminal words
	 public Set<String> setBasic = new HashSet<String>();
	//the set of non-terminal nodes that can only be connected to base nodes but not terminal words
	 public Set<String> medium = new HashSet<String>();
	//the set of grammar rules
	 public Vector<Rule> rules = new Vector<Rule>();
	//record the index of lines that are problematic, the detailed error message is inside line class
	 Vector<Integer> wrongLine=new Vector<Integer>();
	 Vector<Line> lines=new Vector<Line>();
	//record the rules with rules with the same head as a set, convenient for checking rules for every node
	 public Vector<HeadRule>headRules=new Vector<HeadRule>();
	
	

	public Grammar() {
	
		
		//get the text to construct the grammar from Preference and if there is no record
		//inside Preference, it will read the file grammar.txt		
			grammar_text=preference.getString("grammar_text");
			System.out.println(grammar_text);
			constructor(grammar_text);
		
	}
	
	public Grammar(String input){
		constructor(input);
	}
	
	
	//change the grammar to anther grammar, because a lot of places use grammar
	//this method makes sure by reference that grammar is changed
	public void changeToGrammar(Grammar newg) {
		set = newg.set;
		//the set of terminal nodes that can be connected to terminal words
	    setBasic = newg.setBasic;
		//the set of non-terminal nodes that can only be connected to base nodes but not terminal words
		  medium = newg.medium;
		//the set of grammar rules
		 rules = newg.rules;
		//record the index of lines that are problematic, the detailed error message is inside line class
		 wrongLine=newg.wrongLine;
		lines=newg.lines;
		//record the rules with rules with the same head as a set, convenient for checking rules for every node
		 headRules=newg.headRules;
		
	}
	

	//whether the grammar_text is in right form so that Grammar object is successfully created
	public boolean successfully_created(){
		if(wrongLine.size()==0){
			return true;
		}
		else return false;		
	}
	
	//return detailed error_message of some not successfully-created grammar
	//return "" for successfully-created grammar
	public String error_message(){
		String error_message = "";
		for (int i : wrongLine) {
			error_message+="Line "+i+":"+lines.elementAt(i-1).error+Preference.newline;
		}
		return error_message;
		
	}
		
	//helper function for constructor create rules and adding elements to set, setBasic, medium for the right lines
	//modify [wrongLine] if there is a wrong format in some line
	private void constructor (String input){
		//the index of lines, note that it starts from 1
		grammar_text=input;
		int i=1;
		
		for(String line:input.split("\n")){
			Line nowline=new Line(line.trim(),i);
			lines.add(nowline);
			if(nowline.error==null){
				addingRule(nowline);
			}else{
				wrongLine.add(i);
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
	
	/* create a new rule and add it to [rules], add all nodes to set, to medium, setBasic gets some of its members
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
		/* the units of the line, for example line AP-->(Adv) A would be [AP] [-->] [(] [Adv] [)] [A]
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
		
		/*return String[2]
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
					return null;
				}
				
			}while(rest!=null);
			
			return r;
		}
	
	
	
	}
	
	//return null if obeying rules
	//return the error message if there is something wrong
	public String obeyRules(String mother, Vector<String> children){
		//all nodes without children can be considered as words, so no error
		if(children.size()==0){
			return null;
		}else
		//nodes with children should check the headRules
		{ for(HeadRule head_Rule: headRules){
			//if some headRule start with the mother string, should return the
			//error message or return null, which means it obeys some rule
				if(head_Rule.head.equals(mother)){
					return head_Rule.obeyRule(mother, children);
				}
			}
			
			//if no headRule start with the mother string
			return ("Node "+mother+" is not allowed to have children");
			
		}
		
	}



	public static void main(String args[]){

		Grammar g=new Grammar();
		for(String x:g.set){
			System.out.println("set "+x);
		}
		for(String x:g.setBasic){
			System.out.println("setBasic"+x);
		}
		for(String x:g.medium){
			System.out.println("medium "+x);
		}
		for(Rule x:g.rules){
			System.out.println(x.head.node+"head");
		}
		
		
		for(Rule x:g.rules){
			System.out.println(x.string);
		}
				
}

		


}
