package lazyTrees;
import java.util.*;
/**
 * LazySearchTree is an implementation of a binary search tree with lazy deletion.
 * @author vincentyao
 *
 * @param <E>
 */
public class LazySearchTree<E extends Comparable< ? super E > > implements Cloneable
{
   protected int mSize; //size of soft tree without deleted nodes
   protected LazySTNode mRoot; //root of the tree
   protected int mSizeHard; //size of hard tree with deleted+non-deleted nodes
   
	private class LazySTNode
	{
	   // use protected access so the tree, in the same package, 
	   // or derived classes can access members 
	   protected LazySTNode lftChild, rtChild;
	   protected E data;
	   protected LazySTNode myRoot; // needed to test for certain error
	   protected boolean deleted; 
	   
	   /**
	    * Constructor initializing variables
	    * @param d : E
	    * @param lft : LazySTNode
	    * @param rt : lazySTNode
	    */
	   public LazySTNode( E d, LazySTNode lft, LazySTNode rt )
	   {
	      lftChild = lft; 
	      rtChild = rt;
	      data = d;
	      deleted = false;
	   }
	   //...
	}
	/**
	 * Default constructor for LazySearchTree
	 */
   public LazySearchTree() 
   { 
      clear(); 
   }
   /**
    * Getter for mSizeHard
    * @return int
    */
   public int sizeHard(){
	   return mSizeHard;
   }
   /**
    * Returns true if LazySearchTree is empty and false otherwise.
    * @return boolean
    */
   public boolean empty() 
   { 
      return (mSize == 0); 
   }
   /**
    * Getter for mSize
    * @return int
    */
   public int size() 
   { 
      return mSize; 
   }
   /**
    * Clears the LazySearchTree
    */
   public void clear() 
   { 
      mSize = 0; 
      mSizeHard = 0;
      mRoot = null; 
   }
   /**
    * Finds x in the LazySearchTree
    * @param x : E
    * @return E
    */
   E find( E x )
   {
      LazySTNode resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }
   /**
    * Protected helper method for finding x in LazySearchTree.
    * @param root : LazySTNode
    * @param x : E
    * @return LazySTNode
    */
   protected LazySTNode find( LazySTNode root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      else if(root.deleted == true){
    	  return null;
      }
      return root;
   }
   /**
    * Returns true if x is in LazySearchTree and false otherwise.
    * @param x : E
    * @return boolean
    */
   boolean contains(E x)  
   { 
       return find(mRoot, x) != null; 
   }
   /**
    * Finds min in LazySearchTree
    * @return E
    */
   public E findMin() 
   { 
	   if (mRoot == null)
	         throw new NoSuchElementException();
	   return findMin(mRoot).data;
   }
   /**
    * Protected helper method for finding min in LazySearchTree
    * @param root : LazySTNode
    * @return LazySTNode
    */
   protected LazySTNode findMin( LazySTNode root ) 
   {
      if (root == null)
         return null;
      if (root.lftChild == null && !root.deleted)
         return root;
      else if(root.lftChild == null && root.deleted){
    	  return findMin(root.rtChild);
      }
      return findMin(root.lftChild);
   }
   /**
    * Finds max in LazySearchTree
    * @return E
    */
   public E findMax() 
   {
	   if(mRoot == null)
		   throw new NoSuchElementException();
	   return findMax(mRoot).data;
   }
   /**
    * Protected helper method for finding max in LazySearchTree
    * @param root : LazySTNode
    * @return LazySTNode
    */
   public LazySTNode findMax( LazySTNode root){
	   if (root == null)
		   return null;
	   if(root.rtChild == null && !root.deleted)
		   return root;
	   else if(root.rtChild == null && root.deleted){
		   return findMax(root.lftChild);
	   }
	   else if(root.rtChild.deleted == true && root.rtChild.rtChild == null){
		   return root;
	   }
	   return findMax(root.rtChild);
   }
   // ... and so on ...
   /**
    * Inserts x into LazySearchTree
    * @param x : E
    * @return boolean
    */
   public boolean insert( E x )
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }
   /**
    * Protected helper method for inserting x into LazySearchTree
    * @param root : LazySTNode
    * @param x : E
    * @return LazySTNode
    */
   protected LazySTNode insert( LazySTNode root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()
      
      if (root == null)
      {
         mSize++;
         mSizeHard++;
         return new LazySTNode(x, null, null);
      }
      
      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         root.lftChild = insert(root.lftChild, x);
      else if ( compareResult > 0 )
         root.rtChild = insert(root.rtChild, x);
      else if (root.deleted == true){
    	  root.deleted = false;
    	  mSize++;
      }
      return root;
   }
   /**
    * Soft removes x from LazySearchTree
    * @param x
    * @return
    */
   public boolean remove( E x )
   {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }
   /**
    * Protected helper for soft removing x from LazySearchTree (toggling delete)
    * @param root : LazySTNode
    * @param x : E
    */
   protected void remove( LazySTNode root, E x  )
   {
      int compareResult;  // avoid multiple calls to compareTo()
     
      if (root == null)
         return;

      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         remove(root.lftChild, x);
      else if ( compareResult > 0 )
         remove(root.rtChild, x);
      // found the node
      else 
      { 
         root.deleted = true;
         mSize--;
      }
   }
   /**
    * Hard removes deleted nodes from LazySearchTree by creating a newTree without them.
    * @param root : LazySTNode
    * @param newTree : LazySearchTree<E>
    * @return LazySearchTree<E>
    */
   private LazySearchTree<E> removeHard(LazySTNode root, LazySearchTree<E> newTree){
	   if(root == null)
		   return newTree;
	   if(root.deleted == false){
		  newTree.insert(root.data);
	   }
	   removeHard(root.lftChild, newTree);
	   removeHard(root.rtChild, newTree);
	   return newTree;
   }

   /**
    * Hard traverse through all deleted/non-deleted nodes of LazySearchTree
    * @param func : F
    */
   public < F extends Traverser< ? super E > > 
   void traverseHard(F func)  
   {
	   traverseH(func, mRoot); 
   }
  /**
   * Protected helper for hard traverse through all deleted/non-deleted nodes of LazySearchTree.
   * @param func : F
   * @param treeNode : LazySTNode
   */
   protected < F extends Traverser< ? super E > > 
   void traverseH(F func, LazySTNode treeNode)
   {
      if (treeNode == null)
         return;
      traverseH(func, treeNode.lftChild);
      func.visit(treeNode.data);
      traverseH(func, treeNode.rtChild);
   }
   /**
    * Soft traverse not including deleted nodes of LazySearchTree
    * @param func : F
    */
   public < F extends Traverser< ? super E > > 
   void traverseSoft(F func)  
   { 
	   traverseS(func, mRoot); 
   }
   /**
    * Protected helper for soft traverse not including deleted nodes of LazySearchTree
    * @param func : F
    * @param treeNode : LazySTNode
    */
   protected < F extends Traverser< ? super E > > 
   void traverseS(F func, LazySTNode treeNode)
   {
      if (treeNode == null)
         return;

      traverseS(func, treeNode.lftChild);
      if(!treeNode.deleted){
          func.visit(treeNode.data); 
      }
      traverseS(func, treeNode.rtChild);
   }
}

