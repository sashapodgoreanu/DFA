package dfa2;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Stack;

/**
 * Un oggetto della classe NFA rappresenta un automa a stati finiti non
 * deterministico con epsilon transizioni
 */
public class NFA {

    /**
     * Usiamo il carattere nullo per rappresentare una epsilon transizione
     */
    public static final char EPSILON = '\0';

    /**
     * Numero degli stati dell'automa. Ogni stato e` rappresentato da un numero
     * interno non negativo, lo stato con indice 0 e` lo stato iniziale.
     */
    private int numberOfStates;

    /**
     * Insieme degli stati finali dell'automa.
     */
    private HashSet<Integer> finalStates;

    /**
     * Funzione di transizione dell'automa, rappresentata come una mappa da
     * mosse a insiemi di stati di arrivo.
     */
    private HashMap<Move, HashSet<Integer>> transitions;

    /**
     * Crea un NFA con un dato numero di stati.
     *
     * @param n Il numero di stati dell'automa.
     */
    public NFA(int n) {
        numberOfStates = n;
        finalStates = new HashSet<Integer>();
        //la funzione di transizione ritorna un insieme di stati per un solo carattere
        transitions = new HashMap<Move, HashSet<Integer>>();
    }

    /**
     * Aggiunge uno stato all'automa.
     *
     * @return L'indice del nuovo stato creato
     */
    public int newState() {
        return numberOfStates++;
    }

    /**
     * Aggiunge uno stato finale.
     *
     * @param p Lo stato che si vuole aggiungere a quelli finali.
     * @return <code>true</code> se lo stato e` valido, <code>false</code>
     * altrimenti.
     */
    public boolean addFinalState(int p) {
        if (validState(p)) {
            finalStates.add(p);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determina se uno stato e` valido oppure no.
     *
     * @param p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` valido, <code>false</code>
     * altrimenti.
     * @see #numberOfStates
     */
    public boolean validState(int p) {
        return (p >= 0 && p < numberOfStates);
    }

    /**
     * Determina se uno stato e` finale oppure no.
     *
     * @param p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` finale, <code>false</code>
     * altrimenti.
     * @see #finalStates
     */
    public boolean finalState(int p) {
        return finalStates.contains(p);
    }

    /**
     * Restituisce il numero di stati dell'automa.
     *
     * @return Numero di stati.
     */
    public int numberOfStates() {
        return numberOfStates;
    }

    /**
     * Aggiunge una transizione all'automa.
     *
     * @param p Lo stato di partenza della transizione.
     * @param ch Il simbolo che etichetta la transizione.
     * @param q Lo stato di arrivo della transizione.
     * @return <code>true</code> se lo stato di partenza e lo stato di arrivo
     * sono validi, <code>false</code> altrimenti.
     */
    public boolean addMove(int p, char ch, int q) {
        if (validState(p) && validState(q)) {
            Move toAdd = new Move(p, ch);
            boolean found = false;
            for (Move aMove : transitions.keySet()) {
                //Se esiste gia una mossa da p leggendo ch che va in x allora aggiungere p leggendo ch che va in q
                if (toAdd.equals(aMove)) {
                    found = true;
                    transitions.get(aMove).add(q);
                    return true;
                }
            }
            //se non esiste gia una mossa da p leggendo ch allora si aggiunge la mossa alle transizioni
            if (!found) {
                HashSet<Integer> resultT = new HashSet<Integer>();
                resultT.add(q);
                transitions.put(toAdd, resultT);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Determina se c'e` uno stato finale in un insieme di stati.
     *
     * @param s L'insieme di stati da controllare.
     * @return <code>true</code> se c'e` uno stato finale in <code>s</code>,
     * <code>false</code> altrimenti.
     * @see #finalStates
     */
    private boolean finalState(HashSet<Integer> s) {
        for (int p : s) {
            if (finalState(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce l'alfabeto dell'automa, ovvero l'insieme di simboli che
     * compaiono come etichette delle transizioni dell'automa. Notare che
     * <code>EPSILON</code> non e` un simbolo.
     *
     * @return L'alfabeto dell'automa.
     */
    public HashSet<Character> alphabet() {
        HashSet<Character> alphabet = new HashSet<Character>();
        for (Move move : transitions.keySet()) {
            if (move.getCh() != NFA.EPSILON) {
                alphabet.add(move.getCh());
            }
        }
        return alphabet;
    }

    /**
     * Esegue una mossa dell'automa.
     *
     * @param p Stato di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Insieme di stati di arrivo dopo la transizione. Questo insieme
     * puo` essere vuoto.
     */
    public HashSet<Integer> move(int p, char ch) {
        HashSet<Integer> move = null;
        Move mosa = new Move(p, ch);
        for (Move key : transitions.keySet()) {
            if (key.equals(mosa)) {
                move = transitions.get(key);
            }
        }
        return move;
    }

    /**
     * Esegue una mossa dell'automa.
     *
     * @param s Insieme di stati di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Insieme di stati di arrivo dopo la transizione. Questo insieme
     * puo` essere vuoto.
     */
    public HashSet<Integer> move(HashSet<Integer> s, char ch) {
        HashSet<Integer> qset = new HashSet<Integer>();
        for (int p : s) {
            System.out.println("from " + p + " with symbols:" + s.toString() + "to:" + move(p, ch));
            if (move(p, ch) != null) {
                qset.addAll(move(p, ch));
            }
        }
        return qset;
    }

    /**
     * Calcola la epsilon chiusura di un insieme di stati dell'automa.
     *
     * @param s Insieme di stati di cui calcolare l'epsilon chiusura.
     * @return Insieme di stati raggiungibili da quelli contenuti in
     * <code>s</code> per mezzo di zero o piu` epsilon transizioni.
     */
    public HashSet<Integer> epsilonClosure(HashSet<Integer> s) {
        HashSet<Integer> qset = new HashSet<Integer>();
        for (int p : s) {
            qset.addAll(epsilonClosure(p));
        }
        return qset;
    }

    /**
     * Calcola la epsilon chiusura di uno stato dell'automa. E` un caso
     * specifico del metodo precedente.
     *
     * @param p Insieme di cui calcolare l'epsilon chiusura.
     * @return Insieme di stati raggiungibili da <code>p</code> per mezzo di
     * zero o piu` epsilon transizioni.
     * @see #epsilonClosure
     */
    public HashSet<Integer> epsilonClosure(int p) {
        HashSet<Integer> epsilonClosure = new HashSet<Integer>();
        epsilonClosure.add(p); //aggiunge questo stato alla chiusura
        for (Move move : transitions.keySet()) { //Se lo stato p ha una transizione epsilon a uno stato q, q si aggiunge a epsilonClosure.
            //Se lo stato q ha a sua volta una transizione epsilon a un'altro stato z allora si chiama ricorsivamente epsilonClosure(z)
            if (move.start == p && move.ch == NFA.EPSILON) {
                HashSet<Integer> res = move(p, NFA.EPSILON);
                for (Integer result : res) {
                    if (move(result, NFA.EPSILON) != null && !epsilonClosure.contains(result)) {
                        epsilonClosure.addAll(epsilonClosure(result));
                    }
                }
                epsilonClosure.addAll(res);
            }
        }
        return epsilonClosure;
    }

    /**
     *
     */
    public static NFA nth(int n) {
        NFA automa = new NFA(n + 1);
        for (int i = 0; i < n - 1; i++) {
            for (int j = i; j < n; j++) {
                automa.addMove(i, EPSILON, j);
                automa.addMove(i, '0', j);
                automa.addMove(i, '1', j);
            }
        }
        for (int i = 0; i < n; i++) {
            automa.addMove(i, '1', n);
        }
        automa.addFinalState(n);
        return automa;
    }

    /**
     * Calcola l'automa a stati finiti deterministico equivalente.
     *
     * @return DFA equivalente.
     */
    public DFA dfa() {
        // la costruzione del DFA utilizza due tabelle hash per tenere
        // traccia della corrispondenza (biunivoca) tra insiemi di
        // stati del NFA e stati del DFA
        HashMap<HashSet<Integer>, Integer> indexOfSet
                = new HashMap<HashSet<Integer>, Integer>();    // NFA -> DFA
        HashMap<Integer, HashSet<Integer>> setOfIndex
                = new HashMap<Integer, HashSet<Integer>>();    // DFA -> NFA

        DFA dfa = new DFA(1);                            // il DFA
        Stack<Integer> newStates = new Stack<Integer>(); // nuovi stati del DFA
        HashSet<Character> alphabet = alphabet();

        indexOfSet.put(epsilonClosure(0), 0); // stati dell'NFA corrisp. a q0
        setOfIndex.put(0, epsilonClosure(0));
        newStates.push(0);                    // nuovo stato da esplorare

        while (!newStates.empty()) { // finche' ci sono nuovi stati da visitare
            final int p = newStates.pop(); // ne considero uno e lo visito
            final HashSet<Integer> pset = setOfIndex.get(p); // stati del NFA corrisp.
            for (char ch : alphabet) { // considero tutte le possibili transizioni
                HashSet<Integer> qset = epsilonClosure(move(pset, ch));
                if (indexOfSet.containsKey(qset)) { // se qset non e` nuovo...
                    final int q = indexOfSet.get(qset); // recupero il suo indice
                    dfa.setMove(p, ch, q);          // aggiungo la transizione
                } else {                            // se invece qset e` nuovo
                    final int q = dfa.newState();   // creo lo stato nel DFA
                    indexOfSet.put(qset, q);        // aggiorno la corrispondenza
                    setOfIndex.put(q, qset);
                    newStates.push(q);              // q e` da visitare
                    dfa.setMove(p, ch, q);          // aggiungo la transizione
                }
            }
        }

        // stabilisco gli stati finali del DFA
        for (int p = 0; p < dfa.getNumberOfStates(); p++) {
            if (finalState(setOfIndex.get(p))) {
                dfa.addFinalState(p);
            }
        }

        return dfa;
    }

    @Override
    public String toString() {
        String a = "NFA{" + "numberOfStates=" + numberOfStates + ", finalStates=" + finalStates + ", transitions=[ ";
        for (Move move : transitions.keySet()) {
            a = a + move.toString() + "->" + transitions.get(move).toString() + " ";
        }
        return a + "]}";
    }

}
