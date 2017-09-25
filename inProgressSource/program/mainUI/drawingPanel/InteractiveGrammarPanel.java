package program.mainUI.drawingPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import interactiveAction.InAction;
import interactiveAction.Interactive;
import interactiveAction.OutAction;
import program.grammar.Grammar;
import program.mainUI.drawingPanel.grammarPanel.GrammarPanel;
import program.mainUI.drawingPanel.grammarPanel.RuleFormPanel;
import program.mainUI.drawingPanel.grammarPanel.SingleColumnPanel;
import program.mainUI.drawingPanel.topComponents.TopLabel;
import program.mainUI.drawingPanel.topComponents.TopMovable;



//this class is the JScrollPane that has GrammarPanel in it
//the JScrolllPane has the mouseListener connecting it to the outside world
//the outside world is a **transparent** larger JPanel **on top** of the JScrollPane
public class InteractiveGrammarPanel extends JScrollPane implements Interactive{

	private GrammarPanel back;
	private MouseAdapter mouseAdapter;
	
	//helper field to connect actions with Top
	private Top top;

	//--------------------------------------------------------------------------------------------------
	
	private class GPSMouseAdapter extends MouseAdapter{
		//helper attribute 
		//the nodeLabel mousePressed() get
		private JLabel label_got;
		//helper int to indicate whether the node is dragged
		private int dragged=0;
				
		@Override
		public void mousePressed(MouseEvent e) {
			//the mouse position on the JScrollPane
			Point pos_scrollable = e.getPoint();
			//the mouse position on the back JPanel
			Point pos_panel = new Point(pos_scrollable.x - back.getLocation().x,
					pos_scrollable.y - back.getLocation().y);
			label_got=back.getTheLabel(pos_panel.x, pos_panel.y);
			//if get a movable LLabel
			if(label_got!=null){
				try {
					Point label_location=new Point(pos_scrollable.x-label_got.getSize().width/2,pos_scrollable.y-label_got.getSize().height/2);
					TopLabel topLabel=new TopLabel(label_got.getText());
					Vector<TopMovable>outSet=new Vector<TopMovable>();
					topLabel.setLocation(label_location);
					outSet.add(topLabel);					
					OutAction action=new OutAction(outSet,InteractiveGrammarPanel.this, top);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(label_got!=null){
				//used to tell mouseReleased that the node has been dragged
				//otherwise it is simply clicked, which is another situation
				dragged=1;
				//for the MOUSE_DRAGGED event, the source will always be where it is generated
				//in this case, it is ConnectedGrammarPanelScrollable object
				//so every time a drag event happens, send that event to top
				//top is not listening in this case but actually doing action
				
				//grammarPanel's position on the screen
				Point gp_pos=InteractiveGrammarPanel.this.getLocationOnScreen();
				//top's position on the screen
				Point top_pos=top.getLocationOnScreen();
				//mouseEvent on top created
				MouseEvent e_top = new MouseEvent(top, MouseEvent.MOUSE_DRAGGED, 0, 0, 
						e.getX()+gp_pos.x-top_pos.x,e.getY()+gp_pos.y-top_pos.y, 0, false);
				//mimic that event happens on top
				top.mouseAdapter.doMouseEvent(e_top);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(label_got!=null){
				//if dragged
				if(dragged==1){
				Point gp_pos=InteractiveGrammarPanel.this.getLocationOnScreen();
				Point top_pos=top.getLocationOnScreen();
				MouseEvent e_top = new MouseEvent(top, MouseEvent.MOUSE_RELEASED, 0, 0, 
					e.getX()+gp_pos.x-top_pos.x,e.getY()+gp_pos.y-top_pos.y, 0, false);
				top.mouseAdapter.doMouseEvent(e_top);
				dragged=0;
				}
				//if clicked
				else{
					//enable the mouseListener on top because top is covering the grammarPanel,
					//so the grammarPanel will lose the mouseListener
					//since the action is mouseReleased, so the next mouseEvent will be generated on Top
					//instead of grammarPanel
					top.enableMouseListener();					
				}
				//in the end, reset the label_got to null because one mouseEvent is complete
				label_got=null;
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {

			
		}
	
	}
	
	//--------------------------------------------------------------------------------------------------

	//constructor
	public InteractiveGrammarPanel(Grammar grammar, Top top, String type) throws Exception{
		if(type.equals("RuleFormPanel")){
			back=new RuleFormPanel(grammar);
		}else if(type.equals("SingleColumnPanel")){
			back=new SingleColumnPanel(grammar);
		}else {
			throw new Exception("Type of GrammarPanel not defined");
		}
		mouseAdapter=new GPSMouseAdapter();
		this.setViewportView(back);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.top=top;
	}
	
	//--------------------------------------------------------------------------------------------------
	
	
	//--------------------------------------------------------------------------------------------------
		
	//test on the interaction between Top and ConnectedGrammarPanelScrollable
	public static void main(String args[]) throws Exception{
		//Top must be set to be on top of ConnectedGrammarPanelScrollable
		//Top must be set to be transparent
		Top top=new Top();
		top.setSize(new Dimension(900,900));
		top.setOpaque(false);
		top.setBorder(new LineBorder(Color.black,2));

		
		InteractiveGrammarPanel scrollable=new InteractiveGrammarPanel(new Grammar(),top,"SingleColumnPanel");
		scrollable.setSize(new Dimension(300,300));
		scrollable.setLocation(100,100);
		
		InteractiveGrammarPanel s2=new InteractiveGrammarPanel(new Grammar(),top,"RuleFormPanel");
		s2.setSize(new Dimension(300,400));
		s2.setLocation(400,100);
		

		JFrame frame=new JFrame("Grammar_Rule_Panel");
		frame.setLayout(null);
		frame.setSize(new Dimension(1000,1000));
		//make sure add top first so it is on the top
		frame.add(top);
		frame.add(scrollable);
		frame.add(s2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void receiveAction(InAction action) {
		// TODO Auto-generated method stub
		
	}
	
}
