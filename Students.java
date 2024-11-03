import java.util.ArrayList;

public class Students {
    private Node first;

    public Students() {
        first = null;
    }

    public void addStudent(BinaryTree student) {
        Node newNode = new Node(student);
        if (first == null) {
            first = newNode;
        } else {
            Node current = first;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void removeStudent(String name) {
        if (first != null && first.info.getName().equals(name)) {
            first = first.next;
        } else {
            Node current = first;
            while (current.next != null && !current.next.info.getName().equals(name)) {
                current = current.next;
            }
            if (current.next != null) {
                current.next = current.next.next;
            }
        }
    }

    public BinaryTree getStudent(String name) {
        Node current = first;
        while (current != null && !current.info.getName().equals(name)) {
            current = current.next;
        }
        return current == null ? null : current.info;
    }

    public ArrayList<String> getAllStudentsName() {
        ArrayList<String> names = new ArrayList<>();
        Node current = first;
        while (current != null) {
            names.add(current.info.getName());
            current = current.next;
        }
        return names;
    }

    private class Node {
        Node next;
        BinaryTree info;

        public Node(BinaryTree info) {
            this.info = info;
            next = null;
        }
    }
}