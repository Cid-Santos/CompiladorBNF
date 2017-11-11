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
public class PairIntList {

    int val; // first
    int elem;  // second
    PairIntList next;

    PairIntList(int c, int a, PairIntList s) {
        val = c;
        elem = a;
        next = s;
    }

    /**
     * Conversions PairInt <--> PairIntList
     */
    public PairIntList(PairInt d, PairIntList s) {
        this(d.val, d.elem, s);
    }

    /**
     * Returns the first element of the list.
     */
    public PairInt head() { // nonempty list !
        return new PairInt(val, elem);
    }

    /**
     * Returns the first element of the first pair.
     */
    public int getFirst() {
        return val;
    }

    /**
     * Returns the second element of the first pair.
     */
    public int getSecond() {
        return elem;
    }

    public static String showAI(PairIntList l, Alphabet a) {
        if (l == null) {
            return ". ";
        } else {
            return "[" + a.toChar(l.val) + "," + l.elem + "] "
                    + showAI(l.next, a);
        }
    }

    public String show(Alphabet a) {
        String s = "[" + val + "," + a.toChar(elem) + "] ";
        if (next == null) {
            return s + ". ";
        } else {
            return s + next.show(a);
        }
    }

    public String showII(String name) {
        return "\n" + name + " : " + showII() + "\n";
    }

    public String showII() {
        String s = "[" + val + "," + elem + "] ";
        if (next == null) {
            return s + ". ";
        } else {
            return s + next.showII();
        }
    }

    public String showAI(String name, Alphabet a) {
        return "\n" + name + " : " + showAI(this, a) + "\n";
    }

    public String show(String name, Alphabet a) {
        return "\n" + name + " : " + show(a) + "\n";
    }

    public String showWithoutln(String name, Alphabet a) {
        return "\n" + name + " : " + show(a);
    }

    public static void main(String[] args) {
        Alphabet a = new Alphabet(3);
        PairIntList l = new PairIntList(1, a.toShort('a'), null);
        System.out.print(l.show(a));
    }

}
