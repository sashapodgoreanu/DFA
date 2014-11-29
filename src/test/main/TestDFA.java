/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.main;

import dfa2.DFA;

/**
 *
 * @author SashaAlexandru
 */
public class TestDFA {

    public static void main(String[] args) {

        /**
         * Esercizio 2.2. Aggiungere alla classe DFA un metodo scan che accetta
         * una stringa s e ritorna true se s e riconosciuta dall’automa, ` false
         * altrimenti. Scrivere un semplice programma di prova che crea una
         * istanza della classe DFA, costruisce la rappresentazione del DFA
         * mostrato in Figura 1, e verifica se una stringa data in input
         * (passata dalla linea di comando o letta da tastiera) e o meno
         * riconosciuta dall’automa.
         */
        System.out.println("Esercizio 2.2.");
        DFA dfa22 = new DFA(4);
        //Aggiungi le transizioni all'automa
        dfa22.setMove(0, '1', 0);
        dfa22.setMove(0, '0', 1);
        dfa22.setMove(1, '0', 2);
        dfa22.setMove(2, '0', 3);
        dfa22.setMove(1, '1', 0);
        dfa22.setMove(2, '1', 0);
        dfa22.setMove(3, '1', 3);
        dfa22.setMove(3, '0', 3);
        //Aggiungi stati finali all'automa
        dfa22.addFinalState(3);
        //Stampa l'automa 2.2 in formato Graphviz
        System.out.println(dfa22.toDot());
        //Verifica se l'automa 2.2 valida la stringa
                System.out.println(dfa22.scan("00000")? "- Input: OK" : "- Input: NOPE");
                System.out.println(dfa22.scan("1")? "- Input: OK" : "- Input: NOPE");
                System.out.println(dfa22.scan("1000000000000")? "- Input: OK" : "- Input: NOPE");
        System.out.println("- Alfabeto dell'Automa" + dfa22.alphabet().toString());
        /**
         * **************************************************************
         */

        /*
         Esercizio 2.3. Ripetere l’esercizio precedente con il DFA dell’esercizio 1.2.
         */
        System.out.println("Esercizio 2.3.");
        DFA dfa23 = new DFA(8);
        //Aggiungi le transizioni all'automa
        dfa23.setMove(0, '+', 1);
        dfa23.setMove(0, '-', 1);
        dfa23.setMove(5, '+', 6);
        dfa23.setMove(5, '-', 6);
        dfa23.setMove(0, '.', 3);
        for (int i = 48; i < 58; i++) {
            dfa23.setMove(0, (char)i, 2);
            dfa23.setMove(1, (char)i, 2);
            dfa23.setMove(2, (char)i, 2);
            dfa23.setMove(3, (char)i, 4);
            dfa23.setMove(4, (char)i, 4);
            dfa23.setMove(5, (char)i, 7);
            dfa23.setMove(7, (char)i, 7);
            dfa23.setMove(6, (char)i, 7);
        }

        dfa23.setMove(1, '.', 3);
        dfa23.setMove(2, '.', 3);
        dfa23.setMove(2, 'e', 5);
        dfa23.setMove(3, 'e', 5);
        dfa23.setMove(4, 'e', 5);

   
        //Aggiungi stati finali all'automa
        dfa23.addFinalState(2);
        dfa23.addFinalState(4);
        dfa23.addFinalState(7);
        //Stampa l'automa 2.3 in formato Graphviz
        System.out.println(dfa23.toDot());
        //Verifica se l'automa 2.3 valida la stringa
        System.out.println(dfa23.scan("1.e2")? "- Input: OK" : "- Input: NOPE");
        System.out.println(dfa23.complete()? "- Automa Completa: SI" : "Automa Completa: NOPE");
        System.out.println("- Alfabeto dell'Automa" + dfa23.alphabet().toString());
        /**
         * **************************************************************
         */
        
        /**
         * 3.1
         */
         System.out.println("Esercizio 2.3.");
        DFA dfa31 = new DFA(5);
        //Aggiungi le transizioni all'automa
        dfa31.setMove(0, '0', 1);
        dfa31.setMove(0, '1', 2);
        dfa31.setMove(1, '0', 1);
        //da comentare questo
       dfa31.setMove(1, '1', 3);
        /****//////
        dfa31.setMove(3, '0', 3);
        dfa31.setMove(3, '1', 3);
        dfa31.setMove(4, '0', 3);
        dfa31.setMove(4, '1', 2);
        dfa31.setMove(2, '0', 2);
        dfa31.setMove(2, '1', 2);
        dfa31.addFinalState(3);
        System.out.println(dfa31.toDot());
        System.out.println("Stati raggiungibili da 0" + dfa31.reach(0).toString());
       
        /*System.out.println("Stati raggiungibili da 2" + dfa31.reach(2).toString());
        System.out.println("Stati raggiungibili da 1" + dfa31.reach(1).toString());
        System.out.println("Stati raggiungibili da 3" + dfa31.reach(3).toString());
        System.out.println("Stati raggiungibili da 4" + dfa31.reach(4).toString());
        System.out.println(dfa31.empty()? "- Riconosce il linguaggio vuoto" : "- Non riconosce il linguaggio vuoto");
        System.out.println(dfa31.empty());
        System.out.println("Stati Pozzo"+dfa31.sink().toString());

   */
        
        
        DFA d = new DFA(10);
        //Aggiungi le transizioni all'automa
        d.setMove(0, '0', 1);
        d.setMove(0, '1', 4);
        d.setMove(0, '2', 2);
        d.setMove(3, '0', 1);
        d.setMove(7, '0', 3);
        d.setMove(9, '0', 0);
        d.setMove(5, '0', 2);
        d.setMove(5, '1', 8);
        d.setMove(6, '0', 5);
        d.addFinalState(5);
        d.addFinalState(7);
        d.addFinalState(7);
        System.out.println(d.toDot());
        System.out.println("Stati raggiungibili da 0" + d.reach(0).toString());
        System.out.println("Stati raggiungibili da 1" + d.reach(1).toString());
        System.out.println("Stati raggiungibili da 2" + d.reach(2).toString());
        System.out.println("Stati raggiungibili da 3" + d.reach(3).toString());
        System.out.println("Stati raggiungibili da 4" + d.reach(4).toString());
        System.out.println("Stati raggiungibili da 5" + d.reach(5).toString());
        System.out.println("Stati raggiungibili da 6" + d.reach(6).toString());
        System.out.println("Stati raggiungibili da 7" + d.reach(7).toString());
        System.out.println("Stati raggiungibili da 8" + d.reach(8).toString());
        System.out.println("Stati raggiungibili da 9" + d.reach(9).toString());


     

    }

}