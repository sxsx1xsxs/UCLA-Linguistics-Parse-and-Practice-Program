import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.xenoage.util.gui.FontChooserComboBox;

public class Total {
	
	 static class Type{
     	String type="";
     	public void change(String x){
     		type=x;
     	}
     }
	 static class Int{
	     	int type=0;
	     	public void change(int x){
	     		type=x;
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
	  
	  private static void createAndShowUI() {
	        final JFrame frame = new JFrame("UCLA-PPP UCLA Parse and Practice Program");
	        final Grammar grammar=new Grammar();
	        final LMenuBar menu=new LMenuBar();
	        final JPanel mainwindow=new JPanel();
	        final Type type=new Type();
	        final Type font=new Type();
	        final Int size=new Int();
	        font.change("Serif");
	        size.change(24);
	        
	        
	        type.change("free");
	       
	        
	        Plain layers=new Plain(grammar.set, menu);
	       
	        ActionListener modetoclause= new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(1);
					 Interface x= new Interface();
				     final Interface.WindowL window = x.new WindowL(grammar,menu);
				     JScrollPane scroll=new JScrollPane(window);
				     mainwindow.removeAll();
				     mainwindow.add(scroll);
				     mainwindow.revalidate();
				     mainwindow.repaint();
				     type.change("clause");;
				    
				    // frame.removeAll();
//					 frame.add(scroll,BorderLayout.CENTER);
					 frame.revalidate();
					 frame.repaint();
		        	
				}
	        	
	        };
            
           menu.clauseentering.addActionListener(modetoclause);
           
           ActionListener modetofreedrawing= new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					//System.out.println(1);
					 Plain layers=new Plain(grammar.set, menu);
				     mainwindow.removeAll();
				     mainwindow.add(layers);
				     mainwindow.revalidate();
				     mainwindow.repaint();
				    
				    // frame.removeAll();
//					 frame.add(scroll,BorderLayout.CENTER);
					 frame.revalidate();
					 frame.repaint();
					 type.change("free");
					
		        	
				}
	        	
	        };
           
          menu.freedrawing.addActionListener(modetofreedrawing);
          
          final JFrame f =new JFrame();
          ActionListener grammarListener= new ActionListener(){
              String grammarInput="";
          	final JFileChooser fileChooser = new JFileChooser();
              public void actionPerformed(ActionEvent ae) {
                  int result = fileChooser.showOpenDialog(f);
                  if (result==JFileChooser.APPROVE_OPTION) {
                      File file = fileChooser.getSelectedFile();
                      
                     
                      try {
                          byte bt[]= Files.readAllBytes(file.toPath());   
                          grammarInput=new String(bt,"UTF-8");
                         // System.out.println(grammarInput);
                       grammar.changeGrammar(grammarInput);
                       String x="line ";
                       for(int i:grammar.errors){
                    	   i=i+1;
                      	 x=x+i+", ";
                       }
                        if(grammar.errors.size()==0){
                      	  JOptionPane.showMessageDialog(
                                    null,
                                    new JLabel("new grammar successfully imported"));
                      	  if(type.type=="free"){
                      		 Plain layers=new Plain(grammar.set, menu);
        				     mainwindow.removeAll();
        				     mainwindow.add(layers);
        				     mainwindow.revalidate();
        				     mainwindow.repaint();
        				    
        				    // frame.removeAll();
//        					 frame.add(scroll,BorderLayout.CENTER);
        					 frame.revalidate();
        					 frame.repaint();
        					 type.change("free");
                      	  }
                      	  
                      	  if(type.type=="clause"){
                      		 Interface x1= new Interface();
        				     final Interface.WindowL window = x1.new WindowL(grammar,menu);
        				     JScrollPane scroll=new JScrollPane(window);
        				     mainwindow.removeAll();
        				     mainwindow.add(scroll);
        				     mainwindow.revalidate();
        				     mainwindow.repaint();
        				     type.change("clause");
        				    
        				    // frame.removeAll();
//        					 frame.add(scroll,BorderLayout.CENTER);
        					 frame.revalidate();
        					 frame.repaint();
                      	  }
                        }else{
                      	  JOptionPane.showMessageDialog(
                                    null,
                                    new JLabel(x+"has errors, so grammar is not successfully imported"));
                        }
               		
                      } catch (IOException ex) {
                          
                      }
                     
                  }
              }
          
          };
          
         menu.importgrammar.addActionListener(grammarListener);
	        
          frame.add(menu,BorderLayout.NORTH);
	       
	       frame.add(mainwindow);
	       
	       mainwindow.setBackground(Color.white);
	       // frame.add(scroll,BorderLayout.CENTER);
	        //frame.removeAll();
	       mainwindow.setLayout(new BorderLayout());
	       GridBagConstraints c=new GridBagConstraints();
	       c.gridx=0;
	       c.gridx=0;
	       c.fill=GridBagConstraints.BOTH;
	       mainwindow.setSize(new Dimension(900,900));
	       
	       menu.large.addActionListener(new ActionListener() {
	            @Override
	            
	            public void actionPerformed(ActionEvent event) {
	            	
	            	//System.out.println();
	            	
	            	
	            	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,28)); 
	           
	            	SwingUtilities.updateComponentTreeUI(frame);
	            	frame.revalidate();
	            	frame.repaint();
	            }
	        });
	       
	       menu.medium.addActionListener(new ActionListener() {
	            @Override
	            
	            public void actionPerformed(ActionEvent event) {
	            	
	            	//System.out.println();
	            	
	            	
	            	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,24)); 
	           
	            	SwingUtilities.updateComponentTreeUI(frame);
	            	frame.revalidate();
	            	frame.repaint();
	            }
	        });
	       
	       menu.customize.addItemListener(new FontChooserComboBox());
	       
	       menu.small.addActionListener(new ActionListener() {
	            @Override
	            
	            public void actionPerformed(ActionEvent event) {
	            	
	            	//System.out.println();
	            	
	            	
	            	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,20)); 
	           
	            	SwingUtilities.updateComponentTreeUI(frame);
	            	frame.revalidate();
	            	frame.repaint();
	            }
	        });
	       
	       
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        String[] array = ge.getAvailableFontFamilyNames();
	        final JComboBox<String> box = new JComboBox<String>(array);
	        final JPanel hi=new JPanel();
	        hi.setLayout(new GridBagLayout());
	        JLabel x=new JLabel("Select Font Family");
	        x.setHorizontalAlignment(SwingConstants.LEFT);
	        JLabel y=new JLabel("Enter Font Size");
	        y.setHorizontalAlignment(SwingConstants.LEFT);
	        final JTextField text=new JTextField();
	        final FontChooserComboBox box2=new FontChooserComboBox();
	        
	        GridBagConstraints c1=new GridBagConstraints();
	        c1.gridx=0;
	        c1.gridy=0;
	        c1.insets=new Insets(10,0,10,0);
	        hi.add(x,c1);
	        
	        GridBagConstraints c2=new GridBagConstraints();
	        c2.gridx=0;
	        c2.gridy=1;
	        c2.insets=new Insets(0,20,0,20);
	        hi.add(box2,c2);
	        
	        GridBagConstraints c3=new GridBagConstraints();
	        c3.gridx=0;
	        c3.gridy=2;
	        c3.insets=new Insets(10,0,10,0);
	        hi.add(y,c3);
	        
	        GridBagConstraints c4=new GridBagConstraints();
	        c4.gridx=0;
	        c4.gridy=3;
	        c4.fill=GridBagConstraints.HORIZONTAL;
	        c4.insets=new Insets(0,20,0,20);
	        hi.add(text,c4);
	        
	        
	        
	        
	        
	        
	       
	        
	        
	        
	        //Components[]=new Component[];
	        box.setEditable(true);
	        //final FontChooserComboBox box2=new FontChooserComboBox();
	        box2.setSize(new Dimension(100,50));
	        final JFrame frame2 = new JFrame();

	       // JButton button = new JButton("Button");
	        menu.customize.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	
	                JOptionPane.showMessageDialog(frame2, hi,"Customize Font", JOptionPane.PLAIN_MESSAGE);
	                font.change(box2.getSelectedItem().toString());
	                try{
	                size.change(Integer.parseInt(text.getText()));}
	                catch(NumberFormatException ei){
	                	
	                }
	                System.out.println(box2.getSelectedItem());
	                System.out.println(font.type);
	                System.out.println(size.type);
	                
	                setUIFont (new javax.swing.plaf.FontUIResource(font.type,Font.PLAIN,size.type)); 
	 	           
	            	SwingUtilities.updateComponentTreeUI(frame);
	            	frame.revalidate();
	            	frame.repaint();
	                
	                
	                
	                
	            }
	        });
	       
	        JScrollPane scroll=new JScrollPane(layers);
	        mainwindow.add(scroll);
	      //  mainwindow.removeAll();
	        
//	        Interface x= new Interface();
//		     final Interface.WindowL window = x.new WindowL();
//		     JScrollPane scroll=new JScrollPane(window);
		   //  mainwindow.add(scroll);
	        frame.setPreferredSize(new Dimension(800,800));
	        
	       
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        
	    }
	 public static void main(String[] args) {
	    	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,24));
	        java.awt.EventQueue.invokeLater(new Runnable() {
	            public void run() {
	            	
	                createAndShowUI();
	            }
	        });
	    }
	
	
}
