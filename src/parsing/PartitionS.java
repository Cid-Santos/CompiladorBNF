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
public class PartitionS {

    int t; // taille
    IntQueue dans;
    IntQueue[] classe;

    public PartitionS(int t) {
        this.t = t;
        dans = new IntQueue();
        classe = new IntQueue[t];
        for (int c = 0; c < t; c++) {
            classe[c] = new IntQueue();
        }
    }

    void parLargeur(IntQueue s, int[] larg, boolean verbose) {
        for (IntList ll = s.front; ll != null; ll = ll.next) {
            int p = ll.val; // numéro de l'état
            int w = larg[p];
            if (classe[w].isEmpty()) {
                dans.add(w);
            }
            classe[w].add(p);
        }
        if (verbose) {
            System.out.println("Largeurs des états de cette hauteur ");
// 	    for (IntList ld = dans.front; ld != null ; ld = ld.next) {
// 		int w = ld.val;System.out.println("ok");
// 		System.out.print(classe[w].show(w + " : "));
// 		System.out.println();
// 	    }
            System.out.println();
        }
    }

    void removeAll() {
    // nettoyage en temps proportionnel à M + m,
        // où m est le nombre de files non vides et M est le nombre d'éléments,
        // donc O(M)
        for (IntList ld = dans.front; ld != null; ld = ld.next) {
            int w = ld.val;
            classe[w].removeAll();// O(M)
        }
        dans.removeAll(); // O(m)
    }

    static PartitionS parHauteur(int[] haut, int H) {
        PartitionS Pi = new PartitionS(1 + H);
        for (int p = 0; p < haut.length; p++) {
            Pi.add(p, haut);
        }
        return Pi;
    }

    int taille(int w) {
        return classe[w].size();
    }

    int length() {
        return t;
    }

    void add(int p, int[] cle) {
        if (classe[cle[p]].isEmpty()) {
            dans.add(cle[p]);
        }
        classe[cle[p]].add(p);
    }
}
