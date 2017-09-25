package program.mainUI.drawingPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import interactiveAction.InAction;
import interactiveAction.Interactive;
import interactiveAction.OutAction;



public class InteractiveCanvas extends JScrollPane implements Interactive{

	public CanvasWithAction back;
	private MouseAdapter mouseAdapter;

	//helper field to connect actions with Top
	//accessed by CanvasWithAction to establish transferring ability from CanvasWithAction to Top
	Top top;

	//--------------------------------------------------------------------------------------------------

	//constructor
	public InteractiveCanvas(Top top) {
		this.top=top;
		back=new CanvasWithAction(this);
		mouseAdapter=new ICMouseAdapter();
		this.setViewportView(back);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);


		//getViewport().setViewPosition(new Point(1000,1000));

	}

	//--------------------------------------------------------------------------------------------------

	private class ICMouseAdapter extends MouseAdapter{
		//the following function have similar contents, they are just send the mouseAction 
		//to CanvasWithAction
		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println("IC:pressed");
			Point back_pos=InteractiveCanvas.this.getViewport().getLocation();
			MouseEvent e_back = new MouseEvent(back, MouseEvent.MOUSE_PRESSED, 0, 0, 
					e.getX()+back_pos.x,e.getY()+back_pos.y, 0, false);
			back.mouseAdapter.doMouseEvent(e_back);

		}

		@Override
		public void mouseDragged(MouseEvent e) {
//			System.out.println("IC:dragged");
			Point back_pos=InteractiveCanvas.this.getViewport().getLocation();
			MouseEvent e_back = new MouseEvent(back, MouseEvent.MOUSE_DRAGGED, 0, 0, 
					e.getX()+back_pos.x,e.getY()+back_pos.y, 0, false);
			back.mouseAdapter.doMouseEvent(e_back);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
//			System.out.println("IC:released");
			Point back_pos=InteractiveCanvas.this.getViewport().getLocation();
			MouseEvent e_back = new MouseEvent(back, MouseEvent.MOUSE_RELEASED, 0, 0, 
					e.getX()+back_pos.x,e.getY()+back_pos.y, 0, false);
			back.mouseAdapter.doMouseEvent(e_back);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			System.out.println("IC:moved");
			Point back_pos=InteractiveCanvas.this.getViewport().getLocation();
			MouseEvent e_back = new MouseEvent(back, MouseEvent.MOUSE_MOVED, 0, 0, 
					e.getX()+back_pos.x,e.getY()+back_pos.y, 0, false);
			back.mouseAdapter.doMouseEvent(e_back);			
		}

	}

	//--------------------------------------------------------------------------------------------------


	
	//--------------------------------------------------------------------------------------------------
	
	//currently assuming only Top or CanvasWithAction will send action to InteractiveCanvas
	@Override
	public void receiveAction(InAction action) {
		
		try {
			OutAction transferToCanvas =new OutAction(action.getObject(),this,back);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	//used in [Top.TopMouseAdapter.mouseReleased]
	//to clean things to restore in CanvasWithAction--if a change of CanvasWithAction is invalid, then restore the original CanvasLabels and Lines
	public void cleanThingsToRestore() {
		back.cleanThingsToResore();
		
	}
	
	//--------------------------------------------------------------------------------------------------
	public static void main(String args[]){
		JFrame frame = new JFrame("InteractiveCanvas");
		frame.setPreferredSize(new Dimension(300, 300));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public void clean() {
		back.clean();
		
	}

	public void inputSentence(String s) {
		back.inputSentence(s, this.getViewport().getViewRect());		
	}

}

