import java.util.ArrayList;

// CC connection component 联通分量
public class CC {
    private Graph G;
    private int[] visited;
    private int ccount = 0;

    public CC(Graph G) {
        if(G.isDirected()) {
            // CC 类只能用于求解无向图的联通分量。
            throw  new IllegalArgumentException("CC only works in undirected graph.");
        }
        this.G = G;
        visited = new int[G.V()];
        for(int i=0; i<visited.length; i ++) {
            visited[i] = -1;
        }
        for(int v=0; v<G.V(); v ++) {
            if(visited[v] == -1) {
                dfs(v, ccount);
                ccount ++;
            }
        }
    }
    private void dfs(int v, int ccid) {
        visited[v] = ccid;
        for(int w : G.adj(v)) {
            if(visited[w] == -1) {
                dfs(w, ccid);
            }
        }
    }

    public int count() {
        return ccount;
    }

    public boolean isConnected(int v, int w) {
        G.validateVertex(v);
        G.validateVertex(w);
        return visited[v] == visited[w];
    }

    public ArrayList<Integer>[] components() {
        ArrayList<Integer>[] res = new ArrayList[ccount];
        for(int i=0; i<ccount; i ++) {
            res[i] = new ArrayList<>();
        }
        for(int v=0; v<G.V(); v ++) {
            res[visited[v]].add(v);
        }

        return res;
    }
}
