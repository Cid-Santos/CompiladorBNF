/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

/**
 *
 * @author Cid
 */
public class DFA {

    int[][] next;          // the nextstate function
    int initial;           // the initial state
    Partition terminal;    // the partition of terminal states
    Alphabet alphabet;     // the alphabet
    int nbStates;
    int nbLetters;
    //    int[] info;            // for the LR analysis
    int sink;              // the sink (default -1)

    /**
     * creates a DFA with <code>n</code> states and <code>k</code> letters.
     */
    public DFA(int n, int k) {
        this(n, new Alphabet(k));
    }

    /**
     * creates a DFA with n states and alphabet a.
     */
    public DFA(int n, Alphabet a) {
        nbStates = n;
        alphabet = a;
        nbLetters = alphabet.size();
        next = new int[nbStates][nbLetters];
        terminal = new Partition(nbStates);
        //info = new int[n];
        sink = -1;
    }

    /**
     * returns the state reached from state <code>p</code> after reading the
     * word <code>w</code>.
     *
     * @param p starting state
     * @param w input word (w is not <code>null</code>
     * @return state reached
     */
    public int next(int p, String w) {
        return next(p, alphabet.toShort(w));
    }

    int next(int p, short[] w) {
        //transition by a word   w in a DFA
        for (int i = 0; i < w.length; i++) {
            p = next[p][w[i]];
            if (p == -1) {
                break;
            }
        }
        return p;
    }

    /**
     * Minimizes the automaton using the method m.
     */
    public static DFA minimize(DFA a, Minimizer m) throws Exception {
        return m.minimize(a);
    }

    public DFA minimize(Minimizer m) throws Exception {
        return m.minimize(this);
    }

    int index(int[] c) {
        int m = -1;
        for (int i = 0; i < c.length; i++) {
            m = Math.max(m, c[i]);
        }
        return 1 + m;
    }

    /**
     * Returns the quotient of the DFA by the partition <code>c</code>
     *
     * @param c a partition of the state set compatible with the DFA
     * @return the new DFA.
     */
    public DFA quotient(int[] c) {
        int m = index(c);
        int[] t = new int[m];
        DFA s = new DFA(m, alphabet);
        s.initial = c[initial];
        for (int p = 0; p < nbStates; p++) {
            int q = c[p];
            for (int u = 0; u < nbLetters; u++) {
                s.next[q][u] = c[next[p][u]];
            }
            t[q] = terminal.blockName[p];
        }
        s.terminal = new Partition(t);
        return s;
    }

    public DFA quotient(Partition p) {
        return quotient(p.blockName);
    }

    static DFA makeit(int choix) {
        Alphabet al = new Alphabet(2);
        if (choix == 1) {
            DFA a = new DFA(7, al);
            int[] t = new int[]{0, 1, 1, 1, 1, 0, 1};
            a.next[0][0] = 0;
            a.next[0][1] = 0;
            a.next[1][0] = 2;
            a.next[1][1] = 5;
            a.next[2][0] = 3;
            a.next[2][1] = 0;
            a.next[3][0] = 3;
            a.next[3][1] = 4;
            a.next[4][0] = 3;
            a.next[4][1] = 0;
            a.next[5][0] = 6;
            a.next[5][1] = 0;
            a.next[6][0] = 0;
            a.next[6][1] = 5;
            //a.t[1]=a.t[2]=a.t[3]= a.t[4]= a.t[6]=1;
            a.terminal = new Partition(t);
            return a;
        } else {
            DFA a = new DFA(7, al);
            a.next[0][0] = 1;
            a.next[0][1] = 2;
            a.next[1][0] = 3;
            a.next[1][1] = 5;
            a.next[2][0] = 5;
            a.next[2][1] = 4;
            a.next[3][0] = 6;
            a.next[3][1] = 6;
            a.next[4][0] = 6;
            a.next[4][1] = 4;
            a.next[5][0] = 6;
            a.next[5][1] = 6;
            a.next[6][0] = 6;
            a.next[6][1] = 6;
            //a.t[1]=a.t[3]=a.t[5]= a.t[6]=1;
            return a;
        }
    }

    public String toString() {
        String s = "initial=" + initial + "\n";
        //s +="terminal="+terminal.toString()+"\n";
        s += "nbStates=" + nbStates + "\n";
        s += "  ";
        //for(int c=0;c<nbLetters;c++)
        //s+=alphabet.toChar(c)+" ";
        s += "\n";
        StringBuffer u = new StringBuffer(s);
        for (int i = 0; i < nbStates; i++) {
            //s+=i+" ";
            u.append(i + " ");
            for (int c = 0; c < nbLetters; c++) //s+=next[i][c]+" ";System.out.print(s);
            {
                u.append(next[i][c] + " ");
            }
            //s+="\n";
            u.append("\n");
        }
        s = u.toString() + "terminals = " + terminal.blockList[1];
        return s;
    }

    public static void main(String[] args) throws Exception {
        DFA a, b, c, d;
        a = makeit(1);
        //a.show();
        System.out.println(a);
        b = minimize(a, new NMinimizer());
        System.out.println(b);

    }

}