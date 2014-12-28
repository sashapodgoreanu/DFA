/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.main;

import dfa2.NFA;
/**
 *
 * @author SashaAlexandru
 */
public class TestNFA {
    public static void main(String args[]){
        NFA nfa5 = new NFA(7);
        nfa5.addMove(0, 'a', 1);
        nfa5.addMove(0, 'b', 5);
        nfa5.addMove(0, 'a', 2);
        nfa5.addMove(0, NFA.EPSILON, 6);
        nfa5.addMove(0, NFA.EPSILON, 0);
        nfa5.addMove(0, 'a', 0);
        nfa5.addMove(1, 'b', 2);
        nfa5.addMove(1, NFA.EPSILON, 4);
        nfa5.addMove(0, NFA.EPSILON, 3);
        nfa5.addMove(5, NFA.EPSILON, 1);
        nfa5.addMove(3, NFA.EPSILON, 2);
        nfa5.addMove(2, NFA.EPSILON, 4);
        nfa5.addMove(3, 'b', 4);
        nfa5.addFinalState(2);
        nfa5.addFinalState(4);
        nfa5.addFinalState(6);
        System.out.println(nfa5.toString());
        System.out.println(nfa5.alphabet());
        System.out.println(nfa5.move(0, 'a'));
        System.out.println(nfa5.epsilonClosure(5));
        System.out.println(nfa5.epsilonClosure(nfa5.move(0, 'b')));
        System.out.println(nfa5.dfa().toDot());
        System.out.println(nfa5.dfa().minimize().toDot());
        
        System.out.println("alfabet"+NFA.nth(3).alphabet().toString());
        System.out.println(NFA.nth(3).dfa().minimize().toDot());
        //System.out.println(NFA.nth(3).dfa());
    }
    
}
