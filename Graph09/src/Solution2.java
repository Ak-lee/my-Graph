import java.util.Arrays;

// leetcode 980
// 基于记忆化搜索
class Solution2 {
    private int[][] grid;
    private int R, C;
    private int start, end;
    private int[][] dirs = {{-1, 0},{0, 1}, {1, 0},  {0, -1}};
    private int[][] memo;

    public int uniquePathsIII(int[][] grid) {

        this.grid = grid;
        R = grid.length;
        C = grid[0].length;
        int left = R * C;
        memo = new int[1 << (R*C)][R*C];

        for(int i = 0; i < memo.length; i ++) {
            Arrays.fill(memo[i], -1);
        }

        for(int i = 0; i < R; i ++) {
            for(int j = 0; j < C; j ++) {
                if(grid[i][j] == 1) {
                    start = i * C + j;
                    grid[i][j] = 0;
                } else if(grid[i][j] == 2) {
                    end = i * C + j;
                    grid[i][j] = 0;
                } else if(grid[i][j] == -1) {
                    left --;
                }
            }
        }

        int visited = 0;
        return dfs(visited, start, left);
    }

    // 在 visited 这样的状态下，我们从 v 顶点出发，还要经过left个不同的顶点。返回我们能找到的哈密尔顿路径的数量
    int dfs(int visited ,int v, int left) {

        if(memo[visited][v] != -1) {
            return memo[visited][v];
        }
        visited += (1 << v);
        left --;

        // 递归终止条件
        if(left == 0 && v == end) {
            visited -= (1 << v);
            memo[visited][v] = 1;
            return 1;
        }

        int res = 0;
        int x = v / C, y = v % C;
        for(int d = 0; d < 4; d ++) {
            int nextx = x + dirs[d][0];
            int nexty = y + dirs[d][1];
            int next = nextx * C + nexty;
            if(inArea(nextx, nexty) && grid[nextx][nexty] == 0 && (visited & (1 << next))== 0) {
                res += dfs(visited, next, left);
            }
        }

        visited -= (1<<v);
        memo[visited][v] = res;
        return res;
    }

    private boolean inArea(int x, int y) {
        return x >= 0 && x < R && y >= 0 && y < C;
    }
}