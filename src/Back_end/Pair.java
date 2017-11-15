package Back_end;

/**
 *
 * @author Cid
 * @param <A>
 * @param <B>
 */
public class Pair <A,B>{
     public Pair( A o1, B o2 ) { this.o1 = o1; this.o2 = o2; }
     @Override
    public int hashCode() {
        return o1.hashCode() + o2.hashCode();
    }
     @Override
    public boolean equals( Object other ) {
        if( other instanceof Pair ) {
            Pair p = (Pair) other;
            return o1.equals( p.o1 ) && o2.equals( p.o2 );
        } else return false;
    }
     @Override
    public String toString() {
        return "Pair "+o1+","+o2;
    }
    public A getO1() { return o1; }
    public B getO2() { return o2; }

    protected A o1;
    protected B o2; 
}
