package GameCore.Utils;

import java.util.ArrayList;
import java.util.HashSet;

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

    public static <T> void addWithoutDuplicates(ArrayList<T> addedTo, ArrayList<T> toBeAdded) {
        if (addedTo == null | toBeAdded == null)
            return;
        for (int i = 0; i < toBeAdded.size(); i++) {
            T item = toBeAdded.get(i);
            if (addedTo.contains(item))
                continue;
            addedTo.add(item);
        }
    }

    public static <T> void intersectArrayListTo1(ArrayList<T> list1, ArrayList<T> list2) {
        if (list1 == null)
            return;
        if (list2 == null) {
            list1.clear();
            return;
        }
        for (int i = 0; i < list1.size(); i++) {
            T item = list1.get(i);
            if (!list2.contains(item))
                list1.remove(item);
        }
    }

    public static <T> void addWithoutDuplicates(ArrayList<T> addedTo, T toBeAdded) {
        if (!addedTo.contains(toBeAdded))
            addedTo.add(toBeAdded);
    }
}
