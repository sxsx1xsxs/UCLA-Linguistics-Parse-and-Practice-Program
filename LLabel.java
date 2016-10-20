import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class LLabel extends JLabel{
	Boolean editable=false;
	Grammar2 grammar;
	//can be "basic", "medium", "symbol"
	String type;
	
	public LLabel(String x, Grammar2 g){
		super(x);
		LegalText legal=new LegalText();
		for(String w:legal.setOfSymbols){
			if(x.equals(w)){
				editable=false;
				type="symbol";
				return;
			}
		}
		
		for(String w:g.setBasic){
			if(x.equals(w)){
				type="basic";
				editable=true;
				super.setOpaque(true);
				super.setBackground(Color.decode("0x99CCFF"));
				super.setBorder(new BevelBorder(BevelBorder.RAISED));
				return;
			}
		}
		
		for(String w:g.medium){
			if(x.equals(w)){
				type="medium";
				editable=true;
				super.setOpaque(true);
				super.setBackground(Color.decode("0xCCE5FF"));
				super.setBorder(new BevelBorder(BevelBorder.RAISED));
				return;
			}
		}
	}
}
