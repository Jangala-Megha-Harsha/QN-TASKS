public class SegmentTree {
    private final int[] tree;
    private final int size;

    public SegmentTree(int[] values) {
        size = values.length;
        tree = new int[Math.max(1, size * 4)];
        if (size > 0)
            build(1, 0, size - 1, values);
    }

    private void build(int node, int left, int right, int[] values) {
        if (left == right) {
            tree[node] = values[left];
            return;
        }
        int middle = (left + right) / 2;
        build(node * 2, left, middle, values);
        build(node * 2 + 1, middle + 1, right, values);
        tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
    }

    public void update(int index, int value) {
        checkIndex(index);
        update(1, 0, size - 1, index, value);
    }

    private void update(int node, int left, int right, int index, int value) {
        if (left == right) {
            tree[node] = value;
            return;
        }
        int middle = (left + right) / 2;
        if (index <= middle)
            update(node * 2, left, middle, index, value);
        else
            update(node * 2 + 1, middle + 1, right, index, value);
        tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
    }

    public int query(int left, int right) {
        if (size == 0 || left < 0 || right >= size || left > right)
            throw new IndexOutOfBoundsException();
        return query(1, 0, size - 1, left, right);
    }

    private int query(int node, int left, int right, int queryLeft, int queryRight) {
        if (queryLeft <= left && right <= queryRight)
            return tree[node];
        int middle = (left + right) / 2, result = Integer.MIN_VALUE;
        if (queryLeft <= middle)
            result = Math.max(result, query(node * 2, left, middle, queryLeft, queryRight));
        if (queryRight > middle)
            result = Math.max(result, query(node * 2 + 1, middle + 1, right, queryLeft, queryRight));
        return result;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    public static void main(String[] args) {
        SegmentTree tree = new SegmentTree(new int[] { 1, 5, 2, 4 });
        System.out.println("Initial maximum [0,3]: " + tree.query(0, 3));
        tree.update(1, 0);
        System.out.println("Updated maximum [0,3]: " + tree.query(0, 3));
    }
}
