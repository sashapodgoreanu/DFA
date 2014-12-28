package dfa2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Un oggetto della calsse DFA rapresenta un automa a stati finiti
 * deterministico
 *
 * @author SashaAlexandru
 */
public class DFA {

    /**
     * Numero degli stati della automa. Ogni stato e rapresentato da un numero
     * non negativo. Lo stato con numero 0 e lo stato iniziale.
     */
    private int numberOfStates;
    /**
     * Numero degli stati finali della automa
     */
    private HashSet<Integer> finalStates;
    /**
     * Funzione di transazione dell'automa rapresentato come una mappa di mosse
     * a stati di arrivo <code>HashMap<Key, Value></code>
     */
    private HashMap<Move, Integer> transitions;

    /**
     * Crea un DFA con n numero di stati
     *
     * @param n numero di stati dell'automa
     */
    public DFA(int n) {
        this.numberOfStates = n;
        finalStates = new HashSet<>();
        transitions = new HashMap<>();
    }

    /**
     * aggiunge uno stato alla automa
     *
     * @return l'indice dello stato creato
     */
    public int newState() {
        return numberOfStates++;
    }

    /**
     * Aggiunge una transizione all'automa
     *
     * @param p LO stato di partenza della transizione
     * @param ch IL simbolo che etichetta la transizine
     * @param q LO stato di arrivo della transizione
     * @return true se lo stato di partenza p e lo stato di arrivo q sono validi
     * altrimenti false
     *
     */
    public boolean setMove(int p, char ch, int q) {
        if (validState(p) && validState(q)) {
            //System.out.println("Valid");
            transitions.put(new Move(p, ch), q);
            return true;
        }
        return false;
    }

    /**
     * Aggiunge all'automa uno stato finale
     *
     * @param p Lo stato che si vuole aggiungere a quelli finali.
     * @return true se lo stato p e valido altrimenti false
     */
    public boolean addFinalState(int p) {
        if (validState(p)) {
            finalStates.add(p);
            return true;
        }
        return false;
    }

    /**
     * Determina se uno stato è valido opure no
     *
     * @param p Lo stato da verificare
     * @return true se p esiste altrimenti false
     */
    public boolean validState(int p) {
        return (p < numberOfStates && p > -1);
    }

    /**
     * Determina se p è tra i stati finali
     *
     * @param p Lo satto da verificare
     * @return true se p è uno stato finale
     */
    public boolean finalState(int p) {
        return finalStates.contains(p);
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    /**
     * Restituisce l’alfabeto dell’automa, ovvero l’insieme di simboli che
     * compaiono come etichette delle transizioni dell’automa.
     *
     * @return L’alfabeto dell’automa.
     */
    public HashSet<Character> alphabet() {
        HashSet<Character> alphabet = new HashSet<>();
        for (Move m : transitions.keySet()) {
            alphabet.add(m.ch);
        }
        return alphabet;
    }

    /**
     * Esegue una mosa dallo stato p leggendo il simbolo c
     *
     * @param p Lo stato di partenza prima della transizione
     * @param c simbolo da riconoscere
     * @return Stato di arrivo dopo la transizione oppure -1 se l'automa non ha
     * una transizione eticetata con il simbolo c dallo stato p
     */
    public int move(int p, char c) {
        //System.out.println("Mooving from state " + p + " with transition symbol: " + c);
        Move move = new Move(p, c);
        if (transitions.containsKey(move)) {
            return transitions.get(move);
        } else {
            return -1;
        }

    }

    public boolean scan(String s) {
        System.out.println("Scanning " + s);
        int i = 0;
        int p = 0;
        while (i < s.length() && p > -1) {
            char ch = s.charAt(i);
            i++;
            int q = move(p, ch);
            p = q;
        }
        return finalState(p);
    }

    /*
     * Verifica se la funzione di transizione è completa, ossia definita per tutti gli stati dell’automa ed i simboli del suo alfabeto di riferimento.
     * @return True se la funzione di transizione è definita per tutti gli stati ed i simboli dell'alfabeto, False altrimenti.
     */
    public boolean complete() {
        HashSet<Character> input = alphabet();			//ottengo l'alfabeto dei caratteri in input per l'automa
        for (int i = 0; i < numberOfStates; i++) {		//ciclo su tutti gli stati dell'automa e, per ogni stato, su tutti i caratteri dell'alfabeto
            for (Character ch : input) {
                if (move(i, ch) == -1) //se vado nello stato di errore (-1) non esiste una transizione
                {
                    return false;						//etichettata 'ch' a partire dallo stato 'i', quindi return false
                }
            }
        }
        return true;
    }

    public String toDot() {

        String diGraph = "digraph DFA{\n";
        diGraph = diGraph + "node [color=blue4, shape = circle]; \n";
        diGraph = diGraph + "rankdir=LR; \n";
        diGraph = diGraph + "secret_node [style=invis] \n";
        if (!transitions.isEmpty()) {
            diGraph = diGraph + "secret_node -> 0;\n";
        }

        /**
         * Riempiamo una lista con i stati del'automa. esempio funzione di
         * transizione: (0,a) -> 1, (0,)b -> 1, (0,c)->2 Allora la lista sara
         * [{(0,1),(a,b)}, {(0,2),(c)}]
         */
        HashMap<Pair<Integer, Integer>, ArrayList<Character>> lista = new HashMap<>();
        //loop tra tutti i elementi della hash map transitions
        for (Map.Entry<Move, Integer> entry : transitions.entrySet()) {
            Move move = entry.getKey();
            Integer endNode = entry.getValue();
            Integer startNode = move.getStart();
            Character ch = move.getCh();
            Pair pair = new Pair(startNode, endNode);
            boolean toAdd = true;
            for (Map.Entry<Pair<Integer, Integer>, ArrayList<Character>> coppia : lista.entrySet()) {
                Pair<Integer, Integer> pairTocompare = coppia.getKey();
                if (pair.equals(pairTocompare)) {
                    toAdd = false;
                    coppia.getValue().add(ch);
                }
            }
            if (toAdd) {
                ArrayList<Character> listCharToAdd = new ArrayList<>();
                listCharToAdd.add(ch);
                lista.put(pair, listCharToAdd);
            }
        }
        //System.out.println("********************************************");
        for (Map.Entry<Pair<Integer, Integer>, ArrayList<Character>> coppia : lista.entrySet()) {
            Pair<Integer, Integer> pairToStamp = coppia.getKey();
            //System.out.println(pairToStamp.toString() + " : " + coppia.getValue().toString());
        }
        //System.out.println("********************************************");

        for (Map.Entry<Pair<Integer, Integer>, ArrayList<Character>> coppia : lista.entrySet()) {
            Pair<Integer, Integer> coppiaStati = coppia.getKey();
            ArrayList<Character> listaChar = coppia.getValue();

            Integer startNode = coppiaStati.getKey();
            Integer endNode = coppiaStati.getValue();
            diGraph = diGraph + startNode + " -> " + endNode + "[label = <<font color=\"darkgreen\">" + listaChar.toString() + "</font>>]; \n";
        }
        //diGraph = diGraph + startNode.start + " -> " + endNode + "[label = <<font color=\"darkgreen\">" + startNode.getCh() + "</font>>]; \n";
        for (Integer toEnd : finalStates) {
            diGraph = diGraph + toEnd + " [shape = \"doublecircle\"]; \n";
        }
        diGraph = diGraph + "}";
        return diGraph;
    }

    public HashMap<Move, Integer> getTransitions() {
        return transitions;
    }

    /**
     * Esercizio 3.1 (parte del esercizio) Il metodo empty che ritorna true se e
     * solo se l’automa riconosce il linguaggio vuoto (si ricorda che il
     * linguaggio vuoto corrisponde all’insieme vuoto, e non e uguale a ` {ε},
     * il linguaggio che contiene la stringa vuota).
     *
     * Per tutti i stati finali il metodo verifica se sono raggiungibili
     */
    /*
     TO - DO
     */
    public boolean empty() {
        if (finalStates.isEmpty()) {
            return true;
        }
        //raggiungibili contiene tutti i stati che si possono raggiungere dallo stato iniziale
        HashSet<Integer> raggiungibili = reach(0);
        //raggiungibili contera la intersezione tra raggiungibili e finalStates
        raggiungibili.retainAll(finalStates);
        //se la intersezione tra raggiungibili e finalStates e vuoto allora l'automa riconosce il linguaggio vuoto
        if (raggiungibili.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Il metodo restituisce tutti i stati pozzo dell'automa
     *
     * @return HashSet di interi che rapresentano i stati dell'automa
     */
    public HashSet<Integer> sink() {
        HashSet<Integer> statiPozzo = new HashSet<>();
        //per ogni stato dell'automa se verifica se puo raggiungere uno stato finale
        for (int i = 0; i < numberOfStates; i++) {
            HashSet<Integer> raggiungibili = reach(i);
            raggiungibili.retainAll(finalStates);
            //se la intersezione tra tutti gli stati raggiungibili da i e i finalState e vuota alllora i è uno stato pozo
            if (raggiungibili.isEmpty()) {
                statiPozzo.add(i);
            }
        }
        return statiPozzo;
    }

    /**
     * Il metodo restituisce una HashMap che ha come chiavi i simboli delle
     * transizioni possibili partendo da uno stato dell'automa e come valori i
     * stati di arrivo eseguendo la transazione.
     *
     * @param q uno stato dell'automa
     * @return una mappa con tutte le transizioni possibili e i stati di arrivo
     * per ogni transizioni dello stato q
     */
    private HashMap<Character, Integer> getTransitions(int q) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (Map.Entry<Move, Integer> entry : transitions.entrySet()) {
            Move m = entry.getKey();
            if (m.start == q) {
                map.put(m.getCh(), entry.getValue());
            }
        }
        return map;
    }

    /**
     * Dato uno stato q restituisce l'insieme raggiungibili degli statti
     * partendo da q Si considera sempre raggiungibile lo stesso stato dato. q e
     * raggiungibile da q con il simbolo di transizione epsilon
     *
     * @param q uno statto dell'automa
     * @return Insieme non vuoto di stati raggiungibili partendo da q.
     *
     */
    public HashSet<Integer> reach(int q) {
        //espanso contiene tutti i stati che sono stati visitati
        HashSet<Integer> espanso = new HashSet<>();
        //r[i] = true se esiste un camino da q a ialtrimenti r[i] = false
        boolean[] r = new boolean[numberOfStates];
        r[q] = true;
        boolean nuovoStato = true;
        //Loop finche non sono piu stati da esplorare
        while (nuovoStato) {
            //per ogni stato dell'automa si esplora stato i se è accessibile da q e non e stato esplorato
            for (int i = 0; i < numberOfStates; i++) {
                if (r[i] == true && !espanso.contains(i)) {
                    nuovoStato = false;
                    espanso.add(i);
                    //mappa: partendo da stato i e leggendo simbolo c -> stato
                    HashMap<Character, Integer> possibleTransitions = getTransitions(i);
                    //Se ci sono transizioni dallo stato i legendo qualunque simbolo
                    if (!possibleTransitions.isEmpty()) {
                        for (Character key : possibleTransitions.keySet()) {
                            //si aggiunge a r[j] = true se esiste transizione da i a j
                            r[move(i, key)] = true;
                            // se esiste transizione da i a i legendo simbolo c, ed i è gia stato esplorato, 
                            // i non è considerato como uno stato inesplorato
                            if (!espanso.contains(possibleTransitions.get(key))) {
                                nuovoStato = true;
                            }
                        }
                    }
                }
            }
        }
        return espanso;
    }

    public HashSet<Integer> reach2(Integer q) {
        boolean[] r = new boolean[numberOfStates];	  	//array di booleani associati agli stati dell'automa
        HashSet<Integer> s = new HashSet<Integer>();  	//HashSet da riempiere e restituire al termine
        HashSet<Character> input = alphabet();		  	//ottengo l'alfabeto dei caratteri in input per l'automa

        for (int i = 0; i < r.length; i++) {			  	//inizializzo l'array degli stati: false = stato non raggiungibile, true = stato raggiungibile
            if (i == q) {								  	//lo stato q è sempre raggiungibile da sè stesso, attraverso il percorso etichettato con la stringa vuota.
                r[i] = true;
                s.add(i);
            } else {
                r[i] = false;    					  	//assumiamo inizialmente che tutti gli altri stati non siano raggiungibili da q
            }
        }

        boolean newStateFound = true;				  	//booleano di supporto: indica la presenza di nuovi stati da valutare
        while (newStateFound) //ciclo finchè ho nuovi stati per i quali devo valutare la raggiungibilità
        {
            newStateFound = false;					  	//all'inizio di ogni ciclo non ho ancora trovato nuovi stati
            for (int i = 0; i < r.length; i++) {		  	//scorro tutto l'array dei booleani associati agli stati dell'automa
                if (r[i] == true) {					  	//se l'elemento i-esimo dell'array è true significa che lo stato i è raggiungibile
                    for (Character ch : input) {	      	//per tutti i possibili caratteri dell'alfabeto in input
                        Integer j = move(i, ch);		//eseguo la mossa leggendo ch a partire dallo stato i e metto in j lo stato di destinazione ottenuto
                        if (j != -1 && r[j] == false) {	//se la mossa è valida (j!=1) e non ho ancora valutato lo stato j...
                            newStateFound = true;		//ho trovato un nuovo stato
                            r[j] = true;				//marco j come raggiungibile
                            s.add(j);					//aggiungo j all'HashSet degli stati raggiungibili
                        }
                    }
                }
            }
        }
        return s;
    }

    /**
     * Esercizio 3.2. Implementare un metodo samples che ritorna un insieme di
     * stringhe campione accettate dall’automa, una per ogni stato finale
     * dell’automa. Suggerimento: La struttura del metodo samples e
     * fondamentalmente identica a quella del metodo ` reach: basta raffinare
     * l’algoritmo di raggiungibilita in modo da tenere traccia, per ogni stato
     * ` p raggiungibile dallo stato iniziale q0 dell’automa, di un esempio di
     * stringa w che consente di raggiungere p da q0, ovvero tale che ˆδ(q0, w)
     * = p. Per fare cio, cambiare il vettore ` r in modo che contenga stringhe
     * invece che boolean e in questo vettore usare il valore null invece che
     * false per marcare gli stati irraggiungibili.
     *
     */
    public HashSet<String> sample() {
        HashSet<Integer> espanso = new HashSet<>();
        HashSet<String> result = new HashSet<>();
        String[] r = new String[numberOfStates];
        r[0] = "";
        for (int i = 1; i < numberOfStates; i++) {
            r[i] = null;
        }

        int numeroStatiScoperti = 1;
        while (numeroStatiScoperti > 0) {
            for (int i = 0; i < numberOfStates; i++) {
                if (r[i] != null && !espanso.contains(i)) {
                    numeroStatiScoperti--;
                    espanso.add(i);
                    HashMap<Character, Integer> possibleTransitions = getTransitions(i);
                    if (!possibleTransitions.isEmpty()) {
                        for (Character key : possibleTransitions.keySet()) {
                            r[move(i, key)] = r[i] + Character.toString(key);
                            if (!espanso.contains(possibleTransitions.get(key))) {
                                numeroStatiScoperti++;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < numberOfStates; i++) {
            if (finalStates.contains(i)) {
                result.add(r[i]);
            }
        }
        return result;
    }

    public void completeDFA() {
        numberOfStates++;
        for (int i = 0; i < numberOfStates; i++) {
            HashMap<Character, Integer> transizionOfi = getTransitions(i);
            Set<Character> keys = transizionOfi.keySet();
            HashSet<Character> allKeys = alphabet();
            for (Character verify : allKeys) {
                if (!keys.contains(verify)) {
                    setMove(i, verify, numberOfStates - 1);
                }
            }
        }
    }

    /**
     * Metodo che implementa il algoritmo minimize. Il metodo minimiza un DFA modificando la struttura del'automa.
     */
    public void minimize() {
        //1-allocare la matrice
        boolean[][] eq = new boolean[numberOfStates][numberOfStates];
        //eq[i][j] = true se i e j sono stati entrambi finali o entrambi nonfinali
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                if (finalStates.contains(i)) {
                    if (finalStates.contains(j)) {
                        eq[i][j] = true;
                    } else {
                        eq[i][j] = false;
                    }
                } else if (finalStates.contains(j)) {
                    eq[i][j] = false;
                } else {
                    eq[i][j] = true;
                }
            }
        }

        //per ogni ch: eq[i][j] = false dove eq[move(i,ch)][move(j,ch)]= false.
        HashSet<Character> alfabeto = alphabet();
        boolean newEq = true;
        while (newEq) {
            newEq = false;
            for (int i = 0; i < numberOfStates; i++) {
                for (int j = 0; j < numberOfStates; j++) {
                    for (Character ch : alfabeto) {
                        if (eq[i][j] == true  && eq[move(i, ch)][move(j, ch)] == false) {
                            eq[i][j] = false;
                            eq[j][i] = false;
                            newEq = true;
                        }
                    }
                }
            }
        }

        // fix equivalenze. Se esiste eq[1][2] = true allora eq[1][1] = false ed eq[2][2] = false
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                if (eq[i][j] == true && i != j) {
                    eq[i][i] = eq[j][j] = false;
                }
            }
        }

        //riempi array con equivalenze
        ArrayList<Pair<Integer, Integer>> m = new ArrayList<>();
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j <= i; j++) {
                if (eq[i][j] == true) {
                    Pair<Integer, Integer> p = new Pair(i, j);
                    m.add(p);
                }
            }
        }

        //crea DFA nuova minima 
        DFA minima = new DFA(m.size());
        for (Pair<Integer,Integer> i : m) {
            for (Character ch : alphabet()) {
                int dest = move(i.getValue(), ch);
                for (int k = 0; k < m.size(); k++) {
                    if(m.get(k).getKey() == dest || m.get(k).value == dest){
                        minima.setMove(m.indexOf(i), ch, k);
                    }
                }
            }
            if (finalStates.contains(i.getValue())) {
                minima.addFinalState(m.indexOf(i));
            }
        }
        //rende this minima
        this.finalStates = minima.finalStates;
        this.numberOfStates = minima.numberOfStates;
        this.transitions = minima.transitions;
    }

    public DFA minimize2() {
        //PASSO 1: inizializzazione della tabella (matrice) dell'algoritmo
        int n = numberOfStates;
        boolean[][] eq = new boolean[n][n];		//matrice di boolean usata nell'algoritmo "riempi-tabella"
        for (int i = 0; i < n; i++) {					//scorro tutti gli elementi della matrice con gli indici i e j per inizializzarli
            for (int j = 0; j < n; j++) {
                if (finalState(i) && finalState(j) || !finalState(i) && !finalState(j)) {
                    eq[i][j] = true;				//se gli stati i e j sono entrambi finali
                } else //o entrambi non finali li marco come non distinguibili (true)
                {
                    eq[i][j] = false;				//altrimenti li marco come stati distinguibili (false)
                }
            }
        }
        //PASSO 2: esecuzione dell'algoritmo di minimizzazione che modifica e completa la tabella
        HashSet<Character> input = alphabet();		//ottengo l'alfabeto dei caratteri in input per l'automa
        boolean newCoupleFind = true;				//booleano di supporto: indica la presenza di una nuova coppia di stati da controllare
        while (newCoupleFind) {						//finchè scopro nuove coppie di stati da valutare...
            newCoupleFind = false;
            for (int i = 0; i < n; i++) {				//scorro gli elementi della matrice
                for (int j = 0; j < n; j++) {
                    for (Character ch : input) {	    //per tutti i possibili caratteri dell'alfabeto in input
                        if (eq[i][j] == true && eq[move(i, ch)][move(j, ch)] == false) {	//se due stati i e j sono non distinguibili fra loro, ma leggendo
                            eq[i][j] = false;											//ch a partire da i e j arrivo in una coppia di stati distinguibili,
                            newCoupleFind = true;										//allora anche i e j sono a loro volta distinguibili
                        }
                    }
                }
            }
        }
        //PASSO 3: creazione dell'automa minimo
        int m[] = new int[n];						//vettore di supporto per indicare le classi di equivalenza degli stati. Ogni elemento di m rappresenta uno stato dell'automa di partenza
        for (int i = 0; i < n; i++) {					//scorro il vettore e la matrice.
            boolean rappMinTrovato = false;			//Scegliamo come rappresentate canonico di una classe di equivalenza lo stato della classe con indice piu' piccolo
            //il booleano indica se per ogni stato l'ho gia' trovato
            for (int j = 0; j < n; j++) {
                if (eq[i][j] == true) {				//se gli stati i e j dell'automa originale non sono distinguibili...
                    if (j <= i && !rappMinTrovato) {	//se j è uno stato con indice minore/uguale a i e non ho ancora trovato il rappresentante di indice minimo...
                        m[i] = j;					//imposto j come rappresentatne canonico per lo stato i
                        rappMinTrovato = true;		//ho trovato il rappresentante per i
                    }
                }
            }
        }
        //PASSO 3.1: definizione delle transazioni e degli stati finali dell'automa minimizzato
        int k = getMax(m);							//trovo il rappresentante con indice massimo
        if (k != m.length) {							//se il rappresentante con indice max è diverso da m.length (cioè dal numero degli stati dell'automa originale)...
            DFA minDfa = new DFA(k + 1);				//allora l'automa è minimizzabile e avra' k+1 stati
            for (int i = 0; i < m.length; i++) {		//scorro l'array dei rappresentati
                for (Character ch : input) {			//per tutti i caratteri in input
                    int j = move(i, ch);				//metto in j la destinazione della mossa a partire dallo stato i leggendo ch
                    if (validState(j)) //se la mossa è valida
                    {
                        minDfa.setMove(m[i], ch, m[j]);	//allora nel nuovo automa ci sara' una transizione da dal rappresentante di i a quello di j etichettata ch
                    }
                }
                if (finalState(i)) //se lo stato i dell'automa originale era finale
                {
                    minDfa.addFinalState(m[i]);		//allora lo sara' anche la classe di equivalenza a cui appartiene
                }
            }
            return minDfa;							//restituisco l'automa
        }
        return this;								//se l'automa non è minimizzabile lo sostituisco cosi' com'è
    }
    
    public boolean equivalentTo(DFA automa){
		if(!this.alphabet().equals(automa.alphabet()))				//se i DFA hanno alfabeti diversi, sicuramente riconoscono linguaggi distinti
			return false;											//e pertanto non sono equivalenti
		DFA dfa1 = this.minimize2();									//altrimenti minimizzo entrambi gli automi
		automa = automa.minimize2();
		if(!dfa1.getTransitions().equals(automa.getTransitions()))	//confronto le transazioni degli automi minimi ottenuti
			return false;											//se sono diverse i DFA non sono equivalenti
		return true;		//se non sono entrato negli if, allora i DFA sono equivalenti
	}
    

    //metodo di supporto per cercare il valore max in un array
    private int getMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;

    }

    /**
     * <p>
     * A convenience class to represent name-value pairs.</p>
     *
     * @since JavaFX 2.0
     */
    private static class Pair<K, V> {

        /**
         * Key of this <code>Pair</code>.
         */
        private K key;

        /**
         * Gets the key for this pair.
         *
         * @return key for this pair
         */
        public K getKey() {
            return key;
        }

        /**
         * Value of this this <code>Pair</code>.
         */
        private V value;

        /**
         * Gets the value for this pair.
         *
         * @return value for this pair
         */
        public V getValue() {
            return value;
        }

        /**
         * Creates a new pair
         *
         * @param key The key for this pair
         * @param value The value to use for this pair
         */
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        /**
         * <p>
         * <code>String</code> representation of this <code>Pair</code>.</p>
         *
         * <p>
         * The default name/value delimiter '=' is always used.</p>
         *
         * @return <code>String</code> representation of this <code>Pair</code>
         */
        @Override
        public String toString() {
            return key + "=" + value;
        }

        /**
         * <p>
         * Generate a hash code for this <code>Pair</code>.</p>
         *
         * <p>
         * The hash code is calculated using both the name and the value of the
         * <code>Pair</code>.</p>
         *
         * @return hash code for this <code>Pair</code>
         */
        @Override
        public int hashCode() {
            // name's hashCode is multiplied by an arbitrary prime number (13)
            // in order to make sure there is a difference in the hashCode between
            // these two parameters:
            //  name: a  value: aa
            //  name: aa value: a
            return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
        }

        /**
         * <p>
         * Test this <code>Pair</code> for equality with another
         * <code>Object</code>.</p>
         *
         * <p>
         * If the <code>Object</code> to be tested is not a <code>Pair</code> or
         * is <code>null</code>, then this method returns
         * <code>false</code>.</p>
         *
         * <p>
         * Two <code>Pair</code>s are considered equal if and only if both the
         * names and values are equal.</p>
         *
         * @param o the <code>Object</code> to test for equality with this
         * <code>Pair</code>
         * @return <code>true</code> if the given <code>Object</code> is equal
         * to this <code>Pair</code> else <code>false</code>
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key != null ? !key.equals(pair.key) : pair.key != null) {
                    return false;
                }
                if (value != null ? !value.equals(pair.value) : pair.value != null) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return "DFA{" + "numberOfStates=" + numberOfStates + ", finalStates=" + finalStates + ", transitions=" + transitions + '}';
    }

}
