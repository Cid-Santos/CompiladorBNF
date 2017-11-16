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

    private static final Map<Set<Item>, State> map = new HashMap<>();

    public static State v(Set<Item> items) {
        State ret = map.get(items);
        if (ret == null) {
            ret = new State(items);
            map.put(items, ret);
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("\n");
        items.stream().map((item) -> {
            ret.append(item);
            return item;
        }).forEachOrdered((_item) -> {
            ret.append("\n");
        });
        return ret.toString();
    }
}
