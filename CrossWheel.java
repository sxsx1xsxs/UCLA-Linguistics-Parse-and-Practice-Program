
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class CrossWheel extends JPanel{
  JLabel b1;
  JLabel b2;
  JLabel b3;
  JLabel b4;
  
	
	public CrossWheel(){
		  super.setLayout(new GridBagLayout());
	      GridBagConstraints c=new GridBagConstraints();
	      c.gridx=0;
	      c.gridy=1;
	      c.gridheight=2;
	      c.insets=new Insets(0,5,0,0);
	      c.anchor=GridBagConstraints.CENTER;
	      b1=new JLabel("-");
	      int x=b1.getPreferredSize().height;
	      b1.setBorder(new LineBorder(Color.black, 2));
	     b1.setHorizontalAlignment(SwingConstants.CENTER);
	     b1.setVerticalAlignment(SwingConstants.CENTER);
	      
	      b1.setSize(x,x);
	      b1.setPreferredSize(new Dimension(x,x));
      
	      super.add(b1, c);
	      c.gridx=1;
	      c.gridy=0;
	      c.gridheight=1;
	      c.gridwidth=2;
	      c.insets=new Insets(5,0,0,0);
	      b2=new JLabel("+");
	      b2.setSize(x,x);
	      b2.setPreferredSize(new Dimension(x,x));
	      b2.setBorder(new LineBorder(Color.black, 2));
		     b2.setHorizontalAlignment(SwingConstants.CENTER);
		     b2.setVerticalAlignment(SwingConstants.CENTER);
	      super.add(b2, c);
	      c.gridx=3;
	      c.gridy=1;
	      c.gridheight=2;
	      c.gridwidth=1;
	      c.insets=new Insets(0,0,0,5);
	      b3=new JLabel("+");
	      b3.setSize(x,x);
	      b3.setPreferredSize(new Dimension(x,x));
	      
	      b3.setBorder(new LineBorder(Color.black, 2));
		     b3.setHorizontalAlignment(SwingConstants.CENTER);
		     b3.setVerticalAlignment(SwingConstants.CENTER);
	      super.add(b3, c);
	      c.gridx=1;
	      c.gridy=3;
	      c.gridwidth=2;
	      c.gridheight=1;
	      c.insets=new Insets(0,0,5,0);
	      b4=new JLabel("-");
	      b4.setSize(x,x);
	      b4.setPreferredSize(new Dimension(x,x));
	      b4.setBorder(new LineBorder(Color.black, 2));
		     b4.setHorizontalAlignment(SwingConstants.CENTER);
		     b4.setVerticalAlignment(SwingConstants.CENTER);
	      super.add(b4, c);
	      JLabel image=new JLabel(); 
	      image.setSize(new Dimension(2*x,2*x));
	      image.setPreferredSize(new Dimension(2*x,2*x));
//	      ImageIcon img = new ImageIcon("1.png");
//	      Image resizedImage = 
//	    		    img.getScaledInstance(image.getWidth(), image.getHeight(), null);
	     // new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/open.gif"))))
	      
	      ImageIcon imageIcon = 
	      new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/1.png")));
	      //new ImageIcon("/images/1.png"); // load the image to a imageIcon
	      Image image1 = imageIcon.getImage(); // transform it 
	      Image newimg = image1.getScaledInstance(image.getSize().height, image.getSize().width,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	      imageIcon = new ImageIcon(newimg);  // transform it back
	      
	      image.setIcon(imageIcon);
	      c.gridx=1;
	      c.gridy=1;
	      c.gridheight=2;
	      c.gridwidth=2;
	      super.add(image, c);
	      
	   //  super.setBackground(Color.red);
	      super.setVisible(true);
	      super.setSize(getPreferredSize());
	}
	
	
	static public void main(String args[]){
		JFrame frame=new JFrame();
		  frame.setLocationRelativeTo(null);
	      frame.setVisible(true);
	      frame.setPreferredSize(new Dimension(800,800));
	      frame.pack();

	      frame.setLayout(new GridBagLayout());
	      frame.add(new CrossWheel());
	      
	      
		
	}
}
