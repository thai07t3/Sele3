package utils;

import java.util.List;

public class SortUtils {
    /**
     * Check whether a list is sorted in ascending order (A-Z, 0-9) or not.
     *
     * @param list list to check
     * @return true if the list is sorted in ascending order, false otherwise
     */
    public static <T extends Comparable<? super T>> boolean isSortedAsc(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        for (int i = 0; i < list.size() - 1; i++) {
            T current = list.get(i);
            T next = list.get(i + 1);

            if (current.compareTo(next) > 0) {
                return false; // element at index i is GREATER THAN element at index i + 1 → Not sorted
            }
        }
        return true;
    }

    /**
     * Check whether a list is sorted in descending order (Z-A, 9-0) or not.
     *
     * @param list list to check
     * @return true if the list is sorted in descending order, false otherwise
     */
    public static <T extends Comparable<? super T>> boolean isSortedDesc(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        for (int i = 0; i < list.size() - 1; i++) {
            T current = list.get(i);
            T next = list.get(i + 1);

            if (current.compareTo(next) < 0) {
                return false; // element at index i is LESS THAN element at index i + 1 → Not sorted
            }
        }
        return true;
    }
}
