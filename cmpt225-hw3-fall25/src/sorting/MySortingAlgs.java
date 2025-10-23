package sorting;

import java.util.Arrays;
import java.util.Stack;

public class MySortingAlgs {

    public static void sortStrings(String[] a) {
        // TODO implement me
    }


	/**
	 * Assumption arr[start...mid-1] is sorted and arr[mid...end] is sorted.
     * The function merges the two parts into a sorted subarray arr[start...end]
     * All elements outside arr[start...end] remain unchanged
	 */
	public static <T extends Comparable<T>> void merge(T[] arr, int start, int mid, int end) {
        // TODO implement me
	}


	/**
	 * sorts the subarray arr[start...end] using the Merge Sort algorithm
	 */
    public static <T extends Comparable<T>> void mergeSort(T[] arr, int start, int end) {
        if (start == end)
            return;

        int mid = (start + end) / 2;
        mergeSort(arr, start, mid-1);
        mergeSort(arr, mid, end);
        merge(arr, start, mid, end);
    }

    /**
     * sorts the subarray arr using the Merge Sort algorithm
     */
    public static <T extends Comparable<T>> void mergeSort(T[] arr) {
        mergeSort(arr,0,arr.length-1);
    }

}
