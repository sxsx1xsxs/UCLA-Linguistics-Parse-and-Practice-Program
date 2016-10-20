
public class CompSet implements Comparable<CompSet>{
   String t="";
   public CompSet(String string){
	   t=string;
   }
	@Override
	public int compareTo(CompSet o) {
		if(o.t.equals("S")){
			return 1;
		}
		
		if(o.t.equals("NP")&&!t.equals("S")){
			return 1;
		}
		
		if(o.t.equals("VP")&&!t.equals("S") &&!t.equals("NP")){
			return 1;
		}
		
		else{
			return -1;
		}
		
	}

}
