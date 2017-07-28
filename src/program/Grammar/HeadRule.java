package program.Grammar;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class HeadRule {
	String newline=System.getProperty("line.separator");
	    //the head of the rule
		public String head=null;
		//block containing subrules, subrules are connected by substitute
		Vector<Rule>rules=new Vector<Rule>();
		//storing the leagal children of the head
		Set<String> legalChildren=new HashSet<String>();

		
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
		
		
		//return null if obeying this headRule
		//return error message and proper hint for the legal grammar if not obeying this headRule
		public String obeyRule(String mother,Vector<String> children){
			//if the input has the same head as the headRule, provide error message and hints
			if(head.equals(mother)){
				//check whether it obeys any rule under this headrule
				for(Rule x:rules){
					if(x.obeyRule(mother,children)){
						return null;
					}
				}
				//if not obeying any rule under this headRule, providing error message
				String error="";
				for(String xy:children){
					if(!legalChildren.contains(xy)){
						error= "Warning: "+mother+" has illegal daughter ["+xy+"];"+newline;
						break;
					}
				}
				
				if(error.equals("")){
				error=mother+" cannot be formed by [";
				for(int i=0;i<children.size();i++){
					error+=children.elementAt(i);
					if(i!=children.size()-1){
						error+=" ";
					}
				}
				error+="];"+newline;}
				
				
				error+=newline+"Legal rules: "+newline;
				for(Rule x:rules){
					error+=x.string+newline;
				}
				return error;
				
			}else
			//if the input has a different head than this headRule, point that out
			{
				return "The input combination does not obey this headRule because it has a different head than this headRule";
			}
			
		}
}
