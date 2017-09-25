package program.mainUI.interactiveTreeDrawing;

import java.awt.Dimension;

import javax.swing.JFrame;

public class FreeTreeDrawing extends InteractiveTreeDrawing{

	public FreeTreeDrawing() throws Exception {
		super();
		freedrawingMode();
	}

	public void freedrawingMode(){

		dialogue.setEditable(false);
		sentence.setEditable(true);
		meaning.setVisible(false);

		sn.setVisible(false);
		savechange.setVisible(false);
		saveasnew.setVisible(false);

		check.setVisible(false);
		next.setVisible(false);

		tlabel.setText("Tree Drawing");
		mlabel.setVisible(false);
		dialogue.setText("");


		start.setVisible(true);
		sl.setVisible(true);
		clean.setVisible(true);
		cleanExceptSentence.setVisible(false);	

	}

	static public void main(String args[]) throws Exception{
		JFrame frame = new JFrame("InteractiveTreeDrawing");
		FreeTreeDrawing interactiveDrawing=new FreeTreeDrawing();
		frame.setPreferredSize(new Dimension(1200, 900));
		frame.add(interactiveDrawing);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		interactiveDrawing.drawingPanel.setDividersLocation();
	}
}
