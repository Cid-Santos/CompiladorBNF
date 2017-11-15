package Back_end;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Cid
 */
public class Grammar {

    public Set<String> terminals = new LinkedHashSet<>();
    public Set<String> nonterminals = new LinkedHashSet<>();
    public Set<Production> productions = new LinkedHashSet<>();
    public String start;

    public boolean isTerminal(String s) {
        return terminals.contains(s);
    }

    public boolean isNonTerminal(String s) {
        return nonterminals.contains(s);
    }

    public List<String> syms() {
        List<String> ret = new ArrayList<>();
        ret.addAll(terminals);
        ret.addAll(nonterminals);
        return ret;
    }
}
