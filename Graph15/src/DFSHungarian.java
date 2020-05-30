import java.util.*;

public class DFSHungarian {
    private Graph G;
    private int maxMatching = 0;
    private int[] matching;
    private boolean[] visited;

    public DFSHungarian(Graph G) {
        BipartitionDetection bd = new BipartitionDetection(G);
        if(!bd.isBipartite()) { // 若图不是二分图
            throw new IllegalArgumentException("Hungarian only works for bipartite graph.");
        }
        this.G = G;

        int[] colors = bd.colors();

        matching = new int[G.V()];  // matching[i] 表示与节点i已经匹配的点
        Arrays.fill(matching, -1);  // matching[i] 初始化为-1，表示未匹配
        visited = new boolean[G.V()];
        for(int v=0; v<G.V(); v ++) {
            if(colors[v] == 0 && matching[v] == -1) {    // 我们只遍历左侧的还未匹配的点
                Arrays.fill(visited, false);
                if(dfs(v)) maxMatching ++;  // 如果寻找到增广路径，则maxMatching 增1
            }
        }
    }

    private boolean dfs(int v) {
        visited[v] = true;
        for(int u : G.adj(v)) {
            if(!visited[u]) {
                visited[u] = true;
                if(matching[u] == -1 || dfs(matching[u])) {
                    matching[v] = u;
                    matching[u] = v;
                    return true;
                }
            }
        }
        return false;
    }

    public int maxMatching() {
        return maxMatching;
    }
    public boolean isPerfectMatching() {
        return maxMatching * 2 == G.V();
    }

    public static void main(String[] args) {
        Graph g = new Graph("g.txt");
        DFSHungarian hungarian = new DFSHungarian(g);
        System.out.println(hungarian.maxMatching());

        Graph g2 = new Graph("g2.txt");
        DFSHungarian hungarian2 = new DFSHungarian(g2);
        System.out.println(hungarian2.maxMatching());
    }
}

