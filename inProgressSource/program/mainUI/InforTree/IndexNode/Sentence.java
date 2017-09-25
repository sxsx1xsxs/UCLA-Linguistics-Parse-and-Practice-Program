package program.mainUI.InforTree.IndexNode;

import java.awt.Rectangle;

import javax.swing.JTextField;

import program.Interface.Interface;
import program.Interface.Drawing_Panel.Plain;

public class Sentence extends IndexNode{
	public String structure="";
	public String response="";
	public int correct=0;
	
	Interface.Tree tree = null;
	
	public void updateTree(Interface.Tree treeInput) {

		name = treeInput.sentence;
		super.setUserObject(index + " " + name);
		type = "sentence";
		tree = treeInput;

	}
	
	// the constructor for sentence type node
	public IndexNode(Interface.Tree treeInput) {
		name = treeInput.sentence;
		type = "sentence";
		tree = treeInput;
	}
	
	public void outputTree(final JTextField sentenceString, final JTextField meaning, final Plain plain,
			Rectangle bounds) {
		sentenceString.setText(tree.sentence);
		meaning.setText(tree.meaning);
		tree.setLocation(bounds);
		tree.addInLines();
		plain.showTree(tree);
	}
}
