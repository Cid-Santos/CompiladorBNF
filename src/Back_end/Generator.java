package Back_end;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Classe principal do gerador LALR/SLR.
 *
 * @author Cid
 */
public class Generator {

    /**
     * Gramatica Livre de contexto
     */
    Grammar grammar;
    /**
     * Um mapa de cada não-terminal para as produções que o expandem.
     */
    Map<String, List<Production>> lhsToRules;

    public Generator(Grammar grammar) {
        this.table = new HashMap<>();
        this.lhsToRules = new HashMap<>();
        this.grammar = grammar;
        grammar.nonterminals.forEach((nonterm) -> {
            lhsToRules.put(nonterm, new ArrayList<>());
        });
        grammar.productions.forEach((p) -> {
            List<Production> mapRules = lhsToRules.get(p.lhs);
            mapRules.add(p);
        });
    }

    /**
     * Compute the closure of a set of items using the algorithm of Appel, p. 60
     *
     * @param i
     * @return
     */
    public Set<Item> closure(Set<Item> i) {
        boolean change;
        while (true) {
            Set<Item> oldI = new HashSet<>(i);
            oldI.stream().filter((item) -> !(!item.hasNextSym())).map((item) -> item.nextSym()).filter((x) -> !(grammar.isTerminal(x))).forEachOrdered((String x) -> {
                lhsToRules.get(x).forEach((r) -> {
                    i.add(Item.v(r, 0));
                });
            });
            if (i.equals(oldI)) {
                return i;
            }
        }
    }

    /**
     * Calcule o conjunto goto para o estado i e o símbolo x, usando o algoritmo
     * de Appel, p. 60
     *
     * @param i
     * @param x
     * @return
     */
    public Set<Item> goto_(Set<Item> i, String x) {
        Set<Item> j = new HashSet<>();
        i.stream().filter((item) -> !(!item.hasNextSym())).filter((item) -> !(!item.nextSym().equals(x))).forEachOrdered((item) -> {
            j.add(item.advance());
        });
        return closure(j);
    }
    private final Map<List<String>, Set<String>> generalFirstCache = new HashMap<>();

    /**
     * Calcule o primeiro generalizado usando a definição de Recurso, p. 50.
     *
     * @param l
     * @return
     */
    public Set<String> generalFirst(List<String> l) {
        Set<String> ret = generalFirstCache.get(l);
        if (ret == null) {
            ret = new HashSet<>();
            if (l.isEmpty()) {
                return ret;
            } else {
                ret.addAll(first.get(l.get(0)));
                int i = 0;
                while (i < l.size() - 1 && nullable.contains(l.get(i))) {
                    ret.addAll(first.get(l.get(i + 1)));
                    i++;
                }
            }
            generalFirstCache.put(l, ret);
        }
        return ret;
    }

    private final Map<Set<Item>, Set<Item>> closureCache = new HashMap<>();

    /**
     * Calcule o fechamento de um conjunto de itens LR (1) usando o algoritmo de
     * Appel, p. 63
     *
     * @param i
     * @return
     */
    public Set<Item> lr1_closure(Set<Item> i) {
        boolean change;
        Set<Item> ret = closureCache.get(i);
        if (ret == null) {
            Set<Item> origI = new HashSet<>(i);
            Queue<Item> q = new LinkedList<>(i);
            while (!q.isEmpty()) {
                Item item = q.remove();
                if (!item.hasNextSym()) {
                    continue;
                }
                String x = item.nextSym();
                if (grammar.isTerminal(x)) {
                    continue;
                }
                List<String> betaz = new ArrayList<>();
                for (int p = item.pos + 1; p < item.rule.rhs.length; p++) {
                    betaz.add(item.rule.rhs[p]);
                }
                betaz.add(item.lookahead);
                Collection<String> ws = generalFirst(betaz);
                lhsToRules.get(x).forEach((Production r) -> {
                    ws.stream().map((w) -> Item.v(r, 0, w)).filter((newItem) -> (i.add(newItem))).forEachOrdered((newItem) -> {
                        q.add(newItem);
                    });
                });
            }
            closureCache.put(origI, i);
            ret = i;
        }
        return ret;
    }

    private final Map<Pair<State, String>, State> gotoCache = new HashMap<>();

    /**
     * Calcule o LR (1) goto set para o estado i e o símbolo x, usando o
     * algoritmo de Appel, p. 63
     *
     * @param i
     * @param x
     * @return
     */
    public State lr1_goto_(State i, String x) {
        Pair<State, String> pair = new Pair<>(i, x);
        State ret = gotoCache.get(pair);
        if (ret == null) {
            Set<Item> j = new HashSet<>();
            i.items.stream().filter((item) -> !(!item.hasNextSym())).filter((item) -> !(!item.nextSym().equals(x))).forEachOrdered((item) -> {
                j.add(item.advance());
            });
            ret = State.v(lr1_closure(j));
            gotoCache.put(pair, ret);
        }
        return ret;
    }

    /**
     * Adicione a ação à tabela de análise para o estado de estado e símbolo
     * sym.       Comunicar um conflito se a tabela já contiver uma ação para o
     * mesmo       estado e símbolo.
     */
    private boolean addAction(State state, String sym, Action a) {
        boolean ret = false;
        Pair<State, String> p = new Pair<>(state, sym);
        Action old = table.get(p);
        if (old != null && !old.equals(a)) {
            throw new Error(
                    "Conflito no símbolo " + sym + " no estado " + state + "\n"
                    + "Possíveis ações:\n"
                    + old + "\n" + a);
        }
        if (old == null || !old.equals(a)) {
            ret = true;
        }
        table.put(p, a);
        return ret;
    }

    /**
     * Retornar verdadeiro se todos os símbolos em l estiverem no conjunto
     * anulável.
     */
    private boolean allNullable(String[] l) {
        return allNullable(l, 0, l.length);
    }

    /**
     * Retornar true se os símbolos start..end em l estão no conjunto nullable.
     */
    private boolean allNullable(String[] l, int start, int end) {
        boolean ret = true;
        for (int i = start; i < end; i++) {
            if (!nullable.contains(l[i])) {
                ret = false;
            }
        }
        return ret;
    }

    //Os conjuntos NULLABLE, FIRST e FOLLOW. Ver Appel, pp. 47-49
    Set<String> nullable = new HashSet<>();
    Map<String, Set<String>> first = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();

    /**
     * Calcula conjuntos NULLABLE, FIRST e FOLLOW usando o algoritmo de Appel,
     * p. 49
     */
    public void computeFirstFollowNullable() {
        grammar.syms().stream().map((String z) -> {
            first.put(z, new HashSet<>());
            return z;
        }).map((z) -> {
            if (grammar.isTerminal(z)) {
                first.get(z).add(z);
            }
            return z;
        }).forEachOrdered((String z) -> {
            follow.put(z, new HashSet<>());
        });
        boolean change;
        do {
            change = false;
            for (Production rule : grammar.productions) {
                if (allNullable(rule.rhs)) {
                    if (nullable.add(rule.lhs)) {
                        change = true;
                    }
                }
                int k = rule.rhs.length;
                for (int i = 0; i < k; i++) {
                    if (allNullable(rule.rhs, 0, i)) {
                        if (first.get(rule.lhs).addAll(
                                first.get(rule.rhs[i]))) {
                            change = true;
                        }
                    }
                    if (allNullable(rule.rhs, i + 1, k)) {
                        if (follow.get(rule.rhs[i]).addAll(
                                follow.get(rule.lhs))) {
                            change = true;
                        }
                    }
                    for (int j = i + 1; j < k; j++) {
                        if (allNullable(rule.rhs, i + 1, j)) {
                            if (follow.get(rule.rhs[i]).addAll(
                                    first.get(rule.rhs[j]))) {
                                change = true;
                            }
                        }
                    }
                }
            }
        } while (change);
    }
    /**
     * Calculado a analize da tabela .
     */
    Map<Pair<State, String>, Action> table;
    State initialState;

    /**
     * Gera a tabela de análise LR (0) usando os algoritmos nas pp. 60 de Appel.
     */
    public void generateLR0Table() {
        Set<Item> startRuleSet = new HashSet<>();
        lhsToRules.get(grammar.start).forEach((r) -> {
            startRuleSet.add(Item.v(r, 0));
        });
        initialState = State.v(closure(startRuleSet));
        Set<State> t = new HashSet<>();
        t.add(initialState);
        boolean change;
        // ações de goto computado
        do {
            change = false;
            for (State i : new ArrayList<>(t)) {
                for (Item item : i.items) {
                    if (!item.hasNextSym()) {
                        continue;
                    }
                    String x = item.nextSym();
                    State j = State.v(goto_(i.items, x));
                    if (t.add(j)) {
                        change = true;
                    }
                    if (addAction(i, x, new ShiftAction(j))) {
                        change = true;
                    }
                }
            }
        } while (change);
        // calcular ações de redução
        t.forEach((State i) -> {
            i.items.stream().filter((item) -> !(item.hasNextSym())).forEachOrdered((Item item) -> {
                grammar.syms().forEach((x) -> {
                    addAction(i, x, new ReduceAction(item.rule));
                });
            });
        });
    }

    /**
     * Gera a tabela de análise SLR (1) usando os algoritmos nas pp. 60 e 62 de
     * Appel.
     */
    public void generateSLR1Table() {
        Set<Item> startRuleSet = new HashSet<>();
        lhsToRules.get(grammar.start).forEach((r) -> {
            startRuleSet.add(Item.v(r, 0));
        });
        initialState = State.v(closure(startRuleSet));
        Set<State> t = new HashSet<>();
        t.add(initialState);
        boolean change;
        // ações de goto computado
        do {
            change = false;
            for (State i : new ArrayList<>(t)) {
                for (Item item : i.items) {
                    if (!item.hasNextSym()) {
                        continue;
                    }
                    String x = item.nextSym();
                    State j = State.v(goto_(i.items, x));
                    if (t.add(j)) {
                        change = true;
                    }
                    if (addAction(i, x, new ShiftAction(j))) {
                        change = true;
                    }
                }
            }
        } while (change);
        // calcular ações de redução
        t.forEach((State i) -> {
            i.items.stream().filter((item) -> !(item.hasNextSym())).forEachOrdered((Item item) -> {
                follow.get(item.rule.lhs).forEach((x) -> {
                    addAction(i, x, new ReduceAction(item.rule));
                });
            });
        });
    }

    /**
     * Gera a tabela de análise LR (1) usando os algoritmos nas pp. 60, 62 e 64
     * de Appel.
     *
     * @return
     */
    public String generateLR1Table() {
        String output = null;
        Set<Item> startRuleSet = new HashSet<>();
        output = "\nCalcular estado Inicial";
        lhsToRules.get(grammar.start).forEach((r) -> {
            startRuleSet.add(Item.v(r, 0, grammar.terminals.iterator().next()));
        });
        initialState = State.v(lr1_closure(startRuleSet));
        Set<State> t = new HashSet<>();
        t.add(initialState);
        Queue<State> q = new LinkedList<>();
        q.add(initialState);
        // ações de goto computado
        output += "\nCalcular ações goto";
        while (!q.isEmpty()) {
            State i = q.remove();
            for (Item item : i.items) {
                if (!item.hasNextSym()) {
                    continue;
                }
                String x = item.nextSym();
                State j = lr1_goto_(i, x);
                if (t.add(j)) {
                    output += ".";
                    q.add(j);
                }
                addAction(i, x, new ShiftAction(j));
            }
        }
        // calcular ações de redução
        output += "\nCalcular Açoes reduce";
        t.forEach((State i) -> {
            i.items.stream().filter((item) -> !(item.hasNextSym())).forEachOrdered((item) -> {
                addAction(i, item.lookahead, new ReduceAction(item.rule));
            });
        });
        return output;
    }

    public Set<Item> core(Set<Item> items) {
        Set<Item> ret = new HashSet<>();
        items.forEach((item) -> {
            ret.add(Item.v(item.rule, item.pos));
        });
        return ret;
    }

    /**
     * Gera a tabela de análise LALR (1) usando os algoritmos nas pp. 60, 62 e
     * 64 de Appel.
     * @return 
     */
    public String generateLALR1Table() {
        String output = null;
        Set<Item> startRuleSet = new HashSet<>();
        output = "\nCalculando estado inicial";
        lhsToRules.get(grammar.start).forEach((r) -> {
            startRuleSet.add(Item.v(r, 0, grammar.terminals.iterator().next()));
        });
        initialState = State.v(lr1_closure(startRuleSet));
        Set<State> t = new HashSet<>();
        t.add(initialState);
        Queue<State> q = new LinkedList<>();
        q.add(initialState);
        output += "\nCalculando estados";
        while (!q.isEmpty()) {
            State i = q.remove();
            for (Item item : i.items) {
                if (!item.hasNextSym()) {
                    continue;
                }
                String x = item.nextSym();
                State j = lr1_goto_(i, x);
                if (t.add(j)) {
                    output += ".";
                    q.add(j);
                }
            }
        }
        output += "\nMesclando estados";
        Map<Set<Item>, Set<Item>> coreToState = new HashMap<>();
        for (State state : t) {
            Set<Item> items = state.items;
            Set<Item> core = core(items);
            Set<Item> accum = coreToState.get(core);
            if (accum == null) {
                output += ".";
                accum = new HashSet<Item>(items);
                coreToState.put(core, accum);
            } else {
                accum.addAll(items);
            }
        }
        Map<Set<Item>, State> coreToStateState = new HashMap<>();
        t.stream().map((state) -> core(state.items)).forEachOrdered((core) -> {
            coreToStateState.put(core, State.v(coreToState.get(core)));
        });
        // ações de goto computado
        output += "\nCalcular ações goto";
        t.clear();
        initialState = State.v(coreToState.get(core(initialState.items)));
        t.add(initialState);
        q.add(initialState);
        while (!q.isEmpty()) {
            State i = q.remove();
            for (Item item : i.items) {
                if (!item.hasNextSym()) {
                    continue;
                }
                String x = item.nextSym();
                State j = lr1_goto_(i, x);
                j = coreToStateState.get(core(j.items));
                if (t.add(j)) {
                    output += ".";
                    q.add(j);
                }
                addAction(i, x, new ShiftAction(j));
            }
        }
        // calcular ações de redução
        output += "\nCalcular ações de redução";
        t.forEach((State i) -> {
            i.items.stream().filter((item) -> !(item.hasNextSym())).forEachOrdered((item) -> {
                addAction(i, item.lookahead, new ReduceAction(item.rule));
            });
        });
        return output;
    }

    /**
     * Imprimir os elementos de uma lista separada por espaços.
     *
     * @param l
     * @return
     */
    public static String listToString(List l) {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (Object o : l) {
            if (!first) {
                ret.append(" ");
            }
            first = false;
            ret.append(o);
        }
        return ret.toString();
    }

    /**
     * Produza a saída de acordo com as especificações de saída.
     *
     * @return
     */
    public String generateOutput() {
        String outPut;
        Map<Production, Integer> ruleMap = new HashMap<>();
        int i = 0;
        for (Production r : grammar.productions) {
            ruleMap.put(r, i++);
        }
        Map<State, Integer> stateMap = new HashMap<>();
        i = 0;
        stateMap.put(initialState, i++);
        for (Action a : table.values()) {
            if (!(a instanceof ShiftAction)) {
                continue;
            }
            State state = ((ShiftAction) a).nextState;
            if (!stateMap.containsKey(state)) {
                stateMap.put(state, i++);
            }
        }
        for (Pair<State, String> key : table.keySet()) {
            State state = key.getO1();
            if (!stateMap.containsKey(state)) {
                stateMap.put(state, i++);
            }
        }
        outPut = "Total de estado = " + String.valueOf(i);
        outPut += String.valueOf("  transição = " + table.size());
        for (Map.Entry<Pair<State, String>, Action> e : table.entrySet()) {
            Pair<State, String> p = e.getKey();
            outPut += ("\n" + stateMap.get(p.getO1()) + " " + p.getO2() + " ");
            Action a = e.getValue();
            if (a instanceof ShiftAction) {
                outPut += ("shift " + stateMap.get(((ShiftAction) a).nextState));
            } else if (a instanceof ReduceAction) {
                outPut += ("reduce " + ruleMap.get(((ReduceAction) a).rule));
            } else {
                throw new Error("Internal error: unknown action");
            }
        }
        return outPut;
    }
}
