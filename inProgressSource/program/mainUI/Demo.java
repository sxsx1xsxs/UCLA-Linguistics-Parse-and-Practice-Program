package program.mainUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Demo extends JFrame{
	
	int index=0;
	Vector<JLabel> labels=new Vector<JLabel>();
	JPanel panel=new JPanel();
	
	private JLabel getLabel(String name, int width){
		
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/"+name));// load the image to a imageIcon
		int x=imageIcon.getIconWidth();
		int y=imageIcon.getIconHeight();		
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(width, width*y/x,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);
		JLabel picLabel = new JLabel(imageIcon);		
		return picLabel;
	}

	public Demo(){
		
		

		super.setTitle("Demo");
		super.setPreferredSize(new Dimension(800,800));
		int width=700;
		labels.add(getLabel("1.png",width));
		labels.add(getLabel("2.png",width));
		labels.add(getLabel("3.png",width));
		labels.add(getLabel("4.png",width));
		labels.add(getLabel("5.png",width));
		labels.add(getLabel("6.png",width));
		labels.add(getLabel("7.png",width));
		labels.add(getLabel("8.png",width));
		labels.add(getLabel("9.png",width));
		labels.add(getLabel("10.png",width));
		
		
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=3;
		
		final JPanel imageP=new JPanel();
		imageP.add(labels.get(0));
		panel.add(imageP,c);
		
		final JButton previous =new JButton ("Previous");
		previous.setEnabled(false);
		c.gridx=0;
		c.gridy=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.gridwidth=1;
		panel.add(previous, c);
		
		final JButton next=new JButton("Next");
		c.gridx=1;
		c.gridy=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.gridwidth=1;
		panel.add(next, c);
		
		final JButton exit=new JButton("Exit");
		c.gridx=2;
		panel.add(exit, c);
		
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Demo.this.dispose();
			}
			
		});
		
		next.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(index<9){
				previous.setEnabled(true);
				imageP.remove(labels.get(index));
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=3;
				index=index+1;
				imageP.add(labels.get(index),c);
				panel.revalidate();
				if(index==9){
					next.setEnabled(false);
				}

				
				}
				
			}
			
		});
		
		previous.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(index>0){
				next.setEnabled(true);
				imageP.remove(labels.get(index));
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=3;
				index=index-1;
				imageP.add(labels.get(index),c);
				panel.revalidate();
				if(index==0){
					previous.setEnabled(false);
				}

				
				}
				
			}
		});
		
		
		
		super.add(panel);
		//super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
	}
	
	
	public static void main(String args[]){
		Demo demo=new Demo();
	}
}
