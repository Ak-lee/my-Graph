import java.io.File;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Scanner;

// 无权图（支持有向，无向）
public class Graph implements Cloneable{
    private int V;  // 所表示的图有多少个节点
    private int E;  // 所表示的图有多少条边
    private TreeSet<Integer>[] adj;
    private boolean directed;
    private int[] indegrees, outdegrees;

    // directed 代表用户要创建的图是有向的还是无向的。
    public Graph(String filename, boolean directed) {
        this.directed = directed;
        File file = new File(filename);
        try(Scanner scanner = new Scanner(file)) {
            V = scanner.nextInt();
            if(V < 0) {
                throw new IllegalArgumentException("V must be non-negative");
            }
            adj = new TreeSet[V];
            for(int i=0; i<V; i++) {
                adj[i] = new TreeSet<>();
            }
            indegrees = new int[V];
            outdegrees = new int[V];
            E = scanner.nextInt();
            if(E < 0) {
                throw new IllegalArgumentException("E must be non-negative");
            }
            for(int i=0; i<E; i++) {
                int a = scanner.nextInt();
                validateVertex(a);
                int b = scanner.nextInt();
                validateVertex(b);

                if(a == b) {    // 判断是否出现自环边
                    throw new IllegalArgumentException("Self Loop is Detected!");
                }
                if(adj[a].contains(b)) {    // 处理出现平行边的情况
                    throw new IllegalArgumentException("Parallel Edges are Detected!");
                }
                adj[a].add(b);
                if(directed) {
                    // 如果当前的图是有向图
                    indegrees[b] ++;
                    outdegrees[a] ++;
                } else{
                    // 图是无向图才执行
                    adj[b].add(a);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Graph(String filename) {
        // 如果用户不传第二个参数，默认创建无向图
        this(filename, false);
    }

    public Graph(TreeSet<Integer>[] adj, boolean directed) {
        // 这个图的构造函数通过接受一个邻接表来创建图
        this.adj = adj;
        this.directed = directed;
        this.V = adj.length;
        this.E = 0;

        indegrees = new int[V];
        outdegrees = new int[V];
        for(int v=0; v<V; v ++) {
            for(int w : adj[v]) {
                indegrees[w] ++;
                outdegrees[v] ++;
                this.E ++;
            }
        }
        if(!directed) this.E /= 2;  // 若图为无向图，边数重复计算了。
    }

    public Graph reverseGraph() {
        TreeSet<Integer>[] rAdj = new TreeSet[V];
        for(int i=0; i<V; i ++) {
            rAdj[i] = new TreeSet<>();
        }
        for(int v=0; v<V; v ++) {
            for(int w:adj(v)) {
                rAdj[w].add(v);
            }
        }
        return new Graph(rAdj, directed);
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

    public static void main(String[] args) {
        Graph g = new Graph("ug.txt", true);
        System.out.println(g);

        for(int v=0; v< g.V(); v ++) {
            System.out.println(g.indegree(v) + " " + g.outdegree(v));
        }
    }
}

