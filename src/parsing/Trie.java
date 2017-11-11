/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.io.IOException;

/**
 *
 * @author Cid
 */
public interface Trie {

    /**
     * Computes the pair composed of the length of the longest prefix of
     * s[j..n-1] in the trie and the vertex reached by this prefix. Implements
     * the function
     * <code>LongestPrefixInTrie()</code> of Section 1.3.1.
     *
     * @param s the input string
     * @param j the starting index
     * @return the computed pair
     */
    public Pair longestPrefixInTrie(String s, int j);

    /**
     * Returns true if the trie contains <code>s</code>. Implements the function
     * <code>IsInTrie()</code> of Section 1.3.1.
     */
    public boolean isInTrie(String s);

    /**
     * Adds the word s to the trie. Implements the function
     * <code>AddToTrie()</code> of Section 1.3.1.
     *
     * @param s the string to be added.
     */
    public void addToTrie(String s);

    /**
     * Returns true if the node <code>p</code> is a leaf of the trie. Implements
     * the function <code>IsLeaf()</code> of Section 1.3.1.
     */
    public boolean isLeaf();

    /**
     * removes the string s from the trie. Implements the function
     * <code>RemoveFromTrie()</code> of Section 1.3.1.
     *
     * @param s the string to be removed
     */
    public void removeFromTrie(String s);

    /**
     * Builds a trie representing the list of strings read from a file line by
     * line.
     *
     * @param name the name of the file
     */
    public void fromFile(String name) throws IOException;
}
