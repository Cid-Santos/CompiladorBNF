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
        this.lhs = lhs;
        this.rhs = rhs;
    }
    private static Map<Pair<String, String[]>, Production> map = new HashMap<>();

    public static Production v(String lhs, String[] rhs) {
        Pair<String, String[]> pair = new Pair<>(lhs, rhs);
        Production ret = map.get(pair);
        if (ret == null) {
            ret = new Production(lhs, rhs);
            map.put(pair, ret);
        }
        return ret;
    }

    public Production() {
        throw new UnsupportedOperationException("Ainda não suportado."); //Para alterar o corpo de métodos gerados, escolha Ferramentas | Modelos.
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(lhs);
        //ret.append(" ->");
        for (String sym : rhs) {
            ret.append(" ").append(sym);
        }
        return ret.toString();
    }
}
