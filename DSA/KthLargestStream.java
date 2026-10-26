import java.util.PriorityQueue;

public class KthLargestStream {
    private final int k;
    private final PriorityQueue<Integer> heap = new PriorityQueue<>();

    public KthLargestStream(int k, int[] initial) {
        if (k <= 0)
            throw new IllegalArgumentException("k must be positive");
        this.k = k;
        for (int value : initial) {
            heap.offer(value);
            if (heap.size() > k)
                heap.poll();
        }
    }

    public Integer add(int value) {
        heap.offer(value);
        if (heap.size() > k)
            heap.poll();
        return heap.size() == k ? heap.peek() : null;
    }

    public static void main(String[] args) {
        KthLargestStream stream = new KthLargestStream(3, new int[] {});
        System.out.println(stream.add(4));
        System.out.println(stream.add(5));
        System.out.println(stream.add(8));
        System.out.println(stream.add(10));
    }
}
