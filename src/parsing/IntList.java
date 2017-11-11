/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.util.LinkedList;

/**
 *
 * @author Cid
 */
public class IntList {

    int val;
    IntList next;

    IntList(int p) {
        this(p, null);
    }

    IntList(int p, IntList s) {
        val = p;
        next = s;
    }

    IntList() {
    }

    static IntList add(int p, IntList s) {
        return new IntList(p, s);
    }

    public boolean equals(IntList l) {
        if (val != l.val) {
            return false;
        }
        if (next == null) {
            return l.next == null;
        }
        return next.equals(l.next);
    }

    public String toString() {
        String s = " " + val;
        if (next == null) {
            return s + ".";
        } else {
            return s + next;
        }
    }

    public String show(String name) {
        return name + this;
    }

    public static void main(String[] args) {
        IntList l = null, m = null;
        l = add(0, l);
        m = add(0, m);
        System.out.println(l.equals(m));
        LinkedList t = new LinkedList();
        t.addLast(l);
        System.out.println(t.indexOf(m));
    }
}
