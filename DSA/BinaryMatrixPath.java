import java.util.ArrayDeque;
import java.util.Queue;

public class BinaryMatrixPath {
    public static int solve(int[][] grid) {
        int rows = grid.length;
        if (rows == 0 || grid[0].length == 0 || grid[0][0] != 0 || grid[rows - 1][grid[0].length - 1] != 0)
            return -1;
        int columns = grid[0].length;
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] { 0, 0, 1 });
        grid[0][0] = 1;
        while (!queue.isEmpty()) {
            int[] current = queue.remove();
            if (current[0] == rows - 1 && current[1] == columns - 1)
                return current[2];
            for (int[] direction : directions) {
                int row = current[0] + direction[0], column = current[1] + direction[1];
                if (row >= 0 && row < rows && column >= 0 && column < columns && grid[row][column] == 0) {
                    grid[row][column] = 1;
                    queue.add(new int[] { row, column, current[2] + 1 });
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Shortest path: " + solve(new int[][] { { 0, 1 }, { 1, 0 } }));
    }
}
