package lazyTrees;
/**
 * Traverser interface containing the method signature for visit
 * @author vincentyao
 *
 * @param <E>
 */
public interface Traverser<E> {
	/**
	 * Defines what to do when visiting x
	 * @param x : E
	 */
	public void visit(E x);
}
