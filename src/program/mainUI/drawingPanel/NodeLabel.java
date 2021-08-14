package program.mainUI.drawingPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;


public class NodeLabel extends JPanel implements Comparable<NodeLabel>,Serializable{
		// the main visible label
		public JLabel label = new JLabel();
		
		//??needs to fix!!!
		//when interacting with plain
		transient Plain plain;
		//transient Canvas canvas
		
		//the string to record the grammar error message of that node
		public String grammar_error="";

		
		//???needs to be fixed
		// the location relative to drawroom, useful when the NodeLabel is on
		// the dragLayer
		// when dragging, but the activity is still considered in drawroom and
		// should use the coordinate relative to drawroom
		public Point location = new Point(0, 0);

		public Vector<NodeLabel> children = new Vector<NodeLabel>();
		public Vector<NodeLabel> parents = new Vector<NodeLabel>();

		public Vector<Line> parentlines = new Vector<Line>();
		public Vector<Line> childrenlines = new Vector<Line>();
		public Vector<Line> parentArrows = new Vector<Line>();
		public Vector<Line> childrenArrows = new Vector<Line>();

	int color = 0;
		// color=1, blue, when chosen or mouse hovering
		// color=3, 994C00 orange for ready nodes to be paired up-however, never
		// set to 3 because this
		// color is temporary
		// color=4, red, grammar is not correct
		// color=5, 4C9900, dark green, FOR DOUBLENODES double clicks

		// right-click popup menu for nodes
		transient public JPopupMenu right_click_menu;

		 
		// type recording whether the node is a word in the sentence or a tree node {terminal or non-terminal}
		//when==1 it is a word
		public int type = 0;
		// Similar in principle, used to work on reserved nodes like islands
		// 1 = island
		public int grammarType = 0;

		// modified after calling the function bracketForm()
		// start from 1;
		int levelsUp = 0;
		int levelsDown = 0;

		// modified after calling the function bracketForm()
		public String bracketForm = null;

		// modified by bracketForm
		int standardx = 0; // the x-coordinate
		int yUp = 0; // the y-coordinate when bottom up
		int yDown = 0; // the y-coordinate when top down

		//level up for single smaller tree
		//used in adjustOneTree
		int level = 0;

		Timer timer = null;

		// used in the MouseAdapter to distinguish between when the dragLabel is
		// on drawroom
		// vs the dragLabel is added to the dragLayer
		// the relative coordinate is different for those two
		// if ==0 -on drawroom
		// if ==1 -on dragLayer
		int dragged = 0;
		

		// set label into assigned color according to color property
		// special color is associated with bold font
		public void setColor() {
			if (color == 1) {
				label.setForeground(Color.blue);
				setBold();
			} else if (color == 3) {
				label.setForeground(Color.decode("0x994C00"));
				setBold();
			} else if (color == 4) {
				label.setForeground(Color.red);
				setBold();
			} else if (color == 5) {
				label.setForeground(Color.decode("0x4c9900"));
				setBold();
			} else {
				label.setForeground(Color.black);
				setPlain();
			}
		}

		// set label into Plain instead of boldfont
		public void setPlain() {
//			Font font = label.getFont();
//			Font plainFont = new Font(font.getFontName(), Font.PLAIN, font.getSize());
//			label.setFont(plainFont);
		}

		// set label into Bold instead of Plain
		public void setBold() {
//			Font font = label.getFont();
//			Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
//			label.setFont(boldFont);
		}


		//return null if wellformed_and_complete
		//else return the wrong part, which is the message displayed in the info window
		public String wellFormed_and_complete(){
			//check whether wellFormed by sending the information to Grammar.obeyRule(),
			//if not well_formed, write the message and return
			
			
			//if well_formed, check whether complete
			//for words NodeLabel, it must have one and only one terminal parent
			return null;
		}
		
		// returns 0 if the label is not in range including inside the label or
		// outside the outer border
		// returns a integer number larger than 0 for the distance from the
		// point to the label
		// passing in the point location relative to drawroom

		public int distanceToLabel(Point p) {
			// the left corners x-cor for inner layer
			int xi1 = super.getLocation().x + label.getLocation().x;
			// the right corners x-cor for inner layer
			int xi2 = super.getLocation().x + label.getLocation().x + label.getWidth();
			// the up corners y-cor for inner layer
			int yi1 = super.getLocation().y + label.getLocation().y;
			// the lower corners y-cor for inner layer
			int yi2 = super.getLocation().y + label.getLocation().y + label.getHeight();

			// the left corners x-cor for outlayer
			int xo1 = xi1 - label.getHeight();
			// the right corners x-cor for outlayer
			int xo2 = xi2 + label.getHeight();
			// the up corners y-cor for outlayer
			int yo1 = yi1 - label.getHeight();
			// the lower corners y-cor for outlayer
			int yo2 = yi2 + label.getHeight();

			int x = p.x;
			int y = p.y;

			if (x <= xo2 && x >= xo1 && y > yo1 && y < yo2) {
				if (!(x <= xi2 && x >= xi1 && y >= yi1 && y <= yi2)) {
					if (x <= xi2 && x >= xi1) {
						if (y < yi1) {
							return yi1 - y;
						} else {
							return y - yi2;
						}
					} else if (y >= yi1 && y <= yi2) {
						if (x < xi1) {
							return xi1 - x;
						} else {
							return x - xi2;
						}
					} else if (x < xi1 && x >= xo1) {
						if (y < yi1) {
							if (yi1 - y > xi1 - x) {
								return yi1 - y;
							} else {
								return xi1 - x;
							}
						} else {
							if (y - yi2 > xi1 - x) {
								return y - yi2;
							} else {
								return xi1 - x;
							}
						}
					} else {
						if (y < yi1) {
							if (yi1 - y > x - xi2) {
								return yi1 - y;
							} else {
								return x - xi2;
							}
						} else {
							if (y - yi2 > x - xi2) {
								return y - yi2;
							} else {
								return x - xi2;
							}
						}
					}

				} else {
					return -1;
				}
			}

			return 0;

		}
		
		public void update(){
			// used to trigger island resizes on being dragged
			// call after position update or resize
			for(NodeLabel parent : parents){
				if(!children.contains(parent) && parent!=this) {
					parent.update();
				}
			}
		}
		
		public void attachTo(Plain p){
			plain=p;
		}
		
		
		public NodeLabel(String x, Plain p) {
			super.setOpaque(false);
			super.setLayout(new GridBagLayout());
			label.setText(x);
			Dimension labelsize = label.getPreferredSize();
			plain=p;

			super.add(label);
			super.setPreferredSize(new Dimension((int) (labelsize.getWidth() + labelsize.getHeight()),
					(int) (labelsize.getHeight() * 2)));

			label.addMouseListener(new ML());

			label.addMouseMotionListener(new ML());
			label.setSize(labelsize);
			label.setLocation(labelsize.height/2, labelsize.height/2);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);

			JPanel changeto = new JPanel();
			changeto.setBackground(Color.white);
			changeto.setLayout(new GridBagLayout());
			GridBagConstraints ctc = new GridBagConstraints();
			ctc.gridx = 0;
			ctc.gridy = 0;
			ctc.gridwidth = 2;
			ctc.insets = new Insets(0, 20, 0, 0);
			JLabel ch = new JLabel("Change Node to:");
			ctc.anchor = GridBagConstraints.WEST;
			changeto.add(ch, ctc);

			final JTextField item = new JTextField();
			item.setPreferredSize(new Dimension(item.getPreferredSize().height * 4, item.getPreferredSize().height));

			ctc.gridx = 0;
			ctc.gridy = 1;
			ctc.gridwidth = 1;
			ctc.weightx = 1;
			changeto.add(item, ctc);
			ctc.insets = new Insets(0, 0, 0, 0);
			ctc.gridx = 1;
			ctc.weightx = 0;
			
			
			right_click_menu = new JPopupMenu();
			final JButton change_to = new JButton("GO");
			changeto.add(change_to, ctc);

			change_to.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					label.setText(item.getText());
					Dimension labelsize = label.getPreferredSize();
					setSize(new Dimension((int) (labelsize.getWidth() + labelsize.getHeight()),
							(int) (labelsize.getHeight() * 2)));
					repaint();
				}
			});
			JMenuItem delete;
			delete = new JMenuItem("Delete");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Vector<NodeLabel> pp = new Vector<NodeLabel>();
					for (NodeLabel xx : parents) {
						pp.add(xx);
					}
					for (NodeLabel xii : pp) {
						plain.removelines(NodeLabel.this, xii);
					}
					Vector<NodeLabel> cc = new Vector<NodeLabel>();
					for (NodeLabel xx : NodeLabel.this.children) {
						cc.add(xx);
					}
					for (NodeLabel x : cc) {
						plain.removelines(NodeLabel.this, x);
					}

					for (Line ll : NodeLabel.this.childrenlines) {
						plain.linelist.remove(ll);

					}
					for (Line ll : NodeLabel.this.parentlines) {
						plain.linelist.remove(ll);

					}

					plain.drawroom.canvas.remove(NodeLabel.this);
					plain.list.remove(NodeLabel.this);
					plain.removeNode(NodeLabel.this);
					plain.drawroom.repaint();
					// repaint();

				}
			});
			right_click_menu.add(delete);

			JMenu item3 = new JMenu("Change To");
			right_click_menu.add(item3);
			item3.add(changeto);
			item.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						change_to.doClick();
					}
				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});

			label.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_1) {

						plain.drawroom.delete.doClick();
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

			});
			// menu.add(changeto);
		}
		public NodeLabel makeBasicCopy(Plain p){
			return new NodeLabel(getLabelName(),p);
		}
		// get the name used in the bracket form used to store things
		public String getLabelName(){
			return label.getText();
		}
		//helpfer function of crossingLine
		//given a nodeLabel and a line,
		//return true if the two ends of line are both outside of the dragLabel's label border
		//not including touching the border, must cross out the border
		//return false otherwise
		private boolean twoPointsOut(NodeLabel nl, Line l){
			//store two ends of l into a set for later convenience
			Vector<Point> p=new Vector<Point>();
			p.add(l.start);
			p.add(l.end);
			
			//border cor value of dragLabel nl's label
			int nx1=nl.location.x+nl.label.getLocation().x;
			int nx2=nx1+nl.label.getWidth();
			int ny1=nl.location.y+nl.label.getLocation().y;
			int ny2=ny1+nl.label.getHeight();
			
			for(Point i:p){
				int x=i.x;
				int y=i.y;
				//if any of the end is within the label's border
				if(((x<=nx1&&x>=nx2)||(x>=nx1&&x<=nx2))&&((y<=ny1&&x>=ny2)||(y>=ny1&&y<=ny2))){
					return false;
				}
				
			}
			return true;
		}

		//helper function for addinline
		//return true if a nodeLabel's label crosses a line segment
		//necessary requirement, both ends of the line is beyond the borders
		//include the line is overlapping with one of the border and covers the whole border and 
		//both ends go beyond the border
		//including the line only touch the borders with one point being the corner point
		//return false if not
		public boolean crosslingLine(Line x){
			
			//get the cor for both ends of line x
			int x1=x.start.x;
			int y1=x.start.y;
			int x2=x.end.x;
			int y2=x.end.y;
			
			//get the cor for the borders of the label
			int lx1=location.x+label.getLocation().x;
			int lx2=lx1+label.getWidth();
			int ly1=location.y+label.getLocation().y;
			int ly2=ly1+label.getHeight();
			
			
			//the necessary requirement of two ends goes beyond the borders is satisfied
			if(twoPointsOut(this,x)){
				if(x1==x2){
					//if the line is vertical and overlap with one border and goes beyond the border
					if(x1>=lx1&&x1<=lx2&&ly2<y1&&ly1>y2){
						return true;
					}
				}
				else if(y1==y2){
					//if the line is horizontal and overlap with one border and goes beyond the border
					//this is nearly inpossible in real-time use,
					//so the bug leaves unfixed
					//if the line is horizontal and not overlap with one border, will not return true
					if(y1>=ly1&&y1<=ly2){
						return true;
					}
				}
				else{
					
					//tangent of the line and c of the line
					//the line's formula is y=tx+c;
					double t=(double)(y2-y1)/(x2-x1);
					double c=y1-t*x1;

					
					//get the x-y cor for intersection point
					double ynow1=t*lx1+c;
					double xnow1=(ly1-c)/t;
					double ynow2=t*lx2+c;
					double xnow2=(ly2-c)/t;
					//if the line and the borders touch even in one point


					if((ynow1<=ly2&&ynow1>=ly1&&((lx1<=x1&&lx1>=x2)||(lx1>=x1&&lx1<=x2)))
							||(xnow1<=lx2&&xnow1>=lx1&&((ly1<=y1&&ly2>=y2)||(ly1>=y1&&ly2<=y2)))
							||(ynow2<=ly2&&ynow2>=ly1&&((lx2<=x1&&lx2>=x2)||(lx1>=x1&&lx2<=x2)))
							||(xnow2<=lx2&&xnow2>=lx1&&((ly2<=y1&&ly2>=y2)||(ly2>=y1&&ly2<=y2)))){
						return true;
					}
					
				}
				
				
			}
			return false;
		}
		void brownTwinkle() {
			Font font = label.getFont();
			Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
			setFont(boldFont);
			//setSize(super.getPreferredSize());
			timer = new Timer(400, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(!isVisible());
					repaint();
				}
			});

			timer.setRepeats(true);
			timer.setCoalesce(true);

			timer.start();

		}

		void stop() {
			class IntSingleton {
				/* This replaces the Int class, and serves as a temporary way to hold a global var for the timer event
				   I would prefer to not do this, but I'm a little concerned about how much code relies on being able
				   to stop this timer. As a result, I compromised.*/
				public int number = 0;
			}
			final IntSingleton i = new IntSingleton();
			timer.stop();
			timer = new Timer(200, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					i.number++;

					if (i.number == 1 || i.number == 2) {
						label.setForeground(Color.decode("0x4c9900"));
						setBold();

					} else if (i.number == 3) {
						setColor();
					} else {
						timer.stop();
					}

					repaint();
				}
			});

			timer.setRepeats(true);
			timer.setCoalesce(true);

			setVisible(true);
			timer.start();
			Font font = label.getFont();
			setFont(font);
		}
		public Point lowerMid(){
			return new Point(location.x + getWidth()/2, location.y + getHeight()- label.getLocation().y);
		}
		public Point upperMid(){
			return new Point(location.x + getWidth()/2, location.y + label.getLocation().y);
		}
	public Point upperLeftCorner(){
			// returns the upper left corner of a theoretical island around the node and its children
			int x = getX();
			int y = getY();
			if(!getParent().equals(plain.drawroom.canvas)){
				x=location.x;
				y=location.y;
			}
			for(NodeLabel child : children){
				Point loc = child.upperLeftCorner();
				if(loc.x < x){x = loc.x;}
				if(loc.y < y){y = loc.y;}
			}
			return new Point(x,y);
		}
		public Point lowerRightCorner(){
			// returns the lower right corner of a theoretical island around the node and its children
			int x = getX() + getWidth();
			int y = getY() + getHeight();
			if(!getParent().equals(plain.drawroom.canvas)){
				x=location.x + getWidth();
				y=location.y + getHeight();
			}
			for(NodeLabel child : children){
				Point loc = child.lowerRightCorner();
				if(loc.x > x){x = loc.x;}
				if(loc.y > y){y = loc.y;}
			}
			return new Point(x,y);
		}

	public class ML extends MouseAdapter {

			@Override
			public void mouseExited(MouseEvent me) {
				label.setBorder(null);
				plain.drawroom.repaint();
				Point zz = me.getPoint();
				Point hh = label.getLocation();
				Point xx = NodeLabel.super.getLocation();
				Point yy = plain.drawroom.getLocation();
				MouseEvent e = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0, xx.x + yy.x + zz.x + hh.x,
						xx.y + yy.y + zz.y + hh.y, 0, false);

			}

			@Override
			public void mouseMoved(MouseEvent me) {
				Point zz = me.getPoint();
				Point xx = location;
				Point hh = label.getLocation();
				Point yy = plain.drawroom.canvas.getLocation();
				Point tt= plain.getLocation(2);

				MouseEvent e = new MouseEvent(plain, MouseEvent.MOUSE_MOVED, 0, 0, tt.x+xx.x + yy.x + zz.x + hh.x,
							xx.y + yy.y + zz.y + hh.y+tt.y, 0, false);
				plain.myMouseAdapter.mouseMoved(e);
				plain.info.setText("Node " + label.getText() + " is ready to be dragged or chosen");
			}

			@Override
			public void mouseEntered(MouseEvent me)

			{
				label.setForeground(Color.blue);
				label.setBorder(new LineBorder(Color.black, 2));
				//label.setSize(getPreferredSize());
				//plain.drawroom.repaint();
				plain.info.setText("Node "+ label.getText() + " is ready to be dragged or chosen");
				plain.info.revalidate();
				plain.info.repaint();
				repaint();

			}

			@Override
			public void mousePressed(MouseEvent me)

			{
				if (me.getButton() == MouseEvent.BUTTON1) {
					Point zz = me.getPoint();
					Point hh = label.getLocation();
					Point xx = location;
					Point yy = plain.drawroom.canvas.getLocation();
					Point tt= plain.getLocation(2);
					MouseEvent e = new MouseEvent(plain, 0, 0, 0, xx.x + yy.x + zz.x + hh.x+tt.x,
							xx.y + yy.y + zz.y + hh.y+tt.y, 1, false);
					plain.myMouseAdapter.mousePressed(e);

				} else if (me.getButton() == MouseEvent.BUTTON3) {
					right_click_menu.show(me.getComponent(), me.getX(), me.getY());
				}

			}

			@Override
			public void mouseDragged(MouseEvent me)

			{
				Point zz = me.getPoint();
				Point xx = location;
				Point hh = label.getLocation();
				Point yy = plain.drawroom.canvas.getLocation();
				Point tt= plain.getLocation(2);
					MouseEvent e = new MouseEvent(plain, MouseEvent.MOUSE_DRAGGED, 0, 0,
							xx.x + zz.x + yy.x + hh.x+tt.x, xx.y + zz.y + yy.y + hh.y+tt.y, 0, false);
					plain.myMouseAdapter.mouseDragged(e);

				dragged = 1;
			}

			@Override
			public void mouseReleased(MouseEvent me)

			{
				Point zz = me.getPoint();
				Point xx = location;
				Point hh = label.getLocation();
				Point yy = plain.drawroom.canvas.getLocation();
				Point tt= plain.getLocation(2);

					MouseEvent e = new MouseEvent(plain, MouseEvent.MOUSE_RELEASED, 0, 0,
							tt.x+xx.x + zz.x + yy.x + hh.x, xx.y + zz.y + yy.y + hh.y+tt.y, 0, false);
					plain.myMouseAdapter.mouseReleased(e);
					dragged = 0;
			}

		}

		@Override
		public int compareTo(NodeLabel o) {
			int midxo = o.location.x + o.getWidth() / 2;
			int midx = location.x + getWidth() / 2;

			// ascending order
			// return midx - midxo;

			// descending order
			return midxo - midx;
		}

	}