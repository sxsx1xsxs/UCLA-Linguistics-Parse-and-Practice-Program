import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Grammar {
	Set<String> set=new HashSet<String>();
	Set<String> setBasic=new HashSet<String>();
	Set<String> medium=new HashSet<String>();
	
	Set<String> setRules= new HashSet<String>();
	Vector<Line> lines=new Vector<Line>();
	Vector<Integer> errors=new Vector<Integer>();
	
	
	
		
	class Line{
		String content;
		//which line it is in the input file
		int index=0;
		
		
		int error=0;
		//1. has ill formed arguments like "(np"
		//2. basetype line does not have exactly one non-optional argument
		//3. rule line has more than 1 :
		//4. rule line before : does not have exactly one non-optional argument
		//5. rule line after : has no non-optional argument
		
		//empty,comment,base,rule
		String type="";
		
		//stored data
		Vector<String> rule=new Vector<String>();
		String baseType="";
		String intermediate="";
		Vector<String> base=new Vector<String>();
		
		
		
		
		//helper functions
		int isOptional(String input){
			int i=0;//if not optional return 0;
			
			if(input.length()>2&&input.charAt(0)=='('&&input.charAt(input.length()-1)==')'){
				i=1;//start with ( and end with ), so correct optional format
			}
			else if
			(input.length()>0&&input.charAt(0)=='('){
				i=2;//only has left parenthesis, so not in the right format
			}
			else if(input.length()>0&&input.charAt(input.length()-1)==')'){
				i=2;//only has right parenthesis, so not in the right format
			}
			
			return i;
		}
		
		int hasNonOptional(String[] x){
			
			for(String n:x){
				if(isOptional(n)==0){
					return 1;//1 means has nonOptional words
				}
				else if(isOptional(n)==1);
				else if(isOptional(n)==2){
					return 2;//2 means has wrong format words
				}
			}
			
			return 0;//0 means is empty or don't have nonOptional words
		}
		
		//call only when is optional
		String noParenthesis(String x){
			StringBuffer sb=new StringBuffer();
			
			for(int i=1;i<x.length()-1;i++){
				sb.append(x.charAt(i));
				
			}
			String sr=sb.toString();
			return sr;
			
		}
		
		//after deleting the extra space
		void insertSpaceP(){
			StringBuffer bu=new StringBuffer();
			for(int i=0;i<content.length();i++){
				if(content.charAt(i)==')'&&i<content.length()-1&&content.charAt(i+1)!=' '){
					bu.append(content.charAt(i));
					bu.append(" ");
				}
				else{
					bu.append(content.charAt(i));
				}
			}
			
			String xr=bu.toString();
			content=xr;
		}
		
		Boolean detectcomment(){
			int i=0;
			while(i<content.length()-1&&content.charAt(i)==' '){
				i++;
			}
			if(i<content.length()-1&&content.charAt(i)=='/'&&content.charAt(i+1)=='/'){
				type="comment";
				return true;
			}
			return false;
		}
		
		void changetocolon(){
			StringBuffer temp=new StringBuffer();
			for(int i=0; i<content.length();i++){
				if(i<content.length()-1&&content.charAt(i)=='-'&&content.charAt(i+1)=='>'){
					temp.append(':');
					i++;
				}else if(i<content.length()-2&&content.charAt(i)=='-'&&content.charAt(i+1)=='-'&&content.charAt(i+2)=='>'){
					temp.append(':');
					i++;
					i++;
				}
				else{
					temp.append(content.charAt(i));
				}
			}
			 String newcontent=temp.toString();
		  		
			  content=newcontent;
			System.out.println(content);
		}
		void changeFormat(){
		  StringBuffer temp=new StringBuffer();
		  int countofcolon=0;
		  //deleting extra space
		  for(int i=0;i<content.length();i++){
			  if(content.charAt(i)==':'){
				  countofcolon++;
			  }
			  if(i<content.length()-1&&content.charAt(i)==' '&&content.charAt(i+1)==':');
			  else if(i<content.length()-1&&content.charAt(i)==':'&&content.charAt(i+1)==' '){
				  temp.append(content.charAt(i));
				  while(i<content.length()-1&&content.charAt(i+1)==' ')
				  i++;
			  }
			  else if(i<content.length()-1&&content.charAt(i)=='('&&content.charAt(i+1)==' '){
				  temp.append(content.charAt(i));
				  while(i<content.length()-1&&content.charAt(i+1)==' ')
				  {i++;}
			  }
			  else if(i<content.length()-1&&content.charAt(i)==' '&&content.charAt(i+1)==')');
			  else if(i<content.length()-1&&content.charAt(i)==' '&&content.charAt(i+1)==' ');
			  else{
				  temp.append(content.charAt(i));
			  }
		  }
		  if(countofcolon>1){
			  error=3;
		  }
		  
		  //deleting first and last space
		  else {
			  if(temp.length()>0&&temp.charAt(0)==' '){
			  temp.deleteCharAt(0);
		  }
		  
		  if(temp.length()>0&&temp.charAt(temp.length()-1)==' '){
			  temp.deleteCharAt(temp.length()-1);
		  }
		  
		  }
		  
		  String newcontent=temp.toString();
		  		
		  content=newcontent;
		  insertSpaceP();
		  
		}
		
		Line(String sent){
			content=sent;
			if(detectcomment());
			else{
				changetocolon();
				changeFormat();
				detectError();
				System.out.println(content);
				if(error==0&&type=="rule"){
					importRule();
				}
			}
			
			
		}
		
		void detectError(){

                //changeFormat already makes sure content only contains one colon if it contains any colon   
				if(content.contains(":")){
					 
					String[] cons=content.split(":");
					if(cons.length==1){
						error=5;
					}
					else if(cons.length==2){
						String[] before=cons[0].split(" ");
						String[] after=cons[1].split(" ");
						if(before.length==1){
							if(before[0]=="")
							{
								error=4;
							}
							else if(isOptional(before[0])==2){
								error=1;
							}else if(isOptional(before[0])==1){
								error=4;
							}else{
								if(hasNonOptional(after)==2){
									error=1;
								}else if(hasNonOptional(after)==0){
									error=5;
								}else{
									type="rule";
								}
							}
							
						}else{
							error=4;
						}
						
					}
					
				}
				//if not contain colon, it's base line
				else{
					
					String[] hello=content.split(" ");
					if(hello.length==1){
						if(hello[0]==""){
							type="empty line";
							
						}
						else if(isOptional(hello[0])==2){
							error=2;
						}
						else{
							type="base";
							baseType=hello[0];
						}
					}
					else{
						error=2;
					}
				}
			      
			}
			
			
	
		
		void importRule(){
			
			
                   
			
					 
					String[] cons=content.split(":");
					
						
						String[] after=cons[1].split(" ");
						
						String rulex=cons[0]+",";
						Vector<String> possibleRules=new Vector<String>();
						possibleRules.add(rulex);
						intermediate=cons[0];
							
							for(String x: after){
								if(isOptional(x)==0){
									Vector<String> tempRules=new Vector<String>();
									for(String y:possibleRules){
										String z=y+x+",";
										tempRules.addElement(z);
									}
									possibleRules=tempRules;
									base.add(x);
							}else if(isOptional(x)==1){
								Vector<String> tempRules=new Vector<String>();
								for(String y:possibleRules){
									
									tempRules.addElement(y);
									String z=y+noParenthesis(x)+",";
									tempRules.addElement(z);
								}
								possibleRules=tempRules;
								base.add(noParenthesis(x));}}
							rule=possibleRules;
								
							
	}
			
			
			
			
		
		
		
	}
	
	Grammar(){
		String count[] = {"S","NP","Aux","VP","Art","AP","N","PP","Pronoun","V","VP","Adv","A","Comp","Conj", "P","CP"};
		 String countbasic[] = {"Aux","Art","N","Pronoun","V","Adv","A","Comp","Conj","P"};
		 String collectionrules[]= {"NP,Art,N,",
				 "VP,V,NP,NP,PP,S,",
				 "NP,N,",
				 "VP,VP,Conj,VP,",
				 "S,NP,VP,",
				 "VP,V,NP,S,",
				 "VP,V,NP,NP,S,",
				 "CP,Comp,S,",
				 "VP,V,PP,S,",
				 "PP,PP,Conj,PP,",
				 "CP,S,",
				 "PP,P,NP,",
				 "NP,NP,Conj,NP,",
				 "S,S,Conj,S,",
				 "VP,V,NP,NP,PP,",
				 "S,NP,Aux,VP,",
				 "NP,Pronoun,",
				 "V,V,Conj,V,",
				 "NP,Art,N,PP,",
				 "AP,AP,Conj,AP,",
				 "NP,NP,AP,N,",
				 "NP,NP,N,PP,",
				 "AP,A,",
				 "NP,AP,N,PP,",
				 "NP,AP,N,",
				 "NP,Art,AP,N,",
				 "NP,Art,AP,N,PP,",
				 "NP,N,PP,",
				 "VP,V,NP,NP,",
				 "NP,NP,AP,N,PP,",
				 "VP,V,S,",
				 "VP,V,PP,",
				 "VP,V,NP,",
				 "NP,NP,N,",
				 "VP,V,NP,PP,",
				 "AP,Adv,A,",
				 "VP,V,",
				 "VP,V,NP,PP,S,"};
		 
		
	        for(int i = 0; i<count.length; i++){
	           set.add(count[i]);
	        }
	        for(int i = 0; i<countbasic.length; i++){
		           setBasic.add(countbasic[i]);
		        }
			for(String x: collectionrules){
				setRules.add(x);
			}
	}
	public void changeGrammar(String grammarInput){
		
		 set=new HashSet<String>();
		setBasic=new HashSet<String>();
		medium=new HashSet<String>();
		
		 setRules= new HashSet<String>();
		lines=new Vector<Line>();
		errors=new Vector<Integer>();
		
		  String[] text = grammarInput.split(System.getProperty("line.separator"));
         for(int i=0;i<text.length;i++){
        	 Line examine=new Line(text[i]);
        	 lines.add(examine);
        	if(examine.error!=0){
        		errors.add(i);
        	}
        	 
            
	}
         if(errors.size()==0){
        	 for(Line xx:lines){
        		 if(xx.type=="rule"){
        			 medium.add(xx.intermediate);
        			 set.add(xx.intermediate);
        			 for(String yy:xx.rule){
        				 setRules.add(yy);
        			 }
        			 for(String yy:xx.base){
        				 set.add(yy);
        				 if(!medium.contains(yy)){
        					 setBasic.add(yy);
        				 }
        				
        			 }
        		 }
        		 if(xx.type=="base"){
        			 set.add(xx.baseType);
        			 setBasic.add(xx.baseType);
        		 }
        	 }
         }
//     	System.out.println("basetype:");
//		for(String y: setBasic){
//			System.out.println(y);
//		}
//		System.out.println("types:");
//		for(String y: set){
//			System.out.println(y);
//		}
//		System.out.println("rules:");
//		for(String y: setRules){
//			System.out.println(y);
//		}
//		for(int i: errors){
//			System.out.println(lines.elementAt(i).error+lines.elementAt(i).content);
//		}
	
		
          
	}
	
	
	
	
	public static void main(String args[]){
		String x =
				"n \n"
				+ "np-> art n \n"
				+ "//hello \n"
				+ "s --> {vp np } p \n"
				+ "v \n"
				+ 
				"np {(a) n}";
		System.out.println(x);
		//Grammar.Line e=new Grammar.Line("n: np");
		
		Grammar g=new Grammar();
		g.changeGrammar(x);
		System.out.println(g.errors.size());
		
		
		System.out.println("basetype:");
		for(String y: g.setBasic){
			System.out.println(y);
		}
		System.out.println("types:");
		for(String y: g.set){
			System.out.println(y);
		}
		System.out.println("rules:");
		for(String y: g.setRules){
			System.out.println(y);
		}
		
	}
}
