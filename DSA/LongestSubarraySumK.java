import java.util.HashMap;
import java.util.Map;

public class LongestSubarraySumK {
    public static int solve(int[] values, int k) {
        Map<Long, Integer> firstIndex = new HashMap<>();
        firstIndex.put(0L, -1);
        long prefix = 0;
        int best = 0;
        for (int i = 0; i < values.length; i++) {
            prefix += values[i];
            firstIndex.putIfAbsent(prefix, i);
            Integer start = firstIndex.get(prefix - k);
            if (start != null)
                best = Math.max(best, i - start);
        }
        return best;
    }

    public static void main(String[] args) {
        int[] values = { 1, -1, 5, -2, 3 };
        System.out.println("Longest subarray length: " + solve(values, 3));
    }
}
