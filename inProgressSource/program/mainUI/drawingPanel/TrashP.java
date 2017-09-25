package program.mainUI.drawingPanel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interactiveAction.InAction;
import interactiveAction.Interactive;

public class TrashP extends JScrollPane implements Interactive{
	public TrashP(){
		JLabel message=new JLabel("trash");
		JPanel trash=new JPanel();
		trash.add(message);
		this.setViewportView(trash);
	}
	
	public static void main(String args[]){
		TrashP trash=new TrashP();
		trash.setSize(trash.getPreferredSize());
		System.out.println("hi");
		
		JFrame frame=new JFrame("Trash");
		frame.setLayout(null);
		frame.setSize(new Dimension(50,50));
		//make sure add top first so it is on the top
		frame.add(trash);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void receiveAction(InAction action) {
		//do nothing
		
	}
}
