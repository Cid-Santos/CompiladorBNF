package Back_end;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cid
 */
public class Production {

    public String lhs;
    public String[] rhs;
    public Production(String lhs, String[] rhs) {
        this.lhs = lhs; this.rhs = rhs;
    }
    private static Map<Pair<String, String[]>, Production> map =
        new HashMap<Pair<String, String[]>, Production>();
    public static Production v(String lhs, String[] rhs) {
        Pair<String, String[]> pair = new Pair<String, String[]>(lhs, rhs);
        Production ret = map.get(pair);
        if(ret == null) {
            ret = new Production(lhs, rhs);
            map.put(pair, ret);
        }
        return ret;
    }

    public Production() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append(lhs);
        //ret.append(" ->");
        for(String sym : rhs) {
            ret.append(" "+sym);
        }
        return ret.toString();
    }
}
