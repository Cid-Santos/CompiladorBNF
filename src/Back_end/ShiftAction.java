package Back_end;

/**
 *
 * @author Cid
 */
public class ShiftAction extends Action {

    State nextState; // o estado do autômato para mover depois do turno

    public ShiftAction(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public int hashCode() {
        return nextState.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ShiftAction)) {
            return false;
        }
        ShiftAction o = (ShiftAction) other;
        return nextState.equals(o.nextState);
    }

    @Override
    public String toString() {
        return "Troca " + nextState;
    }
}
