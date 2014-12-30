//Esercizio 6.1
//La classe RegExpStar rappresenta un nodo * (chiusura di Kleene) nella struttura ad albero di un'espressione regolare
//Applica la chiusura di Kleene ad un'espressione regolare
package dfa2;

public class RegExpStar implements RegExp {

    private RegExp e1;

    //costruttore
    public RegExpStar(RegExp e1) {
        this.e1 = e1;
    }

    /*
     * Crea un DFA che rappresenta la chiusura di Kleene dell'espressione regolare
     * @return NFA generato
     */
    public NFA compile() {
        NFA a = new NFA(2);
        final int n = a.append(e1.compile());
        a.addMove(0, NFA.EPSILON, 1);
        a.addMove(0, NFA.EPSILON, n);
        a.addMove(n + 1, NFA.EPSILON, n);
        a.addMove(n + 1, NFA.EPSILON, 1);
        a.addFinalState(1);
        return a;
    }
}
