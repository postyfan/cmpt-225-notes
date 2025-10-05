package assignment2;

import java.util.*;

/**
 *
 * This is a generic class representing a list of objects.
 * The operations on the list are as follows:
 * - adding and removing elements from the left and from the right.
 * - reversing the list
 * - getting the size of the list
 * 
 * **All operations must run in O(1) time.**
 */
public class MyLinkedList<T> {

	public class MyLinkedListNode<T> {
		T data;
		MyLinkedListNode<T> next;
		// one param
		MyLinkedListNode(T data) {
			this.data = data;
			this.next = null;
		}
		// two param
		MyLinkedListNode(T data, MyLinkedListNode next) {
			this.data = data;
			this.next = next;
		}
		// getter
		MyLinkedListNode<T> getNext() {
			return this.next;
		}
	}

	private MyLinkedListNode<T> head;
	private MyLinkedListNode<T> tail;
	private int length;

	/**
	 * The constructor creates an empty list
	 */
	public MyLinkedList() {
		head = null;
		tail = null;
		length = 0;
	}

	/**
	 * Adds the new item to the left of the list. 
	 */
	public void addLeft(T item) {
		// newNode points to head
		MyLinkedListNode<T> newNode = new MyLinkedListNode<T>(item, this.head);
		// point head to newNode
		this.head = newNode;
		if (length==0)
			this.tail = newNode;
		length++;
	}

	/**
	 * Adds the new item to the right of the list. 
	 */
	public void addRight(T item) {
		MyLinkedListNode<T> newNode = new MyLinkedListNode<T>(item, null);
		// if length == 0
		if (length == 0) {
			this.head = newNode;
			this.tail = newNode;
		}
		this.tail.next = newNode;
		this.tail = newNode;
		length++;
	}

	/**
	 * Removes the leftmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeLeft() {
		T temp;
		if (this.length == 0) {
			throw new NoSuchElementException("List is empty");
		}

		if (this.head == this.tail) {
			temp = this.head.data;
			this.head.data = null;
			return temp;
		}

		temp = this.head.data;
		this.head = this.head.next;
		length--;
		return temp;
	}

	/**
	 * Removes the rightmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeRight() {
		if (this.length == 0)
			throw new NoSuchElementException("List is empty");

		T temp = this.tail.data;

		if (this.head == this.tail) {
			this.head.data = null;
			return temp;
		}

		this.tail = this.head;
		for (int i = 0; i < this.length-1; i++) {
			this.tail = this.tail.next;
		}

		length --;

		return null;
	}


	/**
	 * Reverses the list
	 */
	public void reverse() {}

	/**
	 * Returns the size of the list.
	 */
	public int size() {
		return -1;
	}

	/**
	 * Returns true if list is empty, and returns false otherwise.
	 */
	public boolean isEmpty() {
		return false;
	}

}
