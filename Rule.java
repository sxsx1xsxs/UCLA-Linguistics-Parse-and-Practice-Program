import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;

public class Rule {
	//the head of the rule
	Block head=null;
	//blocklist containing the rest of the rule
	Vector<Block> blocks=new Vector<Block>();
	//storing the leagal children of the head
	Vector<Block> legalChildren=new Vector<Block>();
	//modified by constructor, if cannot form a rule, will record the error message
	String error=null;
	//modified by obeyRule, if not obeying Rule, will record the error message
	String grammarError=null;
	//record the rule
	String string;

	
	
	
	
	Grammar2 grammar;
	
	//input the list of string on the right side of the rule
	/*
	 * 1.cannot consume all the strings before use up the blocks
	 * 2.one block cannot be satisfied regarding to all the possible left string vectors
	 */
	public boolean obeyRule(Vector<NodeLabel> right){
		Vector<Vector<String>>process=new Vector<Vector<String>>();
		Vector<String>rights=new Vector<String>();
		for(NodeLabel nl:right){
			rights.add(nl.label.getText());
		}
		process.add(rights);
		
		//for those rules containing only the head
		if(blocks.size()==0){
			if(right.size()==1&&right.elementAt(0).type==1){
				return true;
			}else{
				grammarError="Terminal node "+head.node+" can only have one regular word as its child";
				return false;
			}
		}
		
		//for those that no possible list is left before all blocks are consumed
		for(int i=0;i<blocks.size();i++){
			Block block=blocks.elementAt(i);
			process=block.obeyBlock(process);
			if(process==null){
				grammarError="Block "+block.node+" cannot be satisfied";
				return false;
			}else{
				System.out.println("Block "+block.node+" satisfied");
			}
		}
		
		//the index storing the list that has the least number of elements
		int indexofmin=0;
		//the int storing the min size of the list
		int min=0;
		for(int i=0;i<process.size();i++){
			Vector<String>x=process.elementAt(i);
			if (x.size()==0){
				return true;
			}
			if(x.size()<min||min==0){
				min=x.size();
				System.out.println("x size is"+x.size());
				indexofmin=i;
			}
		}
		Vector<String> e=process.elementAt(indexofmin);
		String ee="";
		for(String ex:e){
			System.out.println(ex);
			ee+=ex+",";
		}
		grammarError=ee+" cannot be consumed by the rule";
		return false;
	}
	
	
	
	
	//used when constructing a rule containing only head in Grammar2
	public Rule(String headInput){
		head=new Block(headInput);
		string=headInput;
	}
	
	
	/*
	 * create a rule
	 * if there is an error, modify [error]
	 */
	public Rule(Vector<Block> allMaterial){
	
		
		if(allMaterial.size()==0){
			return;
		}
		//if the first element is not a word but a symbol
		else if(allMaterial.elementAt(0) instanceof Unit){
			error="The beginning element "+allMaterial.elementAt(0).node+" is not a legal head of a rule";
			return;
		}
		//if there is only a word in that line
		else if(allMaterial.size()==1){
			head=allMaterial.elementAt(0);
			string=head.node;
			return;
		}
		//if the second element is not -->
		else if(!allMaterial.elementAt(1).node.equals("-->")){
			error="The symbol --> for a rule is missing";
			return;
		}
		//if the size of the list is just 2
		else if(allMaterial.size()==2){
			error="It cannot be empty on the right side of symbol ->";
			return;
		}
		/*
		 * the major regular process, already make sure
		 * 1. there is a head
		 * 2. the second symbol is -->
		 * 3. there is something on the right side of -->
		 */		
		else{
			head=allMaterial.elementAt(0);
			string=head.node+"-->";
			Stack<Block> process=new Stack<Block>();
			for(int i=2;i<allMaterial.size();i++){
				Block now=allMaterial.elementAt(i);
				//when encounter unit
				if(now instanceof Unit){
					Unit nowUnit=(Unit)now;
					//when encounter right Unit
					if(nowUnit.position=="right"){
						//only star does not require a counterpart, so discuss first
						if(nowUnit.property.equals("repetitive")){
							if(process.size()<1){
								error="There is a * that cannot be applied";
								return;
							}else{
								Block test=process.pop();
								if(!test.type.equals("basic")&&!(test.type.equals("complex"))){
								error="There is a * that cannot be applied";
								return;
								}else{
									if(test.property.equals("optional")){
										test.property="repetitive";
										test.node+="*";
										process.add(test);
									}else{
									Block complex=new Block();
									complex.node=test.node+"*";
									complex.type="complex";
									complex.property="repetitive";
									complex.blockList=new Vector<Block>();
									complex.blockList.add(test);
									process.add(complex);}
								}
							}
						}
						//the situations that have left counterparts
						else{
							Block complex=new Block();
							complex.type="complex";
							complex.node=nowUnit.node;
							complex.property=nowUnit.property;
							complex.blockList=new Vector<Block>();
							//on the process stack, there should at least be a left unit and a block left
							//if not, error
							if(process.size()<2){
								error="There s a "+nowUnit.node+" that cannot be applied";
								return;
							}
							//at least one Two elements left
							else{
							Block previous=process.peek();
							//when a Unit is not encountered
							int ii=0;
							while(!(previous instanceof Unit)){
								Block nn=process.pop();
								complex.blockList.insertElementAt(nn, 0);
								
								if(ii==0){
								complex.node=nn.node+complex.node;}
								else{
									complex.node=nn.node+" "+complex.node;
								}
								ii=1;
								if(process.size()<1){
									error="There s a "+nowUnit.node+" that cannot be applied";
									return;
								}else{
									previous=process.peek();
								}	
							}	
							//when out the while loop, there must be at least one Unit element left on process
							//stack, otherwise returned earlier
							Unit left=(Unit)process.pop();
							complex.node=left.node+complex.node;
							if(left.property!=nowUnit.property||left.position!="left"){
								error="There s a "+nowUnit.node+" that cannot be applied";
								return;
							}else{
								process.add(complex);
							}
							}
							
						}
					}
					//when encountering the left Unit
					else{
						process.add(now);
					}
				}
				//when encountering regular words
				else{
					//if it's basic block, add to legalChildren
					if(now.type.equals("basic")){
						legalChildren.add(now);
						process.add(now);
					}
					
				}
			}
			for(int i=0;i<process.size();i++){
				Block x=process.elementAt(i);
				if(x instanceof Unit){
					error="There is a "+x.node+" that cannot be applied";
					blocks=new Vector<Block>();
					legalChildren=new Vector<Block>();
					return;
				}
				blocks.add(x);
				string=string+" "+x.node;
			}
			
		}
	}


	
	static public void main(String args[]){
		Block cp=new Block("CP");
		Unit arrow=new Unit("-->");
		Block cp1=new Block("CP1");
		Unit left=new Unit("(");
		Block conj=new Block("Conj");
		Block cp2=new Block("CP");
		Unit right=new Unit(")");
		Unit star=new Unit("*");
		
		Vector<Block> b=new Vector<Block>();
		b.add(cp);
		b.add(arrow);
		b.add(cp1);
		b.add(left);
		b.add(conj);
		b.add(cp2);
		b.add(right);
		//b.add(star);
		Rule test=new Rule(b);
		Vector<String> units=new Vector<String>();
		units.add("CP1");
		units.add("Conj");
		units.add("CP");
		units.add("Conj");
		units.add("CP");
		//System.out.println(test.obeyRule(units));	
		//System.out.println(test.grammarError);
	}
	
	
	
	
	
	
	
	
}
