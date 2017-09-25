package program.mainUI.interactiveTreeDrawing;

import java.awt.Dimension;

import javax.swing.JFrame;

public class PracticeTreeDrawing extends InteractiveTreeDrawing {

	public PracticeTreeDrawing() throws Exception {
		super();
		practiceMode();
	}

	public void practiceMode() {		
		mlabel.setVisible(true);

		dialogue.setEditable(false);
		sentence.setEditable(false);
		meaning.setEditable(false);
		meaning.setVisible(true);

		sn.setVisible(false);
		savechange.setVisible(false);
		saveasnew.setVisible(false);

		check.setVisible(true);
		next.setVisible(true);

		tlabel.setText("Practice Mode");

		start.setVisible(false);
		sl.setVisible(true);
		clean.setVisible(false);
		cleanExceptSentence.setVisible(true);

	}

	static public void main(String args[]) throws Exception{
		JFrame frame = new JFrame("InteractiveTreeDrawing");
		PracticeTreeDrawing interactiveDrawing=new PracticeTreeDrawing();
		frame.setPreferredSize(new Dimension(1200, 900));
		frame.add(interactiveDrawing);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		interactiveDrawing.drawingPanel.setDividersLocation();
	}


}
