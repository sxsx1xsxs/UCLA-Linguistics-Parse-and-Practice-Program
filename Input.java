import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;




public class Input {
	Grammar2 g=new Grammar2();
//	Set<String> set=new HashSet<String>();
//	Set<String> setBasic=new HashSet<String>();
//	Set<String> setRules= new HashSet<String>();
	//set.add("n");
	
	String sent="";
	int error=0;
	String errorMessage="";
	Vector<StringWithError> list=new Vector<StringWithError> ();
	//1.error message: the clause should begin with a "["
	//2.error message: the clause should end with a "]"
	//3.error message: after[
	//4.error message: "]" must be followed by "[" or "]"
	//5.error message: the number of "[" does not match the number of "]"
	//6.error message: xxx is not a defined type
	//7.error message: xxx is not the the base type
	
	
	//8.error message: no such rule as xxx
	//9.error message: there is more than one entity. Please enter them separately or combine them into a larger entity.
	//error message 5, 8, 9, 10
	
	Input(String input){
		StringBuffer n = new StringBuffer("");
		for(int i=0;i<input.length();i++){
			if(input.charAt(i)=='['){
				n.append("[ ");
			}
			else if(input.charAt(i)==' ');
			else if(input.charAt(i)==']'){
				n.append("] ");
			}
			else{
				while(i<input.length()-1&&input.charAt(i)!=' '&&input.charAt(i)!='['&&input.charAt(i)!=']'){
					n.append(input.charAt(i));
					i++;
				}
				if(i==input.length()-1&&input.charAt(i)!=']'&&input.charAt(i)!='['&&input.charAt(i)!=' ')
				{
					n.append(input.charAt(i));
					n.append(' ');
				}
				else{
				n.append(' ');
				i--;}
			}
			
		}
		
		sent= n.toString();
		int index=0;
		
		for(String x: sent.split(" ")){
			StringWithError y=new StringWithError();
			y.index=index;
			y.s=x;
			list.add(y);
			index++;
		}
			
	}
	
	Input(String input, Grammar2 grammar){
		g=grammar;
		StringBuffer n = new StringBuffer("");
		for(int i=0;i<input.length();i++){
			if(input.charAt(i)=='['){
				n.append("[ ");
			}
			else if(input.charAt(i)==' ');
			else if(input.charAt(i)==']'){
				n.append("] ");
			}
			else{
				while(i<input.length()-1&&input.charAt(i)!=' '&&input.charAt(i)!='['&&input.charAt(i)!=']'){
					n.append(input.charAt(i));
					i++;
				}
				if(i==input.length()-1&&input.charAt(i)!=']'&&input.charAt(i)!='['&&input.charAt(i)!=' ')
				{
					n.append(input.charAt(i));
					n.append(' ');
				}
				else{
				n.append(' ');
				i--;}
			}
			
		}
		
		sent= n.toString();
		int index=0;
		
		for(String x: sent.split(" ")){
			StringWithError y=new StringWithError();
			y.index=index;
			y.s=x;
			list.add(y);
			index++;
		}
			
	}
	
	
	int countLeftBracket(){
		int i=0;
		for(StringWithError x : list){
			if(x.s.equals("[")){
				i++;
			}
		}
		return i;
	}
	
	int countRightBracket(){
		int i=0;
		for(StringWithError x : list){
			if(x.s.equals("]")){
				i++;
			}
		}
		return i;
	}
	Boolean ableToBuildTree(){
		if (!list.elementAt(0).s.equals("[")){
			error=1;
			errorMessage="The clause should begin with a \"[\"";
		}
		else if (!list.elementAt(list.size()-1).s.equals("]")){
			error=2;
			errorMessage="The clause should end with a \"]\"";
		}
		else{
			int left=0;
			int right=0;
			for(int i=0;i<list.size();i++){
				if (error==0)
				{
				if(list.elementAt(i).s.equals("["))
					{
					left++;

					if(i>list.size()-4){
						error=3;
						for(int j=i;j<list.size();j++){
							list.elementAt(j).error=3;
						}
					
					}
					else{
						if(list.elementAt(i+1).s.equals("[")||list.elementAt(i+1).s.equals("]")){
							error=3;
							list.elementAt(i+1).error=3;
							list.elementAt(i).error=3;
							
						}
						else{
							if(list.elementAt(i+2).s.equals("]")){
								error=3;
								list.elementAt(i+1).error=3;
								list.elementAt(i).error=3;
								list.elementAt(i+2).error=3;
								
							}
							
							else{
								if(list.elementAt(i+2).s.equals("["));
								else if(!list.elementAt(i+3).s.equals("]")){
									error=3;
									list.elementAt(i+1).error=3;
									list.elementAt(i).error=3;
									list.elementAt(i+2).error=3;
									list.elementAt(i+3).error=3;
								}
							}
						}
					}
					if(error==3)
						errorMessage="\"[\" can only be "
								+ "followed by \"a base type + a word in that type + ]\" like \"[n apple]\" or"
								+ " \"a intermediate level type + [\" like \"[np [\"";
					}
				if(list.elementAt(i).s.equals("]")){
					right++;
					if(i<list.size()-1&&!list.elementAt(i+1).s.equals("]")&&!list.elementAt(i+1).s.equals("[")){
						error=4;
						list.elementAt(i).error=4;
						list.elementAt(i+1).error=4;
						errorMessage="\"]\" must be followed by \"[\" or \"]\"";
					}
				}
				}
					
			}
			
		
			if(error==0 && left!=right){
				error=5;
				errorMessage="The number of \"[\" does not match the number of \"]\"";
				for(StringWithError x: list){
					if(x.s.equals("[")||x.s.equals("]")){
						x.error=5;
					}
				}
				
			}
			
		}
		
		if(error==0)
			return true;
		else
			return false;
		
	}
	

	
	
	public Vector<Clause> createBasicClauses(){

		Vector<Clause> v1= new Vector<Clause>();
		int countOfWords=0;
		int countOfStart=0;
		int countOfEnd=0;
		for(int i=0;i<list.size();i++){
			
			if(i<list.size()-1&&i>1&&!list.elementAt(i).s.equals("[")&&!list.elementAt(i).s.equals("]")
					&&list.elementAt(i+1).s.equals("]")&&!list.elementAt(i-1).s.equals("[")&&
					!list.elementAt(i-1).s.equals("]"))
			{
			countOfWords++;
			countOfEnd+=list.elementAt(i).s.length();
			Clause basics = new Clause();
			basics.node=list.elementAt(i);
			basics.string=list.elementAt(i).s;
			basics.mid= (countOfStart+countOfEnd)/2.0+countOfWords;
			basics.length=list.elementAt(i).s.length();
			v1.add(basics);
			countOfStart=countOfEnd;
			}
			else{
				Clause basics= new Clause();
				basics.node=list.elementAt(i);
				v1.add(basics);
			}
		}
		return v1;
	
	}
	
	
	//cannot be called if not pass ableToBuildTree Test
	public Stack<Clause> buildStructure(Vector<Clause> v2){
		
		
		Stack<Clause> s1= new Stack<Clause>();
		for(Clause x: v2){
			if(!x.node.s.equals("]")){
				s1.push(x);
				
			}
			else{
				Stack<Clause> temp= new Stack<Clause>();
				while(!s1.peek().node.s.equals("[")){
					temp.push(s1.pop());
					
				}
				s1.pop();
				Clause newClause= new Clause();
				//if (!temp.empty());
				Clause top= temp.pop();
				
				newClause.node=top.node;
				int length=0;
				int countOfParts=0;
				int levelMax=0;
				String string = "";
				double mid=0;
				while(!temp.empty()){
					Clause now=temp.pop();
					if(now.levels>levelMax){
						levelMax=now.levels;
					}
					string +=now.string;
					string +=" ";
					
					countOfParts++;
					mid+=now.mid;
					length+=now.length;
					newClause.content.add(now);
				}
				newClause.length=length+countOfParts;
				newClause.mid=mid/countOfParts;
				newClause.levels=levelMax+1;
				newClause.string=string;
					if(!g.set.contains(newClause.node.s)){
					list.elementAt(newClause.node.index).error=6;
					newClause.node.error=6;
					error=6;
					errorMessage+="\""+newClause.node.s+"\" is not a defined type\n";
					
				}
				else if(newClause.levels==1&&!g.setBasic.contains(newClause.node.s)){
					list.elementAt(newClause.node.index).error=7;
					newClause.node.error=7;
					error=7;
					errorMessage+="\""+newClause.node.s+"\" is not the the base type\n";
					
				}
				else if(newClause.levels>1){
					String rule=newClause.node.s+",";
					String output="";
					for(Clause x1: newClause.content){
						rule+=x1.node.s;
						output+=x1.node.s;
						output+=" ";
						rule+=",";
					}
					if(!g.setRules.contains(rule)){
						if(error==0){
						error=8;
						list.elementAt(newClause.node.index).error=8;
						newClause.node.error=8;
						for(Clause x1: newClause.content){
							list.elementAt(x1.node.index).error=8;
							x1.node.error=8;
						}
						
						errorMessage="There is no such rule as: "+newClause.node.s+" --> "+output+"\n";
						}
					}
				}
				newClause.setLevels();
				s1.push(newClause);
				
				
				
				
			}
		}
		if(s1.size()>1){
			error=9;
			errorMessage+="Oops! There is more than one entity. "
					+ "Please enter them separately or combine them into a larger entity.";
			for(Clause x:s1){
				list.elementAt(x.node.index-1).error=9;
				if(x.node.index-1>0){
					list.elementAt(x.node.index-2).error=9;	
				}
			}
			list.elementAt(list.size()-1).error=9;
		}
		return s1;
	}
	
	public static void main(String args[]){
		Input x = new Input ("[n [np name][np hheeh]][np n]");
	
	}
	
}
