//Esercizio 6.1
//La classe RegExpEpsilon rappresenta una foglia EPSILON (stringa vuota) nella struttura ad albero di un'espressione regolare
//Riconosce espressioni regolari costituite dalla sola stringa vuota
package dfa2;
public class RegExpEpsilon implements RegExp{

   	private char ch;

	//costruttore
    RegExpEpsilon(){
		this.ch = NFA.EPSILON;
    }

   /* crea un NFA con due stati (q0 iniziale, q1 finale) ed EPSILON come etichetta dell'arco
	* @return NFA generato
	*/
    public NFA compile(){
		NFA a = new NFA(2);
		a.addMove(0, ch, 1);
		a.addFinalState(1);
		return a;
    }
}