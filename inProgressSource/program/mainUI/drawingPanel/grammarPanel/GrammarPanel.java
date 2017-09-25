package program.mainUI.drawingPanel.grammarPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import program.grammar.Grammar;

public abstract class GrammarPanel extends JPanel{
	private Grammar grammar;

    public abstract JLabel getTheLabel(int x, int y);
	public abstract void updateGrammar();
}
