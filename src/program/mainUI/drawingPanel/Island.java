package program.mainUI.drawingPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Island extends NodeLabel{
    static int horiPadding = 20;
    static int vertPadding = 20;
    static int borderPadding = 10;
    // JLabels to position around the borders it looks like they're clickable
    private JLabel right = new JLabel();
    private JLabel left = new JLabel();
    private JLabel bottom = new JLabel();
    public Island(Plain p) {
        super("Island", p);
        grammarType = 1;
    }
    @Override
    public void makeGraphics(){
        super.makeGraphics();
        setSize(50,50);
        setBorder(new LineBorder(Color.red,2,true));
        right_click_menu.remove(1);
        setLayout(null);
        label.setLocation(0,0);
        label.setSize(horiPadding, vertPadding);
        label.setFont(label.getFont().deriveFont(0.0F));
        MouseListener adapter = label.getMouseListeners()[0];
        ((ML)adapter).setShowGraphics(false);
        right.addMouseListener(adapter);
        right.addMouseMotionListener((MouseMotionListener) adapter);
        left.addMouseListener(adapter);
        left.addMouseMotionListener((MouseMotionListener) adapter);
        bottom.addMouseListener(adapter);
        bottom.addMouseMotionListener((MouseMotionListener) adapter);
        add(bottom);
        add(left);
        add(right);
        label.setBackground(new Color(0,0,0,0)); // transparent
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
        if(children.size()==1 && parents.size()==1){
            // if we have exactly one child, we should try to make it look like the lines go through the island
            // to the node below it. To do this, we have to make the lines overlap.
            Point upperMid = upperMid();
            Point lowerMid = lowerMid();
            Point intersectionPoint;
            double xySlope = ((double) upperMid.y -(double) lowerMid.y)/((double)upperMid.x - (double)lowerMid.x);
            double yxSlope = ((double) upperMid.x -(double) lowerMid.x)/((double)upperMid.y - (double)lowerMid.y);
            // calculate the slopes intersecting the vertical and horizontal walls
            double vertTargetX = lowerMid.x < newLocation.x ? newLocation.x : newRightCorner.x;
            double vertTargetY = (vertTargetX-(double)upperMid.x)/yxSlope + (double) upperMid.y;
            double horTargetY = newLocation.y;
            double horTargetX = (horTargetY-(double)upperMid.y)/xySlope + (double) upperMid.x;
            if(horTargetX <= newRightCorner.x && horTargetX >= newLocation.x){
                intersectionPoint = new Point((int)horTargetX,(int)horTargetY);
            } else {
                intersectionPoint = new Point((int)vertTargetX,(int)vertTargetY);
            }
            for(Line L : parentlines){
                L.start = intersectionPoint;
            }
            for(Line L : childrenlines){
                L.end= intersectionPoint;
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
        label.setBounds(0,0,width,borderPadding); // the top
        bottom.setBounds(0,height-borderPadding,width,borderPadding);
        right.setBounds(width-borderPadding,0,borderPadding,height);
        left.setBounds(0,0,borderPadding,height);
    }
    @Override
    public int getArrowLow(){
        return location.y + getHeight() - Arrow.BOTTOM_PAD;
    }
    @Override
    public void setFonts(Font f){
        // do nothing, we don't wanna change our font!
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
