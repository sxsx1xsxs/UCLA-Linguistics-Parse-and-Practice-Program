import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class Plain extends JLayeredPane{
	JPanel nodes=new JPanel();
	JPanel trash=new JPanel();
	DrawPanel drawroom=new DrawPanel();
	JPanel back=new JPanel();
	JTextArea text=new JTextArea();
	JButton button=new JButton("start");
	JButton button2=new JButton("add element");
	Vector<String> sentence=new Vector<String>();
	Vector<NodeLabel> words=new Vector<NodeLabel> ();
	 Vector<NodeLabel> list= new Vector<NodeLabel>();
	 NodeLabel hello=new NodeLabel();
	 int i=0;
	 Grammar g=new Grammar();
	 LMenuBar menuBar= new LMenuBar();
	 
	 private static final long serialVersionUID = 1L;

     /**
      * Layout each of the components in this JLayeredPane so that they all fill
      * the entire extents of the layered pane -- from (0,0) to (getWidth(), getHeight())
      */
     @Override
     public void doLayout() {
         super.doLayout();
         // Synchronizing on getTreeLock, because I see other layouts doing that.
         // see BorderLayout::layoutContainer(Container)
         synchronized (getTreeLock()) {
             int w = getWidth();
             int h = getHeight();
             for (Component c : getComponents()) {
                 if (getLayer(c) == JLayeredPane.DEFAULT_LAYER) {
                   //  c.setFont(new Font("Serif", Font.PLAIN, (int) (getHeight() * 0.8)));
                     c.setBounds(0, 0, w, h);
                 }
                 
             }
         }
     }


	public Plain(Set<String> set, LMenuBar menu){
		menuBar=menu;
		
	    
        back.setLayout(new GridBagLayout());
        
        
        GridBagConstraints c1=new GridBagConstraints();
        
        c1.fill=GridBagConstraints.HORIZONTAL;
        c1.gridx=0;
        c1.gridy=0;
        c1.gridwidth=2;
       
        back.add(menuBar, c1);
        
        
        back.setSize(new Dimension(800,800));       
        back.setBackground(Color.white);
       // super.setLayout(new BorderLayout());
        super.add(back, JLayeredPane.DEFAULT_LAYER);
        
        
        JPanel labelbutton=new JPanel();
        GridBagConstraints c21=new GridBagConstraints();
        c21.gridx=0;
        c21.gridy=1;
        c21.gridwidth=2;
        c21.fill=GridBagConstraints.HORIZONTAL;
        back.add(labelbutton, c21);
        labelbutton.setLayout(new GridBagLayout());
        
        
        
        JLabel label=new JLabel("Please enter the sentence you wish to draw tree for: ");
        GridBagConstraints c2=new GridBagConstraints();
        c2.gridx=0;
        c2.gridy=0;
        //c2.gridwidth=2;
        c2.weightx=1;
        c2.insets = new Insets(0,10,0,0);
        c2.fill=GridBagConstraints.HORIZONTAL;
        labelbutton.add(label, c2);
        
        
        
        GridBagConstraints c3=new GridBagConstraints();
        c3.gridx=0;
        c3.gridy=3;
       // c3.weightx=0.2;
        c3.weighty=0.4;
        c3.fill=GridBagConstraints.BOTH; 
        nodes.setPreferredSize(new Dimension(200,380));
       // nodes.setLocation(10, 210);
        c3.insets = new Insets(10,10,5	,5); 
        back.add(nodes,c3);
        
        GridBagConstraints c4=new GridBagConstraints();
        c4.gridx=1;
        c4.gridy=0;
       //c4.fill=GridBagConstraints.BOTH; 
        c4.anchor = GridBagConstraints.EAST;
        c4.fill=GridBagConstraints.HORIZONTAL;
       //c4.weightx=1;
        c4.insets = new Insets(0,0,0,5);
        //button.setSize(new Dimension(70,30));
        //button.setLocation(700,70);
        labelbutton.add(button,c4);
        GridBagConstraints c41=new GridBagConstraints();
        c41.gridx=2;
        c41.gridy=0;
       //c4.fill=GridBagConstraints.BOTH; 
       // c4.anchor = GridBagConstraints.EAST;
        c41.fill=GridBagConstraints.HORIZONTAL;
       //c4.weightx=1;
        c41.insets = new Insets(0,0,0,5);
        //button.setSize(new Dimension(70,30));
        //button.setLocation(700,70);
        labelbutton.add(button2,c41);
        
        
        //hello.setVisible(false);
        
     
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	list=new Vector<NodeLabel>();
            	drawroom.removeAll();
            	String x=text.getText();
              
              
               FontMetrics fm = hello.getFontMetrics(hello.getFont());
              
              
               int height=fm.getHeight();
               
               
               
             int heightdraw=(int) (drawroom.getSize().height*0.8);
                   i=0;
             
              
              
               for(String y:x.split(" ")){
            	   i=i+height*2;
            	   int textWidth = fm.stringWidth(y);
            	   NodeLabel add=new NodeLabel();
            	   add.setText(y);
                   add.setOpaque(true);
                   add.setLocation(i,heightdraw);
                   add.setSize(textWidth,height);
                   add.location=add.getLocation();
                   
                   i=i+textWidth;
                   
                  // words.add(add);
                   list.addElement(add);
                   drawroom.add(add);
//                   FontMetrics fm = label.getFontMetrics(label.getFont());
//                   String text = tf.getText();
//                   int textWidth = fm.stringWidth(text);
//                   label.setText("text width for \""+text+"\": " +textWidth);
                   
 
               }
             text.setText("");
             drawroom.repaint();
            }          
         });
        
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String x=text.getText();
              
              
               FontMetrics fm = hello.getFontMetrics(hello.getFont());
              
              
               int height=fm.getHeight();
               
               
               
             int heightdraw=(int) (drawroom.getSize().height*0.2);
                   i=0;
             
              
              
               for(String y:x.split(" ")){
            	   i=i+height*2;
            	   int textWidth = fm.stringWidth(y);
            	   NodeLabel add=new NodeLabel();
            	   add.setText(y);
                   add.setOpaque(true);
                   add.setLocation(i,heightdraw);
                   add.setSize(textWidth,height);
                   add.location=add.getLocation();
                   
                   i=i+textWidth;
                   
                  // words.add(add);
                   list.addElement(add);
                   drawroom.add(add);
//                   FontMetrics fm = label.getFontMetrics(label.getFont());
//                   String text = tf.getText();
//                   int textWidth = fm.stringWidth(text);
//                   label.setText("text width for \""+text+"\": " +textWidth);
                   
 
               }
             text.setText("");
             drawroom.repaint();
            }          
         });
        
        GridBagConstraints c5=new GridBagConstraints();
        c5.gridx=0;
        c5.gridwidth=2;
        c5.gridy=2;
        c5.fill=GridBagConstraints.BOTH; 
        text.setPreferredSize(new Dimension(780,40));
		//text.setLocation(10,100);
		text.setBackground(Color.LIGHT_GRAY);
		back.add(text,c5);	
        
		  GridBagConstraints c6=new GridBagConstraints();
	        c6.gridx=0;
	        c6.gridy=4;
	        c6.weighty=1;
	        c6.fill=GridBagConstraints.BOTH; 
	        c6.insets = new Insets(5,10,10	,5);  //top padding
        trash.setPreferredSize(new Dimension(200,170));
       // trash.setLocation(10, 600);
        JLabel message=new JLabel ("Trash Bin");
        trash.setLayout(new GridBagLayout());
        GridBagConstraints ct=new GridBagConstraints();
        ct.gridx=0;
       // ct.gridwidth=0;
        ct.gridy=0;
        ct.fill=GridBagConstraints.BOTH; 
        ct.anchor=GridBagConstraints.CENTER;
        trash.add(message, ct);
        back.add(trash,c6);
        
//        JTextArea message2=new JTextArea ("Drag here to delete");
//        message2.setEditable(false);
//        message2.setSize(new Dimension(200,100));
//      //  trash.setLayout(new GridBagLayout());
//        GridBagConstraints cm=new GridBagConstraints();
//        ct.gridx=0;
//       // ct.gridwidth=0;
//        cm.gridy=1;
//        cm.fill=GridBagConstraints.BOTH; 
//        cm.weighty=1;
//        cm.anchor=GridBagConstraints.CENTER;
//        trash.add(message2, cm);
       // back.add(trash,c6);
        
        
        //trash.setLayout(null);
      

        
        nodes.setLayout(new FlowLayout());
        
       
        
        GridBagConstraints c7=new GridBagConstraints();
        c7.gridx=1;
        c7.gridy=3;
        c7.gridheight=2;
        c7.weightx=1;
        c7.insets = new Insets(10,5,10	,10); 
        c7.fill=GridBagConstraints.BOTH; 
      
        
        addNodes(set);
        
       // NodeLabel hello=new NodeLabel();
        hello.setText("hello");
        nodes.add(hello);
        hello.setVisible(false);
       
        //JScrollPane scroll=new JScrollPane(drawroom);
       // scroll.setPreferredSize(new Dimension(570,560));
        drawroom.setSize(new Dimension(570,560));
        // drawroom.setLocation(220, 210);
        drawroom.setLayout(null);
        back.add(drawroom,c7);
        
       
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        super.addMouseListener(myMouseAdapter);
        super.addMouseMotionListener(myMouseAdapter);
        
        
	}
	
	public void addlabel(){
		
		for(NodeLabel x: words){
			drawroom.add(x);
		}
		
		
	}
	
	
	
	public void addNodes(Set<String> set){
		for(String x:set){
			SingleNode panel=new SingleNode(x);
			nodes.add(panel);
		}
	};
	 private static void createAndShowUI() {
	        JFrame frame = new JFrame("DragLabelOnLayeredPane");
	        Grammar grammar=new Grammar();
	        Plain layers=new Plain(grammar.set,new LMenuBar());
	        frame.setPreferredSize(new Dimension(800,800));
	       // JPanel holder=new JPanel();
	        
	       // holder.add(layers);
	       // holder.setBackground(Color.white);
	        frame.add(layers,BorderLayout.CENTER);
	        
	       
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        
	    }
	 
	 class DrawPanel extends JPanel{
		 
		
		 Vector<NodeLabel> doublenode=new Vector<NodeLabel>();
		 JLabel start=new JLabel("hihihi");
		 
		

		 public void paint(Graphics g) {

			
			 
	            super.paint(g);
	            
	          

	            if(doublenode.size()==2){
	            	 if(doublenode.elementAt(0).location.y>doublenode.elementAt(1).location.y){
	            		 if(doublenode.elementAt(0).parents.contains(doublenode.elementAt(1))){
	            			 doublenode.elementAt(0).parents.remove(doublenode.elementAt(1));
	            			 doublenode.elementAt(1).children.remove(doublenode.elementAt(0));
	            			 
	            		 }
	            		 else{doublenode.elementAt(0).parents.add(doublenode.elementAt(1));
		                	doublenode.elementAt(1).children.add(doublenode.elementAt(0));}
		                	
		                }else if(doublenode.elementAt(0).location.y<doublenode.elementAt(1).location.y){
		                	
		                	 if(doublenode.elementAt(1).parents.contains(doublenode.elementAt(0))){
		            			 doublenode.elementAt(1).parents.remove(doublenode.elementAt(0));
		            			 doublenode.elementAt(0).children.remove(doublenode.elementAt(1));
		            			 
		            		 }
		                	 else{
		                	doublenode.elementAt(1).parents.add(doublenode.elementAt(0));
		                	doublenode.elementAt(0).children.add(doublenode.elementAt(1));}
		                }
		                
	            	
	            
	            	
	               // g.drawLine(doublenode.elementAt(0).location.x+20, doublenode.elementAt(0).location.y+40, doublenode.elementAt(1).location.x+20, doublenode.elementAt(1).location.y);
	                //error
	               doublenode.elementAt(0).setForeground(Color.black);
	               doublenode.elementAt(1).setForeground(Color.black);
	               
	               
	                doublenode.removeElementAt(1);
	                
		           doublenode.removeElementAt(0);
		           
	            }
	        	for(NodeLabel x:list){
            		if(x.children.size()>0){
            			int widthx=x.getWidth()/2;
            			int heightx=x.getHeight()/2;
            			for(NodeLabel y:x.children){
            				int widthy=y.getWidth()/2;
            				int heighty=y.getHeight()/2;
            				g.drawLine(x.location.x+widthx, x.location.y+heightx, y.location.x+widthy, y.location.y+heighty);
            			}
            		}
            	}
	        	
	            for(NodeLabel x: list){
	            	x.revalidate();
            		x.repaint();
            	}
	            
	            validate();
//	            if(store.size()==1){
//	            	g.setColor(Color.red);
//	            	 g.drawLine(store.elementAt(0).x, store.elementAt(0).y, pointEnd.x, pointEnd.y);
//	            	 pointEnd=null;
//	            	
//	            }
	            
	        }
	 }
	 class NodeLabel extends JLabel{
		 Point location=null;
		 Set<NodeLabel> children=new HashSet<NodeLabel>();
		 Set<NodeLabel> parents=new HashSet<NodeLabel>();
		 
		 
		 
		 
		 
	 }
	 class SingleNode extends JPanel{
		
		 
		 public SingleNode(String s){
			 NodeLabel forever= new NodeLabel();
			 forever.setForeground(Color.red);
			 
			// super.setSize(40,40);
			 forever.setText(s);
			 super.setLayout(new BorderLayout());
			 super.add(forever);
			  super.setBorder(new LineBorder(Color.black, 2));
			

			 refresh();
			 
			 
		 }
		 
		 public NodeLabel refresh(){
			 Component[] components = super.getComponents();
			 NodeLabel label=(NodeLabel) components[0];
			 
			 super.remove(label);
			 
			 
				 NodeLabel forever2=new NodeLabel();
				 forever2.setText(label.getText());
				 forever2.setForeground(Color.blue);
				 forever2.setHorizontalAlignment(SwingConstants.CENTER);
				 forever2.setVerticalAlignment(SwingConstants.CENTER);
				// forever2.setBackground(Color.yellow);
				 forever2.setOpaque(true);
				 super.add(forever2);
				 return label;
			 
			 
		 }
		 
	
		 
		 
	 }
	 
	  private class MyMouseAdapter extends MouseAdapter {
	        private NodeLabel dragLabel = null;
	        private int dragLabelWidthDiv2;
	        private int dragLabelHeightDiv2;
	        private JPanel clickedPanel = null;
	        private SingleNode node=null;
	        int clicked=0;

	        @Override
	        public void mousePressed(MouseEvent me) {
	        	
	        	clickedPanel = (JPanel) back.getComponentAt(me.getPoint());
	        	if(clickedPanel==nodes){
                    node=(SingleNode) nodes.getComponentAt(me.getPoint().x-nodes.getLocation().x, me.getPoint().y-nodes.getLocation().y);
                    if(node==null){
                    	return;
                    }
                    else{
                    	dragLabel=node.refresh();
                        revalidate();
                    	repaint();
                    	 dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
                         dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

                         int x = me.getPoint().x - dragLabelWidthDiv2;
                         int y = me.getPoint().y - dragLabelHeightDiv2;
                         dragLabel.setLocation(x, y);
                         add(dragLabel, JLayeredPane.DRAG_LAYER);
                         repaint();
                         clicked=1;
                    }
                    
                    clickedPanel=null;
                    
	        	}
	        	
	        	if(clickedPanel==drawroom){
	        		dragLabel=(NodeLabel)clickedPanel.getComponentAt(me.getPoint().x-drawroom.getLocation().x, me.getPoint().y-drawroom.getLocation().y);
	        		if(dragLabel==null){
	        			return;
	        		}
	        		else{
	        			clicked=3;
	        			//dragLabel.setForeground(Color.red);
//	        			revalidate();
//	        			repaint();
	        			
	        		}
	        	}
	        	
	        	
	        	
	        }

	        @Override
	        public void mouseDragged(MouseEvent me) {
	        	 if (dragLabel == null) {
	                 return;
	             }
	        	 
	        	 if(clicked==1){
	        		
                     clicked=2;
//                     remove(dragLabel);
//                     add(dragLabel, JLayeredPane.DRAG_LAYER);
                   
	        	 }
	        	 
	        	 if(clicked==3){
	        		 clicked=4;
	        		 dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
                     dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

                     int x = me.getPoint().x - dragLabelWidthDiv2;
                     int y = me.getPoint().y - dragLabelHeightDiv2;
                     dragLabel.setLocation(x, y);
                     drawroom.remove(dragLabel);
                     add(dragLabel, JLayeredPane.DRAG_LAYER);
                     repaint();
	        	 }
	            
	             int x = me.getPoint().x - dragLabelWidthDiv2;
	             int y = me.getPoint().y - dragLabelHeightDiv2;
	             dragLabel.setLocation(x, y);
	             dragLabel.location=new Point(dragLabel.getLocation().x-drawroom.getLocation().x,dragLabel.getLocation().y-drawroom.getLocation().y);
	             for(NodeLabel i:dragLabel.children){
	            	 if(dragLabel.location.y>i.location.y){
	            		 i.parents.remove(dragLabel);
	            		 i.children.add(dragLabel);
	            		 dragLabel.children.remove(i);
	            		 dragLabel.parents.add(i);
	            	 }
	             }
	             for(NodeLabel i:dragLabel.parents){
	            	 if(dragLabel.location.y<i.location.y){
	            		 i.parents.add(dragLabel);
	            		 i.children.remove(dragLabel);
	            		 dragLabel.children.add(i);
	            		 dragLabel.parents.remove(i);
	            	 }
	             }
	             

	             drawroom.repaint();
	             repaint();
	        }

	        @Override
	        public void mouseReleased(MouseEvent me) {
	        	clickedPanel = (JPanel) back.getComponentAt(me.getPoint());
	        	
	        	
	        	

	        	if(clickedPanel==drawroom){
	        		if (dragLabel == null) {
	                    return;
	                }
	        		
	        		if(clicked==2){
	        		dragLabel.setLocation(dragLabel.getLocation().x-drawroom.getLocation().x,dragLabel.getLocation().y-drawroom.getLocation().y);
	        		dragLabel.setOpaque(true);
	        		dragLabel.location=dragLabel.getLocation();
	        		list.add(dragLabel);
	        		
	        		
	        		
	        		
	        	    drawroom.add(dragLabel);
	        	   // dragLabel.setOpaque(true);
	                remove(dragLabel);
	                dragLabel=null;
	               // revalidate();
	                repaint();
	                }
	        		else if(clicked==3){
	        			dragLabel.setForeground(Color.red);
		        	   // drawroom.add(dragLabel);
	        			dragLabel.setOpaque(true);
		        	    drawroom.doublenode.add(dragLabel);
		        	   drawroom.repaint();
		        	   // revalidate();
		        	    repaint();
	        		}else if(clicked==4){

		        		
		        		dragLabel.setLocation(dragLabel.getLocation().x-drawroom.getLocation().x,dragLabel.getLocation().y-drawroom.getLocation().y);
		        		//dragLabel.setOpaque(false);
		        		dragLabel.setOpaque(true);
		        		
		        		dragLabel.location=dragLabel.getLocation();
		        		
		        		
		        		
		        		
		        		
		        	    drawroom.add(dragLabel);
		        	    dragLabel.setOpaque(true);
		        	    
		                remove(dragLabel);
		                dragLabel=null;
		              //  revalidate();
		                drawroom.repaint();
			        	   // revalidate();
			        	    repaint();
		                
		                
	        		}
	        		
	        		clicked=0;
	        	
	        }
	        	else{
	        		
		        		if (dragLabel == null) {
		                    return;
		                }
		        		
		        		if(clicked==4){
		        			for(NodeLabel x:dragLabel.parents){
		        				x.children.remove(dragLabel);
		        			}
		        			for(NodeLabel x:dragLabel.children){
		        				x.parents.remove(dragLabel);
		        			}
		        			list.remove(dragLabel);
		        		}
		                remove(dragLabel);
		              //  revalidate();
		                repaint();
		                clicked=0;
		        	
		        

	        	}

		             int i=0;

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

	    public static void main(String[] args) {
	    	setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.PLAIN,24));
	        java.awt.EventQueue.invokeLater(new Runnable() {
	            public void run() {
	            	
	                createAndShowUI();
	            }
	        });
	    }
}
