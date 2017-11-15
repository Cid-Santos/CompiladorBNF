package Back_end;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Cid
 */
public class State {
   public Set<Item> items;
    private State(Set<Item> items) {
        this.items = items;
    }
    private static Map<Set<Item>, State> map = new HashMap<Set<Item>, State>();
    public static State v(Set<Item> items) {
        State ret = map.get(items);
        if(ret == null) {
            ret = new State(items);
            map.put(items, ret);
        }
        return ret;
    }
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("\n");
        for(Item item : items) {
            ret.append(item);
            ret.append("\n");
        }
        return ret.toString();
    }  
}
