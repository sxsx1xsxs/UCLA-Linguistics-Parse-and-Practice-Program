package program.Grammar;


import java.util.Vector;


public class Block{
	//subclass Unit is used for symbols, regular Block class is for nodes
	/*
	 * property of the block
	 * 1. optional represented by ()
	 * 2. substitute represented by {}
	 * 3. repetitive represented by *
	 * 4. together represented by []--actually useless, just to mark the existence of []
	 */
	public String property=null;
	public Vector<Block> blockList=new Vector<Block>();
	/*
	 * basic  -NP AP
	 * complex -(Conj AP)*  {Aux TP} (NP)
	 * primitive - "[" "]" "{" "}", etc
	 * when==basic, node has content, blocklist==null
	 * when==complex, blockList has content, node==null	
	 */
	public String type="";
	public String node=null;

	
	
	//construct basic Block
	public Block(String input){
		node=input;
		type="basic";
	}
	
	
	//construct complex Block
	public Block(){
		
	}
	
	
	//when returning null, means this set of list cannot satisfy this block
	//otherwise, return the set of list of all possible remaining list after applying this block
	public Vector<Vector<String>> obeyBlock(Vector<Vector<String>> material){
		Vector<Vector<String>> r=new Vector<Vector<String>>();
		//if any list satisfy, will return a list set of size larger than 0
		//else return a list size of size ==0
		

		for(Vector<String>mm:material){
			//if oneline satisfy, add remaining list
			Vector<Vector<String>> now=obeyBlock1(mm);
			if(now!=null){
				for(Vector<String> pp:now){
					r.add(pp);
				}
			}
		}
		if(r.size()==0){
			return null;
		}else{
			return r;
		}

	}
	
	
	//helper function of public obeyBlock
	//return null if this list cannot satisfy this block
	//else return all possible remaining list
	private Vector<Vector<String>> obeyBlock1(Vector<String> material){
		Vector<Vector<String>> r=new Vector<Vector<String>>();
		if(type.equals("basic")){
			if(material.size()>0&&material.elementAt(0).equals(node)){
				Vector<String> now =new Vector<String>();
				for(int i=1;i<material.size();i++){
					String add=material.elementAt(i);
					now.add(add);
				}
				r.add(now);
				return r;
			}else{
				return null;
			}
		}
		else if(property.equals("optional")){
			r.add(material);
			Vector<Vector<String>> process=new Vector<Vector<String>>();
			process.add(material);
			
			for(Block block:blockList){
				process=block.obeyBlock(process);
				if(process==null){
					break;
				}}
			if(process!=null){
			for(Vector<String> pp:process){
				r.add(pp);
			}}
			
			return r;
	}
		else if(property.equals("substitute")){
			for(Block x:blockList){
				Vector<Vector<String>> test=x.obeyBlock1(material);
				if(test!=null){
					for(Vector<String> xx:test){
						r.add(xx);
					}
			}
				
		}
			
			if(r.size()==0){
				return null;
			}else{
				return r;
			}
		
	}
		
		
		//assume the ones with the property-"repetitive" has one element on blocklist
		else if(property.equals("repetitive")){
			r.add(material);
			Vector<Vector<String>> process=new Vector<Vector<String>>();
			process.add(material);
			while(process!=null){
			for(Block block:blockList){
				process=block.obeyBlock(process);
				if(process==null){
					break;
				}}
			if(process!=null){
			for(Vector<String> pp:process){
				r.add(pp);
			}}
			}
			return r;
	}
		
		else if(property.equals("together")){
			Vector<Vector<String>> process=new Vector<Vector<String>>();
			process.add(material);
			for(Block block:blockList){
				process=block.obeyBlock(process);
				if(process==null){
					return null;
				}
				
			}
			return process;
		}
		return null;
		}
}
