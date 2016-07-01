import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;





//[VP [V like] [NP [Art the] [N cat]]]
public class TreeDisplay extends JPanel {
	
	int height=1;
	int recHeight=0;
	int recWidth=0;
	Graphics tree;
	
	Stack<Clause> center=new Stack<Clause>();
	String type="";
	PopUpMenu popupmenu=new PopUpMenu();
	    
	
	static public void main(String args[]){
		 JFrame frame = new JFrame ("UCLA-PPP UCLA Parse and Practice Program");
	        //changeFontRecursive(frame,font);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        Input x= new Input("[VP [V like] [NP [Art the] [N cat]]]");
	        Stack<Clause> haha=x.buildStructure(x.createBasicClauses());
	        JPanel tree1=new JPanel();

	        frame.pack();
	        frame.setVisible(true);
	}

	
	TreeDisplay(){
		
		
		center=new Stack<Clause>();
		type="";
		//super.setPreferredSize(new Dimension (500,500));
		
		super.addMouseListener(popupmenu.new PopupTriggerListener());
		
		repaint();
	}
	   
	    void set(Stack<Clause> inputclause, String s){
	    	center=inputclause;
	    	JLabel test=new JLabel(); 
	    	Font font=test.getFont();
	    	
	    	 height=font.getSize();
	    	 System.out.println("the height is" +height);
	    	type=s;
	    	int length=0;
	    	int levelmax=0;
	    	for(Clause x: center){
	    		length+=x.length;
	    		if(x.levels>levelmax){
	    			levelmax=x.levels;
	    		}
	    	}
	    	
	    	recHeight=3*height*(1+levelmax);
	    	recWidth=(int)(height*length);
	    	Dimension d =new Dimension(recWidth, recHeight);
	    	setPreferredSize(d);
	    	//repaint();
	    	
	    }
	    
	    void set(Stack<Clause> inputclause){
	    	center=inputclause;
	    	JLabel test=new JLabel(); 
	    	Font font=test.getFont();
	    	
	    	 height=font.getSize();
	    	 System.out.println("the height is" +height);
	    	
	    	int length=0;
	    	int levelmax=0;
	    	for(Clause x: center){
	    		length+=x.length;
	    		if(x.levels>levelmax){
	    			levelmax=x.levels;
	    		}
	    	}
	    	
	    	recHeight=3*height*(1+levelmax);
	    	recWidth=(int)(height*length);
	    	Dimension d =new Dimension(recWidth, recHeight);
	    	setPreferredSize(d);
	    	repaint();
	    	
	    }
	    
	    void clear(){
	    	center=new Stack<Clause>();
			type="";
			super.setPreferredSize(new Dimension (10,10));
			repaint();
	    }
	

	    public void drawTopDown (Graphics tree) 
	    {
	    	  Stack<Clause> mywords=new Stack<Clause>();
	        for(Clause cx:center){
	        mywords.push(cx);
	        while(!mywords.empty())
	        {
	     	   Clause temp= mywords.pop();
	     	   FontMetrics metrics = tree.getFontMetrics(); 
//	     	   int height=metrics.getHeight();
	            int tempx=(int) (temp.mid*height);
	            int tempy=2*height+temp.leveldown*3*height+height/3*2;
	     	   for(Clause x: temp.content)
	     	   {
	     		   int tempx2=(int) (x.mid*height);
	                int tempy2=2*height+x.leveldown*3*height-height;
	     		   mywords.push(x);
	     		   tree.drawLine(tempx, tempy, tempx2,tempy2);
	     	   }
	     	   if(temp.node.error!=0){
	     		   tree.setColor(Color.red);
	     	   }
	     	   tree.drawString(temp.node.s, tempx-metrics.stringWidth(temp.node.s)/2, 2*height+temp.leveldown*3*height);
	     	   tree.setColor(Color.black);
	     	   
	     	   
	     	  
	        }
	        
	         
	     }
	        
	    }
	    
	    public void drawBottomUp (Graphics tree) 
	    {
//	    	Font myFont = new Font ("Courier New", 1, 17);
//	    	tree.setFont (myFont);
	    	
	    	for(Clause cx:center){
	    		
	    	
	       Stack<Clause> mywords=new Stack<Clause>();
	       mywords.push(cx);
	       while(!mywords.empty())
	       {
	    	   Clause temp= mywords.pop();
	    	   FontMetrics metrics = tree.getFontMetrics(); 
	           int tempx=(int) (temp.mid*height);
	           int tempy=recHeight-2*height-temp.levels*3*height+height/3*2;
	   
	    	   for(Clause x: temp.content)
	    	   {
	    		             
	               int tempx2=(int) (x.mid*height);
	               int tempy2=recHeight-2*height-x.levels*3*height-height;
	               
	    		   mywords.push(x);
	    		   tree.drawLine(tempx, tempy, tempx2, tempy2);
	    	   }
	    	   if(temp.node.error!=0){
	    		   tree.setColor(Color.red);
	    	   }
	    	   tree.drawString(temp.node.s, tempx-metrics.stringWidth(temp.node.s)/2, recHeight-2*height-temp.levels*3*height);
	    	   tree.setColor(Color.black);
	       }
	    	}

	  }
	    
	    public void paintComponent (Graphics tree) {
	    
        	
        	tree.setColor(SystemColor.white);
        	tree.fillRect(0, 0, recWidth, recHeight);
        	tree.setColor(SystemColor.black);
         // tree.setColor(Color.blue);
	    	System.out.println(12);
         
           
           if (type=="BottomUp")
               drawBottomUp(tree);
           if(type=="TopDown")
           	drawTopDown(tree);
           
       }
	  }





