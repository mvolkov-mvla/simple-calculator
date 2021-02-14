import java.util.ArrayList;
import java.util.EmptyStackException;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericStack.
 *
 * @param <E> the element type
 */

/**
 * @author Matvey Volkov
 *
 */
public class GenericStack<E> {

	/** The stack. */
	private ArrayList<E> stack;
	
	/**
	 * Instantiates a new generic stack.
	 */
	public GenericStack() {
		stack = new ArrayList<E>();
	}
	
	/*
	 * You need to implement the following functions
	 * a) isEmpty() - returns true if the element is empty
	 * b) getSize() - returns the size of the Stack
	 * c) peek() - returns the object that is at the top of the stack
	 * d) pop() - gets the object at the top of stack, then removes it from 
	 *            the stack and returns the object
	 * e) push(o) - adds the object to the top of stack.
	 */
	
	
	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return this.stack.size();
	}
	
	/**
	 * Peek.
	 *
	 * @return the e
	 */
	public E peek() {
		if (this.stack.isEmpty()) {
			throw new EmptyStackException();
		} else {
			return this.stack.get(this.stack.size() - 1);
		}
	}
	
	/**
	 * Pop.
	 *
	 * @return the e
	 */
	public E pop() {
		E objectPoppedOff = this.peek();
		this.stack.remove(this.stack.size() - 1);
		return objectPoppedOff; 
	}
	
	/**
	 * Push.
	 *
	 * @param o the o
	 * @return the e
	 */
	public E push(E o) {
		stack.add(o);
		return o;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		if (stack.isEmpty()) {
			return "stack: []";
		} else {
			String returnString = "stack: [";
			for (int i = 0; i < stack.size(); i++) {
				returnString += (stack.get(i).toString() + ", ");
			}
			returnString = returnString.substring(0, returnString.length() - 2) + "]";
			
			return returnString;
		}
	}
	
}
