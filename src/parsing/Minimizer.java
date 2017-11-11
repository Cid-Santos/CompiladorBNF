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
public interface Minimizer {

    public DFA minimize(DFA a) throws Exception;

    public DFT minimize(DFT a) throws Exception;

    public IDFA minimize(IDFA a) throws Exception;
}
