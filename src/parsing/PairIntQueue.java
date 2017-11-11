/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

/**
 *
 * @author Cid
 */
public class PairIntQueue {
     PairIntList front = null, rear = null;
    /**
       Returns true if the pair <code>(val, elem)</code> is in the queue.
    */
    public boolean isIn(int val, int elem){
	for(PairIntList l = front; l != null; l = l.next){
	    if(l.val == val && l.elem == elem)
		return true;
	}
	return false;
    }
    /**
       Adds the pair <code>(val, elem)</code> to the queue.
    */
    public void add(int val, int elem) {
	if(!isIn(val, elem))
	    add(new PairIntList(val, elem, null));
    }
    public void addFast(int val, int elem) {
	    add(new PairIntList(val, elem, null));
    }
    /**
       Adds the head of the list <code>l</code> to the queue.
    */
    public void add(PairIntList l) {
	l.next = null;
	if (front == null)
	    front = rear = l;
	else
	    rear = rear.next = l;
    }
    /**
       Removes the first element of the queue.
       @return the resulting queue (<code>null</code> if the queue
       is empty).
    */
    public PairIntList remove() {
	if (front == null) return null;
	PairIntList result = front;
	if (front == rear)
	    front = rear = null;
	else
	    front = front.next;
	return result;
    }
    /**
       Returns the first element of the queue and removes it.
    */
    public PairInt removeHead() {
	if (front == null) return null;
	PairIntList result = front;
	if (front == rear)
	    front = rear = null;
	else
	    front = front.next;
	return new PairInt(result);
    }
    /**
       Returns <code>true</code> if the queue is empty, which means
       that <code>front</code> is <code>null</code>
    */
    public boolean isEmpty() { return front == null; }
    public String showAI(String name, Alphabet a) {
	return name + " " + PairIntList.showAI(front, a);
    }
    public String showAI(Alphabet a) { return showAI("", a); }
    public String show(String name, Alphabet a) {
	return name + " " + front.show(a);
    }
    public String show(Alphabet a) { return show("", a); }
    
    public static void main(String[] args){
	Alphabet a = new Alphabet(2);
	PairIntQueue q = new PairIntQueue();
	q.add(a.toShort('a'),7);
	System.out.println(q.showAI(a));
    }

}
