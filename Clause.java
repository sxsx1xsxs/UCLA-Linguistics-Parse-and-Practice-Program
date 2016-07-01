//2016/5/5
//test [ np [ art the ] [ n girl ] ]
//test [ s [ np [ art the ] [ n girl ] ] [ vp [ v pettled ] [ np [ art the ] [ n cat ] ] ] ]
//test[s[np[art the]     [n2    girl]][vp [v pettled ] [ np [ art the ] [ n cat ] ] ] ]
//[s[np[art the][n girl]][vp [v pettled][np[art the][n cat]]]]
//test [s[np[art][n girl]][vp [v pettled][np[art the][n cat]]]]
//[s[np[art the][a girl]][vp [v pettled][np[art the][n cat]]]]
//s[np[art the][n girl]][vp [v pettled][np[art the][n cat]]]]
//[s[np[art the][n girl]][vp [v pettled][np[art the][n cat]]]]
//test [s [np [n John]][vp [v runs]]]

import java.util.Stack;
import java.util.Vector;

public class Clause {
	String string="";
	StringWithError node=new StringWithError();
	Vector<Clause> content= new Vector<Clause>();
	int length=0;
	double mid=0;
	int levels=0;
	int leveldown=0;
	int error=0;
	
	public void setLevels(){
		Stack<Clause> store=new Stack<Clause>();
		store.push(this);
		while(!store.empty()){
			Clause temp = store.pop();
			for( Clause x: temp.content){
				x.leveldown=temp.leveldown+1;
				store.push(x);
			}
		}
	}
	
	
	
		
	

}
