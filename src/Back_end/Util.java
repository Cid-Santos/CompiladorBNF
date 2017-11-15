package Back_end;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Cid
 */
public class Util {

    public static String readLine(Scanner in, String msg) {
        if (!in.hasNextLine()) {
            throw new Error(msg + " mas o arquivo de entrada terminou");
        }
        return in.nextLine();
    }

    public static int toInt(String line, String msg) {
        try {
            return new Integer(line);
        } catch (NumberFormatException e) {
            throw new Error("Esperando " + msg + " mas a linha não é um número:\n" + line);
        }
    }

    public static Grammar readGrammar(Scanner in) {
        Grammar grammar = new Grammar();
        String line = readLine(in, "Esperando número de terminais");
        int nterm = toInt(line, "número de terminais");
        for (int i = 0; i < nterm; i++) {
            String term = readLine(in, "Esperando um terminal").intern();
            if (!grammar.terminals.add(term)) {
                throw new Error("Terminal duplicado: " + term);
            }
        }

        line = readLine(in, "Esperando número de não-terminais");
        int nnonterm = toInt(line, "número de não-terminais");
        for (int i = 0; i < nnonterm; i++) {
            String nonterm = readLine(in, "Esperando um não terminal").intern();
            if (!grammar.nonterminals.add(nonterm)) {
                throw new Error("Duplicado não-terminal: " + nonterm);
            }
            if (grammar.isTerminal(nonterm)) {
                throw new Error("Não pode ser terminal e não terminal: " + nonterm);
            }
        }

        grammar.start = readLine(in, "Esperando o símbolo de início").intern();
        if (!grammar.nonterminals.contains(grammar.start)) {
            throw new Error(
                    "Símbolo de início " + grammar.start + " não foi declarado como não terminal.");
        }

        line = readLine(in, "Esperando número de produções");
        int nprods = toInt(line, "número de produções");
        for (int i = 0; i < nprods; i++) {
            Production prod = readProduction(readLine(in, "Esperando produção"), grammar);
            if (!grammar.productions.add(prod)) {
                throw new Error("Produção duplicada: " + prod);
            }
        }
        if (in.hasNextLine()) {
            System.err.println("Warning: linhas de entrada extras após a gramática; talvez sua contagem de produção esteja errada.");
        }
        return grammar;
    }

    public static Production readProduction(String line, Grammar grammar) {
        Scanner s = new Scanner(line);
        if (!s.hasNext()) {
            throw new Error("Linha vazia em vez de uma produção");
        }
        String lhs = s.next().intern();
        if (!grammar.isNonTerminal(lhs)) {
            throw new Error("Símbolo " + lhs + " não foi declarado como não-terminal, mas aparece no LHS de produção " + line);
        }
        List<String> rhs = new ArrayList<String>();
        while (s.hasNext()) {
            String sym = s.next().intern();
            if (!grammar.isNonTerminal(sym) && !grammar.isTerminal(sym)) {
                throw new Error("Símbolo " + sym + " não faz parte da gramática");
            }
            rhs.add(sym);
        }
        return Production.v(lhs,
                (String[]) rhs.toArray(new String[rhs.size()]));
    }

    static String checkIndent(String line, int indent) {
        for (int i = 0; i < indent; i++) {
            if (line.length() <= i) {
                throw new Error("Esperando a produção, mas obteve uma linha vazia.");
            }
            if (line.charAt(i) != ' ') {
                throw new Error("Produção " + line.substring(i) + " deve ser identado " + indent + " espaço(s), mas é indentado " + i + " espaço");
            }
        }
        if (line.length() <= indent) {
            throw new Error("Esperando a produção, mas obteve uma linha vazia.");
        }
        if (line.charAt(indent) == ' ') {
            throw new Error("Produção " + line + " deve ser identado " + indent + " spaços, mas é mais identado do que isso.");
        }
        return line.substring(indent);
    }

    static void printGrammar(Grammar grammar) {
        System.out.println("Terminals:");
        for (String s : grammar.terminals) {
            System.out.println("   " + s);
        }
        System.out.println();

        System.out.println("Nonterminals:");
        for (String s : grammar.nonterminals) {
            System.out.println("   " + s);
        }
        System.out.println();

        System.out.println("Símbolo de início:");
        System.out.println("   " + grammar.start);
        System.out.println();

        System.out.println("Regras de produção:");
        for (Production s : grammar.productions) {
            System.out.println("   " + s);
        }
    }

    static void writeGrammar(Grammar grammar) {
        System.out.println(grammar.terminals.size());
        for (String s : grammar.terminals) {
            System.out.println(s);
        }
        System.out.println(grammar.nonterminals.size());
        for (String s : grammar.nonterminals) {
            System.out.println(s);
        }
        System.out.println(grammar.start);
        System.out.println(grammar.productions.size());
        for (Production s : grammar.productions) {
            System.out.println(s);
        }
    }
}
