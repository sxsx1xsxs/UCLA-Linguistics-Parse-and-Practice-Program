import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class HeadRule {
	String newline=System.getProperty("line.separator");
	    //the head of the rule
		String head=null;
		//block containing subrules, subrules are connected by substitute
		Vector<Rule>rules=new Vector<Rule>();
		//storing the leagal children of the head
		Set<String> legalChildren=new HashSet<String>();
		//modified by obeyRule, if not obeying Rule, will record the error message
		String error=null;
		
		public HeadRule(Rule x){
			head=x.head.node;
			rules.add(x);
			for(Block yx:x.legalChildren){
				legalChildren.add(yx.node);
			}
		}
		
		
		//call after two rules have the same head
		public void addRule(Rule x){
			rules.add(x);
			for(Block yx:x.legalChildren){
				legalChildren.add(yx.node);
			}
		}
		
		
		//list is the right list
		public boolean obeyRule(String headInput,Vector<NodeLabel> list){
			error="";
			if(head.equals(headInput)){
				for(Rule x:rules){
					if(x.obeyRule(list)){
						return true;
					}
				}
				
				for(NodeLabel xy:list){
					if(!legalChildren.contains(xy.label.getText())){
						error= "Warning: "+headInput+" has illegal daughter ["+xy.label.getText()+"];"+newline;
						break;
					}
				}
				
				if(error.equals("")){
				error=headInput+" cannot be formed by [";
				for(int i=0;i<list.size();i++){
					NodeLabel nl=list.elementAt(i);
					error+=nl.label.getText();
					if(i!=list.size()-1){
						error+=" ";
					}
				}
				error+="];"+newline;}
				
				
				error+=newline+"Legal rules: "+newline;
				for(Rule x:rules){
					error+=x.string+"; "+newline;
				}
				
				
			}
			return false;
		}
}
