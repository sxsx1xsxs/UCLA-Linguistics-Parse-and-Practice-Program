import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GridLayout {
     static public void main(String args[]){
    	 
    	 SwingUtilities.invokeLater(new Runnable()
         {
             public void run()
             {
            	 JFrame frame = new JFrame("Grid Layout Example");
                 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 frame.setVisible(true);
                 frame.setSize(new Dimension (800,800));

                 JPanel pane = new JPanel();                 
                 pane.setLayout(new GridBagLayout());
                 
                 LMenuBar menubar=new LMenuBar();
                 GridBagConstraints c = new GridBagConstraints();
                 c.fill = GridBagConstraints.HORIZONTAL;
                 c.weightx =1;
                 c.weighty=0;
                 c.gridx = 0;
                 c.gridy = 0;
                 //c.gridwidth=3;
                 pane.add(menubar, c);

                 JButton button2 = new JButton("Button 2");
                 GridBagConstraints c2=new GridBagConstraints();
                 c2.fill = GridBagConstraints.BOTH;
                 c2.weightx = 0;
                // c2.weighty=0.2;
                 c2.gridx = 4;
                 c2.gridy = 1;
                 pane.add(button2, c2);

                 JButton button3 = new JButton("Button 3");
                 GridBagConstraints c3=new GridBagConstraints();
                 c3.fill = GridBagConstraints.BOTH;
                 c3.weightx = 1;
                 c3.gridx = 5;
                 c3.gridy = 1;
                 pane.add(button3, c3);

                 JButton button4 = new JButton("Long-Named Button 4");
                 GridBagConstraints c4=new GridBagConstraints();
                 c4.fill = GridBagConstraints.BOTH;
                 c4.ipady = 40;      //make this component tall
                 c4.weightx = 0.0;
                // c4.weighty=0.2;
                 c4.gridwidth = 3;
                 c4.gridx = 0;
                 c4.gridy = 1;
                 pane.add(button4, c4);

                 JButton button5 = new JButton("5");
                 GridBagConstraints c5=new GridBagConstraints();
                 c5.fill = GridBagConstraints.BOTH;
                 c5.ipady = 0;       //reset to default
                 c5.weighty = 1.0;   //request any extra vertical space
                 c5.anchor = GridBagConstraints.PAGE_END; //bottom of space
                 c5.insets = new Insets(0,0,0,0);  //top padding
                 c5.gridx = 1;       //aligned with button 2
                 c5.gridwidth = 2;   //2 columns wide
                 c5.gridy = 2;       //third row
                 pane.add(button5, c5);
                 
                 frame.add(pane);
             }
         });
    	 
         
     }
}
