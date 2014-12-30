//Esercizio 6.1
//La classe RegExpEmpty rappresenta un'espressione regolare che descrive l'insieme vuoto.
package dfa2;
public class RegExpEmpty implements RegExp{

   private char ch;

	//costruttore
    RegExpEmpty(char ch){
		this.ch = ch;
    }

   /* crea un NFA con due stati (q0 iniziale, q1 finale).
    * non � presente la addMove: l'automa riconosce l'insieme vuoto quindi non ci sono archi fra i due stati (lo stato finale non � pertanto raggiungibile)
	* @return NFA generato
	*/
    public NFA compile(){
		NFA a = new NFA(2);
		a.addFinalState(1);
		return a;
    }
}