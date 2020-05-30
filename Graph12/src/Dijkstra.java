import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class Dijkstra {
    private WeightedGraph G;
    private int s;  // 单源路径问题，s 记录源头
    private int[] dis;
    private boolean[] visited;
    private int[] pre;

    private class Node implements Comparable<Node> {
        public int v, dis;
        public Node(int v, int dis) {
            this.v = v;
            this.dis = dis;
        }

        @Override
        public int compareTo(Node another) {
            return dis - another.dis;
        }
    }

    public Dijkstra(WeightedGraph G, int s) {
        this.G = G;
        G.validateVertex(s);
        this.s = s;

        dis = new int[G.V()];
        Arrays.fill(dis, Integer.MAX_VALUE);
        pre = new int[G.V()];
        Arrays.fill(pre, -1);

        dis[s] = 0;
        pre[s] = s;
        visited = new boolean[G.V()];

        // Node 中既包含顶点的序号，又包含顶点的 dis 值
        PriorityQueue<Node> pq = new PriorityQueue<>(); // 最小堆
        pq.add(new Node(s, 0));
        while(!pq.isEmpty()) {
            int cur = pq.remove().v;
            if(visited[cur]) continue;

            visited[cur] = true;
            for(int w : G.adj(cur)) {
                if(!visited[w]) {
                    if(dis[w] > dis[cur] + G.getWeight(cur, w)) {
                        dis[w] = dis[cur] + G.getWeight(cur, w);
                        pq.add(new Node(w, dis[w]));    // 在优先队列中更新每1个顶点的dis值，副作用，每1个顶点的node可能有多份
                        pre[w] = cur;
                    }
                }
            }
        }
    }

    public boolean isConnectedTo(int v) {
        G.validateVertex(v);
        return visited[v];
    }

    public int distTo(int v) {
        G.validateVertex(v);
        return dis[v];
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
        WeightedGraph g = new WeightedGraph("g.txt");
        Dijkstra dij = new Dijkstra(g, 0);
        for(int v=0; v<g.V(); v ++) {
            System.out.print(dij.distTo(v) + " ");
        }
        System.out.println();
        System.out.println(dij.path(4));
    }
}
