package program.mainUI.InforTree.IndexNode;

import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;


public class IndexNode extends DefaultMutableTreeNode {

	public String index = "";
	String name = "";
	public int correct;

	public IndexNode(String indexInput, String nameInput, String typeInput) {
		super(indexInput + " " + nameInput);
		index = indexInput;
		name = nameInput;
	}

	public void changeIndex(String x) {
		index = x;
		super.setUserObject(index + " " + name);
	}


	public void attachTo(DefaultMutableTreeNode root) {
		IndexNode now = null;

		// get all descendents of the root
		Vector<IndexNode> children = new Vector<IndexNode>();
		Stack<IndexNode> childrenstack = new Stack<IndexNode>();
		for (int i = 0; i < root.getChildCount(); i++) {
			IndexNode xc = (IndexNode) root.getChildAt(i);
			childrenstack.add(xc);
		}

		while (childrenstack.size() > 0) {
			IndexNode nowc = childrenstack.pop();
			children.add(nowc);
			for (int i = 0; i < nowc.getChildCount(); i++) {
				IndexNode xc = (IndexNode) nowc.getChildAt(i);
				childrenstack.add(xc);
			}
		}

		String parentIndex = null;
		Pattern p = Pattern.compile("(.*)(\\d+.)$");
		Matcher m = p.matcher(index);
		if (m.find()) {
			parentIndex = m.group(1);
		}


		IndexNode parent = null;
		for (IndexNode ji : children) {
			if (ji.index.equals(parentIndex)) {
				parent = ji;
				break;
			}
		}
		if (parent != null) {
			parent.add(this);
		} else {
			root.add(this);
		}
	}



}
