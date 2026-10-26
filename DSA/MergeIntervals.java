import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeIntervals {
    public static int[][] solve(int[][] intervals) {
        if (intervals.length == 0)
            return new int[0][];
        int[][] sorted = Arrays.stream(intervals).map(int[]::clone).toArray(int[][]::new);
        Arrays.sort(sorted, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> merged = new ArrayList<>();
        int start = sorted[0][0], end = sorted[0][1];
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] <= end)
                end = Math.max(end, sorted[i][1]);
            else {
                merged.add(new int[] { start, end });
                start = sorted[i][0];
                end = sorted[i][1];
            }
        }
        merged.add(new int[] { start, end });
        return merged.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(solve(new int[][] { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 9, 12 } })));
    }
}
