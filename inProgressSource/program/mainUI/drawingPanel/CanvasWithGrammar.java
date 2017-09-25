package program.mainUI.drawingPanel;

import program.grammar.Grammar;

public class CanvasWithGrammar extends Canvas2{
	
	Grammar grammar;
	
	public void changeGrammar(Grammar grammar){
		this.grammar=grammar;
	}
	
	public String checkGrammar(){
		return "";
	}
}
