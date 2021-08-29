package program.mainUI.drawingPanel;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;

public class Arrow extends Line{
    Vector<Point> pointList = new Vector<>();
    @Override
    public Line makeCopy(Plain p){
        return new Arrow(p);
    }
    public Arrow(){

    }
    public Arrow(Plain p){
        attachTo(p);
    }
    @Override
    public void attachTo(Plain p){
        super.attachTo(p);
        p.arrowMode=false;
    }
    static int BOTTOM_PAD=20;
    @Override
    public boolean inRange(Point p){
        int size = pointList.size() - 1;
        for(int i = 0; i < size;i++){
            boolean result = inRangeMath(pointList.get(i).x,pointList.get(i).y,
                    pointList.get(i+1).x,pointList.get(i+1).y,p);
            if(result) return result;
        }
        return false;
    }
    private Island[] getFirstDifferentIslands(NodeLabel n1, NodeLabel n2){
        // returns an array with size two which gives the two outermost islands n1 and n2 do not share
        // Assumes that the two nodes are in a tree, ie they only have one parent(why is this not already mandated by the program code?????)

        // traverse up two nodes trees to make a list of islands.
        NodeLabel traverseNode = n1;
        Vector<NodeLabel> n1Islands = new Vector<>();
        while(traverseNode!=null){
            if(traverseNode.getClass() == Island.class){
                n1Islands.add(traverseNode);
            }
            if(traverseNode.parents.isEmpty()){
                traverseNode = null;
            } else{
                traverseNode = traverseNode.parents.firstElement();
            }
        }
        Vector<NodeLabel> n2Islands = new Vector<>();
        traverseNode = n2;
        while(traverseNode!=null){
            if(traverseNode.getClass() == Island.class){
                n2Islands.add(traverseNode);
            }
            if(traverseNode.parents.isEmpty()){
                traverseNode = null;
            } else{
                traverseNode = traverseNode.parents.firstElement();
            }
        }
        // now that we have both lists, we traverse them backwards to find the outermost island that doesn't match
        // we want to go outside of those two islands
        int n1Iterator = n1Islands.size()-1;
        int n2Iterator = n2Islands.size()-1;
        while(n1Iterator >= 0 && n2Iterator >=0 && n1Islands.get(n1Iterator) == n2Islands.get(n2Iterator)){
            n1Iterator--;
            n2Iterator--;
        }
        Island[] returnArray = {null,null};
        if(n1Iterator >=0){
            returnArray[0] = (Island)n1Islands.get(n1Iterator);
        }
        if(n2Iterator >=0){
            returnArray[1] = (Island)n2Islands.get(n2Iterator);
        }
        return returnArray;
    }
    @Override
    public void paintSelf(Graphics2D g){
        if(parent == null || children == null) {
            plain.removeline(this);
            return;
        }
        BasicStroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g.setStroke(stroke);
        // black plain form
        if (color == 0) {
            g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.blue);
        }
        // orange bold ready form
        else if (color == 3) {
            g.setStroke(new BasicStroke(3.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.decode("0x994C00"));
        } else if (color == 6) {
            g.setStroke(new BasicStroke(3.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.decode("0xFF66FF"));
        }
        // blue bold chosen form
        else {
            g.setStroke(new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.green);
        }
        Island[] differingIslands = getFirstDifferentIslands(parent,children);
        // roadmap for the lines we draw
        // 1. a straight line down to our lowest point
        // 2. a curved line from our lowest point down to lowest point + padding
        // 3. another straight line up to the middle
        int parentX;
        int parentY;
        if(parent.children.isEmpty() || parent.grammarType==1){
            //draw from the bottom if we have no children
            parentX = parent.location.x + parent.getWidth()/2;
            parentY = parent.location.y + parent.getHeight();
        } else {
            //draw from the side if not
            parentX = parent.location.x;
            if(parent.location.x > children.location.x){
                parentX+=parent.getWidth(); // if we are to the right of the child, it needs to come out on the right side
            }
            parentY = parent.location.y + parent.getHeight()/2;
        }
        // the same thing again but for the children
        int childrenX;
        int childrenY;
        if(children.children.isEmpty() || children.grammarType==1){
            //draw from the bottom if we have no children
            childrenX = children.location.x + children.getWidth()/2;
            childrenY = children.location.y + children.getHeight();
        } else {
            //draw from the side if not
            childrenX = children.location.x;
            if(children.location.x > parent.location.x){
                childrenX+=children.getWidth(); // if we are to the right of the child, it needs to come out on the right side
            }
            childrenY = children.location.y + children.getHeight()/2;
        }
        int lowX = parent.location.x < children.location.x ? parent.upperLeftCorner().x : children.upperLeftCorner().x;
        int highX = parent.location.x > children.location.x ? parent.lowerRightCorner().x : children.lowerRightCorner().x;
        int minY = plain.lowestPointBetweenTwoX(lowX,highX);
        final int radius = 3;
        Path2D.Float path = new Path2D.Float();
        path.moveTo(parentX,parentY);
        // determine if we need to draw an extra segment to go out to the right
        int leftSegment;
        if(parent.children.isEmpty() || parent.grammarType==1){
            leftSegment = parentX;
        } else {
            NodeLabel segmentChooser = differingIslands[0] == null ? parent : differingIslands[0]; // if we have a differing island, we make it our "punchout" point to go to the left or right of
            leftSegment = parentX < childrenX ? segmentChooser.upperLeftCorner().x - BOTTOM_PAD/2: segmentChooser.lowerRightCorner().x + BOTTOM_PAD/2;
            int dist = leftSegment-parentX;
            if(dist < 0){
                path.curveTo(parentX + dist/3,parentY - 3*BOTTOM_PAD/24,parentX + dist,parentY - BOTTOM_PAD/12,leftSegment,parentY);
            } else {
                path.curveTo(parentX + dist,parentY - BOTTOM_PAD/12,parentX + dist/3,parentY - 3*BOTTOM_PAD/24,leftSegment,parentY);
            }
        }
        start = new Point(leftSegment,parentY);
        end = new Point(leftSegment,minY + BOTTOM_PAD/2);
        path.lineTo(leftSegment,minY + BOTTOM_PAD/2);
        int rightSegment;
        if(children.children.isEmpty() || children.grammarType == 1){
            rightSegment = childrenX;
        } else {
            NodeLabel segmentChooser = differingIslands[1] == null ? children : differingIslands[1]; // if we have a differing island, we make it our "punchout" point to go to the left or right of
            rightSegment = childrenX < parentX ? segmentChooser.upperLeftCorner().x - BOTTOM_PAD/2 : segmentChooser.lowerRightCorner().x + BOTTOM_PAD/2;
        }
        path.quadTo((leftSegment+rightSegment)/2,minY+BOTTOM_PAD,rightSegment,minY+BOTTOM_PAD/2);
        path.lineTo(rightSegment,childrenY);
        if(rightSegment!=childrenX){
            int dist = rightSegment - childrenX;
            if(dist < 0){
                path.curveTo(childrenX + dist/3,childrenY - 3*BOTTOM_PAD/24,childrenX + dist,childrenY - BOTTOM_PAD/12,childrenX,childrenY);
            } else {
                path.curveTo(childrenX + dist,childrenY - BOTTOM_PAD/12,childrenX + dist/3,childrenY - 3*BOTTOM_PAD/24,childrenX,childrenY);
            }
            drawArrowHead(g,new Point(childrenX,childrenY), new Point(rightSegment,childrenY));
        } else {
            drawArrowHead(g,new Point(childrenX,childrenY), new Point(childrenX,minY + BOTTOM_PAD));
        }
        pointList.clear(); // to add things to the list
        PathIterator i = path.getPathIterator(null);
        while (!i.isDone()) {
            float[] coords = new float[6];
            int segType = i.currentSegment(coords);
            switch (segType){
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    pointList.add(new Point((int)coords[0], (int)coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    pointList.add(new Point((int)coords[0], (int)coords[1]));
                    pointList.add(new Point((int)coords[2], (int)coords[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    pointList.add(new Point((int)coords[0], (int)coords[1]));
                    pointList.add(new Point((int)coords[2], (int)coords[3]));
                    pointList.add(new Point((int)coords[4], (int)coords[5]));
                    break;
            }
            i.next();
        }

        g.draw(path);
    }
    // function from https://coderanch.com/t/340443/java/Draw-arrow-head-line
    // minor modifications to remove color customization
    private void drawArrowHead(Graphics2D g2, Point tip, Point tail)
    {
        double phi = Math.toRadians(40);
        double barb = 10;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        //System.out.println("theta = " + Math.toDegrees(theta));
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++)
        {
            x = tip.x - barb * Math.cos(rho);
            y = tip.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }
}
