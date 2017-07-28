package program.Interface.Infor_Tree;

import java.awt.Rectangle;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import program.Interface.Interface;
import program.Interface.Drawing_Panel.Plain;



public class IndexNode extends DefaultMutableTreeNode {
	
		String index = "";
		//what is shown in STree
		String name = "";
		// type=sentence
		// type=instruction
		// type=folder
		public String type = "";
		public String structure="";
		String response="";

		// will have content only if the type is instruction
		String instruction = "";
		// the following two will have content only if the type is sentence
		Interface.Tree tree = null;
		
		public int correct=0;

		public IndexNode(String indexInput, String nameInput, String typeInput) {

			super(indexInput + " " + nameInput);
			System.out.println("st" + 21);
			index = indexInput;
			name = nameInput;
			type = typeInput;
		}

		public void updateTree(Interface.Tree treeInput) {

			name = treeInput.sentence;
			super.setUserObject(index + " " + name);
			type = "sentence";
			tree = treeInput;
			
		}

		// the constructor for sentence type node
		public IndexNode(Interface.Tree treeInput) {
			System.out.println("st" + 23);
			name = treeInput.sentence;
			type = "sentence";
			tree = treeInput;
			System.out.println("new tree indexnode created" + treeInput.sentence);
		}

		public void changeIndex(String x) {
			System.out.println("st" + 24);
			index = x;
			super.setUserObject(index + " " + name);
		}

		public void outputTree(final JTextField sentenceString, final JTextField meaning, final Plain plain,
				Rectangle bounds) {
			sentenceString.setText(tree.sentence);
			meaning.setText(tree.meaning);
			tree.setLocation(bounds);
			tree.addInLines();
			plain.showTree(tree);
		}

		public void attachTo(DefaultMutableTreeNode root) {
			// TODO Auto-generated method stub
			///
			System.out.println("st" + 26);
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

			// Vector<String> indexV=new Vector<String>();
			// String[] indexSet=index.split(".");
			// for(String xk:indexSet){
			// indexV.add(xk);
			// }
			// String parentIndex="";
			// for(int i=0;i<indexV.size()-1;i++){
			// parentIndex+=indexV.elementAt(i)+".";
			// System.out.println("here");
			//
			// }

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

		public void updateInstruction(String text) {
			instruction = text;
			System.out.println("instruction is updated to: "+text);
		}

	}
