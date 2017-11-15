package Back_end;

/**
 *
 * @author Cid
 */
public class ShiftAction extends Action {
   State nextState; // the automaton state to move to after the shift
    public ShiftAction(State nextState) {
        this.nextState = nextState;
    }
    public int hashCode() { return nextState.hashCode(); }
    public boolean equals(Object other) {
        if(!(other instanceof ShiftAction)) return false;
        ShiftAction o = (ShiftAction) other;
        return nextState.equals(o.nextState);
    }
    public String toString() {
        return "shift " + nextState;
    }
}
