package program.mainUI.drawingPanel;

import javax.swing.border.LineBorder;
import java.awt.*;

public class Island extends NodeLabel{
    static int horiPadding = 10;
    static int vertPadding = 10;
    public Island(Plain p) {
        super("Island", p);
        setSize(50,50);
        setBorder(new LineBorder(Color.red,2,true));
        right_click_menu.remove(1);
        setLayout(null);
        label.setLocation(0,0);
        label.setOpaque(true);
        label.setSize(horiPadding, vertPadding);
        grammarType = 1;
    }
    private void resize(){
        if(children.isEmpty()){
            setSize(50,50);
            return;
        }
        // Otherwise, we need to take the lower nodes name for grammar calculations. We just use the first one since
        // there should only be one anyways
        label.setText(children.get(0).label.getText());
        Point newLocation = upperLeftCorner();
        Point newRightCorner = lowerRightCorner();
        int width = newRightCorner.x - newLocation.x;
        int height = newRightCorner.y - newLocation.y ;
        setBounds(newLocation.x,newLocation.y ,width,height);
        label.setLocation(0,0);
        if(children.size()==1 && parents.size()==1){
            // if we have exactly one child, we should try to make it look like the lines go through the island
            // to the node below it. To do this, we have to make the lines overlap.
            Point upperMid = upperMid();
            Point lowerMid = lowerMid();
            for(Line child : childrenlines){
                child.end = lowerMid;
            }
            for(Line parent : parentlines){
                parent.start = upperMid;
            }
        } else {
            int pointLocation = newLocation.x + width/2;
            for(Line child : childrenlines){
                child.end = new Point(pointLocation,
                        newLocation.y);
            }
            for(Line parent : parentlines){
                parent.start = new Point(pointLocation,
                        newLocation.y);
            }
        }

        // Put the island last in the component order, ensuring the nodelabels can always be clicked on
        int count = plain.drawroom.canvas.getComponentCount();
        plain.drawroom.canvas.setComponentZOrder(this,count-1);
        location = getLocation();
    }
    @Override
    public Point lowerMid(){
        if(parents.isEmpty()){
            return new Point(location.x + getWidth()/2,location.y);
        }
        return parents.get(0).lowerMid();
    }
    @Override
    public Point upperMid(){
        if(children.isEmpty()){
            return super.upperMid();
        }
        return children.get(0).upperMid();
    }
    @Override
    public Point upperLeftCorner(){
        // returns the upper left corner of a theorhetical island around the node and its children
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        if(children.isEmpty()){
            Point loc = getLocation();
            return loc;
        }
        for(NodeLabel child : children){
            Point loc = child.upperLeftCorner();
            if(loc.x < x){x = loc.x;}
            if(loc.y < y){y = loc.y;}
        }
        return new Point(x- horiPadding,y- vertPadding);
    }
    @Override
    public Point lowerRightCorner(){
        // returns the lowerRight corner of a theorhetical island around the node and its children
        int x = 0;
        int y = 0;
        if(children.isEmpty()){
            Point loc = getLocation();
            loc.translate(50,50);
            return loc;
        }
        for(NodeLabel child : children){
            Point loc = child.lowerRightCorner();
            if(loc.x > x){x = loc.x;}
            if(loc.y > y){y = loc.y;} //we want to get a tight grip around things, so we vaccum seal
        }
        return new Point(x+ horiPadding,y+ vertPadding);
    }
    @Override
    public void setColor(){
        super.setColor();
        label.setBackground(label.getForeground()); // update the bg to be the same as the fg to get the blocky square button look
    }
    @Override
    public void update(){
        resize();
        super.update();
    }
    @Override
    public String getLabelName(){
        return "__Island"; //return a reserved name for the bracket form so we know to create an island!
    }
    @Override
    public NodeLabel makeBasicCopy(Plain p){
        return new Island(p);
    }
}
