public class MinimumWindow {
    public static String solve(String source, String required) {
        if (source.isEmpty() || required.isEmpty())
            return "";
        int[] need = new int[Character.MAX_VALUE + 1];
        for (char c : required.toCharArray())
            need[c]++;
        int missing = required.length(), left = 0, bestStart = 0, bestLength = Integer.MAX_VALUE;
        for (int right = 0; right < source.length(); right++) {
            char current = source.charAt(right);
            if (need[current] > 0)
                missing--;
            need[current]--;
            while (missing == 0) {
                if (right - left + 1 < bestLength) {
                    bestStart = left;
                    bestLength = right - left + 1;
                }
                char removed = source.charAt(left++);
                need[removed]++;
                if (need[removed] > 0)
                    missing++;
            }
        }
        return bestLength == Integer.MAX_VALUE ? "" : source.substring(bestStart, bestStart + bestLength);
    }

    public static void main(String[] args) {
        System.out.println("Minimum window: " + solve("ADOBECODEBANC", "ABC"));
    }
}
