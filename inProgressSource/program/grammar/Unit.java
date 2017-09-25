package program.grammar;



class Unit extends Block{
//inherited attributes
//	/*
//	 * property of the block
//	 * 1. optional represented by ()
//	 * 2. substitute represented by {}
//	 * 3. repetitive represented by *
//	 * 4. together represented by []--actually useless, just to mark the existence of []
//	 */
//	String property=null;
//	Vector<Block> blockList=null;
//	/*
//	 * basic  -NP AP
//	 * complex -(Conj AP)*  {Aux TP} (NP)
//	 * primitive - "[" "]" "{" "}", etc
//	 * when==basic, node has content, blocklist==null
//	 * when==complex, blockList has content, node==null	
//	 */
//	String type="";
//	String node=null;
	
	//new attributes
	/*
	 * can be left like ( { [
	 * can be right like } * ] ) 
	 */
	String position=null;
	
	public Unit(String input){
		super(input);
		if(input=="("){
			position="left";
			property="optional";
		}else if(input==")"){
			position="right";
			property="optional";
		}else if(input=="{"){
			position="left";
			property="substitute";
		}else if(input=="}"){
			position="right";
			property="substitute";
		}else if(input=="["){
			position="left";
			property="together";
		}else if(input=="]"){
			position="right";
			property="together";
		}else if(input=="*"){
			position="right";
			property="repetitive";
		}
	}
		
	
	
}
