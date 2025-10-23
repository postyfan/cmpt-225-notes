package binarytree;

import java.util.*;
import java.util.function.Function;

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
        // TODO implement me
        return -1;
    }


    @Override
    public boolean equals(Object other) {
        // TODO implement me
        return false;
    }


    /**
     * returns the number of vertices at depth k if k<0 throws
     * IllegalArgumentException
     */
    public int countDepthK(int k) {
        // TODO implement me
        return -1;
    }


    /**
     * returns a preOrder iterator for the tree
     */
    public Iterator<T> preOrderIterator() {
        // TODO implement me
        return null;
    }

}
