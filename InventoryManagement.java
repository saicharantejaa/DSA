class SegmentTree {

    int[] tree;
    int[] lazy;
    int n;

    SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        lazy = new int[4 * n];
        build(arr, 1, 0, n - 1);
    }

    void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;

            build(arr, 2 * node, start, mid);
            build(arr, 2 * node + 1, mid + 1, end);

            tree[node] = Math.max(tree[2 * node],
                                  tree[2 * node + 1]);
        }
    }

    void updateLazy(int node, int start, int end) {

        if (lazy[node] != 0) {

            tree[node] += lazy[node];

            if (start != end) {
                lazy[2 * node] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
            }

            lazy[node] = 0;
        }
    }

    void rangeUpdate(int node, int start, int end,
                     int left, int right, int value) {

        updateLazy(node, start, end);

        if (start > right || end < left)
            return;

        if (start >= left && end <= right) {

            tree[node] += value;

            if (start != end) {
                lazy[2 * node] += value;
                lazy[2 * node + 1] += value;
            }

            return;
        }

        int mid = (start + end) / 2;

        rangeUpdate(2 * node, start, mid,
                left, right, value);

        rangeUpdate(2 * node + 1, mid + 1, end,
                left, right, value);

        tree[node] = Math.max(tree[2 * node],
                              tree[2 * node + 1]);
    }

    int rangeMaxQuery(int node, int start, int end,
                      int left, int right) {

        updateLazy(node, start, end);

        if (start > right || end < left)
            return Integer.MIN_VALUE;

        if (start >= left && end <= right)
            return tree[node];

        int mid = (start + end) / 2;

        int p1 = rangeMaxQuery(2 * node, start, mid,
                left, right);

        int p2 = rangeMaxQuery(2 * node + 1, mid + 1, end,
                left, right);

        return Math.max(p1, p2);
    }
}

public class InventoryManagement {

    public static void main(String[] args) {

        int[] stock = {
                120, 150, 180, 200,
                170, 140, 220, 190,
                250, 300, 280, 260,
                240, 210, 230, 270
        };

        SegmentTree st = new SegmentTree(stock);

        System.out.println("ONLINE STORE INVENTORY MANAGEMENT");
        System.out.println("--------------------------------");

        System.out.println("\nInitial Maximum Stock (Sections 1-16): "
                + st.rangeMaxQuery(1, 0, 15, 0, 15));

        // Add 50 products to sections 2-7
        st.rangeUpdate(1, 0, 15, 1, 6, 50);

        System.out.println("\nAdded +50 products to Sections 2-7");

        int max1 = st.rangeMaxQuery(1, 0, 15, 0, 7);

        System.out.println("Maximum Stock in Sections 1-8: "
                + max1);

        // Add 100 products to sections 9-16
        st.rangeUpdate(1, 0, 15, 8, 15, 100);

        System.out.println("\nAdded +100 products to Sections 9-16");

        int max2 = st.rangeMaxQuery(1, 0, 15, 8, 15);

        System.out.println("Maximum Stock in Sections 9-16: "
                + max2);

        int overallMax = st.rangeMaxQuery(1, 0, 15, 0, 15);

        System.out.println("\nOverall Maximum Stock: "
                + overallMax);

        System.out.println("\nTime Complexity:");
        System.out.println("Range Update         : O(log n)");
        System.out.println("Range Maximum Query  : O(log n)");
        System.out.println("Lazy Propagation     : O(log n)");
    }
}