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

	public class MyLinkedListNode {
		T data;
		MyLinkedListNode next;
		MyLinkedListNode prev;
		// one param
		MyLinkedListNode(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
		// two param
		MyLinkedListNode(T data, MyLinkedListNode next) {
			this.data = data;
			this.next = next;
			this.prev = null;
		}
		// getter
		public MyLinkedListNode getNext() {
			return this.next;
		}
		
		public MyLinkedListNode getPrev() {
			return this.prev;
		}
	}

	private MyLinkedListNode head;
	private MyLinkedListNode tail;
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
		MyLinkedListNode newNode = new MyLinkedListNode(item, this.head);
		if (length == 0) {
			this.head = newNode;
			this.tail = newNode;
			this.head.next = null;
		}
		else {
			this.head.prev = newNode;
			this.head = newNode;
		}
		length++;
	}

	/**
	 * Adds the new item to the right of the list. 
	 */
	public void addRight(T item) {
		MyLinkedListNode newNode = new MyLinkedListNode(item, null);
		// if length == 0
		if (length == 0) {
			this.head = newNode;
			this.tail = newNode;
		}
		else {
			this.tail.next = newNode;
			this.tail.next.prev = this.tail;
			this.tail = newNode;
		}
		length++;
	}

	/**
	 * Removes the leftmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeLeft() {
		// when nothing to remove -> length == 0
		if (this.length == 0) {
			throw new NoSuchElementException("List is empty");
		}
		T temp = this.head.data;
		if (this.length == 1) {
			this.head = null;
			this.tail = null;
		}
		else {
			this.head = this.head.next;
			this.head.prev = null;
		}
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
		if (this.length == 1) {
			this.head = null;
			this.tail = null;
		}
		else {
			this.tail = this.tail.prev;
			this.tail.next = null;
		}
		length--;
		return temp;
	}


	/**
	 * Reverses the list
	 */
	public void reverse() {
		
	}

	/**
	 * Returns the size of the list.
	 */
	public int size() {
		return length;
	}

	/**
	 * Returns true if list is empty, and returns false otherwise.
	 */
	public boolean isEmpty() {
		return length==0;
	}

}
