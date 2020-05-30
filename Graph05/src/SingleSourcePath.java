import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;

public class SingleSourcePath {
    private Graph G;
    private int s;

    private int[] pre;
    private boolean[] visited;

    public SingleSourcePath(Graph G, int s) {
        this.G = G;
        this.s = s;
        visited = new boolean[G.V()];
        pre = new int[G.V()];

        for(int i=0; i<G.V(); i ++) {
            pre[i] = -1;
        }

        bfs(s);
    }

    // 从某个顶点出发，进行广度优先遍历
    private void bfs(int s) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        pre[s] = s;
        while(!queue.isEmpty()) {
            int v = queue.remove();

            for(int w : G.adj(v)) {
                if(!visited[w]) {
                    queue.add(w);
                    visited[w] = true;
                    pre[w] = v;
                }
            }
        }
    }

    public boolean isConnectedTo(int t) {   // t - target
        G.validateVertex(t);
        return visited[t];
    }
    public Iterable<Integer> path(int t) {
        ArrayList<Integer> res = new ArrayList<>();
        if(!isConnectedTo(t)) return res;

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
        Graph g= new Graph("g.txt");
        SingleSourcePath sspath = new SingleSourcePath(g, 0);
        System.out.println("0 -> 6 : " + sspath.path(6));
    }
}
