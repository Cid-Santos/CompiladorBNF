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
public class DListInt {

    int element;
    DListInt prec, next;

    /**
     * Creates a cell containing <code>p</code>.
     */
    public DListInt(int p) {
        element = p;
    }

    /**
     * Inserts <code>p</code> in front.
     */
    public DListInt(int p, DListInt s) { // inserts
        this(p);
        if (s != null) {
            next = s;
            prec = s.prec;
            s.prec = this;
        }
    }

    /**
     * Inserts the cell <code>head</code> in front of the list.
     */
    public DListInt insert(DListInt head) {
        next = head;
        prec = null;
        if (next != null) {
            next.prec = this;
        }
        return this;
    }

    /**
     * Removes the cell <code>head</code> in front of the list and returns it.
     */
    public DListInt remove(DListInt head) {
        if (prec != null) // not the first element in the list
        {
            prec.next = next;
        } else // the first element in the list
        {
            head = next;
        }
        if (next != null) {
            next.prec = prec;
        }
        prec = next = null;
        return head;
    }

    /**
     * Returns the list ended with a dot.
     */
    public String toString() {
        return "" + element + " " + ((next == null) ? "." : next.toString());
    }
}
