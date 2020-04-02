package GameCore.Utils;

import java.util.HashSet;
import java.util.List;

/**
 * some useful functions for working with lists and sets
 */
public class Util {

    /**
     * creates intersection of set1 and set2 and saves it to set1
     *
     * @param set1
     * @param set2
     * @param <T>
     */
    public static <T> void intersectionToSet1(HashSet<T> set1, HashSet<T> set2) {
        for (T element : set1) {
            if (!set2.contains(element)) {
                set1.remove(element);
            }
        }
    }

    /**
     * add a list to a list without making duplicates
     *
     * @param addedTo
     * @param toBeAdded
     * @param <T>
     */
    public static <T> void addWithoutDuplicates(List<T> addedTo, List<T> toBeAdded) {
        if (addedTo == null | toBeAdded == null)
            return;
        outer:
        for (int i = 0; i < toBeAdded.size(); i++) {
            T item = toBeAdded.get(i);
            for (int j = 0; j < addedTo.size(); j++) {
                if (addedTo.get(j).equals(item))
                    continue outer;
            }
            addedTo.add(item);
        }
    }

    /**
     * adds an element to a list only if it is not already in the list
     *
     * @param addedTo
     * @param toBeAdded
     * @param <T>
     */
    public static <T> void addWithoutDuplicates(List<T> addedTo, T toBeAdded) {
        if (!addedTo.contains(toBeAdded))
            addedTo.add(toBeAdded);
    }

    /**
     * removes all items in list 1 that are not in list 2 making list 1 a intersection of both
     *
     * @param list1
     * @param list2
     * @param <T>
     */
    public static <T> void intersectListTo1(List<T> list1, List<T> list2) {
        if (list1 == null)
            return;
        if (list2 == null) {
            list1.clear();
            return;
        }
        int origSize = list1.size();
        outer:
        for (int i = 0; i < list1.size(); i++) {
            T item = list1.get(i);
            for (int j = 0; j < list2.size(); j++) {
                if (list2.get(j).equals(item))
                    continue outer;
            }
            list1.remove(item);
            i--;
        }
    }

    /**
     * removes all items that are equal according to the equals method in list1, can be of different types
     *
     * @param list1
     * @param list2
     * @param <T>
     * @param <Z>
     */
    public static <T, Z> void subtract(List<T> list1, List<Z> list2) {
        int origSize = list1.size();
        outer:
        for (int i = 0; i < list1.size(); i++) {
            T item = list1.get(i);
            for (int j = 0; j < list2.size(); j++) {
                if (item.equals(list2.get(j))) {
                    list1.remove(item);
                    i--;
                    continue outer;
                }
            }
        }
    }
}
