import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Stack;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

//[VP [V like] [NP [Art the] [N cat]]]


    public class Interface {
    	static Grammar2 g=null;
    	
    	    
    	 static void changeFontRecursive(Container root, Font font) {
    		 
    		 try{
    	    for (Component c : root.getComponents()) {
    	        c.setFont(font);
    	        if (c instanceof Container) {
    	            changeFontRecursive((Container) c, font);
    	        }
    	    }
    		 }catch(NullPointerException e){
    			 
    		 }
    	}
    	
    	 
    	 public static void setUIFont (javax.swing.plaf.FontUIResource f){
    		    java.util.Enumeration keys = UIManager.getDefaults().keys();
    		    while (keys.hasMoreElements()) {
    		      Object key = keys.nextElement();
    		      Object value = UIManager.get (key);
    		      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
    		        UIManager.put (key, f);
    		      }
    		    }
    	
    public static void main(String[] args) throws AWTException, IOException {
    	
     
        final JFrame frame = new JFrame ("UCLA-PPP UCLA Parse and Practice Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        
        Interface x= new Interface();
        final WindowL window = x.new WindowL(new Grammar(), new LMenuBar());
        JScrollPane scroll=new JScrollPane(window);
      
        frame.add(scroll);
        Dimension d= new Dimension (800,800);
        frame.setPreferredSize(d);
        frame.pack();
        frame.setVisible(true);
        
       
    	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,24));
    	SwingUtilities.updateComponentTreeUI(frame);
       
        
      
        
       
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
      
           

        }
    
    class WindowL extends JPanel {
    	

        TreeDisplay treeDisplay;
        JTextArea messageBar;
        JLabel brackets;
        TextP inputp;
        JPanel upPanel;
        JPanel buttomHolder;
        LMenuBar menuBar;
        JLabel adding;
        JPanel tree=new JPanel();
        JButton bottomUp;
       
        Stack<Clause> x=new Stack<Clause>();
        int xi=0;
        
        void changei(int i){
        	xi=i;
        }

        public WindowL(Grammar2 grammar, LMenuBar menu) {
        	super.setLayout(new GridBagLayout());
        	
        	
        	g=grammar;
            //draw.setColor(Color.blue);
            //super.setLayout(new BorderLayout());
            treeDisplay = new TreeDisplay();
            
            upPanel = new JPanel();
            upPanel.setLayout(new GridLayout(5,1));
            
           menuBar=menu;
           GridBagConstraints c1=new GridBagConstraints();
       	   c1.gridx=0;
       	   c1.gridy=0;
       	   c1.weightx=1;
       	   c1.fill=GridBagConstraints.HORIZONTAL;
       	   
          
            super.add(menuBar,c1);
            
            messageBar = new JTextArea("Please enter the clause:");
            messageBar.setBackground(Color.LIGHT_GRAY);
            messageBar.setEditable(false);
            messageBar.setLineWrap(true);
            messageBar.setWrapStyleWord(true);
           
            GridBagConstraints c2=new GridBagConstraints();
        	   c2.gridx=0;
        	   c2.gridy=1;
        	   c2.weightx=1;
        	   c2.fill=GridBagConstraints.HORIZONTAL;
        	   c2.anchor=GridBagConstraints.CENTER;
        	   c2.ipady=5;
            super.add(messageBar,c2);       

            
            inputp=new TextP();
            GridBagConstraints c3=new GridBagConstraints();
     	   c3.gridx=0;
     	   c3.gridy=2;
     	   c3.fill=GridBagConstraints.HORIZONTAL;
     	   c3.weightx=1;
     	  c3.ipady=5;
            super.add(inputp,c3);
            DocumentListener documentListener = new DocumentListener() {
    	        public void changedUpdate(DocumentEvent documentEvent) {
    	        	leftRight();
    	        	
    	        	 
    	        }
    	        public void insertUpdate(DocumentEvent documentEvent) {
    	        	leftRight();
    	        	
    	        }
    	        public void removeUpdate(DocumentEvent documentEvent) {
    	        	leftRight();
    	        }
    	        
    	        private void leftRight(){
    	        	String input="";
    	            try {
    	    			input=inputp.getInput();
    	    		} catch (BadLocationException e) {
    	    			// TODO Auto-generated catch block
    	    			e.printStackTrace();
    	    		}
    	            Input sent=new Input(input,g);
    	            int left=sent.countLeftBracket();
    	            int right=sent.countRightBracket();
    	            brackets.setText("Left brackets: " +left+"              Right brackets: "+right);
    	            
    	        }
    	        
    	      };
    	      inputp.getDocument().addDocumentListener(documentListener);
            
            brackets=new JLabel();
            
            brackets.setForeground(Color.darkGray);
//            Font myFont = new Font ("Courier New", 1, 14);
//            brackets.setFont(myFont);
            
            GridBagConstraints c4=new GridBagConstraints();
       	   c4.gridx=0;
       	   c4.gridy=3;
       	   c4.fill=GridBagConstraints.HORIZONTAL;
       	   c4.weightx=1;
       	c4.ipady=5;
       	 //  c4.weighty=1;
            super.add(brackets,c4);
           
            repaint();
            JLabel changed=new JLabel("hi");
            
            JPanel rightupbuttom=new JPanel();
            rightupbuttom.setBackground(Color.white);
           rightupbuttom.setLayout(new BorderLayout());

            JPanel rightbuttom=new JPanel();
 
           // add(upPanel, BorderLayout.NORTH);
            
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.CENTER;
          //  gridbag.setConstraints(tree, constraints);
            tree.setLayout(gridbag);
            tree.setBackground(Color.white);
            
            final GridBagConstraints c5=new GridBagConstraints();
      	   c5.gridx=0;
      	   c5.gridy=4;
      	   c5.fill=GridBagConstraints.BOTH;
      	   c5.anchor=GridBagConstraints.CENTER;
      	   c5.weightx=1;
      	   c5.weighty=1;
            tree.add(treeDisplay,constraints);
            super.add(tree, c5);
          
         
          
           // add(tree,BorderLayout.CENTER);

          
            
           
      
            
           bottomUp = new JButton("Bottom Up Tree");
           JButton clear = new JButton("Clear");
           JButton topDown= new JButton("Top Down Tree");
           
            
            
            class BottomUpListener implements ActionListener {
            	 public void actionPerformed (ActionEvent event) {
            		
            		 
                 	String sent="";
                 	try {
    					sent=inputp.getInput();
    				} catch (BadLocationException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                 	
                 	Input input=new Input(sent,g);
                 	if(!input.ableToBuildTree())
                 	{   x=new Stack<Clause>();
                 		inputp.clear();
                        messageBar.setText(input.errorMessage);
                        treeDisplay.clear();
                     //   changeFontRecursive(upPanel,font);
                      // treeDisplay.paintComponent(draw);
                      
                 	for(StringWithError y:input.list){
                 		inputp.insertInput(y);
                 	}}
                 	else{
                 		x=input.buildStructure(input.createBasicClauses());
                 		inputp.clear();
                 		for(StringWithError y:input.list){
                    		inputp.insertInput(y);
                    	}
                 		messageBar.setText(input.errorMessage);
                 		if(input.error==0){
                 			messageBar.setText("The clause you entered has the following bottom-up tree structure:");
                 		}
                 		
                 		
                 		
                 		treeDisplay.set(x,"BottomUp");
                 		
                         add(tree,c5);
                   	    repaint();
                 	   
                 	}


            }}

            class ClearListener implements ActionListener {
                public void actionPerformed (ActionEvent event) {
                    treeDisplay.clear();
                    inputp.clear();
                    messageBar.setText("The window has been cleared, please enter the clause");
                    repaint();
                }
            }
            
            class TopDownListener implements ActionListener {
           	 public void actionPerformed (ActionEvent event) {
        		
             
             	String sent="";
             	try {
    				sent=inputp.getInput();
    			} catch (BadLocationException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
             	
             	Input input=new Input(sent,g);
             	if(!input.ableToBuildTree())
             	{x=new Stack<Clause>();
             		inputp.clear();
                    messageBar.setText(input.errorMessage);
                    treeDisplay.clear();
                    repaint();
             	for(StringWithError y:input.list){
             		inputp.insertInput(y);
             	}}
             	else{
             		x=input.buildStructure(input.createBasicClauses());
             		inputp.clear();
             		for(StringWithError y:input.list){
                		inputp.insertInput(y);
                	}
             		messageBar.setText(input.errorMessage);
             		if(input.error==0){
             			messageBar.setText("The clause you entered has the following top-down tree structure:");
             		}
             		
             		
             		
             		treeDisplay.set(x, "TopDown");
             	
             	    repaint();
             	   
             	}


        }}
            
            
            BottomUpListener bottomUplistener = new BottomUpListener();
            bottomUp.addActionListener(bottomUplistener);
            
            ClearListener clearlistener = new ClearListener();
            clear.addActionListener(clearlistener);
            
            TopDownListener topDownlistener = new TopDownListener();
            topDown.addActionListener(topDownlistener);

            buttomHolder = new JPanel();
            buttomHolder.add(bottomUp);
            buttomHolder.add(topDown);
            buttomHolder.add(clear);
            GridBagConstraints c6=new GridBagConstraints();
       	   c6.gridx=0;
       	   c6.gridy=5;
       	   c6.fill=GridBagConstraints.HORIZONTAL;
       	   c6.weightx=1;
       	   //c6.ipady=10;
       	  // c6.weighty=1;
            super.add(buttomHolder, c6);
            
           
            treeDisplay.popupmenu.screenshot.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	
                	
                	 BufferedImage image = new BufferedImage(
               		      treeDisplay.recWidth,
               		      treeDisplay.recHeight,
               		      BufferedImage.TYPE_INT_RGB
               		      );
               		 
               		   treeDisplay.paint( image.getGraphics() );
                	
                 	try {
                         // write the image as a PNG
                         ImageIO.write(
                           image,
                           "png",
                           new File("treeimage.png"));
                       } catch(Exception ex) {
                         ex.printStackTrace();
                       }

                  }
                });
        }
        
       
        public void paintComponent (Graphics tree) {
        	
            super.paintComponent(tree);
            String input="";
            try {
    			input=inputp.getInput();
    		} catch (BadLocationException e) {

    			e.printStackTrace();
    		}
            Input sent=new Input(input,g);
            int left=sent.countLeftBracket();
            int right=sent.countRightBracket();

            brackets.setText("Left brackets: " +left+"              Right brackets: "+right);
            
           
 
        }
        
       
    }
    
    }
    

    
    
    
   