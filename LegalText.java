import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegalText {
	/*
	 * constants
	 * the following two are the only parts that need to be modified if more symbols want to recognizable
	 * w means [A-Za-z_0-9]+
	 * now because there is no overlap between words and the symbols, so "w+" is fine there
	 * but later if there is overlap, then, if may not be fine
	 */
	String[] setOfSymbols = {"*","{","}","(",")","[","]","-->"};
	String[] setOfWords={"w+"};
   /*
    * this set holds all the symbols in reverse sorted order
    * for example, "bc, a, pcc, bccc" would be reordered as "pcc, bccc, bc, a"
    * this order is useful for recognizing symbols, it make sure it examine the longest possible symboal first
    */
	TreeSet<String> set=new TreeSet<String>();
	
    
    public LegalText(){
    	
    	//putting every symbol into set
    	for(String x:setOfSymbols){
    		set.add(x);
    	}
    	for(String x:setOfWords){
    		set.add(x);
    	}
    	set=(TreeSet) set.descendingSet();
    }
    
    
    /*
     * this function returns the regular expression used to pick out each symbol at the
     * beginning of the line
     * can only passed in defined symbols, otherwise, return null
     */
    public String getRE(String symbol){
    	if(set.contains(symbol)){
    		return "^(\\s*)(\\"+symbol+")(.*)";
    	}else{
    		return null;
    	}
    	
    }

   
   static public void main(String args[]){
	   LegalText text=new LegalText();
	   Iterator<String> it = text.set.iterator();
	    while (it.hasNext()) {
	       // Get element
	       Object element = it.next();
	       System.out.println(element.toString());
	    }
	    
	    System.out.println(text.getRE("w+"));
   }
  
   
  
}
