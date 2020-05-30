import java.util.LinkedList;
import java.util.Queue;

public class Solution {

    private int[][] dirs = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1},
                            {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
    private int R, C;

    public int shortestPathBinaryMatrix(int[][] grid) {
        R = grid.length;
        C = grid[0].length;
        boolean[][] visited = new boolean[R][C];
        int[][] dis = new int[R][C];    // 记录到每一个顶点所找到的最短路径的值


        if(grid[0][0] == 1) return -1;
        if(R == 1 && C == 1) return 1;

        // BFS
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0][0] =  true;
        dis[0][0] = 1;
        while(!queue.isEmpty()) {
            int cur = queue.remove();
            int curx = cur / C;
            int cury = cur % C;
            for(int d = 0; d < 8; d ++) {
                int nextx = curx + dirs[d][0];
                int nexty = cury + dirs[d][1];
                if(inArea(nextx, nexty) && grid[nextx][nexty] == 0 && !visited[nextx][nexty]) {
                    queue.add(nextx * C + nexty);
                    visited[nextx][nexty] = true;
                    dis[nextx][nexty] = dis[curx][cury] + 1;

                    if(nextx == R - 1 && nexty == C - 1) {
                        return dis[nextx][nexty];
                    }
                }
            }
        }
        return -1;
    }
    private boolean inArea(int x, int y) {
        return x >= 0 && x < R && y >= 0 && y < C;
    }
}
