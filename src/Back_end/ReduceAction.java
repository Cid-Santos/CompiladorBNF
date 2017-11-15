package Back_end;

/**
 *
 * @author Cid
 */
public class ReduceAction extends Action {

    Production rule; // the production to reduce by

    public ReduceAction(Production rule) {
        this.rule = rule;
    }

    @Override
    public int hashCode() {
        return rule.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ReduceAction)) {
            return false;
        }
        ReduceAction o = (ReduceAction) other;
        return rule.equals(o.rule);
    }

    @Override
    public String toString() {
        return "reduce " + rule;
    }
}
