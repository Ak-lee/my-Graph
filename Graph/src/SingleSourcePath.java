import java.util.ArrayList;
import java.util.Collections;

public class SingleSourcePath {
    private Graph G;
    private int s;

    private boolean[] visited;
    private int[] pre;

    public SingleSourcePath(Graph G, int s) {   // 单源路径，这里的s表示源
        this.G = G;
        G.validateVertex(s);
        this.s = s;

        visited = new boolean[G.V()];
        pre = new int[G.V()];
        for(int i=0; i<pre.length; i ++) {
            pre[i] = -1;
        }

        dfs(s, s);
    }
    private void dfs(int v, int parent) {
        visited[v] = true;
        pre[v] = parent;
        for(int w : G.adj(v)) {
            if(!visited[w]) {
                dfs(w, v);
            }
        }
    }

    public boolean isConnectedTo(int t) { // 判断从源 s 到顶点 t 是否是可达的。t - target
        G.validateVertex(t);
        return visited[t];
    }

    public Iterable<Integer> path(int t) {  // t-target
        ArrayList<Integer> res = new ArrayList<>();
        if(!isConnectedTo(t)) {
            return res; // 返回空的数组
        }
        int cur = t;
        while(cur != s) {
            res.add(cur);
            cur = pre[cur];
        }
        res.add(s);
        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args) {
        Graph g = new Graph("g.txt");
        SingleSourcePath sspath = new SingleSourcePath(g, 0);
        System.out.println("0 -> 6:" + sspath.path(6));
        System.out.println("0 -> 5:" + sspath.path(5));
    }
}
