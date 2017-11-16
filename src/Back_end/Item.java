package Back_end;

import java.util.HashMap;
import java.util.Map;

/**
 * JLALR constrói LALR (1) e SLR (1) analisa tabelas de uma gramática, usando  
 * os algoritmos descritos no capítulo 3 do Appel, _Modern Compiler 
 * Implementação em Java, segunda edição_, 2002. JLALR lê uma gramática na
 * entrada padrão e grava a gramática gerada e analisa as tabelas em saída
 * padrão.
 *
 * @author Cid
 */
public class Item {

    Production rule; // a produção
    int pos; //posição do ponto (0 <= pos <= rule.rhs.size ())
    String lookahead; // o terminal lookahead

    private Item(Production rule, int pos, String lookahead) {
        this.rule = rule;
        this.pos = pos;
        this.lookahead = lookahead;
    }
    private static final  Map<Pair<Production, Pair<Integer, String>>, Item> map = new HashMap<>();

    public static  Item v(Production rule, int pos, String lookahead) {
        Pair<Production, Pair<Integer, String>> triple = new Pair<>(rule, new Pair<>(pos, lookahead));
        Item ret = map.get(triple);
        if (ret == null) {
            ret = new Item(rule, pos, lookahead);
            map.put(triple, ret);
        }
        return ret;
    }

    public static Item v(Production rule, int pos) {
        return v(rule, pos, "");
    }

    /**
     * Retorna verdadeiro se o ponto não estiver no final do RHS.
     * @return 
     */
    public boolean hasNextSym() {
        return pos < rule.rhs.length;
    }

    /**
     * Retorna o símbolo imediatamente após o ponto.
     * @return 
     */
    public String nextSym() {
        if (!hasNextSym()) {
            throw new RuntimeException("Erro interno: obter o próximo símbolo de um item sem o próximo símbolo");
        }
        return rule.rhs[pos];
    }

    /**
     * Returns the item obtained by advancing (shifting) the dot by one symbol.
     * @return 
     */
    public Item advance() {
        if (!hasNextSym()) {
            throw new RuntimeException("Erro interno: avançar um item sem o próximo símbolo");
        }
        return Item.v(rule, pos + 1, lookahead);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("(");
        ret.append(rule.lhs);
        ret.append(" ->");
        int i;
        for (i = 0; i < pos; i++) {
            ret.append(" ").append(rule.rhs[i]);
        }
        ret.append(" ##");
        for (; i < rule.rhs.length; i++) {
            ret.append(" ").append(rule.rhs[i]);
        }
        ret.append(", ");
        ret.append(lookahead);
        ret.append(")");
        return ret.toString();
    }
}
