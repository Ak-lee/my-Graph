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
                if(!directed) {
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

//    public int degree(int v) {  // 求一个顶点的度
//        validateVertex(v);
//        return adj[v].size();
//    }

    public void removeEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        if(adj[v].contains(w)) E --;

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
        Graph graph = new Graph("ug.txt", true);
        System.out.println(graph);
    }
}

