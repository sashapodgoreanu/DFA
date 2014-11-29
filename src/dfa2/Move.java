/*
 * Un oggetto della calsse Move rapresenta una mossa di un'automa a stati finiti
 * ovvero una coppia costituita da uno stato di partenza e da un simbolo 
 * dell'alfabeto dell'automa
 */
package dfa2;

/**
 *
 * @author SashaAlexandru
 * 
 */
public class Move {
    final int start; // lo stato di partenza
    final char ch;//il simbolo che etichetta la transizione

    /**
     * Crea una mossa con lo stato di partenza start e etichetta di transizione ch
     * @param start Lo stato di partenza
     * @param ch Il simbolo che etichetta la transizione
     */
    public Move(int start, char ch) {
        this.start = start;
        this.ch = ch;
    }

    @Override
    public int hashCode() {
        return start ^ (int)ch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Move other = (Move) obj;
        if (this.start != other.start) {
            return false;
        }
        if (this.ch != other.ch) {
            return false;
        }
        return true;
    }

    public int getStart() {
        return start;
    }

    public char getCh() {
        return ch;
    }
  
}
