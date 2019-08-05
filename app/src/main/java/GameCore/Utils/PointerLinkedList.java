package GameCore.Utils;

public class PointerLinkedList<T> {
    Node<T> root;
    Node<T> tail;
    Node<T> pointedNode;
    Node<T> NILFRONT;
    Node<T> NILEND;

    public PointerLinkedList() {
        NILFRONT = new Node<>(null);
        NILEND = new Node<>(null);
        NILFRONT.setnext(root);
        NILEND.setprev(tail);
    }


    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
            tail = newNode;
            pointedNode = newNode;
            root.setprev(NILFRONT);
            tail.setnext(NILEND);
        } else if (root == tail) {
            root = newNode;
            tail.setprev(root);
            root.setnext(tail);
            tail.setnext(NILEND);
        } else {
            newNode.setnext(root);
            newNode.setprev(NILFRONT);
            root.setprev(newNode);
            root = newNode;
        }
    }

    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
            tail = newNode;
            pointedNode = newNode;
            root.setprev(NILFRONT);
            tail.setnext(NILEND);
        } else if (root == tail) {
            tail = newNode;
            tail.setprev(root);
            root.setnext(tail);
            tail.setnext(NILEND);
        } else {
            newNode.setprev(tail);
            newNode.setnext(NILEND);
            tail.setnext(newNode);
            tail = newNode;
        }
    }

    public boolean remove() {
        if (isEmpty())
            return false;
        else if (root == tail) {
            root = null;
            tail = null;
            pointedNode = null;
        } else {
            if (pointedNode == root) {
                root = pointedNode.getNext();
                root.setprev(NILFRONT);
            } else if (pointedNode == tail) {
                tail = pointedNode.getPrev();
                tail.setnext(NILEND);
            } else {
                pointedNode.getNext().setprev(pointedNode.getPrev());
                pointedNode.getPrev().setnext(pointedNode.getNext());
            }
        }
        return true;
    }

    public boolean removeAllBefore() {
        if (isEmpty() || pointedNode == NILFRONT || pointedNode == NILEND)
            return false;
        root = pointedNode;
        pointedNode.setprev(NILFRONT);
        return true;
    }

    public boolean removeAllAfter() {
        if (isEmpty() || pointedNode == NILFRONT || pointedNode == NILEND)
            return false;
        tail = pointedNode;
        pointedNode.setnext(NILEND);
        return true;
    }

    public void pointToNext() {
        if (!pointedNode.hasNext())
            return;
        pointedNode = pointedNode.getNext();
    }

    public void pointToPrev() {
        if (!pointedNode.hasPrev())
            return;
        pointedNode = pointedNode.getPrev();
    }

    public T getPointedValue() {
        if (pointedNode == null)
            return null;
        return pointedNode.getValue();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean hasNext() {
        return pointedNode.hasNext();
    }

    public boolean hasPrev() {
        return pointedNode.hasPrev();
    }

    public boolean pointerFrontNil() {
        return pointedNode == NILFRONT;
    }

    public boolean pointerEndNil() {
        return pointedNode == NILEND;
    }

    public boolean nextEndNil() {
        return pointedNode.getNext() == NILEND;
    }

    public boolean prevFrontNil() {
        return pointedNode.getPrev() == NILFRONT;
    }

    public int size() {
        int i = 0;
        Node<T> currentNode = root;
        while (currentNode != null) {
            currentNode = currentNode.getNext();
            i++;
        }
        return i;
    }

    public T get(int index) {
        Node<T> returningNode = root;
        while (index > 0 && returningNode != null) {
            returningNode = returningNode.getNext();
            index--;
        }
        return returningNode == null ? null : returningNode.getValue();
    }

    private class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        public Node(T value) {
            this.value = value;
            next = null;
            prev = null;
        }

        public void setnext(Node<T> next) {
            this.next = next;
        }

        public void setprev(Node<T> prev) {
            this.prev = prev;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public T getValue() {
            return value;
        }

        public boolean hasNext() {
            return next != null;
        }

        public boolean hasPrev() {
            return prev != null;
        }
    }
}
