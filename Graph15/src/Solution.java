import java.util.*;

public class Solution {
    public class Graph implements Cloneable {
        private int V;  // 所表示的图有多少个节点
        private int E;  // 所表示的图有多少条边
        private TreeSet<Integer>[] adj;
        private boolean directed;
        private int[] indegrees, outdegrees;

        public Graph(int V, boolean directed) {
            this.V = V;
            this.directed = directed;
            this.E = 0;

            adj = new TreeSet[V];
            for(int i=0; i<V; i ++) {
                adj[i] = new TreeSet<>();
            }
        }

        public void addEdge(int a, int b) {
            validateVertex(a);
            validateVertex(b);

            if(a == b) {    // 判断是否出现自环边
                throw new IllegalArgumentException("Self Loop is Detected!");
            }
            if(adj[a].contains(b)) {    // 处理出现平行边的情况
                throw new IllegalArgumentException("Parallel Edges are Detected!");
            }

            adj[a].add(b);
            if(!directed) {
                adj[b].add(a);
            }
            this.E ++;
        }

        public boolean isDirected() {
            return directed;
        }

        public void validateVertex(int v) {    // 判断传入的顶点序号是否合法
            if(v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + "is invalid");
            }
        }

        public int V() {
            return V;
        }

        public int E() {
            return E;
        }

        public boolean hasEdge(int v, int w) {
            validateVertex(v);
            validateVertex(w);
            return adj[v].contains(w);
        }

        public Iterable<Integer> adj(int v) {  // 返回和顶点ｖ相邻的顶点的数组
            validateVertex(v);
            return adj[v];
        }

        public int degree(int v) {  // 求一个顶点的度
            if(directed) {
                // 求度数函数只作用于无向图上
                throw new RuntimeException("degree only works in undirected graph.");
            }
            validateVertex(v);
            return adj[v].size();
        }

        public int indegree(int v) {
            if(!directed) {
                // 入度只在有向图中存在
                throw new RuntimeException("indegree only works in directed graph.");
            }
            validateVertex(v);
            return indegrees[v];
        }

        public int outdegree(int v) {
            if(!directed) {
                // 入度只在有向图中存在
                throw new RuntimeException("outdegree only works in directed graph.");
            }
            validateVertex(v);
            return outdegrees[v];
        }

        public void removeEdge(int v, int w) {
            validateVertex(v);
            validateVertex(w);

            if(adj[v].contains(w)) {
                E --;
                if(directed) {
                    // 若图为有向图
                    outdegrees[v] --;
                    indegrees[w] --;
                }
            }
            adj[v].remove(w);
            if(!directed) {
                // 若图为无向图
                adj[v].remove(v);
            }
        }

        @Override
        public Object clone() {
            try {
                Graph cloned = (Graph) super.clone();
                cloned.adj = new TreeSet[V];
                for(int v=0; v < V; v ++) {
                    cloned.adj[v] = new TreeSet<Integer>();
                    for(int w: adj[v]) {
                        cloned.adj[v].add(w);
                    }
                }
                return cloned;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(String.format("V = %d, E = %d directed = %b\n", V, E, directed));
            for(int v=0; v<V; v ++) {
                sb.append(String.format("%d: ", v));
                for(int w : adj[v]) {
                    sb.append(String.format("%d ", w));
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

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

    public class Hungarian {
        private Graph G;
        private int maxMatching = 0;
        private int[] matching;
        private Object ArrayList;

        public Hungarian(Graph G) {
            BipartitionDetection bd = new BipartitionDetection(G);
            if(!bd.isBipartite()) { // 若图不是二分图
                throw new IllegalArgumentException("Hungarian only works for bipartite graph.");
            }
            this.G = G;

            int[] colors = bd.colors();

            matching = new int[G.V()];  // matching[i] 表示与节点i已经匹配的点
            Arrays.fill(matching, -1);  // matching[i] 初始化为-1，表示未匹配
            for(int v=0; v<G.V(); v ++) {
                if(colors[v] == 0 && matching[v] == -1) {    // 我们只遍历左侧的还未匹配的点
                    if(bfs(v)) maxMatching ++;  // 如果寻找到增广路径，则maxMatching 增1
                }
            }
        }

        private boolean bfs(int v) {
            Queue<Integer> q = new LinkedList<>();
            int[] pre = new int[G.V()];
            Arrays.fill(pre, -1);

            q.add(v);
            pre[v] = v;
            while(!q.isEmpty()) {
                int cur = q.remove();
                for(int next : G.adj(cur)) {
                    if(pre[next] == -1) {   // 如果 next 还没有被广度优先遍历到
                        if(matching[next] != -1) {  // 如果next已经匹配过了
                            pre[next] = cur;
                            pre[matching[next]] = next;
                            q.add(matching[next]);
                        } else {
                            pre[next] = cur;
                            ArrayList<Integer> augPath = getAugPath(pre, v, next);
                            for(int i = 0; i<augPath.size(); i = i + 2) {
                                matching[augPath.get(i)] = augPath.get(i+1);
                                matching[augPath.get(i+1)] = augPath.get(i);
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private ArrayList<Integer> getAugPath(int[] pre, int start, int end) {
            // start 为路径的起点，end为路径的终点
            ArrayList<Integer> res = new ArrayList<>();
            int cur = end;
            while(cur != start) {
                res.add(cur);
                cur = pre[cur];
            }
            res.add(start);
            return res;
        }

        public int maxMatching() {
            return maxMatching;
        }
        public boolean isPerfectMatching() {
            return maxMatching * 2 == G.V();
        }
    }

    public int domino(int n, int m, int[][] broken) {
        int[][] board = new int[n][m];
        // 在 board 中值为0的位置就是好格子，值为1的位置就是坏格子。
        for(int[] p : broken) {
            board[p[0]][p[1]] = 1;
        }
        Graph g = new Graph(n*m, false);
        for(int i=0; i<n; i ++) {
            for(int j=0; j<m; j ++) {
                if(j + 1 < m && board[i][j] == 0 && board[i][j+1] == 0) {
                    g.addEdge(i*m+j, i*m+j+1);
                }
                if(i+1 < n && board[i][j] == 0 && board[i+1][j] == 0) {
                    g.addEdge(i*m+j, (i+1)*m+j);
                }
            }
        }

        Hungarian hungarian = new Hungarian(g);
        return hungarian.maxMatching();
    }
}
