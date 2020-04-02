package GameCore.Utils;

import java.util.List;

/**
 * generic simple iterator
 *
 * @param <T>
 */
public class CustomIterator<T> {
    private List<? extends T> list;
    private T singleItem;
    private boolean singleElement;


    public CustomIterator(List<? extends T> list) {
        if (list.size() == 1) {
            singleElement = true;
            singleItem = list.get(0);
        } else
            this.list = list;
    }

    public CustomIterator(T item) {
        singleItem = item;
        singleElement = true;
    }

    public boolean hasNext() {
        if (singleElement)
            return !(singleItem == null);
        else {
            return list.size() > 0;
        }
    }

    public T getNext() {
        assert (hasNext());
        if (singleElement) {
            T out = singleItem;
            singleItem = null;
            return out;
        } else {
            return list.remove(0);
        }
    }
}