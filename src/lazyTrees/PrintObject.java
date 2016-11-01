package lazyTrees;
/**
 * Functor class to traverse and print out data from LazySearchTree.
 * @author vincentyao
 *
 * @param <E>
 */
public class PrintObject<E> implements Traverser<E> {
	/**
	 * Defines what to do when visiting x
	 * 
	 * @param x : E
	 */
	public void visit(E x){
		System.out.print(x);
	}
}
