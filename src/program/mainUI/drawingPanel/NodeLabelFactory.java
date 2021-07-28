package program.mainUI.drawingPanel;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NodeLabelFactory {
    final static Pattern childrenArrows = Pattern.compile("\\>\\d+");
    final static Pattern parentArrows = Pattern.compile("\\<\\d+");
    public static NodeLabel createWithReservedTextAndArrows(String x, Plain p, HashMap<String,Arrow> map, Vector<Line> lineList){
        if(!x.contains("|")){
            return createWithReservedText(x,p);
        }
        String[] arrowsAndName = x.split("\\|",2);
        NodeLabel current = createWithReservedText(arrowsAndName[1],p);
        Matcher childrenMatch = childrenArrows.matcher(arrowsAndName[0]);  //use regex to get all incoming matches of the regex
        while(childrenMatch.find()){
            String arrow = childrenMatch.group().substring(1); // to chop off the > character
            // get the arrow from the map, making it if we don't have it
            Arrow childrenArrow;
            if(!map.containsKey(arrow)){
                childrenArrow = new Arrow();
                childrenArrow.plain = p;
                lineList.add(childrenArrow);
                map.put(arrow,childrenArrow);
            } else {
                childrenArrow = map.get(arrow);
            }
            childrenArrow.parent = current;
            current.childrenArrows.add(childrenArrow);
        }
        Matcher parentMatch = parentArrows.matcher(arrowsAndName[0]);  //use regex to get all incoming matches of the regex
        while(parentMatch.find()){
            String arrow = parentMatch.group().substring(1); // to chop off the > character
            // get the arrow from the map, making it if we don't have it
            Arrow parentArrow;
            if(!map.containsKey(arrow)){
                parentArrow = new Arrow();
                parentArrow.plain = p;
                lineList.add(parentArrow);
                map.put(arrow,parentArrow);
            } else {
                parentArrow = map.get(arrow);
            }
            parentArrow.children = current;
            current.parentArrows.add(parentArrow);
        }
        return current;
    }
    public static NodeLabel createWithReservedText(String x, Plain p){
        if(x.equals("__Island")){
            return new Island(p);
        }
        return new NodeLabel(x,p);
    }
}
