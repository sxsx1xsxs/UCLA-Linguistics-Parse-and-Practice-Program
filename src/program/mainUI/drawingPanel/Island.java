package program.mainUI.drawingPanel;

import javax.swing.*;
import java.awt.*;

public class Island extends NodeLabel{
    static int leftPadding = 10;
    static int topPadding = 10;
    public Island(String x, Plain p) {
        super("Island", p);
        setBorder(BorderFactory.createLineBorder(Color.red,2));
        setSize(50,50);
    }
    public Island(Plain p) {
        super("Island", p);
        setSize(50,50);
        setBorder(BorderFactory.createLineBorder(Color.red,2));

    }
    private void resize(){
        if(children.isEmpty()){
            return;
        }
        Point newLocation = upperLeftCorner();
        Point newRightCorner = lowerRightCorner();
        int width = newRightCorner.x - newLocation.x;
        int height = newRightCorner.y - newLocation.y ;
        Point drawroomLocation = plain.drawroom.getLocation();
        setBounds(newLocation.x,newLocation.y ,width,height);
        label.setSize(label.getPreferredSize());
        label.setLocation(0,0);
        for(Line child : childrenlines){
            child.end = new Point(newLocation.x + width/2,
                    newLocation.y);
        }
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
        return new Point(x-leftPadding,y-topPadding);
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
        return new Point(x+leftPadding,y+topPadding);
    }
    @Override
    public void update(){
        resize();
        super.update();
    }
}
