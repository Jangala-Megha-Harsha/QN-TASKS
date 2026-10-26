public class CycleLinkedList {
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public static boolean removeCycle(Node head) {
        if (head == null)
            return false;
        Node slow = head, fast = head;
        do {
            if (fast == null || fast.next == null)
                return false;
            slow = slow.next;
            fast = fast.next.next;
        } while (slow != fast);
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        while (fast.next != slow)
            fast = fast.next;
        fast.next = null;
        return true;
    }

    public static void main(String[] args) {
        Node first = new Node(1), second = new Node(2), third = new Node(3);
        first.next = second;
        second.next = third;
        third.next = second;
        System.out.println("Cycle removed: " + removeCycle(first));
        System.out.println("Tail points to: " + third.next);
    }
}
