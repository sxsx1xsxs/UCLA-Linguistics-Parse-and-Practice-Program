package program.Interface;



import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TextP extends JTextPane {

	StyleContext sc = new StyleContext();
    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
    Style mainStyle;
    Style cwStyle;
    Style heading2Style;
    
	public TextP(){
		
		
	    super.setStyledDocument(doc);
	    
	   
	    Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

	    mainStyle = sc.addStyle("MainStyle", defaultStyle);
	   

	    // Create and add the constant width style
	    cwStyle = sc.addStyle("ConstantWidth", null);
	    StyleConstants.setForeground(cwStyle, Color.red);
	   // StyleConstants.setBold(cwStyle, true);

	    // Create and add the heading style
	    heading2Style = sc.addStyle("Heading2", null);
	    StyleConstants.setForeground(heading2Style, Color.green);
	    //StyleConstants.setBold(heading2Style, true);

	    doc.setLogicalStyle(0, mainStyle);
	    
	   
	    

	}
	void clear(){
		 try {
	           
	            doc.remove(0, doc.getLength());
	          
	          } catch (BadLocationException e) {
	          }
	}
	void setInnerText(String text){
		 doc.setLogicalStyle(0, mainStyle);
      
		 try {
	           
	            doc.insertString(doc.getLength(), text, null);

	          } catch (BadLocationException e) {
	          }
		
	}
	
	
	void highlight(int offset, int length){
		 doc.setCharacterAttributes(offset, length, cwStyle, false);
		 doc.setLogicalStyle(0, mainStyle);
	}
	void highlight2(int offset, int length){
		 doc.setCharacterAttributes(offset, length, heading2Style, false);
		// doc.setLogicalStyle(0, mainStyle);
	}
	
	
	String getInput() throws BadLocationException{
		String x="";
		Segment txt=new Segment();
		super.getDocument().getText(0, super.getDocument().getLength(), txt);
		x=txt.toString();
		return x;
	}
	
	void insertInput(StringWithError x){
		int l=doc.getLength();
		setInnerText(x.s+" ");
		if(x.error==9){
			highlight2(l,x.s.length());
		}
		else if(x.error !=0){
			highlight(l,x.s.length());
		}
		
	}
}
