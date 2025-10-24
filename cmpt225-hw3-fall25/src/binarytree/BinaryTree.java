package binarytree;

import java.util.*;

public class BinaryTree<T> {

    private BTNode<T> root;

    public BinaryTree(BTNode<T> root) {
        this.root = root;
    }

    public BTNode<T> getRoot() {
        return root;
    }

    public int size() {
        return root.size();
    }

    public int height() {
        return root.height();
    }

    public void printInOrder() {
        root.printInOrder();
    }

    public void printPreOrder() {
        root.printPreOrder();
    }

    public void printPostOrder() {
        root.printPostOrder();
    }

    /**************** Assignment 3 *************************/


    /**
     * returns the number of leaves in the tree
     */
    public int numberOfLeaves() {
        if (root == null) 
            return 0;
        if (root.isLeaf())
            return 1;
        
        int leftLeaves = 0, rightLeaves = 0;
        if (root.getLeftChild() != null) {
            BinaryTree<T> left = new BinaryTree<>(root.getLeftChild());
            leftLeaves = left.numberOfLeaves();
        }
        if (root.getRightChild() != null) {
            BinaryTree<T> right = new BinaryTree<>(root.getRightChild());
            rightLeaves = right.numberOfLeaves();
        }
        return leftLeaves + rightLeaves;
    }


    @Override
    public boolean equals(Object other) {
        // TODO implement me
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        BinaryTree<?> n = (BinaryTree<?>) other;
        return same(root, n.getRoot());
    }

    public boolean same(BTNode<T> b1, BTNode<?> b2) {
        if ( b1 == null && b2 == null)
            return true;
        if (b1 == null || b2 == null)
            return false;
        if (!b1.getData().equals(b2.getData()))
            return false;

        boolean l = same(b1.getLeftChild(), b2.getLeftChild());
        boolean r = same(b1.getRightChild(), b2.getRightChild());

        return l == true && r == true;
    }

    /**
     * returns the number of vertices at depth k if k<0 throws
     * IllegalArgumentException
     */
    public int countDepthK(int k) {
        // TODO implement me
        // Need to use BFS to go to level and count nodes in that level
        LinkedList<BTNode<T>> queue = new LinkedList<>();
        int count = 0, level = 0;
        queue.add(root);

        while (!queue.isEmpty() && level != k+1) {
            count = 0;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                BTNode<T> cur = queue.poll();
                if (level == k)
                    count++;
                
                if (cur.getLeftChild() != null)
                    queue.add(cur.getLeftChild());
                if (cur.getRightChild() != null)
                    queue.add(cur.getRightChild());
            }
            level++;
        }
        return count;
    }


    /**
     * returns a preOrder iterator for the tree
     */
    public Iterator<T> preOrderIterator() {
        // TODO implement me
        return new BTIterator<>(root);
    }

}
