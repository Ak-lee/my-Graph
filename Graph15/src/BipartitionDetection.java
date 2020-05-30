import java.util.ArrayList;

public class BipartitionDetection {
    private Graph G;
    private boolean[] visited;
    private int[] colors;
    private boolean isBipartite = true;

    public BipartitionDetection(Graph G) {
        this.G = G;
        visited = new boolean[G.V()];
        colors = new int[G.V()];
        for(int i=0; i<G.V(); i ++) {
            colors[i] = -1;
        }
        for(int v=0; v<G.V(); v ++) {
            if(!visited[v]) {
                if(!dfs(v, 0)) {
                    isBipartite = false;
                    break;
                }
            }
        }
    }

    // 从顶点v开始遍历，顶点 v 的颜色染为 color,返回该子图是否为二分图
    private boolean dfs(int v, int color) {
        visited[v] = true;
        colors[v] = color;
        for(int w : G.adj(v)) {
            if(!visited[w]) {
                if(!dfs(w, 1-color)) return false;
            } else if(colors[w] == colors[v]) return false;
        }
        return true;
    }

    public boolean isBipartite() {
        return  isBipartite;
    }

    public int[] colors() {
        return colors;
    }
}
